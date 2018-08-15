package teamx.group.reminderapp;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;
import android.content.BroadcastReceiver;
import android.os.Build;

public class RemindersPresenter {
    ArrayList<RemindersModel> reminder_list=new ArrayList<RemindersModel>();
    //function to load up and store reminders to sql

    public void create_reminder(String reminder_name, Date date_time, VoiceProfile voice_profile){
        RemindersModel new_reminder=new RemindersModel(reminder_name,date_time,voice_profile);
        reminder_list.add(new_reminder);
        set_alarm_manager(new_reminder);
    }

    public RemindersModel get_reminder(Integer position_of_reminder){
        return(reminder_list.get(position_of_reminder));
    }

    public void snooze_reminder(RemindersModel reminder_model){
        //requires to access the presenter from alarm manager models
        //fuse the alarm mgr model?
    }

    public void set_alarm_manager(RemindersModel reminders_model){
        // send intent to alarm receiver, let alarm receiver determine the api build of the app
}


}
