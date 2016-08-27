package vlabs.tubealarm.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vlabs.tubealarm.R;
import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;

public class AlarmListFragment extends ListFragment implements AdapterView.OnItemClickListener, AlarmListAdapter.AlarmListAdapterListener {

    AlarmDatabaseHelper alarmDatabaseHelper = null;
    List<Alarm> list = null;
    AlarmListAdapter alarmListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmDatabaseHelper = new AlarmDatabaseHelper(getActivity());
        list = alarmDatabaseHelper.getAll();
        alarmListAdapter = new AlarmListAdapter(getActivity(), R.layout.alarm_item, R.layout.alarm_item, list, this);
//        List<Alarm> alarms = alarmDatabaseHelper.getAll();
//        arrayAdapter = new ArrayAdapter<Alarm>(this, null, R.layout.alarm_item, )
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(alarmListAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIndicatorClick(Alarm alarm) {
        alarmDatabaseHelper.update(alarm);
        alarmListAdapter.notifyDataSetChanged();
    }

}
