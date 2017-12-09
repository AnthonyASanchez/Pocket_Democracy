package pdco.pocketdemocracy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kianb on 12/6/2017.
 */

public class vote_prompt extends DialogFragment {

    private TextView vote_title;
    private String titleToDisplay;
    private DatabaseReference candidateReference;

    @Override
    public Dialog onCreateDialog(final Bundle instance){
        super.onCreate(instance);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_vote_prompt, null);

        titleToDisplay = getArguments().getString("voteTitle");
        vote_title = (TextView) view.findViewById(R.id.vote_title);
        vote_title.setText(titleToDisplay);

        candidateReference = FirebaseDatabase.getInstance().getReferenceFromUrl(getArguments().getString("candidateReference"));

        builder.setView(view)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        candidateReference.child("Votes").push().setValue(1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        candidateReference.child("Votes").push().setValue(-1);
                    }
                });
        return builder.create();
    }
}
