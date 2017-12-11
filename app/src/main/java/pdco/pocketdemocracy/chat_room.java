package pdco.pocketdemocracy;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private String oldTitle;
    private long endTimer;
    private long startTimer;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Bundle b = getIntent().getExtras();
        String keycode = b.getString("key");
        key = keycode;
        //Log.i("KEYCODE IN ROOM",key);
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
                        .child("ChatRooms/" +key+"/Messages")
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

        //Creating candidate node
        FirebaseDatabase.getInstance().getReference().child("ChatRooms/" +key+"/Candidate")
                .setValue(new vote("empty", "empty"));
        roomReference = FirebaseDatabase.getInstance().getReference().child("ChatRooms/" +key);
        candidateReference = FirebaseDatabase.getInstance().getReference().child("ChatRooms/" +key+"/Candidate");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                candidateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        oldTitle = dataSnapshot.getValue(vote.class).getTitle();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                candidateReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        //dataSnapshot.getValue(vote.class).getTitle()
                        String title = dataSnapshot.getValue(vote.class).getTitle();
                        if(!title.equals(oldTitle) && !title.equals("empty")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DialogFragment dialogFrag = new vote_prompt();
                                    Bundle b = new Bundle();
                                    b.putString("voteTitle", dataSnapshot.getValue(vote.class).getTitle());
                                    b.putString("candidateReference", candidateReference.toString());
                                    dialogFrag.setArguments(b);
                                    dialogFrag.show(getFragmentManager(),"VotePrompt");
                                    startTimer = (System.currentTimeMillis()/1000);
                                    endTimer = (System.currentTimeMillis()/1000) + 15;
                                }
                            }, 2500);
                            oldTitle = title;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(chat_room.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 2500);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if((endTimer - startTimer) < 0){
                                    candidateReference.child("Votes").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int voteSum = 0;
                                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                                voteSum = voteSum + child.getValue(Integer.class);
                                            }
                                            DialogFragment dialogFrag = new vote_notify();
                                            Bundle b = new Bundle();
                                            if(voteSum > 0){
                                                b.putString("result", "Passed");
                                            }
                                            else{
                                                b.putString("result", "Denied");
                                            }
                                            dialogFrag.setArguments(b);
                                            dialogFrag.show(getFragmentManager(),"VoteNotify");
                                            FirebaseDatabase.getInstance().getReference().child("ChatRooms/" +key+"/Candidate")
                                                    .setValue(new vote("empty", "empty"));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    endTimer = 0;
                                    startTimer = 0;
                                }
                                else{
                                    if(startTimer != 0 && endTimer != 0){
                                        startTimer = startTimer + 1;
                                    }
                                }
                            }
                        });
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };
        t.start();

        displayChatMessages();
    }


    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<message>(this, message.class,
                R.layout.activity_message, FirebaseDatabase.getInstance().getReference().child("ChatRooms/" +key+"/Messages")) {
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
