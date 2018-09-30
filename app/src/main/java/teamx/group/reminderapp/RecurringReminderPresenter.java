package teamx.group.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;

public class RecurringReminderPresenter {
    protected volatile ArrayList<RecurringRemindersModel> reminder_list= new ArrayList<>();
    protected Context from_main;
    protected VoiceProfilePresenter presenter_for_presets;
    private deleteNotification deleteNotificationInterface=ActualMainAndNotificationActivity.receiver::deleteNotification;

    public ArrayList<RecurringRemindersModel> getReminder_list() {
        return reminder_list;
    }

    public synchronized void setReminder_list(ArrayList<RecurringRemindersModel> reminder_list) {
        this.reminder_list = reminder_list;
    }


    public RecurringReminderPresenter(Context context,VoiceProfilePresenter presenter_model){
        this.from_main=context.getApplicationContext();
        this.presenter_for_presets=presenter_model;
        this.reminder_list=load_reminders_from_sql();
        sort_reminders();
    }

    public synchronized ArrayList<RecurringRemindersModel> get_reminder_list() {
        return this.reminder_list;
    }

    public ArrayList<CheckBoxListSingle> new_empty_checkbox_list(){
        ArrayList<CheckBoxListSingle> check_box_return=new ArrayList<CheckBoxListSingle>();
        check_box_return.add(new CheckBoxListSingle(false,""));
        return check_box_return;
    }

    public void insert_reminder(RecurringRemindersModel a){
        this.reminder_list.add(a);
        this.set_alarm_manager(a,this.from_main,101);
    }

    public void change_reminder_uuidbased(RecurringRemindersModel modified){
        UUID modified_uuid=modified.get_reminder_UUID();
        for(int a=0;a<this.reminder_list.size();a++){
            if(this.reminder_list.get(a).get_reminder_UUID()==modified_uuid){
                this.reminder_list.set(a,modified);
            }
        }
        sort_reminders();
    }

    public void change_reminder_position_wremindermodel(RecurringRemindersModel modified,int position){
        this.reminder_list.set(position,modified);
        sort_reminders();;
    }

    public synchronized void change_reminder_similar_object(RecurringRemindersModel original,RecurringRemindersModel modified){
        this.reminder_list.set(this.reminder_list.indexOf(original),modified);
        this.deleteNotificationInterface.deleteNotification(original.get_reminder_UUID().toString());
        this.set_alarm_manager(modified,this.from_main,101);
    }

    public synchronized void delete_object_reminderModel(RecurringRemindersModel a){
        ArrayList<RecurringRemindersModel> temp_list=get_reminder_list();
        int positionOfDel=temp_list.indexOf(a);
        if(positionOfDel!=-1){
            this.deleteNotificationInterface.deleteNotification(a.get_reminder_UUID().toString());
            temp_list.remove(positionOfDel);
        }
        setReminder_list(temp_list);
    }

    public void delete_reminder(RecurringRemindersModel non_modified){
        UUID non_modified_uuid=non_modified.get_reminder_UUID();
        for(int a=0;a<this.reminder_list.size();a++){
            if(this.reminder_list.get(a).get_reminder_UUID()==non_modified_uuid){
                this.reminder_list.remove(a);
            }
        }
    }

    public void delete_reminder_position(int position){
        this.reminder_list.remove(position);
    }

    public RemindersModel get_reminder(Integer position_of_reminder){
        return(this.reminder_list.get(position_of_reminder));
    }

    public String combine(String[] arrayString, String pattern){
        String combined = "";
        for (int a = 0; a < arrayString.length; a++) {
            if (a != arrayString.length - 1){
                combined = combined.concat(arrayString[a]).concat(pattern);
            } else{
                combined=combined.concat(arrayString[a]);
            }
        }
        return combined;
    }

    public ArrayList<RecurringRemindersModel> load_reminders_from_sql () {
        SQLiteDatabase my_database=this.from_main.openOrCreateDatabase("Reminders", Context.MODE_PRIVATE,null);
        ArrayList<RecurringRemindersModel> reminders_list_for_save=new ArrayList<RecurringRemindersModel>();
        ArrayList<Thread> threads_load_checklist=new ArrayList<Thread>();

        try {
            Cursor c = my_database.rawQuery("SELECT * FROM RecurringReminders ORDER BY year,month,day,hour,minute", null);
            int title_index=c.getColumnIndex("title");
            int hour_index=c.getColumnIndex("hour");
            int minute_index=c.getColumnIndex("minute");
            int year_index=c.getColumnIndex("year");
            int month_index=c.getColumnIndex("month");
            int day_index=c.getColumnIndex("day");
            int voice_profile_index=c.getColumnIndex("voiceProfile");
            int uuid_reminder_index=c.getColumnIndex("UUID");
            int day_repetition_index=c.getColumnIndex("dayRepetition");
            int week_repetition_index=c.getColumnIndex("weekRepetition");
            int count=0;

            c.moveToFirst();

            for(int i=0;i<c.getCount();i++){
                String reminder_name = c.getString(title_index);

                Calendar date_time = Calendar.getInstance();
                date_time.set(Calendar.DAY_OF_MONTH, Integer.valueOf(c.getString(day_index)));
                date_time.set(Calendar.MONTH, Integer.valueOf(c.getString(month_index)));
                date_time.set(Calendar.YEAR, Integer.valueOf(c.getString(year_index)));
                date_time.set(Calendar.HOUR_OF_DAY, Integer.valueOf(c.getString(hour_index)));
                date_time.set(Calendar.MINUTE, Integer.valueOf(c.getString(minute_index)));
                date_time.set(Calendar.SECOND, 0);
                date_time.set(Calendar.MILLISECOND, 0);

                int dayRepetition=Integer.valueOf(c.getString(day_repetition_index));
                int weekRepetition=Integer.valueOf(c.getString(week_repetition_index));

                //insert checkboxlistsingle here to sql database
                //UUID reminder_UUID=UUID.fromString(String.join("-",c.getString(uuid_reminder_index).split("_")));
                UUID reminder_UUID=UUID.fromString(combine(c.getString(uuid_reminder_index).split("_"),"-"));


                String voice_profile_str=c.getString(voice_profile_index);
                add_thread(threads_load_checklist,reminder_UUID,count);

                if(voice_profile_str.equals("null")){
                    RecurringRemindersModel place_holder=new RecurringRemindersModel(reminder_name,date_time);
                    place_holder.set_days_of_repetition(dayRepetition);
                    place_holder.set_numbers_of_repetition(weekRepetition);
                    place_holder.set_reminder_UUID(reminder_UUID);
                    reminders_list_for_save.add(place_holder);
                } else {
                    //UUID uuid_of_profile = UUID.fromString(String.join("-",c.getString(voice_profile_index).split("_")));
                    UUID uuid_of_profile=UUID.fromString(combine(c.getString(voice_profile_index).split("_"),"-"));
                    VoiceProfileModel voice_profile=this.presenter_for_presets.search_profile(uuid_of_profile);
                    RecurringRemindersModel place_holder=new RecurringRemindersModel(reminder_name,date_time);
                    place_holder.set_reminder_voice_profile(voice_profile);
                    place_holder.set_reminder_UUID(uuid_of_profile);
                    reminders_list_for_save.add(place_holder);
                }
                try {
                    c.moveToNext();
                } catch (Exception e){
                    continue;
                }
                count++;
                // how to determine which voice profile is this? Use UUID
            }
            c.close();
            my_database.close();
            for(int i=0;i<threads_load_checklist.size();i++){
                try {
                    threads_load_checklist.get(i).join();
                    threads_load_checklist.get(i).run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            Log.i("Wrong sql loading in:","Recurring Reminder");
            if(reminders_list_for_save!=null && reminders_list_for_save.size()>0){
                return(reminders_list_for_save);
            } else if(reminders_list_for_save==null || reminders_list_for_save.size()==0){
                ArrayList<CheckBoxListSingle> new_list=new ArrayList<CheckBoxListSingle>();
                new_list.add(new CheckBoxListSingle(false,""));
                reminders_list_for_save.add(new RecurringRemindersModel("",Calendar.getInstance(),new_list));
                return reminders_list_for_save;
            }
        }

        return(reminders_list_for_save);
    }

    public ArrayList<Thread> add_thread(ArrayList<Thread> thread_master,UUID uuid_reminders,int position){
        Thread a=new Thread(()->{
            ArrayList<CheckBoxListSingle> list_holder=load_from_sql_with_uuid_ver(uuid_reminders);
            this.reminder_list.get(position).set_list(list_holder);
        });
        thread_master.add(a);
        return thread_master;
    }

    public void save_reminders_sql(ArrayList<RecurringRemindersModel> reminders_lists) {
        // firebase will be implemented later

        SQLiteDatabase my_database = this.from_main.openOrCreateDatabase("Reminders", Context.MODE_PRIVATE, null);
        Cursor cursor = my_database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + "RecurringReminders" + "'", null);
        if (cursor.getCount() > 0) {
            // do we need to identify which reminder has which kind of UUID?
            // requirement to set proper UUID for different reminders?
            // or just nuke the entire database and make a new one?
            my_database.execSQL("DROP TABLE RecurringReminders");
        }
        my_database.execSQL("CREATE TABLE IF NOT EXISTS RecurringReminders(title VARCHAR,hour INTEGER,minute INTEGER,year INTEGER,month INTEGER,day INTEGER,voiceProfile VARCHAR,UUID VARCHAR,dayRepetition INTEGER,weekRepetition INTEGER)");
        // no need for creating if does not exist, so, just for the sake if there is an empty table, somehow???
        for (int i = 0; i < reminders_lists.size(); i++) {
            RecurringRemindersModel reminder_model=reminders_lists.get(i);
            Calendar temp_date = reminder_model.get_reminder_date_time();
            int dayRepetition=reminder_model.get_days_of_repetition();
            int weekRepetition=reminder_model.get_number_of_repetition();
            try {
                my_database.execSQL("INSERT INTO RecurringReminders(title,hour,minute,year,month,day,voiceProfile,UUID,dayRepetition,weekRepetition) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + String.valueOf(reminders_lists.get(i).get_reminder_voice_profile().get_name()) + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() + "\',\'" +String.valueOf(dayRepetition)+ "\',\'" +String.valueOf(weekRepetition)+"\')");
                save_reminder_checkbox_sql(reminder_model.get_checkbox_list(),reminder_model.get_reminder_UUID());
            } catch (NullPointerException e) {
                if(e.toString().equals("java.lang.NullPointerException: Attempt to invoke virtual method 'java.util.UUID teamx.group.reminderapp.VoiceProfileModel.get_name()' on a null object reference")){
                    save_reminder_checkbox_sql(reminders_lists.get(i).get_checkbox_list(), reminders_lists.get(i).get_reminder_UUID());
                    my_database.execSQL("INSERT INTO RecurringReminders(title,hour,minute,year,month,day,voiceProfile,UUID,dayRepetition,weekRepetition) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + "null" + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() +"\',\'" +String.valueOf(dayRepetition)+ "\',\'" +String.valueOf(weekRepetition)+ "\')");
                    System.out.println("INSERT INTO RecurringReminders(title,hour,minute,year,month,day,voiceProfile,UUID,dayRepetition,weekRepetition) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + "null" + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() +"\',\'" +String.valueOf(dayRepetition)+ "\',\'" +String.valueOf(weekRepetition)+ "\')");

                } else {
                    throw e;
                }
            }
            //create a queue for this later on
        }
    }

    public void save_reminder_checkbox_sql(ArrayList<CheckBoxListSingle> checkbox_lists,UUID uuid){
        String uuid_of_reminder=combine(uuid.toString().split("-"),"_");

        SQLiteDatabase save_database=this.from_main.openOrCreateDatabase("CheckBoxReminders",Context.MODE_PRIVATE,null);
        Cursor cursor = save_database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + "CheckBoxReminders"+uuid_of_reminder + "'", null);
        System.out.println("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + "CheckBoxReminders"+uuid_of_reminder + "'");
        if(cursor.getCount()>0) {
            // do we need to identify which reminder has which kind of UUID?
            // requirement to set proper UUID for different reminders?
            // or just nuke the entire database and make a new one?
            save_database.execSQL("DROP TABLE CheckBoxReminders".concat(uuid_of_reminder));}
        System.out.println("CREATE TABLE IF NOT EXISTS CheckBoxReminders\'"+uuid_of_reminder+"\'(ID int NOT NULL,BooleanState VARCHAR,TextOfList VARCHAR,PRIMARY KEY (ID))");
        save_database.execSQL("CREATE TABLE IF NOT EXISTS CheckBoxReminders"+uuid_of_reminder+"(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,BooleanState VARCHAR,TextOfList VARCHAR)");
        for(int i=0;i<checkbox_lists.size();i++){
            CheckBoxListSingle temp_stuff=checkbox_lists.get(i);
            String boolean_state=String.valueOf(temp_stuff.get_state());
            String text_of_list=temp_stuff.get_name();
            System.out.println("INSERT INTO CheckBoxReminders"+uuid_of_reminder+"(BooleanState,TextOfList) values (\'"+boolean_state+"\',\'"+text_of_list+"\')");
            save_database.execSQL("INSERT INTO CheckBoxReminders"+uuid_of_reminder+"(BooleanState,TextOfList) values (\'"+boolean_state+"\',\'"+text_of_list+"\')");
        }
    }

    public ArrayList<CheckBoxListSingle> load_from_sql_with_uuid_ver(UUID uuid_from_reminders){
        //load sql based on UUID, store based on UUID
        SQLiteDatabase checkbox_database;
        String uuid_string=combine(uuid_from_reminders.toString().split("-"),"_");
        ArrayList<CheckBoxListSingle> onereminder_list=new ArrayList<CheckBoxListSingle>();
        Cursor c;
        try {
            checkbox_database = this.from_main.openOrCreateDatabase("CheckBoxReminders", Context.MODE_PRIVATE,null);
            c = checkbox_database.rawQuery("SELECT * FROM CheckBoxReminders".concat(uuid_string), null);
            int state_index = c.getColumnIndex("BooleanState");
            int text_index = c.getColumnIndex("TextOfList");

            c.moveToFirst();

            while (c != null) {
                Boolean boolean_state = Boolean.valueOf(c.getString(state_index));
                String text_List = c.getString(text_index);

                CheckBoxListSingle new_list = new CheckBoxListSingle(boolean_state, text_List);
                onereminder_list.add(new_list);

                c.moveToNext();
            }
            c.close();
            checkbox_database.close();
        } catch (Exception e){
            if(e.toString().equals("android.database.sqlite.SQLiteCantOpenDatabaseException: unknown error (code 14 SQLITE_CANTOPEN): Could not open database")){
                Log.i("Error","Initialize db later");
                return onereminder_list;
            }
            return onereminder_list;
        }
        return onereminder_list;
    }

    public void set_alarm_manager(RecurringRemindersModel reminders_model,Context main_context,int notification_count){
        //check conditionally if the alarm is even nearer than previous alarm, lest ignore
        AlarmManager alarm_manager=(AlarmManager)main_context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        //does alarm manager work if created under the pretext from a different class from the views of the app?

        Intent intent = new Intent(main_context.getApplicationContext(), AlarmReceiver.class); //needs intent from view class or main activity?
        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
        String dateTime=df.format(reminders_model.get_reminder_date_time().getTime());
        String[] array={reminders_model.get_reminder_name(),dateTime,reminders_model.return_type(),reminders_model.get_reminder_UUID().toString()};

        intent.putExtra("oneReminder",array);

        // put code here to process multiple amount of reminders at the same time
        PendingIntent pending_intent=PendingIntent.getBroadcast(main_context,notification_count,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,reminders_model.get_reminder_date_time().getTimeInMillis(),pending_intent);
        } else {
            alarm_manager.set(AlarmManager.RTC_WAKEUP,reminders_model.get_reminder_date_time().getTimeInMillis(),pending_intent);
        }
        // put reminders name and time for the reminder
        // how to snooze reminders?


        // send intent to alarm receiver, let alarm receiver determine the api build of the app
        // code used to set alarmManager for one set of alarm to the nearest time?
        // requires proper sorting of alarms?
        // what happens if two different reminders have the same time, which means that at least one of the reminders are not detected? implement a delta negligible of 60 seconds or else two reminders are wrapped as one reminders for notification
    }

    public RemindersModel fetch_earliest_reminder(){
        RemindersModel temp_model=this.reminder_list.get(0);
        Calendar temp_cal=temp_model.get_reminder_date_time();
        temp_cal.set(Calendar.SECOND,0);
        temp_cal.set(Calendar.MILLISECOND,0);
        int count=1;
        while(true){
            Calendar temp_cal_later=this.reminder_list.get(count).get_reminder_date_time();
            temp_cal_later.set(Calendar.SECOND,0);
            temp_cal_later.set(Calendar.MILLISECOND,0);
            if(temp_cal.compareTo(temp_cal_later)==0) {
                temp_model.set_reminder_name(String.valueOf(count) + " reminders");
                count += 1;
            } else {
                break;
            }
        }
        return temp_model;
    }

    public void change_name(int element_wanted,String string_name){
        RecurringRemindersModel temporary_reminder=this.reminder_list.get(element_wanted);
        temporary_reminder.set_reminder_name(string_name);
        this.sort_reminders();
        this.reminder_list.set(element_wanted,temporary_reminder);
    }

    protected void sort_reminders(){
        this.quick_sort_helper(this.reminder_list,0,this.reminder_list.size()-1);
        //move this code in to other place/presenter s
    }

    private void quick_sort_helper(ArrayList<RecurringRemindersModel> reminder_list,int left_index,int right_index){
        if(left_index<right_index){
            int split_point=this.quick_sort_partition(reminder_list,left_index,right_index);

            this.quick_sort_helper(reminder_list,left_index,split_point-1);
            this.quick_sort_helper(reminder_list,split_point+1,right_index);
        }

    }

    private int quick_sort_partition(ArrayList<RecurringRemindersModel> reminder_list,int left_index,int right_index){
        Calendar pivot_value=reminder_list.get(left_index).get_reminder_date_time();

        int left_mark=left_index+1;
        int right_mark=right_index;

        boolean done=false;

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
                RecurringRemindersModel temp_model=reminder_list.get(left_mark);
                RecurringRemindersModel temp_model_two=reminder_list.get(right_mark);
                reminder_list.set(left_mark,temp_model_two);
                reminder_list.set(right_mark,temp_model);

                this.reminder_list=reminder_list;
            }
        }

        RecurringRemindersModel temp_model=reminder_list.get(left_index);
        RecurringRemindersModel temp_model_two=reminder_list.get(right_index);

        reminder_list.set(left_index,temp_model_two);
        reminder_list.set(right_index,temp_model);

        this.reminder_list=reminder_list;

        return(right_mark);

    }

    private boolean compare_date(Calendar date_first, Calendar date_second,Boolean direction){
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

    /*public void change_name(int element_wanted,String name_to_change){
        this.reminder_presenter_wrap.change_name(element_wanted,name_to_change);
    }

    public void snooze_reminder(int elemented_wanted,int time_to_snooze){

    }*/
    // is these functions even needed since the classic presenter wrap is the one in control of snoozing and changing the name of the reminder?


}