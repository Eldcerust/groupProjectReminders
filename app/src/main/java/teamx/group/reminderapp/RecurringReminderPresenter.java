package teamx.group.reminderapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecurringReminderPresenter {
    RemindersPresenter reminder_presenter_wrap; //wrap to be able to access and keep the data from the original presenter? (is this allowed)

    public RecurringReminderPresenter(RemindersPresenter presenter_arraylist){
        this.reminder_presenter_wrap=presenter_arraylist;
    }

    public void create_recurring_reminder(String reminder_name, Calendar reminder_date_time, VoiceProfileModel reminder_voice_profile,ArrayList<CheckBoxListSingle> check_boxes, int number_repetition, int days_repetition, boolean micro_reminders){
        RecurringRemindersModel new_reminder=new RecurringRemindersModel(reminder_name,reminder_date_time,reminder_voice_profile,check_boxes,number_repetition,days_repetition,micro_reminders);
        this.reminder_presenter_wrap.reminder_list.add((RemindersModel)new_reminder);
        this.reminder_presenter_wrap.sort_reminders(); //since only the starting point matters, the reminder can be wrapped in that way
        this.set_alarm(); //requires a different alarm to be set
    }

    public void set_alarm(){
        //put alarm for pomodoro instructions
        //also requires to check the reminders option for micro reminding
    }

    /*public void change_name(int element_wanted,String name_to_change){
        this.reminder_presenter_wrap.change_name(element_wanted,name_to_change);
    }

    public void snooze_reminder(int elemented_wanted,int time_to_snooze){

    }*/
    // is these functions even needed since the classic presenter wrap is the one in control of snoozing and changing the name of the reminder?


}