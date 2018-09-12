package teamx.group.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ActualMainAndNotificationActivity extends AppCompatActivity{
    //this will be the activity responsible for creation of the notification

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Intent launcher_intent=new Intent(this, MainActivity.class);
        startActivity(launcher_intent);
    }
}
