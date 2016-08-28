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
    int alarmId = -1;
    Alarm alarm;

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

        final Switch enabled = (Switch) getView().findViewById(R.id.alarm_fragment_enabled);
        final Button saveButton = (Button) getView().findViewById(R.id.alarm_fragment_save_button);
        final Button cancelButton = (Button) getView().findViewById(R.id.alarm_fragment_cancel_button);
        final TimePicker timePicker = (TimePicker) getView().findViewById(R.id.alarm_fragment_time_picker);
        final ToggleButton monday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_monday);
        final ToggleButton tuesday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_tuesday);
        final ToggleButton wednesday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_wednesday);
        final ToggleButton thursday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_thursday);
        final ToggleButton friday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_friday);
        final ToggleButton saturday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_saturday);
        final ToggleButton sunday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_sunday);
        final CheckBox repeatWeekly = (CheckBox) getView().findViewById(R.id.alarm_fragment_repeat_weekly);
        final Switch vibrate = (Switch) getView().findViewById(R.id.alarm_fragment_vibrate);
        final EditText youtubeUrl = (EditText) getView().findViewById(R.id.alarm_fragment_youtube_url);
        final EditText message = (EditText) getView().findViewById(R.id.alarm_fragment_message);

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
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
                    TubeAlarmService.setAlarm(getActivity(), alarm.getId());
                } else {
                    alarmDatabaseHelper.update(alarm);
                    TubeAlarmService.updateAlarm(getActivity(), alarmId);
                }
//                if (alarmId == -1) {
//                    alarmId =
//                    alarm.setId(alarmId);
//                    TubeAlarmService.setAlarm(getActivity(), alarmId);
//                } else {
//                    alarm.setId(alarmId);
//                    alarmDatabaseHelper.update(alarm);
//                    TubeAlarmService.updateAlarm(getActivity(), alarmId);
//                }

                Toast.makeText(getActivity(), "Save called", Toast.LENGTH_SHORT).show();
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

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}
