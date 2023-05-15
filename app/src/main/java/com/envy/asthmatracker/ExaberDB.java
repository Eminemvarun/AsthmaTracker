package com.envy.asthmatracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class ExaberDB {
    private static final String KEY1_ID = "_id";
    private static final String KEY2_ID = "_id";
    private static final String KEY1_DATE = "_date";
    private static final String KEY2_DATE = "_date";

    private static final String KEY1_INTENSITY = "_intensity";
    private static final String KEY2_NAME = "_name";

    private static final String KEY1_NOTES = "_notes";
    private static final String KEY2_PUFFS = "_puffs";

    private static final String KEY2_NOTES = "_notes";
    private static final String DATABASE_NAME = "ExaberDB";
    private static final String DATABASE_TABLE_ONE = "Exaberations";
    private static final String DATABASE_TABLE_TWO = "InhalerUse";
    private final int DATABASE_VERSION = 1;
    private final Context ourContext;
    private DBHelper ourHelper;
    private SQLiteDatabase myDatabase;

    public ExaberDB(Context context) {
        this.ourContext = context;
    }

    public void open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        myDatabase = ourHelper.getWritableDatabase();
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String date, int intensity, String notes) {
        ContentValues cv = new ContentValues();
        cv.put(KEY1_DATE, date);
        cv.put(KEY1_INTENSITY, intensity);
        cv.put(KEY1_NOTES, notes);
        Log.i("vlogs", "createEntry: Data to be added" + date + intensity + notes);
        return myDatabase.insert(DATABASE_TABLE_ONE, null, cv);
    }

    public long createEntry(String date, String name, int puffs, String notes) {
        ContentValues cv = new ContentValues();
        cv.put(KEY2_DATE, date);
        cv.put(KEY2_NAME, name);
        cv.put(KEY2_PUFFS, puffs);
        cv.put(KEY2_NOTES,notes);
        Log.i("vlogs", "createEntry: Data to be added" +date +name+puffs +notes);
        return myDatabase.insert(DATABASE_TABLE_TWO, null, cv);
    }

    public String getData(int id) {
        if (id == 1) {
            String[] cols = new String[]{KEY1_ID, KEY1_DATE, KEY1_INTENSITY, KEY1_NOTES};
            Cursor cursor = myDatabase.query(DATABASE_TABLE_ONE, cols, null, null, null, null, "_date DESC");
            String Result = "";

            int irowid = cursor.getColumnIndex(KEY1_ID);
            int idateid = cursor.getColumnIndex(KEY1_DATE);
            int iintensity = cursor.getColumnIndex(KEY1_INTENSITY);
            int inotes = cursor.getColumnIndex(KEY1_NOTES);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Result = Result + cursor.getString(irowid) + " "
                        + cursor.getString(idateid) + " "
                        + cursor.getString(iintensity) + " "
                        + cursor.getString(inotes) + "\n";
            }
            cursor.close();
            return Result;
        }
        if (id == 2) {
            String[] cols = new String[]{KEY2_DATE, KEY2_NAME, KEY2_PUFFS,KEY2_NOTES};
            Cursor cursor = myDatabase.query(DATABASE_TABLE_TWO, cols, null, null, null, null, "_date DESC");
            String Result = "";

            int idateid = cursor.getColumnIndex(KEY2_DATE);
            int iname = cursor.getColumnIndex(KEY2_NAME);
            int iintensity = cursor.getColumnIndex(KEY2_PUFFS);
            int inotes = cursor.getColumnIndex(KEY2_NOTES);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Result = Result + cursor.getString(idateid) + " "
                        + cursor.getString(iname) + " "
                        + cursor.getString(iintensity) + " "
                        + cursor.getString(inotes) + "\n";
            }
            cursor.close();
            return Result;
        } else return null;
    }

    public void deleteData(int i) {
        if (i == 1) {
            myDatabase.execSQL("DELETE FROM " + DATABASE_TABLE_ONE);
            Log.i("vlogs", "deleteData: Code Ran");
        }
        if (i == 2) {
            myDatabase.execSQL("DELETE FROM " + DATABASE_TABLE_TWO);
            Log.i("vlogs", "deleteData2: Code Ran");
        } else {
            Log.i("vlogs", "deleteData: ERROR");
        }
    }

    public void deleteThisData(int i,String date){
        if(i==2) {
            myDatabase.delete(DATABASE_TABLE_TWO,"_date = " +date , null);
        }

        if(i==1) {
            myDatabase.delete(DATABASE_TABLE_ONE,"_date = " +date , null);
        }
    }

    public String getLatestRecordDateOne() {
        String[] cols = new String[]{KEY1_DATE};
        String result = "";
        Cursor cursor = myDatabase.query(DATABASE_TABLE_ONE, cols, null, null, null, null, "_date DESC", "1");
        int idateid = cursor.getColumnIndex(KEY1_DATE);
        Log.i("vlogs", "Cursor Count is " + cursor.getCount());
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(idateid);
            cursor.close();
        }
        return result;
    }

    public String getLatestRecordDateTwo() {
        String[] cols = new String[]{KEY1_DATE};
        String result = "";
        Cursor cursor = myDatabase.query(DATABASE_TABLE_TWO, cols, null, null, null, null, "_date DESC", "1");
        int idateid = cursor.getColumnIndex(KEY2_DATE);
        Log.i("vlogs", "Cursor Count is " + cursor.getCount());
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(idateid);
            cursor.close();
        }
        return result;
    }
    public int getThisWeekCount(int i) {
        if(i==1) {
            int result = 0;
            Cursor cursor = myDatabase.rawQuery("Select COUNT(_id) FROM Exaberations WHERE _date >= date('now', '-7 days')", null);
            Log.i("vlogs", "Cursor Count is " + cursor.getCount());
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = cursor.getInt(0);
            }
            cursor.close();
            Log.i("vlog", "getThisWeekCount: " + result);
            return result;
        } else if(i==2){
            int result = 0;
            Cursor cursor = myDatabase.rawQuery("Select COUNT(_id) FROM InhalerUse WHERE _date >= date('now', '-7 days')", null);
            Log.i("vlogs", "Cursor Count is " + cursor.getCount());
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = cursor.getInt(0);
            }
            cursor.close();
            Log.i("vlog", "getThisWeekCount: " + result);
            return result;
        }else return 0;
    }

    public int getLastWeekCount(int i) {
        if(i==1) {
            int result = 0;
            Cursor cursor = myDatabase.rawQuery("Select COUNT(_id) FROM Exaberations " +
                    "WHERE _date >= date('now', '-14 days') AND " +
                    "_date < date('now', '-7 days')", null);
            Log.i("vlogs", "Cursor Count is " + cursor.getCount());
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = cursor.getInt(0);
            }
            cursor.close();
            Log.i("vlog", "getThisWeekCount: " + result);
            return result;
        }else if(i==2){
            int result = 0;
            Cursor cursor = myDatabase.rawQuery("Select COUNT(_id) FROM InhalerUse " +
                    "WHERE _date >= date('now', '-14 days') AND " +
                    "_date < date('now', '-7 days')", null);
            Log.i("vlogs", "Cursor Count is " + cursor.getCount());
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result = cursor.getInt(0);
            }
            cursor.close();
            Log.i("vlog", "getThisWeekCount: " + result);
            return result;
        }else return 0;
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String SQLCode = "CREATE TABLE " + DATABASE_TABLE_ONE +
                    " (" + KEY1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY1_DATE + " TEXT NOT NULL, " +
                    KEY1_INTENSITY + " INTEGER NOT NULL, " +
                    KEY1_NOTES + " TEXT NOT NULL);";
            String SQLCode2 = "CREATE TABLE " + DATABASE_TABLE_TWO +
                    " (" + KEY2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY2_DATE + " TEXT NOT NULL, " +
                    KEY2_NAME + " TEXT NOT NULL, " +
                    KEY2_NOTES + " TEXT NOT NULL," +
                    KEY2_PUFFS + " INTEGER NOT NULL);";
            db.execSQL(SQLCode);
            db.execSQL(SQLCode2);
            Log.i("vlogs", "Table Created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ONE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TWO);
            onCreate(db);
        }
    }
}
