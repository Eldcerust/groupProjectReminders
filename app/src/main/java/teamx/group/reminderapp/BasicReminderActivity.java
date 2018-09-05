package teamx.group.reminderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BasicReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_reminder);
    }

    public interface receive_reminders_data{
        public RemindersModel receive_reminders_data(RemindersModel reminder_item);
    }
}
