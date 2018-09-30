package teamx.group.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class ActualMainAndNotificationActivity extends AppCompatActivity{
    //this will be the activity responsible for creation of the notification
    public static AlarmReceiver receiver=new AlarmReceiver();
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        Intent launcher_intent=new Intent(this, MainActivity.class);
        Intent notification_intent=new Intent(this,receiver.getClass());
        startActivity(launcher_intent);
        ContextCompat.startForegroundService(this,notification_intent);
    }
}
