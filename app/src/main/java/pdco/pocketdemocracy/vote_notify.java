package pdco.pocketdemocracy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kianb on 12/7/2017.
 */

public class vote_notify extends DialogFragment {

    private TextView vote_result;
    private String resultToDisplay;

    @Override
    public Dialog onCreateDialog(final Bundle instance){
        super.onCreate(instance);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_vote_notify, null);

        resultToDisplay = getArguments().getString("result");
        vote_result = (TextView) view.findViewById(R.id.vote_result);
        vote_result.setText(resultToDisplay);

        builder.setView(view);

        return builder.create();
    }
}
