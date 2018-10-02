package teamx.group.reminderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;

public class ListDialogSnooze extends DialogFragment{
    protected OnDialogDismissSnoozeListener callingBack;

    public OnDialogDismissSnoozeListener getCallingBack() {
        return callingBack;
    }

    protected Integer[] reminders;
    protected String reminderType;

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public Integer[] getReminders() {
        return reminders;
    }

    public void setReminders(Integer[] reminders) {
        this.reminders = reminders;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] arrayThing=returnStringArray(getReminders());
        //reformat this part later to accept default preferences set by the user
        // through a special user part setting
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select how long to snooze")
                .setItems(arrayThing, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Log.i("Running","ListDialog");
                        callingBack.onDialogDismissSnoozeListener(getReminders()[position],reminderType);
                        setReminders(null);
                        setReminderType(null);
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                            callingBack.onDialogDismissSnoozeListener(99,"cancelSnooze");
                            return true;
                        } else {
                            return true;
                        }
                    }
                });
        return builder.create();
    }

    public interface OnDialogDismissSnoozeListener{
        public void onDialogDismissSnoozeListener(int position,String reminderType);
    }

    public String[] returnStringArray(Integer[] array){
        String[] convertedInt=new String[array.length];
        for(int a=0;a<array.length;a++){
            convertedInt[a]=String.valueOf(array[a]);
        }
        return convertedInt;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            callingBack=(OnDialogDismissSnoozeListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+"must implement OnDialogDismissListener");
        }
    }
}
