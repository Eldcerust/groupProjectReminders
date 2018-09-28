package teamx.group.reminderapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class MyTimePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog;

        try{
            timePickerDialog=new TimePickerDialog(getActivity(), (newBasicReminder)getActivity(), hour, minute,true);
        }catch (Exception e){
            if(e.toString().equals("java.lang.ClassCastException: teamx.group.reminderapp.newRecurringReminder cannot be cast to teamx.group.reminderapp.newBasicReminder")){
                timePickerDialog=new TimePickerDialog(getActivity(), (newRecurringReminder)getActivity(), hour, minute,true);
            }else if(e.toString().equals("java.lang.ClassCastException: teamx.group.reminderapp.newTimeBoxReminder cannot be cast to teamx.group.reminderapp.newBasicReminder")){
                timePickerDialog=new TimePickerDialog(getActivity(), (newTimeBoxedReminder)getActivity(), hour, minute,true);
            }
        }

        return timePickerDialog;
    }
}
