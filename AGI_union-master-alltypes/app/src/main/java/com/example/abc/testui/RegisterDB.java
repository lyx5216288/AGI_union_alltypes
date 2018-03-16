package com.example.abc.testui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by abc on 2017/9/16.
 */

public class RegisterDB extends SQLiteOpenHelper{

    public static final String REGISTER_DB_PATH = "/.ProgramData";  //数据库路径
    public static final String REGISTER_DB_FILE = "ProgramDataV2.db";  //数据库文件
    public static final String TABLE_NAME = "registerMsg";  //数据库中的表明
    public static final int version = 2; //数据库版本

    private static String storagePath;  //数据库文件存储绝对路径
    private static String name;  //数据库文件全名

    static {
        storagePath = Environment.getExternalStorageDirectory().toString()+REGISTER_DB_PATH;
        File file = new File(storagePath);
        if (!file.exists()){
            file.mkdirs();
        }

        name = storagePath + "/" + REGISTER_DB_FILE;
    }


    public RegisterDB(Context context) {
        super(context, name, null, version);
        Log.e("RegisterDB=", name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s(id INTEGER PRIMARY KEY NOT NULL, name TEXT, value TEXT)", TABLE_NAME);

//        String sql = "CREATE TABLE if not EXISTS \"reg\" (\n" +
//                "\"id\"  INTEGER NOT NULL,\n" +
//                "\"name\"  TEXT,\n" +
//                "\"value\"  TEXT\n" +
//                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // 从数据库中读取参数 name 对应的 value 值
    public String getValue(String name){
        SQLiteDatabase readDB = this.getReadableDatabase();

        String res = null;
        Cursor cursor = readDB.query(TABLE_NAME, null, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndex("value"));
        }

        //readDB.close();

        return res;
    }

    // 向数据库中参数 name 对应的 value 写入值
    public void setValue(String name, String value){

        SQLiteDatabase writeDB = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("value", value);

        Cursor cursor = null;
        try{
            cursor = writeDB.query(TABLE_NAME, null, "name=?", new String[]{name}, null, null, null);
            if (cursor.moveToNext()){
                //存在
                writeDB.update(TABLE_NAME, cv, "name=?", new String[]{name});
            }
            else{
                //不存在
                writeDB.insert(TABLE_NAME, null, cv);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //writeDB.close();
    }
}
