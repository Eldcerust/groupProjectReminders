package teamx.group.reminderapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class MyDatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=null;

        try {
            datePickerDialog=new DatePickerDialog(getActivity(), (newBasicReminder) getActivity(), year, month, day);
        }catch(ClassCastException e){
            if(e.toString().equals("java.lang.ClassCastException: teamx.group.reminderapp.newRecurringReminder cannot be cast to teamx.group.reminderapp.newBasicReminder")){
                datePickerDialog=new DatePickerDialog(getActivity(), (newRecurringReminder) getActivity(), year, month, day);
            }else if(e.toString().equals("java.lang.ClassCastException: teamx.group.reminderapp.newTimeBoxReminder cannot be cast to teamx.group.reminderapp.newBasicReminder")){
                datePickerDialog=new DatePickerDialog(getActivity(), (newRecurringReminder) getActivity(), year, month, day);
            }
        }
        return datePickerDialog;
    }
}
