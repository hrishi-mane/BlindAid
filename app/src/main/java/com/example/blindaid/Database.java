package com.example.blindaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
   //Database and Table Name
    private static final String DATABASE_NAME = "bus.db";
    private static final String TABLE_NAME1 = "contact_data";

    //TABLE COLUMN NAMES
    private static final String COL_8 = "FULL_NAME";
    private static final String COL_9 = "PHONE_NUMBER";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql2 = "create table " + TABLE_NAME1 + " (FULL_NAME STRING , PHONE_NUMBER STRING PRIMARY KEY );";
        String[] statements = new String[]{sql2};

        for(String sql : statements){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        onCreate(db);

    }

    public boolean insertContact(String FullName, String Phone_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(COL_8,FullName);
        contentValues1.put(COL_9,Phone_no);

        long res = db.insert(TABLE_NAME1, null, contentValues1);

        if(res == -1)
            return false;
        else
            return true;
    }

    public Cursor retrieveContact(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res1 = db.rawQuery("Select * From contact_data where FULL_NAME = ?", new String[]{ name });
        return res1;
    }

}







