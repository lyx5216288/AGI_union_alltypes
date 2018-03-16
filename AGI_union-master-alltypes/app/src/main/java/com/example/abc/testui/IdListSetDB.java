package com.example.abc.testui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by abc on 2017/9/1.
 */

public class IdListSetDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "idEdit";
    public static final String DB_NAME = "idList";

    public IdListSetDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s(imsi INTEGER PRIMARY KEY NOT NULL, name TEXT DEFAULT NONE)", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
