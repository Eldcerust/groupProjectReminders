package teamx.group.reminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{

    private NotificationManager notif_mgr; //for android Oreo
    private static final int NOTIFICATION_ID=101;
    private static final String TAG="SlightlyDifferentReminder";
    protected volatile Context contextFromMain;


    @Override
    public void onReceive(Context context, Intent intent) {
        //use arrays to seperate between reminders
        String[] intent_alarm_receive=intent.getStringArrayExtra("oneReminder");
        if(intent_alarm_receive[2].equals("Basic Reminders")) {
            NotificationCompat.Builder build_notifications=create_notification(context, "channe11", ActualMainAndNotificationActivity.class, intent_alarm_receive);
            NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(TAG,NOTIFICATION_ID,build_notifications.build());
        } else if(intent_alarm_receive[2].equals("Recurring Reminders")){
            //String array should have time, work session,
            SimpleDateFormat newFormat=new SimpleDateFormat("EEEEEEEEE");
            String day=newFormat.format(Calendar.getInstance().getTime()).replaceAll("\\s+","");
            String[] arrayOfNotif={intent_alarm_receive[0],intent_alarm_receive[1]+" "+day,intent_alarm_receive[2]};
            NotificationCompat.Builder build_notifications=create_notification(context, "channe11", ActualMainAndNotificationActivity.class, arrayOfNotif);
            NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(TAG,NOTIFICATION_ID,build_notifications.build());
        } else if(intent_alarm_receive[2].equals("Time Boxed Reminders")){
            
        }
        this.contextFromMain=context;
        // put logic that allows what code to be detected and what to do
    }

    public NotificationCompat.Builder create_notification(Context context, String channel_id, Class itemclass, String[] array){
        Intent intent=new Intent(context,itemclass);
        PendingIntent intent_followup=PendingIntent.getActivity(context,NOTIFICATION_ID,intent,0);

        NotificationCompat.Builder build_notification=new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.raw.clock)
                .setContentTitle("Reminders Notice")
                .setContentText(array[0]+"  "+array[1]+"      "+array[2])
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(intent_followup)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000});

        return build_notification;

        // does notificationManager even exists if the build sdk is below it
        // use notificationCompat

    }

    static class SimpleReminderHolder{
        String reminder_name;
        String reminder_time;
        String reminder_type;
        String reminder_uuid;
    }
}
