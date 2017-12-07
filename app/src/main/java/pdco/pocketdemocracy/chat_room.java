package pdco.pocketdemocracy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by CaveMonster on 11/1/2017.
 */

public class chat_room extends AppCompatActivity implements View.OnClickListener{

    private FirebaseListAdapter<message> adapter;
    private FloatingActionButton addVote;
    private DatabaseReference candidateReference;
    private DatabaseReference roomReference;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Messages")
                        .push()
                        .setValue(new message(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });

        addVote = (FloatingActionButton) findViewById(R.id.addVote);
        addVote.setOnClickListener(this);
        if(candidateReference == null){
            roomReference = FirebaseDatabase.getInstance().getReference();
            //Toast.makeText(chat_room.this, "room", Toast.LENGTH_SHORT).show();
            roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChildren()) {
                        //Toast.makeText(chat_room.this, "Start up", Toast.LENGTH_SHORT).show();
                        candidateReference = FirebaseDatabase.getInstance().getReference().child("Candidate");
                        candidateReference.setValue(new vote());
                    }
                    else{
                        for(DataSnapshot child: snapshot.getChildren()){
                            candidateReference = child.getRef();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(chat_room.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        displayChatMessages();
    }


    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<message>(this, message.class,
                R.layout.activity_message, FirebaseDatabase.getInstance().getReference().child("Messages")) {
            @Override
            protected void populateView(View v, message model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);


                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("HH:mm",
                        model.getMessageTime()));
            }
        };
        //Log.i(FirebaseDatabase.getInstance().getReference().child("Messages").push().toString(),FirebaseDatabase.getInstance().getReference().child("Messages").push().toString());

        listOfMessages.setAdapter(adapter);
    }

    public void addVote(){
        Bundle b = new Bundle();
        b.putString("voteReference", candidateReference.toString());
        Intent intent = new Intent(chat_room.this, create_vote.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        if(view == addVote){
            addVote();
        }
    }
}
