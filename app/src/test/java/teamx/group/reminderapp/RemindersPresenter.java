package teamx.group.reminderapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.content.BroadcastReceiver;
import android.os.Build;

public class RemindersPresenter {
    ArrayList<RemindersModel> reminder_list=new ArrayList<RemindersModel>();
    public boolean done=false;
    //function to load up and store reminders to sql

    public void create_reminder(String reminder_name, Calendar date_time, VoiceProfileModel voice_profile){
        RemindersModel new_reminder=new RemindersModel(reminder_name,date_time,voice_profile);
        this.reminder_list.add(new_reminder);
        this.sort_reminders();
        this.set_alarm_manager();
    }

    public RemindersModel get_reminder(Integer position_of_reminder){
        return(this.reminder_list.get(position_of_reminder));
    }

    public void snooze_reminder(RemindersModel reminder_model, int minutes_snoozed){
        Calendar reminder_model_time=reminder_model.get_reminder_date_time();
        reminder_model_time.add(Calendar.MINUTE,1);
        this.sort_reminders();
        this.set_alarm_manager();
        //requires to access the presenter from alarm manager models
        //fuse the alarm mgr model?
    }

    public void set_alarm_manager(Context main_context,RemindersModel reminders_model){
        Intent intent = new Intent(main_context.getApplicationContext(), AlarmReceiver.class); //needs intent from view class or main activity?

        intent.putExtra("ReminderNameAndTime",String.valueOf(reminders_model.get_reminder_name()+","+String.valueOf(reminders_model.get_reminder_date_time().getTime())));

        PendingIntent pending_intent
        // put reminders name and time for the reminder
        // how to snooze reminders?


        // send intent to alarm receiver, let alarm receiver determine the api build of the app
        // code used to set alarmManager for one set of alarm to the nearest time?
        // requires proper sorting of alarms?
        // what happens if two different reminders have the same time, which means that at least one of the reminders are not detected? implement a delta negligible of 60 seconds or else two reminders are wrapped as one reminders for notification
    }

    public void change_name(int element_wanted,String string_name){
        RemindersModel temporary_reminder=this.reminder_list.get(element_wanted);
        temporary_reminder.set_reminder_name(string_name);
        this.sort_reminders();
        this.reminder_list.set(element_wanted,temporary_reminder);
    }

    public void sort_reminders(){
        this.quick_sort_helper(this.reminder_list,0,this.reminder_list.size()-1);
        //move this code in to other place/presenter s
    }

    public void quick_sort_helper(ArrayList<RemindersModel> reminder_list,int left_index,int right_index){
        if(left_index<right_index){
            int split_point=this.quick_sort_partition(reminder_list,left_index,right_index);

            this.quick_sort_helper(reminder_list,left_index,split_point-1);
            this.quick_sort_helper(reminder_list,split_point+1,right_index);
        }

    }

    public int quick_sort_partition(ArrayList<RemindersModel> reminder_list,int left_index,int right_index){
        Calendar pivot_value=reminder_list.get(left_index).get_reminder_date_time();

        int left_mark=left_index+1;
        int right_mark=right_index;

        done=false;

        while(!done){
            while((left_mark<=right_mark) && (compare_date(reminder_list.get(left_index).get_reminder_date_time(),pivot_value,true))){
                left_mark+=1;
            }

            while((compare_date(pivot_value,reminder_list.get(right_mark).get_reminder_date_time(),false)) && (right_mark>=left_mark)){
                right_mark-=1;
            }

            if(right_mark<left_mark){
                done=true;
            } else {
                RemindersModel temp_model=reminder_list.get(left_mark);
                RemindersModel temp_model_two=reminder_list.get(right_mark);
                reminder_list.set(left_mark,temp_model_two);
                reminder_list.set(right_mark,temp_model);

                this.reminder_list=reminder_list;
            }
        }

        RemindersModel temp_model=reminder_list.get(left_index);
        RemindersModel temp_model_two=reminder_list.get(right_index);

        reminder_list.set(left_index,temp_model_two);
        reminder_list.set(right_index,temp_model);

        this.reminder_list=reminder_list;

        return(right_mark);

    }

    public boolean compare_date(Calendar date_first, Calendar date_second,Boolean direction){
        //direction is used to correct the orientation of equals than. True is for date_first is less than or equals to date_second and false is vice versa
        //return true if first value is larger, else, false if second value is larger
        if(date_first.compareTo(date_second)>0){
            return(false);
        }else if(date_first.compareTo(date_second)<0){
            return(true);
        }else if(date_first.compareTo(date_second)==0){
            if(direction) {
                return(false);
            } else {
                return(true);
            }
        }
        return Boolean.parseBoolean(null);
    }


}
