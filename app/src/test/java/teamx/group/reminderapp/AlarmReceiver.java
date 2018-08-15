package teamx.group.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID=101;
    private static final String TAG="RemindersNotification";

    public AlarmReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent){
        
    }
}
