package pdco.pocketdemocracy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by atony on 12/6/2017.
 */

public class create_room extends DialogFragment{


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.acvitiy_create_chat, null))

                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                      //  savedInstanceState.putString((EditText)dialogInterface.findViewById(R.id.new_chat_name), "new_chat_name");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        create_room.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
