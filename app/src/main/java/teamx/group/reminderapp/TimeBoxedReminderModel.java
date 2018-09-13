package teamx.group.reminderapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public int get_work_session(){return this.work_session;}

    public int get_short_break_session(){return this.short_break_session;}

    public int get_long_break_session(){return this.long_break_session;}

    public int get_short_to_long_transition(){return this.short_to_long_transition;}

    public void set_work_session(int work_session_desired){this.work_session=work_session_desired;}

    public void set_short_break_sesssion(int short_break_session_desired){this.short_break_session=short_break_session_desired;}

    public void set_long_break_session(int long_break_session_desired){this.long_break_session=long_break_session_desired;}

    public void set_short_to_long_transition(int transition_desired){this.short_to_long_transition=transition_desired;}
}
