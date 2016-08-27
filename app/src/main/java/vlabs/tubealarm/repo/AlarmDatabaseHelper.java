package vlabs.tubealarm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vlabs.tubealarm.model.Alarm;

/**
 * Created by User on 8/26/2016.
 */
public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    /**
     * !!! Important !!!
     * Opening and close db in onResume and onPause is dangerous. Consider a query is being running while the activity goes to Paused state!
     */

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tubeAlarm";

    // Table name
    private static final String TABLE_ALARM = "alarms";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_HOURS = "hours";
    private static final String KEY_MINUTES = "minutes";
    private static final String KEY_FORMAT_24 = "format24";
    private static final String KEY_AM = "am";
    private static final String KEY_MONDAY = "monday";
    private static final String KEY_TUESDAY = "tuesday";
    private static final String KEY_WEDNESDAY = "wednesday";
    private static final String KEY_THURSDAY = "thursday";
    private static final String KEY_FRIDAY = "friday";
    private static final String KEY_SATURDAY = "saturday";
    private static final String KEY_SUNDAY = "sunday";
    private static final String KEY_REPEAT_WEEKLY = "repeatWeekly";
    private static final String KEY_YOUTUBE_URL = "youtubeUrl";
    private static final String KEY_ENABLED = "enabled";

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ALARM + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HOURS + " INTEGER," + KEY_MINUTES + " INTEGER,"
                + KEY_FORMAT_24 + " NUMERIC," + KEY_AM + " NUMERIC,"
                + KEY_MONDAY + " NUMERIC," + KEY_TUESDAY + " NUMERIC,"
                + KEY_WEDNESDAY + " NUMERIC," + KEY_THURSDAY + " NUMERIC,"
                + KEY_FRIDAY + " NUMERIC," + KEY_SATURDAY + " NUMERIC,"
                + KEY_SUNDAY + " NUMERIC,"
                + KEY_REPEAT_WEEKLY + " NUMERIC,"
                + KEY_YOUTUBE_URL + " TEXT,"
                + KEY_ENABLED + " NUMERIC" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        // Create tables again
        onCreate(db);
    }

    // Save entity
    public long save(Alarm alarm) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HOURS, alarm.getHours());
        values.put(KEY_MINUTES, alarm.getMinutes());
        values.put(KEY_FORMAT_24, alarm.getFormat24());
        values.put(KEY_AM, alarm.getAm());
        values.put(KEY_MONDAY, alarm.getMonday());
        values.put(KEY_TUESDAY, alarm.getTuesday());
        values.put(KEY_WEDNESDAY, alarm.getWednesday());
        values.put(KEY_THURSDAY, alarm.getThursday());
        values.put(KEY_FRIDAY, alarm.getFriday());
        values.put(KEY_SATURDAY, alarm.getSaturday());
        values.put(KEY_SUNDAY, alarm.getSunday());
        values.put(KEY_REPEAT_WEEKLY, alarm.getRepeatWeekly());
        values.put(KEY_YOUTUBE_URL, alarm.getYoutubeUrl());
        values.put(KEY_ENABLED, alarm.getEnabled());

        long id = database.insert(TABLE_ALARM, null, values);
        alarm.setId(id);

        //database.close();
        return id;
    }

    // Get single
    public Alarm get(int id) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_ALARM, new String[]{
                KEY_ID,
                KEY_HOURS,
                KEY_MINUTES,
                KEY_FORMAT_24,
                KEY_AM,
                KEY_MONDAY,
                KEY_TUESDAY,
                KEY_WEDNESDAY,
                KEY_THURSDAY,
                KEY_FRIDAY,
                KEY_SATURDAY,
                KEY_SUNDAY,
                KEY_REPEAT_WEEKLY,
                KEY_YOUTUBE_URL,
                KEY_ENABLED
        }, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Alarm alarm = new Alarm();
        alarm.setId(cursor.getLong(0));
        alarm.setHours(cursor.getInt(1));
        alarm.setMinutes(cursor.getInt(2));
        alarm.setFormat24(cursor.getInt(3) != 0);
        alarm.setAm(cursor.getInt(5) != 0);
        alarm.setMonday(cursor.getInt(5) != 0);
        alarm.setTuesday(cursor.getInt(6) != 0);
        alarm.setWednesday(cursor.getInt(7) != 0);
        alarm.setThursday(cursor.getInt(8) != 0);
        alarm.setFriday(cursor.getInt(9) != 0);
        alarm.setSaturday(cursor.getInt(10) != 0);
        alarm.setSunday(cursor.getInt(11) != 0);
        alarm.setRepeatWeekly(cursor.getInt(12) != 0);
        alarm.setYoutubeUrl(cursor.getString(13));
        alarm.setEnabled(cursor.getInt(14) != 0);

        cursor.close();
        //database.close();
        return alarm;
    }

    // Get all
    public List<Alarm> getAll() {
        List<Alarm> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getLong(0));
                alarm.setHours(cursor.getInt(1));
                alarm.setMinutes(cursor.getInt(2));
                alarm.setFormat24(cursor.getInt(3) != 0);
                alarm.setAm(cursor.getInt(5) != 0);
                alarm.setMonday(cursor.getInt(5) != 0);
                alarm.setTuesday(cursor.getInt(6) != 0);
                alarm.setWednesday(cursor.getInt(7) != 0);
                alarm.setThursday(cursor.getInt(8) != 0);
                alarm.setFriday(cursor.getInt(9) != 0);
                alarm.setSaturday(cursor.getInt(10) != 0);
                alarm.setSunday(cursor.getInt(11) != 0);
                alarm.setRepeatWeekly(cursor.getInt(12) != 0);
                alarm.setYoutubeUrl(cursor.getString(13));
                alarm.setEnabled(cursor.getInt(14) != 0);
                list.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        //database.close();
        return list;
    }

    // Count items
    public int count() {
        String countQuery = "SELECT  COUNT(*) FROM " + TABLE_ALARM;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int count = cursor.getInt(0);

        cursor.close();
        //database.close();
        return count;
    }

    // Update single
    public int update(Alarm alarm) {
        SQLiteDatabase database = this.getWritableDatabase();

        // TODO
        // To be refactored. Use same method to put values in update() and save()
        ContentValues values = new ContentValues();
        values.put(KEY_HOURS, alarm.getHours());
        values.put(KEY_MINUTES, alarm.getMinutes());
        values.put(KEY_FORMAT_24, alarm.getFormat24());
        values.put(KEY_AM, alarm.getAm());
        values.put(KEY_MONDAY, alarm.getMonday());
        values.put(KEY_TUESDAY, alarm.getTuesday());
        values.put(KEY_WEDNESDAY, alarm.getWednesday());
        values.put(KEY_THURSDAY, alarm.getThursday());
        values.put(KEY_FRIDAY, alarm.getFriday());
        values.put(KEY_SATURDAY, alarm.getSaturday());
        values.put(KEY_SUNDAY, alarm.getSunday());
        values.put(KEY_REPEAT_WEEKLY, alarm.getRepeatWeekly());
        values.put(KEY_YOUTUBE_URL, alarm.getYoutubeUrl());
        values.put(KEY_ENABLED, alarm.getEnabled());

        return database.update(TABLE_ALARM, values, KEY_ID + " = ?", new String[] { String.valueOf(alarm.getId()) });
    }

    // Delete entity
    public void delete(Alarm alarm) {
        delete(alarm.getId());
    }

    // Delete by id
    public void delete(Long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_ALARM, KEY_ID + " = ?", new String[] {String.valueOf(id)});
        //database.close();
    }
}
