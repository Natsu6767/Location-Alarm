package com.cfd.map.mohit.locationalarm.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Arihant Jain on 2/3/2017.
 */

public class AlarmDatabase extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "AlarmDatabase.DB";
    public static final String TABLE_NAME = "GeoAlarm";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_RING_NAME = "RING_NAME";
    public static final String COL_VIB = "VIB";
    public static final String COL_LONG = "LONG";
    public static final String COL_LATI = "LATI";
    //private Ringtone mRingtone;
    public static final String COL_RAD = "RAD";
    public static final String COL_TIME = "TIME";
    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_NAME + " TEXT,"
                +COL_RING_NAME + " TEXT,"
                + COL_VIB + " BOOL,"
                + COL_LONG + " DOUBLE,"
                + COL_LATI + " DOUBLE,"
                + COL_RAD + " INTEGER,"
                + COL_RAD + " LONG" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(GeoAlarm geoAlarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME,geoAlarm.getName());
        contentValues.put(COL_RING_NAME,geoAlarm.getRingtoneName());
        contentValues.put(COL_VIB,geoAlarm.getVibration());
        LocationCoordiante locationCoordiante = geoAlarm.getmLocationCoordinate();
        contentValues.put(COL_LATI,locationCoordiante.getLatitude());
        contentValues.put(COL_LONG,locationCoordiante.getLongitude());
        contentValues.put(COL_RAD,geoAlarm.getRadius());
        contentValues.put(COL_TIME,geoAlarm.getTime());
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d("add data","" + result);
        if(result ==-1)
            return false;
        else
            return true;
    }
    public String getAllData(){ SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        if(res.getCount() == 0){
            //show message
            return "" ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("NAME :"+ res.getString(1)+"\n");
        }
        // show all data
        return  buffer.toString();
    }
    public boolean updateData(GeoAlarm geoAlarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME,geoAlarm.getName());
        contentValues.put(COL_RING_NAME,geoAlarm.getRingtoneName());
        contentValues.put(COL_VIB,geoAlarm.getVibration());
        LocationCoordiante locationCoordiante = geoAlarm.getmLocationCoordinate();
        contentValues.put(COL_LATI,locationCoordiante.getLatitude());
        contentValues.put(COL_LONG,locationCoordiante.getLongitude());
        contentValues.put(COL_RAD,geoAlarm.getRadius());
        contentValues.put(COL_TIME,geoAlarm.getTime());
        long result = db.update(TABLE_NAME,contentValues,"id = ?",new String[]{geoAlarm.getmId()});
        if(result!=-1){
            return true;
        }
        return false;
    }
    public Integer delete(String Id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{Id});
    }
}
