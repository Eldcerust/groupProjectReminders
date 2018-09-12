package teamx.group.reminderapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver{

    private NotificationManager notif_mgr; //for android Oreo
    private NotificationManagerCompat old_notif_mgr;
    private ArrayList<SimpleReminderHolder> list_of_reminders;
    private static final int NOTIFICATION_ID=101;
    private static final int NOTIFICIATION_ID_PERMANENT=102;
    private static final String TAG="SlightlyDifferentReminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        //use arrays to seperate between reminders
        this.list_of_reminders=new ArrayList<SimpleReminderHolder>();
        String[] intent_alarm_receive=intent.getStringArrayExtra("AlarmCreated");
        if(intent_alarm_receive[0].equals("oneReminder")) {
            SimpleReminderHolder holder=new SimpleReminderHolder();
            holder.reminder_name = intent_alarm_receive[1];
            holder.reminder_time = intent_alarm_receive[2];
            holder.reminder_type = intent_alarm_receive[3];
            list_of_reminders.add(holder);
        }else if(intent_alarm_receive[0].equals("multipleReminder")){
            for(int a=0;a<(intent_alarm_receive.length-1)/3;a++){
                SimpleReminderHolder holder=new SimpleReminderHolder();
                holder.reminder_name=intent_alarm_receive[1+3*a];
                holder.reminder_name=intent_alarm_receive[2+3*a];
                holder.reminder_name=intent_alarm_receive[3+3*a];
                list_of_reminders.add(holder);
            }
        }
        create_notification(context,"SlightDiffReminder",ActualMainAndNotificationActivity.class);
        // put logic that allows what code to be detected and what to do
    }

    public void create_notification(Context context, String channel_id, Class itemclass){
        Intent intent=new Intent(context,itemclass);
        PendingIntent intent_followup=PendingIntent.getActivity(context,NOTIFICATION_ID,intent,0);

        NotificationCompat.Builder build_notification=new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.raw.clock)
                .setContentTitle(construct_plural_string(this.list_of_reminders))
                .setContentText(construct_notification_multitline(this.list_of_reminders))
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(intent_followup)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000});

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            this.notif_mgr.notify(TAG,NOTIFICATION_ID,build_notification.build());
        }else{
            this.old_notif_mgr.notify(NOTIFICATION_ID,build_notification.build());
        }

        // does notificationManager even exists if the build sdk is below it
        // use notificationCompat

    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SlightlyDifferentReminders";
            String description = "Just another variation of reminders.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("SlightDiffReminder", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            this.notif_mgr = context.getSystemService(NotificationManager.class);
            this.notif_mgr.createNotificationChannel(channel);
        } else {
            this.old_notif_mgr = NotificationManagerCompat.from(context);
        }
    }

    public String construct_plural_string(ArrayList<SimpleReminderHolder> list){
        if(list.size()>1){
            return(String.valueOf(list.size())+" reminders need your attention.");
        }else{
            return(String.valueOf(list.size())+" reminder needs your attention");
        }
    }

    public String construct_notification_multitline(ArrayList<SimpleReminderHolder> list){
        // we must construct additional P Y L O N S
        String long_line_of_text="";
        for(int a=0;a<list.size();a++){
            SimpleReminderHolder temp=list.get(a);
            if(a>0){
                long_line_of_text=long_line_of_text.concat("\n");
            }
            long_line_of_text=long_line_of_text.concat(temp.reminder_time+"  "+temp.reminder_name);
        }
        return long_line_of_text;
    }

    static class SimpleReminderHolder{
        String reminder_name;
        String reminder_time;
        String reminder_type;
    }
}
