package vlabs.tubealarm.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import vlabs.tubealarm.R;
import vlabs.tubealarm.config.YoutubeConfig;
import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;
import vlabs.tubealarm.service.TubeAlarmService;

public class AlarmFragment extends Fragment {

    public static interface AlarmFragmentListener {
        void onSaveClick();

        void onCancelClick();
    }

    AlarmFragmentListener alarmFragmentListener;
    AlarmDatabaseHelper alarmDatabaseHelper = null;
    Alarm alarm;
    private int alarmId;

    Switch enabled;
    Button saveButton;
    Button cancelButton;
    TimePicker timePicker;
    ToggleButton monday;
    ToggleButton tuesday;
    ToggleButton wednesday;
    ToggleButton thursday;
    ToggleButton friday;
    ToggleButton saturday;
    ToggleButton sunday;
    CheckBox repeatWeekly;
    Switch vibrate;
    EditText youtubeUrl;
    EditText message;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        try {
            alarmFragmentListener = (AlarmFragmentListener) getActivity();
        } catch (Exception ex) {
            throw new ClassCastException(getActivity().toString() + " must implement AlarmFragmentListener");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmDatabaseHelper = new AlarmDatabaseHelper(getActivity());

        enabled = (Switch) getView().findViewById(R.id.alarm_fragment_enabled);
        saveButton = (Button) getView().findViewById(R.id.alarm_fragment_save_button);
        cancelButton = (Button) getView().findViewById(R.id.alarm_fragment_cancel_button);
        timePicker = (TimePicker) getView().findViewById(R.id.alarm_fragment_time_picker);
        monday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_monday);
        tuesday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_tuesday);
        wednesday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_wednesday);
        thursday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_thursday);
        friday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_friday);
        saturday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_saturday);
        sunday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_sunday);
        repeatWeekly = (CheckBox) getView().findViewById(R.id.alarm_fragment_repeat_weekly);
        vibrate = (Switch) getView().findViewById(R.id.alarm_fragment_vibrate);
        youtubeUrl = (EditText) getView().findViewById(R.id.alarm_fragment_youtube_url);
        message = (EditText) getView().findViewById(R.id.alarm_fragment_message);

        enabled.setChecked(true);
        youtubeUrl.setText(YoutubeConfig.VIDEO_ID);

        loadAlarm();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (youtubeUrl.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "You must enter YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                alarm.setEnabled(enabled.isChecked());
                alarm.setFormat24(true);
                alarm.setHours(timePicker.getCurrentHour());
                alarm.setMinutes(timePicker.getCurrentMinute());
                alarm.setMonday(monday.isChecked());
                alarm.setTuesday(tuesday.isChecked());
                alarm.setWednesday(wednesday.isChecked());
                alarm.setThursday(thursday.isChecked());
                alarm.setFriday(friday.isChecked());
                alarm.setSaturday(saturday.isChecked());
                alarm.setSunday(sunday.isChecked());
                alarm.setRepeatWeekly(repeatWeekly.isChecked());
                alarm.setVibrate(vibrate.isChecked());
                alarm.setYoutubeUrl(youtubeUrl.getText().toString());
                alarm.setMessage(message.getText().toString());

                if (alarm.isNew()) {
                    int newId = alarmDatabaseHelper.save(alarm);
                    alarm.setId(newId);
                } else {
                    alarmDatabaseHelper.update(alarm);
                }

                alarmId = alarm.getId();
                TubeAlarmService.setAlarm(getActivity(), alarm.getId());
                alarmFragmentListener.onSaveClick();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmFragmentListener.onCancelClick();
            }
        });
    }

    public void loadAlarm() {
        if (alarmId != -1) {
            alarm = alarmDatabaseHelper.get(alarmId);
            enabled.setChecked(alarm.getEnabled());
            timePicker.setCurrentHour(alarm.getHours());
            timePicker.setCurrentMinute(alarm.getMinutes());
            monday.setChecked(alarm.getMonday());
            tuesday.setChecked(alarm.getTuesday());
            wednesday.setChecked(alarm.getWednesday());
            thursday.setChecked(alarm.getThursday());
            friday.setChecked(alarm.getFriday());
            saturday.setChecked(alarm.getSaturday());
            sunday.setChecked(alarm.getSunday());
            repeatWeekly.setChecked(alarm.getRepeatWeekly());
            vibrate.setChecked(alarm.getVibrate());
            youtubeUrl.setText(alarm.getYoutubeUrl());
            message.setText(alarm.getMessage());
        } else {
            alarm = new Alarm();
        }
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}
