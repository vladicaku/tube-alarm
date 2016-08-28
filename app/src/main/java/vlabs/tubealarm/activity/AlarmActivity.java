package vlabs.tubealarm.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import vlabs.tubealarm.R;
import vlabs.tubealarm.fragment.AlarmFragment;

public class AlarmActivity extends Activity implements AlarmFragment.AlarmFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);

        int alarmId = getIntent().getIntExtra("id", -1);
        ((AlarmFragment)getFragmentManager().findFragmentById(R.id.alarm_activity_alarm_fragment)).setAlarmId(alarmId);

//        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, ShowAlarmActivity.class);
//        intent.putExtra("id", 1);
//        intent.putExtra("youtube", "asdf");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, Intent.FILL_IN_ACTION);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, 10, 10, pendingIntent);
    }

    @Override
    public void onSaveClick() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", "save");
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCancelClick() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", "cancel");
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }
}
