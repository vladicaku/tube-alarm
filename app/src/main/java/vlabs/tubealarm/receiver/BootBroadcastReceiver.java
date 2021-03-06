package vlabs.tubealarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import vlabs.tubealarm.service.TubeAlarmService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot detected by Alarm", Toast.LENGTH_SHORT).show();
        TubeAlarmService.setAllAlarmsOnBoot(context);
    }
}
