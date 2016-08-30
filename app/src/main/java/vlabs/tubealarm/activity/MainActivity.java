package vlabs.tubealarm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import vlabs.tubealarm.R;
import vlabs.tubealarm.fragment.AlarmListFragment;
import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;

public class MainActivity extends Activity implements AlarmListFragment.AlarmListFragmentListener {

    AlarmDatabaseHelper alarmDatabaseHelper = null;

    Button mbutton;
    private YouTubePlayerView myouTubePlayerView;
    private YouTubePlayer.OnInitializedListener monInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

//        myouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
//        monInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.loadVideo("UxP3OT3lphc");
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };
//
//        mbutton = (Button) findViewById(R.id.button_play_youtube_video);
//        mbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myouTubePlayerView.initialize(API_KEY, monInitializedListener);
//            }
//        });
    }

    @Override
    public void onAlarmClick(Alarm alarm) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("id", alarm.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options_menu_add_alarm) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,"Result get from activity " + requestCode + " - " + resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                ((AlarmListFragment) getFragmentManager().findFragmentById(R.id.activity_main_alarm_list_fragment)).reload();
            }
        }
    }
}
