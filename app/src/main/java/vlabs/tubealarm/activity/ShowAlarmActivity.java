package vlabs.tubealarm.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vlabs.tubealarm.R;
import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;
import vlabs.tubealarm.service.TubeAlarmService;

public class ShowAlarmActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private ConnectivityManager connectivityManager;
    private AlarmDatabaseHelper alarmDatabaseHelper;
    private Alarm alarm;
    private TextView time;
    private TextView message;
    private Button ok;
    private Button later;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_alarm_activity);

        connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        alarmDatabaseHelper = new AlarmDatabaseHelper(this);
        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
        activity = this;

        time = (TextView) findViewById(R.id.show_alarm_activity_time);
        message = (TextView) findViewById(R.id.show_alarm_activity_message);
        ok = (Button) findViewById(R.id.show_alarm_activity_ok);
        later = (Button) findViewById(R.id.show_alarm_activity_later);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        boolean later = intent.getBooleanExtra("later", false);

        if (id == -1) {
            closeApp();
            return;
        }

        alarm = alarmDatabaseHelper.get(id);

        if (!alarm.getEnabled()) {
            closeApp();
            return;
        }

        if (!later) {
            time.setText(String.format("%02d:%02d", alarm.getHours(), alarm.getMinutes()));
        } else {
            Calendar calendar = Calendar.getInstance();
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            time.setText(timeFormat.format(calendar.getTime()));
        }
        message.setText(alarm.getMessage());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeApp();
                return;
            }
        });

        this.later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TubeAlarmService.laterAlarm(activity, alarm.getId());
                closeApp();
                return;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (alarm.getVibrate()) {
            long[] pattern = {0, 200, 0};
            vibrator.vibrate(pattern, 0);
        }
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    private void closeApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private boolean checkConnection() {
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
