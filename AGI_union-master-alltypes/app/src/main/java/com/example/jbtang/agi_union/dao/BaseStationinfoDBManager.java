package com.example.jbtang.agi_union.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jbtang.agi_union.core.CellInfo;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDAO;
import com.example.jbtang.agi_union.dao.lbscellinfos.LbsCellInfoDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘洋旭 on 2017/7/28.
 */
public class BaseStationinfoDBManager {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    public BaseStationinfoDBManager(Context context){
        helper = new BaseStationinfoDBHelper(context);
        db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BaseStationinfoDBHelper.TABLE_NAME +
                "(id INTEGER PRIMARY KEY, lat TEXT, lon TEXT, address TEXT, pci INTEGER, tai INTEGER" +
                ", earfcn INTEGER, ecgi INTEGER)");
    }

    public void add(List<BaseStationinfo> baseinfos){
        db.beginTransaction();
        try{
            int i = 0;
            for(BaseStationinfo baseinfo : baseinfos){
                db.execSQL("INSERT INTO " + BaseStationinfoDBHelper.TABLE_NAME + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{i,baseinfo.lat,baseinfo.lon,baseinfo.address,baseinfo.pci,baseinfo.tai,baseinfo.earfcn,baseinfo.ecgi});
                i++;
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
    }
    public List<BaseStationinfo> listDB(){
        String sql = "SELECT * FROM " + BaseStationinfoDBHelper.TABLE_NAME;
        final Cursor c = db.rawQuery(sql,new String[]{});
        List<BaseStationinfo> baseinfos = new ArrayList<>();
        while(c.moveToNext()){
            String  lat = c.getString(c.getColumnIndex("lat"));
            String  lon = c.getString(c.getColumnIndex("lon"));
            String  address = c.getString(c.getColumnIndex("address"));
            String  pci = c.getString(c.getColumnIndex("pci"));
            String  tai = c.getString(c.getColumnIndex("tai"));
            String  earfcn = c.getString(c.getColumnIndex("earfcn"));
            String  ecgi = c.getString(c.getColumnIndex("ecgi"));
            BaseStationinfo baseinfo = new BaseStationinfo();
            baseinfo.setLat(lat);
            baseinfo.setLon(lon);
            baseinfo.setAddress(address);
            baseinfo.setPci(pci);
            baseinfo.setTai(tai);
            baseinfo.setEarfcn(earfcn);
            baseinfo.setEcgi(ecgi);
            baseinfos.add(baseinfo);
        }
        c.close();
        return baseinfos;
    }
    public void clear(){
        db.execSQL("DELETE FROM " + BaseStationinfoDBHelper.TABLE_NAME);
    }
    public void closeDB(){
        db.close();
    }

}
