package pdco.pocketdemocracy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class directory extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonChat;


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

    }

    @Override
    public void onClick(View view) {
        if(view == buttonChat){
            startActivity(new Intent(this, chat_room.class));
        }
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            //startActivity(new Intent(this, chat_room.class ));
        }
    }


}
