package teamx.group.reminderapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RecurringRemindersModel extends RemindersModel{
    private int number_of_repetition;
    private int days_of_repetition;
    private boolean micro_reminders;

    public RecurringRemindersModel(String reminder_name, Calendar reminder_date_time, VoiceProfileModel reminder_voice_profile, ArrayList<CheckBoxListSingle> check_boxes, int number_repetition, int days_repetition, boolean micro_reminders) {
        super(reminder_name,reminder_date_time,check_boxes);
        super.set_reminder_voice_profile(reminder_voice_profile);
        this.number_of_repetition=number_repetition;
        this.days_of_repetition=days_repetition;
        this.micro_reminders=micro_reminders;
    }

    public RecurringRemindersModel(String name,Calendar reminder_date_time,ArrayList<CheckBoxListSingle> a){
        super(name,reminder_date_time,a);
        this.number_of_repetition=0;
        this.days_of_repetition=0;
        this.micro_reminders=false;
    }

    public RecurringRemindersModel(String name,Calendar reminder_date_time){
        super(name,reminder_date_time);
        this.number_of_repetition=0;
        this.days_of_repetition=0;
        this.micro_reminders=false;
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
    public ArrayList<CheckBoxListSingle> get_checkbox_list() {
        return super.get_checkbox_list();
    }

    @Override
    public void set_reminder_name(String string) {
        super.set_reminder_name(string);
    }

    public int get_days_of_repetition(){return this.days_of_repetition;}

    public int get_number_of_repetition(){return this.number_of_repetition;}

    public void set_days_of_repetition(int days_repeated_required){this.days_of_repetition=number_of_repetition;}

    public void set_numbers_of_repetition(int numbers_repeated){this.number_of_repetition=numbers_repeated;}

    @Override
    public String return_type(){return "RecurringReminders";}
}


