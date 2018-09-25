package teamx.group.reminderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class ListDialogFragment extends DialogFragment {
    OnDialogDismissListener callingBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] arrayThing={"Basic Reminder","Repeating reminder","TimeBox Reminder"};
        //reformat this part later to accept default preferences set by the user
        // through a special user part setting
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select what reminder you want to create.")
                .setItems(arrayThing, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Log.i("Running","ListDialog");
                        callingBack.onDialogDismissListener(position);
                    }
                });
        return builder.create();
    }

    public interface OnDialogDismissListener{
        public void onDialogDismissListener(int position);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            callingBack=(OnDialogDismissListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+"must implement OnDialogDismissListener");
        }
    }
}
