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
        boolean later = intent.getBooleanExtra("later", false);
        int id = intent.getIntExtra("id", -1);
        if (!later) {
            TubeAlarmService.rescheduleAlarm(context, id);
        }
        TubeAlarmService.showAlarm(context, id);
    }
}
