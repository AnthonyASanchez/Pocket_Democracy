package pdco.pocketdemocracy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class directory extends AppCompatActivity implements View.OnClickListener, create_room.create_roomListener, join_room.join_roomListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button createChatButton;
    private Button joinChatButton;
    private DatabaseReference user_chat_room_reference;
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
        createChatButton = (Button) findViewById(R.id.createChatButton);
        joinChatButton = (Button) findViewById(R.id.joinChatButton);


        textViewUserEmail.setText("Welcome "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        createChatButton.setOnClickListener(this);
        joinChatButton.setOnClickListener(this);

        displayRooms();
    }

    public void displayRooms(){
        ListView listOfRooms = (ListView)findViewById(R.id.list_of_rooms);
        listOfRooms.setClickable(true);

        String s = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        s = s.replace("@","");
        s = s.replace(".","");
        adapter = new FirebaseListAdapter<chat_door>(this,  chat_door.class,
                R.layout.activity_chat_door, FirebaseDatabase.getInstance().getReference().child(s)) {
            @Override
            protected void populateView(View v, final chat_door model, int position) {
                    //ArrayList<String> list = model.getGuest_list();
                    EditText room_name = (EditText) v.findViewById(R.id.room_door_name);
                    TextView room_key = (TextView) v.findViewById(R.id.room_key);
                    Button delete_button = (Button) v.findViewById(R.id.room_join);
                    room_name.setText(model.getRoom_name());
                    room_key.setText(model.getKey());
                    delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("Yes","It is listening");
                            Bundle b = new Bundle();
                            b.putString("key", model.getKey());
                            Intent intent = new Intent(directory.this, chat_room.class);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
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
            Log.i("String: ",FirebaseAuth.getInstance().getCurrentUser().getEmail());
            String s = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            s = s.replace("@","");
            s = s.replace(".","");
            chat_door cd =  new chat_door(room_name.getText().toString(),  FirebaseAuth.getInstance().getCurrentUser().getEmail());
            user_chat_room_reference = FirebaseDatabase.getInstance().getReference().child(s).push();
            user_chat_room_reference.setValue(cd);
            chat_room_reference = FirebaseDatabase.getInstance().getReference().child("ChatDoors").push();
            chat_room_reference.setValue(cd);
        }

    }
    public String getUserRef(){
        String s = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        s = s.replace("@","");
        s = s.replace(".","");
        return s;
    }
    public void joinChat(){
        DialogFragment dialogFrag = new join_room();
        dialogFrag.show(getFragmentManager(),"JoinRoom");
    }
    @Override
    public void onJoinRoomKey(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText room_key = (EditText)dialogView.findViewById(R.id.joining_key_code);
        final String keyCode = room_key.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("ChatDoors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            chat_door cd = snap.getValue(chat_door.class);
                            if(cd.getKey().equals(keyCode)){
                                FirebaseDatabase.getInstance().getReference().child(getUserRef()).push().setValue(cd);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == createChatButton){
            promptForChatName();
            //Log.i("CHAT ROOM", "CHAT ROOM");
            //startActivity(new Intent(this, chat_room.class));
        }
        if(view == joinChatButton){
            joinChat();
        }
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            //startActivity(new Intent(this, chat_room.class ));
        }
    }



}
