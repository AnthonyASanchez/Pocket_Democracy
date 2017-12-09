package pdco.pocketdemocracy;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_vote extends AppCompatActivity implements View.OnClickListener{

    private EditText voteTitle;
    private Button castVote;
    private DatabaseReference voteReference;

    @Override
    public void onSaveInstanceState(Bundle outState){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vote);

        voteTitle = (EditText) findViewById(R.id.voteTitle);
        castVote = (Button) findViewById(R.id.castVote);

        castVote.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        String reference = b.getString("voteReference");
        voteReference = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);

    }

    public void sendVote(){
        String voteTitleText = voteTitle.getText().toString();
        voteReference.setValue(new vote(voteTitleText, "Empty"));
        finish();
    }

    @Override
    public void onClick(View view){
        if(view == castVote){
            sendVote();
        }

    }
}
