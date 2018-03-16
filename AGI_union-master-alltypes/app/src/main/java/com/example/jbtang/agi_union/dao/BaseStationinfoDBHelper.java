package com.example.jbtang.agi_union.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 刘洋旭 on 2017/7/28.
 */
public class BaseStationinfoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "basestationinfo";

    public BaseStationinfoDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public BaseStationinfoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY, lat TEXT, lon TEXT, address TEXT, pci TEXT, tai TEXT" +
                ", earfcn TEXT, ecgi TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
