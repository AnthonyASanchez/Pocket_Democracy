package pdco.pocketdemocracy;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class directory extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonChat;
    private FirebaseListAdapter<chat_door> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,log_in.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();



        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogOut);
        buttonChat = (Button) findViewById(R.id.buttonChat);


        textViewUserEmail.setText("Welcome "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        buttonChat.setOnClickListener(this);


        displayRooms();
    }

    public void displayRooms(){
        ListView listOfRooms = (ListView)findViewById(R.id.list_of_rooms);

        adapter = new FirebaseListAdapter<chat_door>(this,  chat_door.class,
                R.layout.activity_chat_door, FirebaseDatabase.getInstance().getReference().child("ChatRooms")) {
            @Override
            protected void populateView(View v, chat_door model, int position) {
                TextView room_name = (TextView)v.findViewById(R.id.room_name);
                TextView room_key = (TextView)v.findViewById(R.id.room_key);
                Button delete_button = (Button)v.findViewById(R.id.room_delete);
                room_name.setText(model.getRoom_name());
                room_key.setText(model.getKey());
            }
        };
        //Log.i(FirebaseDatabase.getInstance().getReference().child("Messages").push().toString(),FirebaseDatabase.getInstance().getReference().child("Messages").push().toString());

        listOfRooms.setAdapter(adapter);
    }

    public void addChatRoom(){
        DatabaseReference db;
        Bundle b = new Bundle();
       // DialogFragment dialogFrag = new create_room(b);
       // dialogFrag.show()
        //db = FirebaseDatabase.getInstance().getReference().child("ChatRooms").push(new chat_door());

    }

    @Override
    public void onClick(View view) {
        if(view == buttonChat){
            //addChatRoom();
            //Log.i("CHAT ROOM", "CHAT ROOM");
            startActivity(new Intent(this, chat_room.class));
        }
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            //startActivity(new Intent(this, chat_room.class ));
        }
    }


}
