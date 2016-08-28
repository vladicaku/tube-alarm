package vlabs.tubealarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import vlabs.tubealarm.activity.ShowAlarmActivity;
import vlabs.tubealarm.service.TubeAlarmService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(context, ShowAlarmActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
