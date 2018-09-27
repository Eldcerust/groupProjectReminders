package teamx.group.reminderapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TimeBoxedReminderModel extends RemindersModel {
    private int work_session;
    private int short_break_session;
    private int long_break_session;
    private int short_to_long_transition;

    public TimeBoxedReminderModel(String reminder_name, Calendar reminder_date_time, ArrayList<CheckBoxListSingle> check_boxes, int work_session_given, int short_break_session_given, int long_break_session_given, int short_to_long_transition_given) {
        super(reminder_name, reminder_date_time,check_boxes);
        this.work_session=work_session_given;
        this.short_break_session=short_break_session_given;
        this.long_break_session=long_break_session_given;
        this.short_to_long_transition=short_to_long_transition_given;
    }

    public TimeBoxedReminderModel(String reminder_name, Calendar reminder_date_time, ArrayList<CheckBoxListSingle> bulletin_list) {
        super(reminder_name, reminder_date_time, bulletin_list);
    }

    public TimeBoxedReminderModel(String reminder_name, Calendar reminder_date_time) {
        super(reminder_name, reminder_date_time);
    }

    @Override
    public void set_reminder_name(String string) {
        super.set_reminder_name(string);
    }

    @Override
    public void set_list(ArrayList<CheckBoxListSingle> list_set) {
        super.set_list(list_set);
    }

    @Override
    public void set_reminder_date_time(Calendar date) {
        super.set_reminder_date_time(date);
    }

    @Override
    public void set_reminder_voice_profile(VoiceProfileModel voice_profile) {
        super.set_reminder_voice_profile(voice_profile);
    }

    @Override
    public void set_reminder_UUID(UUID uuid) {
        super.set_reminder_UUID(uuid);
    }

    @Override
    public String get_reminder_name() {
        return super.get_reminder_name();
    }

    @Override
    public Calendar get_reminder_date_time() {
        return super.get_reminder_date_time();
    }

    @Override
    public UUID get_reminder_UUID() {
        return super.get_reminder_UUID();
    }

    @Override
    public VoiceProfileModel get_reminder_voice_profile() {
        return super.get_reminder_voice_profile();
    }

    @Override
    public String return_type() {
        return "Time Boxed Reminders";
    }

    @Override
    public ArrayList<CheckBoxListSingle> get_checkbox_list() {
        return super.get_checkbox_list();
    }

    public int get_work_session(){return this.work_session;}

    public int get_short_break_session(){return this.short_break_session;}

    public int get_long_break_session(){return this.long_break_session;}

    public int get_short_to_long_transition(){return this.short_to_long_transition;}

    public void set_work_session(int work_session_desired){this.work_session=work_session_desired;}

    public void set_short_break_sesssion(int short_break_session_desired){this.short_break_session=short_break_session_desired;}

    public void set_long_break_session(int long_break_session_desired){this.long_break_session=long_break_session_desired;}

    public void set_short_to_long_transition(int transition_desired){this.short_to_long_transition=transition_desired;}
}
