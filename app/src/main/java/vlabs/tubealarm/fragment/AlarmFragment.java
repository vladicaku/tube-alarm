package vlabs.tubealarm.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import vlabs.tubealarm.R;
import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;

public class AlarmFragment extends Fragment {

    AlarmDatabaseHelper alarmDatabaseHelper = null;
    long alarmId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmDatabaseHelper = new AlarmDatabaseHelper(getActivity());

        final Button saveButton = (Button) getView().findViewById(R.id.alarm_fragment_save_button);
        final TimePicker timePicker = (TimePicker) getView().findViewById(R.id.alarm_fragment_time_picker);
        final ToggleButton monday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_monday);
        final ToggleButton tuesdey = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_tuesday);
        final ToggleButton wednesday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_wednesday);
        final ToggleButton thursday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_thursday);
        final ToggleButton friday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_friday);
        final ToggleButton saturday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_saturday);
        final ToggleButton sunday = (ToggleButton) getView().findViewById(R.id.alarm_fragment_day_sunday);
        final CheckBox repeatWeekly = (CheckBox) getView().findViewById(R.id.alarm_fragment_repeat_weekly);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Pre Save called", Toast.LENGTH_SHORT).show();

                Alarm newAlarm = new Alarm();
                //alarm.setFormat24(timePicker.is24HourView());
                newAlarm.setFormat24(true);
                newAlarm.setHours(timePicker.getCurrentHour());
                newAlarm.setMinutes(timePicker.getCurrentMinute());
                newAlarm.setMonday(monday.isChecked());
                newAlarm.setTuesday(tuesdey.isChecked());
                newAlarm.setWednesday(wednesday.isChecked());
                newAlarm.setThursday(thursday.isChecked());
                newAlarm.setFriday(friday.isChecked());
                newAlarm.setSaturday(saturday.isChecked());
                newAlarm.setSunday(sunday.isChecked());
                newAlarm.setRepeatWeekly(repeatWeekly.isChecked());

                if (alarmId == -1) {
                    alarmId = alarmDatabaseHelper.save(newAlarm);
                } else {
                    newAlarm.setId(alarmId);
                    alarmDatabaseHelper.update(newAlarm);
                }

                Toast.makeText(getActivity(), "Save called", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
