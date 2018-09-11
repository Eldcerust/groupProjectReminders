package teamx.group.reminderapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class AlarmReceive extends BroadcastReceiver{

    private NotificationManager notif_mgr;
    private ArrayList<SimpleReminderHolder> list_of_reminders=new ArrayList<SimpleReminderHolder>();
    @Override
    public void onReceive(Context context, Intent intent) {
        //use arrays to seperate between reminders

        String[] intent_alarm_receive=intent.getStringArrayExtra("AlarmCreated");
        SimpleReminderHolder holder=new SimpleReminderHolder();
        holder.reminder_name=intent_alarm_receive[0];
        holder.reminder_time=intent_alarm_receive[1];
        holder.reminder_type=intent_alarm_receive[2];

        list_of_reminders.add(holder);

    }

    static class SimpleReminderHolder{
        String reminder_name;
        String reminder_time;
        String reminder_type;
    }
}
