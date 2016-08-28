package vlabs.tubealarm.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vlabs.tubealarm.R;
import vlabs.tubealarm.model.Alarm;

/**
 * Created by User on 8/26/2016.
 */
public class AlarmListAdapter extends ArrayAdapter<Alarm> {

    public static interface AlarmListAdapterListener {
        void onIndicatorClick(Alarm alarm);
    }

    private AlarmListAdapterListener alarmListAdapterListener;

    public AlarmListAdapter(Context context, int resource, List<Alarm> items, AlarmListAdapterListener alarmListAdapterListener) {
        super(context, resource, items);
        this.alarmListAdapterListener = alarmListAdapterListener;
    }

    public AlarmListAdapter(Context context, int resource, int textViewResourceId, List<Alarm> objects, AlarmListAdapterListener alarmListAdapterListener) {
        super(context, resource, textViewResourceId, objects);
        this.alarmListAdapterListener = alarmListAdapterListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.alarm_item, null);
        }

        final Alarm alarm = getItem(position);
        if (alarm != null) {
            ((TextView) view.findViewById(R.id.alarm_item_time)).setText(String.format("%02d:%02d", alarm.getHours(), alarm.getMinutes()));

            TextView monday = ((TextView) view.findViewById(R.id.alarm_item_day_monday));
            TextView tuesday = ((TextView) view.findViewById(R.id.alarm_item_day_tuesday));
            TextView wednesday = ((TextView) view.findViewById(R.id.alarm_item_day_wednesday));
            TextView thursday = ((TextView) view.findViewById(R.id.alarm_item_day_thursday));
            TextView friday = ((TextView) view.findViewById(R.id.alarm_item_day_friday));
            TextView saturday = ((TextView) view.findViewById(R.id.alarm_item_day_saturday));
            TextView sunday = ((TextView) view.findViewById(R.id.alarm_item_day_sunday));

            if (alarm.getMonday() == true) {
                monday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                monday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getTuesday() == true) {
                tuesday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                tuesday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getWednesday() == true) {
                wednesday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                wednesday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getTuesday() == true) {
                thursday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                thursday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getFriday() == true) {
                friday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                friday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getSaturday() == true) {
                saturday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                saturday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            if (alarm.getSunday() == true) {
                sunday.setTextColor(getContext().getResources().getColor(R.color.alarm_item_active_day));
            } else {
                sunday.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
            }

            ImageView indicator = (ImageView) view.findViewById(R.id.alarm_item_indicator);
            if (alarm.getEnabled()) {
                indicator.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_online));
            } else {
                indicator.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_invisible));
            }

            /* Image click */
            indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmListAdapterListener.onIndicatorClick(alarm);
                }
            });
        }

        return view;
    }
}
