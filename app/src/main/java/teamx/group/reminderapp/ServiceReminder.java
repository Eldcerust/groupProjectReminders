package teamx.group.reminderapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServiceReminder extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        android.os.Debug.waitForDebugger();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] input = intent.getStringArrayExtra("Pomodoro");
        int[] pomodoroArray={Integer.valueOf(input[4]),Integer.valueOf(input[5]),Integer.valueOf(input[6]),Integer.valueOf(input[7])};
        SimpleDateFormat format=new SimpleDateFormat("HH:mm");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        for(int repeatBreaks=0;repeatBreaks<=pomodoroArray[3];repeatBreaks++){
            Calendar now=Calendar.getInstance();
            if(repeatBreaks==pomodoroArray[3]){
                long longBreakStart=now.getTimeInMillis();
                now.add(Calendar.MINUTE,pomodoroArray[2]);
                String longBreakTime=format.format(now.getTime());
                long longBreakEnd=now.getTimeInMillis();

                Notification longBreak = new NotificationCompat.Builder(this, "channel1")
                        .setContentTitle("Break Time")
                        .setContentText("Long break time ending at "+longBreakTime)
                        .setSmallIcon(R.raw.clock)
                        .setContentIntent(pendingIntent)
                        .build();
                startForeground(1,longBreak);
                try{
                    Thread.sleep(longBreakEnd-longBreakStart);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopForeground(true);

            }else{
                String startingAt=format.format(now.getTime());
                long startingTime=now.getTimeInMillis();

                now.add(Calendar.MINUTE,pomodoroArray[0]);
                String endingAt=format.format(now.getTime());
                long endingTime=now.getTimeInMillis();

                Notification notification = new NotificationCompat.Builder(this, "channel1")
                        .setContentTitle("Work Time")
                        .setContentText("Work time ending at "+endingAt)
                        .setSmallIcon(R.raw.clock)
                        .setContentIntent(pendingIntent)
                        .build();

                startForeground(1, notification);
                try {
                    Thread.sleep(endingTime-startingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopForeground(true);

                now.add(Calendar.MINUTE,pomodoroArray[1]);
                String endingShortBreakAt=format.format(now.getTime());
                long shortBreakMiliseconds=now.getTimeInMillis();

                Notification breakNotification = new NotificationCompat.Builder(this, "channel1")
                        .setContentTitle("Break Time")
                        .setContentText("Short Break time ending at "+endingShortBreakAt)
                        .setSmallIcon(R.raw.clock)
                        .setContentIntent(pendingIntent)
                        .build();

                startForeground(1,breakNotification);
                try {
                    Thread.sleep(shortBreakMiliseconds-endingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
