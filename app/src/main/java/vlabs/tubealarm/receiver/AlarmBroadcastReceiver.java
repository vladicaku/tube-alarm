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
        TubeAlarmService.rescheduleAlarm(context, intent);
        TubeAlarmService.showAlarm(context, intent);
    }
}
