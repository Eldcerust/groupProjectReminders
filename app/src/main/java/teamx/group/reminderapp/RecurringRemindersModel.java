package teamx.group.reminderapp;

import java.util.Calendar;
import java.util.Date;

public class RecurringRemindersModel extends RemindersModel{
    private int number_of_repetition;
    private int days_of_repetition;
    private boolean micro_reminders;

    public RecurringRemindersModel(String reminder_name, Calendar reminder_date_time, VoiceProfileModel reminder_voice_profile, int number_repetition, int days_repetition, boolean micro_reminders) {
        super(reminder_name,reminder_date_time);
        super.set_reminder_voice_profile(reminder_voice_profile);
        this.number_of_repetition=number_repetition;
        this.days_of_repetition=days_repetition;
        this.micro_reminders=micro_reminders;
    }

    public int get_days_of_repetition(){return this.days_of_repetition;}

    public int get_number_of_repetition(){return this.number_of_repetition;}

    public void set_days_of_repetition(int days_repeated_required){this.days_of_repetition=number_of_repetition;}

    public void set_numbers_of_repetition(int numbers_repeated){this.number_of_repetition=numbers_repeated;}

    @Override
    public String return_type(){return "RecurringReminders";}
}


