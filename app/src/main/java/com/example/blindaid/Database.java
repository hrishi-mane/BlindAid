package com.example.blindaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bus.db";
    private static final String TABLE_NAME = "bus_data";
    private static final String COL_2 = "SOURCE";
    private static final String COL_3 = "DESTINATION";
    private static final String COL_4 = "BUS_NO";
    private static final String COL_5 = "BUS_TIME";
    private static final String COL_6 = "ARRIVAL_TIME";
    private static final String COL_7 = "DEPARTURE_TIME";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (SOURCE STRING , DESTINATION STRING , BUS_NO INTEGER PRIMARY KEY , BUS_TIME STRING , ARRIVAL_TIME STRING , DEPARTURE_TIME STRING )" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String Source, String Destination, String Bus_No, String Bus_Time, String Arrival_Time, String Departure_Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Source);
        contentValues.put(COL_3,Destination);
        contentValues.put(COL_4,Bus_No);
        contentValues.put(COL_5,Bus_Time);
        contentValues.put(COL_6,Arrival_Time);
        contentValues.put(COL_7,Departure_Time);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateData(String Source, String Destination, String Bus_No, String Bus_Time, String Arrival_Time, String Departure_Time ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Source);
        contentValues.put(COL_3,Destination);
        contentValues.put(COL_4,Bus_No);
        contentValues.put(COL_5,Bus_Time);
        contentValues.put(COL_6,Arrival_Time);
        contentValues.put(COL_7,Departure_Time);
        db.update(TABLE_NAME, contentValues, " Bus_No = ? ", new String[] { Bus_No } );
        return true;

    }

    public Integer deleteData( String Bus_No ){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, " Bus_No = ? ", new String[] { Bus_No });
    }

    public Cursor retrieveData(String Source, String Destination){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * From bus_data where SOURCE = ? AND DESTINATION = ?", new String[]{ Source, Destination });
        return res;

    }

}







