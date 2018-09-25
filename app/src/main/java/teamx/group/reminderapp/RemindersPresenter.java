package teamx.group.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class RemindersPresenter{
    protected ArrayList<RemindersModel> reminder_list=new ArrayList<RemindersModel>();
    protected Context from_main;
    protected VoiceProfilePresenter presenter_for_presets;

    //function to attach data to viewers
    //logic of data applied sent to viewers, or done here?

    public RemindersPresenter(Context context,VoiceProfilePresenter presenter_model){
        this.from_main=context.getApplicationContext();
        this.presenter_for_presets=presenter_model;
        this.reminder_list=load_reminders_from_sql();
        sort_reminders();
    }

    public ArrayList<RemindersModel> get_reminder_list() {
        return reminder_list;
    }

    public void create_reminder(String reminder_name, Calendar date_time, VoiceProfileModel voice_profile, ArrayList<CheckBoxListSingle> checkBoxListSingles){
        RemindersModel new_reminder=new RemindersModel(reminder_name,date_time,checkBoxListSingles);
        new_reminder.set_reminder_voice_profile(voice_profile);
        this.reminder_list.add(new_reminder);
        this.sort_reminders();
        this.set_alarm_manager(this.from_main,101); // needs another function that determines whether or not two reminders have same time negligibly
        // in other words, both has to be detected and displayed in a manner of having "2 reminders"
    }

    public ArrayList<CheckBoxListSingle> new_empty_checkbox_list(){
        ArrayList<CheckBoxListSingle> check_box_return=new ArrayList<CheckBoxListSingle>();
        check_box_return.add(new CheckBoxListSingle(false,""));
        return check_box_return;
    }

    public void insert_reminder(RemindersModel a){
        this.reminder_list.add(a);
    }

    public void change_reminders(String reminder_name, Calendar date_time, VoiceProfileModel voice_profile,ArrayList<CheckBoxListSingle> checkBoxListSingles, int position){
        this.reminder_list.remove(position);
        this.create_reminder(reminder_name,date_time,voice_profile,checkBoxListSingles);
        sort_reminders();
    }

    public void change_reminder_uuidbased(RemindersModel modified){
        UUID modified_uuid=modified.get_reminder_UUID();
        for(int a=0;a<this.reminder_list.size();a++){
            if(this.reminder_list.get(a).get_reminder_UUID()==modified_uuid){
                this.reminder_list.set(a,modified);
            }
        }
        sort_reminders();
    }

    public void change_reminder_position_wremindermodel(RemindersModel modified,int position){
        this.reminder_list.set(position,modified);
        sort_reminders();;
    }

    public void delete_reminder(RemindersModel non_modified){
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

    public void create_reminder(String reminder_name, Calendar date_time,ArrayList<CheckBoxListSingle> checkBoxListSingles){
        RemindersModel new_reminder=new RemindersModel(reminder_name,date_time,checkBoxListSingles);
        this.reminder_list.add(new_reminder);
        this.sort_reminders();
        this.set_alarm_manager(this.from_main,101); // needs another function that determines whether or not two reminders have same time negligibly
        // in other words, both has to be detected and displayed in a manner of having "2 reminders"
    }

    public RemindersModel get_reminder(Integer position_of_reminder){
        return(this.reminder_list.get(position_of_reminder));
    }

    public ArrayList<RemindersModel> load_reminders_from_sql () {
        SQLiteDatabase my_database=this.from_main.openOrCreateDatabase("Reminders",Context.MODE_PRIVATE,null);
        ArrayList<RemindersModel> reminders_list_for_save=new ArrayList<RemindersModel>();
        ArrayList<Thread> threads_load_checklist=new ArrayList<Thread>();

        try {
            Cursor c = my_database.rawQuery("SELECT * FROM BasicReminders ORDER BY year,month,day,hour,minute", null);
            int title_index=c.getColumnIndex("title");
            int hour_index=c.getColumnIndex("hour");
            int minute_index=c.getColumnIndex("minute");
            int year_index=c.getColumnIndex("year");
            int month_index=c.getColumnIndex("month");
            int day_index=c.getColumnIndex("day");
            int voice_profile_index=c.getColumnIndex("voiceProfile");
            int uuid_reminder_index=c.getColumnIndex("UUID");
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

                //insert checkboxlistsingle here to sql database
                UUID reminder_UUID=UUID.fromString(String.join("-",c.getString(uuid_reminder_index).split("_")));

                String voice_profile_str=c.getString(voice_profile_index);
                add_thread(threads_load_checklist,reminder_UUID,count);

                if(voice_profile_str.equals("null")){
                    RemindersModel place_holder=new RemindersModel(reminder_name,date_time);
                    place_holder.set_reminder_UUID(reminder_UUID);
                    reminders_list_for_save.add(place_holder);
                } else {
                    UUID uuid_of_profile = UUID.fromString(String.join("-",c.getString(voice_profile_index).split("_")));
                    VoiceProfileModel voice_profile=this.presenter_for_presets.search_profile(uuid_of_profile);
                    RemindersModel place_holder=new RemindersModel(reminder_name,date_time);
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
            System.out.println("REEEEEEE".concat(e.toString()));
            if(reminders_list_for_save!=null && reminders_list_for_save.size()>0){
                return(reminders_list_for_save);
            } else if(reminders_list_for_save==null || reminders_list_for_save.size()==0){
                ArrayList<CheckBoxListSingle> new_list=new ArrayList<CheckBoxListSingle>();
                new_list.add(new CheckBoxListSingle(false,""));
                reminders_list_for_save.add(new RemindersModel("",Calendar.getInstance(),new_list));
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

    public void save_reminders_sql(ArrayList<RemindersModel> reminders_lists) {
        // firebase will be implemented later

        SQLiteDatabase my_database = this.from_main.openOrCreateDatabase("Reminders", Context.MODE_PRIVATE, null);
        Cursor cursor = my_database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + "BasicReminders" + "'", null);
        if (cursor.getCount() > 0) {
            // do we need to identify which reminder has which kind of UUID?
            // requirement to set proper UUID for different reminders?
            // or just nuke the entire database and make a new one?
            my_database.execSQL("DROP TABLE BasicReminders");
        }
        my_database.execSQL("CREATE TABLE IF NOT EXISTS BasicReminders(title VARCHAR,hour INTEGER,minute INTEGER,year INTEGER,month INTEGER,day INTEGER,voiceProfile VARCHAR,UUID VARCHAR)");
        // no need for creating if does not exist, so, just for the sake if there is an empty table, somehow???
        for (int i = 0; i < reminders_lists.size(); i++) {
            Calendar temp_date = reminders_lists.get(i).get_reminder_date_time();
            try {
                my_database.execSQL("INSERT INTO BasicReminders(title,hour,minute,year,month,day,voiceProfile,UUID) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + String.valueOf(reminders_lists.get(i).get_reminder_voice_profile().get_name()) + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() + "\')");
                save_reminder_checkbox_sql(reminders_lists.get(i).get_checkbox_list(), reminders_lists.get(i).get_reminder_UUID());
            } catch (NullPointerException e) {
                if(e.toString().equals("java.lang.NullPointerException: Attempt to invoke virtual method 'java.util.UUID teamx.group.reminderapp.VoiceProfileModel.get_name()' on a null object reference")){
                    System.out.println("INSERT INTO BasicReminders(title,hour,minute,year,month,day,voiceProfile,UUID) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + "null" + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() + "\')");
                    save_reminder_checkbox_sql(reminders_lists.get(i).get_checkbox_list(), reminders_lists.get(i).get_reminder_UUID());
                    my_database.execSQL("INSERT INTO BasicReminders(title,hour,minute,year,month,day,voiceProfile,UUID) values (\'" + reminders_lists.get(i).get_reminder_name() + "\',\'" + String.valueOf(temp_date.get(Calendar.HOUR_OF_DAY)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MINUTE)) + "\',\'" + String.valueOf(temp_date.get(Calendar.YEAR)) + "\',\'" + String.valueOf(temp_date.get(Calendar.MONTH)) + "\',\'" + String.valueOf(temp_date.get(Calendar.DAY_OF_MONTH)) + "\',\'" + "null" + "\',\'" + reminders_lists.get(i).get_reminder_UUID().toString() + "\')");
                } else {
                    throw e;
                }
            }
            //create a queue for this later on
        }
    }

    public void save_reminder_checkbox_sql(ArrayList<CheckBoxListSingle> checkbox_lists,UUID uuid){
        String uuid_of_reminder=String.join("_",uuid.toString().split("-"));

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

    public void snooze_reminder(RemindersModel reminder_model, int minutes_snoozed){
        Calendar reminder_model_time=reminder_model.get_reminder_date_time();
        reminder_model_time.add(Calendar.MINUTE,1);
        this.sort_reminders();
        this.set_alarm_manager(this.from_main,101);
        //requires to access the presenter from alarm manager models
        //fuse the alarm mgr model?
    }

    public ArrayList<CheckBoxListSingle> load_from_sql_with_uuid_ver(UUID uuid_from_reminders){
        //load sql based on UUID, store based on UUID
        SQLiteDatabase checkbox_database;
        String uuid_string=String.join("_",uuid_from_reminders.toString().split("-"));
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

    public void set_alarm_manager(Context main_context,int notification_count){
        RemindersModel reminders_model=fetch_earliest_reminder();
        //check conditionally if the alarm is even nearer than previous alarm, lest ignore
        AlarmManager alarm_manager=(AlarmManager)main_context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        //does alarm manager work if created under the pretext from a different class from the views of the app?

        Intent intent = new Intent(main_context.getApplicationContext(), AlarmReceiver.class); //needs intent from view class or main activity?

        intent.putExtra("ReminderNameAndTime",String.valueOf(reminders_model.get_reminder_name()+","+String.valueOf(reminders_model.get_reminder_date_time().getTime())));

        // put code here to process multiple amount of reminders at the same time
        PendingIntent pending_intent=PendingIntent.getBroadcast(main_context,notification_count,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,reminders_model.get_reminder_date_time().getTimeInMillis(),pending_intent);
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
        RemindersModel temporary_reminder=this.reminder_list.get(element_wanted);
        temporary_reminder.set_reminder_name(string_name);
        this.sort_reminders();
        this.reminder_list.set(element_wanted,temporary_reminder);
    }

    protected void sort_reminders(){
        this.quick_sort_helper(this.reminder_list,0,this.reminder_list.size()-1);
        //move this code in to other place/presenter s
    }

    public ArrayList<RemindersModel> sort_reminders(ArrayList<RemindersModel> list){
        this.quick_sort_helper(list,0,list.size()-1);
        //move this code in to other place/presenter s
        return list;
    }

    private void quick_sort_helper(ArrayList<RemindersModel> reminder_list,int left_index,int right_index){
        if(left_index<right_index){
            int split_point=this.quick_sort_partition(reminder_list,left_index,right_index);

            this.quick_sort_helper(reminder_list,left_index,split_point-1);
            this.quick_sort_helper(reminder_list,split_point+1,right_index);
        }

    }

    private int quick_sort_partition(ArrayList<RemindersModel> reminder_list,int left_index,int right_index){
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


}
