package pdco.pocketdemocracy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class join_room extends DialogFragment {

    public interface join_roomListener{
        public void onJoinRoomKey(DialogFragment dialog);
    }

    join_room.join_roomListener listener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            listener = (join_room.join_roomListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement join_roomListener");
        }
    }



    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_join_room, null))

                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onJoinRoomKey(join_room.this);
                        //  savedInstanceState.putString((EditText)dialogInterface.findViewById(R.id.new_chat_name), "new_chat_name");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        join_room.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
