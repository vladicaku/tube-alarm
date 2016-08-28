package vlabs.tubealarm.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;
import java.util.List;

import vlabs.tubealarm.model.Alarm;
import vlabs.tubealarm.receiver.AlarmBroadcastReceiver;
import vlabs.tubealarm.repo.AlarmDatabaseHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class TubeAlarmService extends IntentService {
    private static final String ACTION_SET_ALARM = "vlabs.tubealarm.service.action.SET_ALARM";
    private static final String ACTION_DELETE_ALARM = "vlabs.tubealarm.service.action.DELETE_ALARM";
    private static final String ACTION_UPDATE_ALARM = "vlabs.tubealarm.service.action.UPDATE_ALARM";
    private static final String ACTION_SET_ALL_ALARMS_ON_BOOT = "vlabs.tubealarm.service.action.SET_ALL_ALARMS_ON_BOOT";

    private static final String EXTRA_NEW_ALARM = "vlabs.tubealarm.service.extra.NEW_ALARM";
    private static final String EXTRA_OLD_ALARM = "vlabs.tubealarm.service.extra.OLD_ALARM";
    private static final String EXTRA_ALARM_ID = "vlabs.tubealarm.service.extra.ALARM_ID";

    private static Context serviceContext;
    private static AlarmDatabaseHelper alarmDatabaseHelper;
    private static AlarmManager alarmManager;

    public TubeAlarmService() {
        super("TubeAlarmService");
        serviceContext = this;
        //initService();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        initService();
    }

    private void initService() {
        if (alarmDatabaseHelper == null) {
            alarmDatabaseHelper = new AlarmDatabaseHelper(serviceContext);
        }
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(serviceContext.ALARM_SERVICE);
        }
    }

    /* If the service is already performing a task this action will be queued. */
    public static void setAlarm(Context context, Integer id) {
        Intent intent = new Intent(context, TubeAlarmService.class);
        intent.setAction(ACTION_SET_ALARM);
        intent.putExtra(EXTRA_ALARM_ID, id);
        context.startService(intent);
    }

    public static void deleteAlarm(Context context, Integer id) {
        Intent intent = new Intent(context, TubeAlarmService.class);
        intent.setAction(ACTION_DELETE_ALARM);
        intent.putExtra(EXTRA_ALARM_ID, id);
        context.startService(intent);
    }

    public static void updateAlarm(Context context, Integer id) {
        Intent intent = new Intent(context, TubeAlarmService.class);
        intent.setAction(ACTION_UPDATE_ALARM);
        intent.putExtra(EXTRA_ALARM_ID, id);
        context.startService(intent);
    }

    public static void setAllAlarmsOnBoot(Context context) {
        Intent intent = new Intent(context, TubeAlarmService.class);
        intent.setAction(ACTION_SET_ALL_ALARMS_ON_BOOT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initService();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_ALARM.equals(action)) {
                Integer id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
                handleSetAlarm(id);
                System.out.println("ID " + id);
            } else if (ACTION_UPDATE_ALARM.equals(action)) {
                Integer id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
                System.out.println("ID " + id);
                handleUpdateAlarm(id);
            } else if (ACTION_DELETE_ALARM.equals(action)) {
                Integer id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
                handleDeleteAlarm(id);
            } else if (ACTION_SET_ALL_ALARMS_ON_BOOT.equals(action)) {
                handleSetAllAlarmsOnBoot();
            }
        }
    }

    private void setAlarmForDay(int day, Alarm alarm) {
        if ((day == Calendar.MONDAY) && (!alarm.getMonday())) {
            return;
        } else if ((day == Calendar.MONDAY) && (!alarm.getMonday())) {
            return;
        } else if ((day == Calendar.TUESDAY) && (!alarm.getTuesday())) {
            return;
        } else if ((day == Calendar.WEDNESDAY) && (!alarm.getWednesday())) {
            return;
        } else if ((day == Calendar.THURSDAY) && (!alarm.getThursday())) {
            return;
        } else if ((day == Calendar.FRIDAY) && (!alarm.getFriday())) {
            return;
        } else if ((day == Calendar.SATURDAY) && (!alarm.getSaturday())) {
            return;
        } else if ((day == Calendar.SUNDAY) && (!alarm.getSunday())) {
            return;
        }

        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(now)) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(serviceContext, AlarmBroadcastReceiver.class);
        intent.putExtra("id", alarm.getId());
        intent.putExtra("youtubeUrl", alarm.getYoutubeUrl());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(serviceContext,  alarm.getId()*10 + day, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO
        // !!! IMPORTANT !!!
        // Repeat alarm in receiver
//        if (alarm.getRepeatWeekly()) {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
//        } else {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    private void setAlarmForAllDays(Alarm alarm) {
        setAlarmForDay(Calendar.MONDAY, alarm);
        setAlarmForDay(Calendar.TUESDAY, alarm);
        setAlarmForDay(Calendar.WEDNESDAY, alarm);
        setAlarmForDay(Calendar.THURSDAY, alarm);
        setAlarmForDay(Calendar.FRIDAY, alarm);
        setAlarmForDay(Calendar.SATURDAY, alarm);
        setAlarmForDay(Calendar.SUNDAY, alarm);
    }

    private void deleteAlarmForDay(int day, Alarm alarm) {
        Intent intent = new Intent(serviceContext, AlarmBroadcastReceiver.class);    //OneShotAlarm is the broadcast receiver you use for alarm
        intent.putExtra("id", alarm.getId());
        intent.putExtra("youtubeUrl", alarm.getYoutubeUrl());
        PendingIntent sender = PendingIntent.getBroadcast(serviceContext, alarm.getId()*10 + day, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(sender);
    }

    private void deleteAlarmForAllDays(Alarm alarm) {
        deleteAlarmForDay(Calendar.MONDAY, alarm);
        deleteAlarmForDay(Calendar.TUESDAY, alarm);
        deleteAlarmForDay(Calendar.WEDNESDAY, alarm);
        deleteAlarmForDay(Calendar.THURSDAY, alarm);
        deleteAlarmForDay(Calendar.FRIDAY, alarm);
        deleteAlarmForDay(Calendar.SATURDAY, alarm);
        deleteAlarmForDay(Calendar.SUNDAY, alarm);
    }

    /* Set Alarm */
    private void handleSetAlarm(Integer id) {
        Alarm alarm = alarmDatabaseHelper.get(id);
        deleteAlarmForAllDays(alarm);
        setAlarmForAllDays(alarm);
    }

    /* Update Alarm */
    private void handleUpdateAlarm(Integer id) {
        Alarm alarm = alarmDatabaseHelper.get(id);
        deleteAlarmForAllDays(alarm);
        setAlarmForAllDays(alarm);
    }

    /* Delete Alarm */
    private void handleDeleteAlarm(Integer id) {
        Alarm alarm = alarmDatabaseHelper.get(id);
        deleteAlarmForAllDays(alarm);
    }

    /* Boot */
    private void handleSetAllAlarmsOnBoot() {
        List<Alarm> list = alarmDatabaseHelper.getAll();
        for (Alarm alarm : list) {
            setAlarmForAllDays(alarm);
        }
    }
}
