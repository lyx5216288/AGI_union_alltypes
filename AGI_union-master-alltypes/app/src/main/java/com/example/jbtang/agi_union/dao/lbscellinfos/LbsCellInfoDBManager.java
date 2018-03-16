package com.example.jbtang.agi_union.dao.lbscellinfos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jbtang.agi_union.core.CellInfo;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDAO;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘洋旭 on 2017/7/17.
 */
public class LbsCellInfoDBManager {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public LbsCellInfoDBManager(Context context){
        helper = new LbsCellInfoDBHelper(context);
        db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LbsCellInfoDBHelper.TABLE_NAME +
                "(id INTEGER PRIMARY KEY, earfcn INTEGER, pci INTEGER, tai INTEGER, ecgi INTEGER)");
    }
    public void add(List<CellInfo> cellInfos){
        db.beginTransaction();
        try{
            int i = 0;
            for(CellInfo cellInfo : cellInfos){
                db.execSQL("INSERT INTO " + LbsCellInfoDBHelper.TABLE_NAME + " VALUES(?, ?, ?, ?, ?)",
                        new Object[]{i,cellInfo.earfcn,cellInfo.pci,cellInfo.tai,cellInfo.ecgi});
                i++;
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
    }
    public void insert(CellInfoDAO dao){
        db.execSQL("INSERT INTO " + LbsCellInfoDBHelper.TABLE_NAME + "VALUES(?,?,?,?,?)",
                new Object[]{dao.id,dao.earfcn,dao.pci,dao.tai,dao.ecgi});
    }
    public List<CellInfoDAO> listDB(){
        String sql = "SELECT * FROM " + LbsCellInfoDBHelper.TABLE_NAME;
        final Cursor c = db.rawQuery(sql,new String[]{});
        List<CellInfoDAO> cellInfoDAOs = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(c.getColumnIndex("id"));
            int earfcn = c.getInt(c.getColumnIndex("earfcn"));
            Short pci = c.getShort(c.getColumnIndex("pci"));
            Short tai = c.getShort(c.getColumnIndex("tai"));
            int ecgi = c.getInt(c.getColumnIndex("ecgi"));
            CellInfoDAO cellInfo = new CellInfoDAO(id,earfcn,pci,tai,ecgi);
            cellInfoDAOs.add(cellInfo);
        }
        c.close();
        return cellInfoDAOs;
    }
    public void clear(){
        db.execSQL("DELETE FROM " + LbsCellInfoDBHelper.TABLE_NAME);
    }
    public void closeDB(){
        db.close();
    }

}
