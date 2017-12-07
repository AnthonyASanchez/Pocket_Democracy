package pdco.pocketdemocracy;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class directory extends AppCompatActivity implements View.OnClickListener, create_room.create_roomListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonChat;
    private DatabaseReference chat_room_reference;
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
                EditText room_name = (EditText) v.findViewById(R.id.room_door_name);
                TextView room_key = (TextView)v.findViewById(R.id.room_key);
                Button delete_button = (Button)v.findViewById(R.id.room_delete);
                room_name.setText(model.getRoom_name());
                room_key.setText(model.getKey());
            }
        };
        //Log.i(FirebaseDatabase.getInstance().getReference().child("Messages").push().toString(),FirebaseDatabase.getInstance().getReference().child("Messages").push().toString());

        listOfRooms.setAdapter(adapter);
    }

    public void promptForChatName(){
        DialogFragment dialogFrag = new create_room();
        dialogFrag.show(getFragmentManager(),"CreateRoom");
    }
    //Interface used by the dialogfragment (create_room) This will be
    //Called when the enter button is clicked
    @Override
    public void onEditLineFinish(DialogFragment dialog){
        Dialog dialogView = dialog.getDialog();
        EditText room_name = (EditText)dialogView.findViewById(R.id.new_chat_name);
        if(room_name.getText().toString().equals("")){
            Toast.makeText(directory.this, "Chat room must have a name", Toast.LENGTH_SHORT).show();
        }else{
            //Get reference to chatroom in database and fill with text
            Log.i("String: ",room_name.getText().toString());
            chat_room_reference = FirebaseDatabase.getInstance().getReference().child("ChatRooms").push();
            chat_room_reference.setValue(new chat_door(room_name.getText().toString()));
        }

    }
    @Override
    public void onClick(View view) {
        if(view == buttonChat){
            promptForChatName();
            //Log.i("CHAT ROOM", "CHAT ROOM");
            //startActivity(new Intent(this, chat_room.class));
        }
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            //startActivity(new Intent(this, chat_room.class ));
        }
    }


}
