package com.example.abc.testui;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.example.abc.testui.R;
import com.example.abc.testui.src.WriteBLogDeal;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IdListSetActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private List<IdListDatas> listIdListDatas;
    private IdListSetDB idListSetDB;
    private int position;
    private int position_selected;
    private View view_selected;
    private IdListSetAdapter idListSetAdapter;
    private RecyclerView rv;
    private long imsi_old;
    private RealTimeService.MyBinder myBinder;

    private IdListSetLog idListSetLog;
    private WriteBLogDeal writeBLogDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);

        if (Global.UI_FORMAT == Global.UI_V1){
            setContentView(R.layout.activity_id_list_set);
        }
        else{
//            setContentView(R.layout.activity_id_list_set_v2);
        }


        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        idListSetLog = new IdListSetLog();
        writeBLogDeal = new WriteBLogDeal();
        init_toolbar();
        //在绑定服务后，再调用 initIdList 方法

        findViewById(R.id.btnAdd_id_list_set_id).setOnClickListener(this);
        findViewById(R.id.btnDelete_id_list_set_id).setOnClickListener(this);
        findViewById(R.id.btnEdit_id_list_set_id).setOnClickListener(this);
        findViewById(R.id.btnExport_id_list_set_id).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, IdListAddActivity.class);

        switch (v.getId()){
            case R.id.btnAdd_id_list_set_id:
                intent.putExtra("requestType", 1);  //请求类型为1，表示添加
                startActivityForResult(intent, 1);
                break;
            case R.id.btnDelete_id_list_set_id:
                if (position_selected == -1){
                    Toast.makeText(this, "请选中要删除的行中任意单元", Toast.LENGTH_SHORT).show();
                }
                else if (position!=0) {
                    view_selected.setBackgroundColor(ContextCompat.getColor(IdListSetActivity.this, R.color.white));

                    SQLiteDatabase deleteData = idListSetDB.getWritableDatabase();
                    IdListDatas idListDatas = listIdListDatas.get(position_selected);
                    String imsi_delete = String.format("%d", idListDatas.getImsi());
                    deleteData.delete(IdListSetDB.TABLE_NAME, "imsi=?", new String[]{imsi_delete});
                    deleteData.close();

                    //删除 不记录日志
//                    idListSetLog.writeLog(IdListSetLog.TYPE_DELETE, idListDatas);

                    for (int i=position_selected+1; i<listIdListDatas.size(); i++){
                        listIdListDatas.get(i).setNo(i);
                    }

                    listIdListDatas.remove(position_selected);
                    idListSetAdapter.removeItem(position_selected);
                    position--;
                    position_selected = -1;
                }
                break;
            case R.id.btnEdit_id_list_set_id:
                if (position_selected == -1){
                    Toast.makeText(this, "请选中要编辑的行中任意单元", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("requestType", 2);  //请求类型为2，表示编辑
                    intent.putExtra("position", position_selected);
                    imsi_old = listIdListDatas.get(position_selected).getImsi();
                    intent.putExtra("imsi", listIdListDatas.get(position_selected).getImsi());
                    intent.putExtra("name", listIdListDatas.get(position_selected).getName());
                    startActivityForResult(intent, 2);
                }
                break;
            case R.id.btnExport_id_list_set_id:
                export2file();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:  //确定 返回
                long imsi = data.getLongExtra("imsi", 0);
                String name = data.getStringExtra("name");
                int edit_position = data.getIntExtra("position", 0);
                if (imsi<100000000000000L || imsi>999999999999999L){
                    Toast.makeText(this, "IMSI必须为15位的数字", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (requestCode == 1) {  //添加
                        if (uniqueCheck(imsi) != 0) {
                            Toast.makeText(this, "IMSI不能重复", Toast.LENGTH_SHORT).show();
                        } else {
                            IdListDatas idListDatas = new IdListDatas();
                            idListDatas.setNo(listIdListDatas.size()+1);
                            idListDatas.setImsi(imsi);
                            idListDatas.setName(name);
                            listIdListDatas.add(idListDatas);
                            idListSetAdapter.addItem(position);
                            rv.scrollToPosition(position);
                            position++;

                            SQLiteDatabase writeDB = idListSetDB.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("imsi", imsi);
                            cv.put("name", name);
                            writeDB.insert(IdListSetDB.TABLE_NAME, null, cv);
                            writeDB.close();

//                            idListSetLog.writeLog(IdListSetLog.TYPE_ADD, idListDatas);
                            idListSetLog.writeBLog(writeBLogDeal.GetBytes(Global.username, imsi));
                        }
                    } else if (requestCode == 2) {  //编辑
                        position_selected = -1;
                        if (imsi != imsi_old && uniqueCheck(imsi) != 0) {
                            Toast.makeText(this, "IMSI不能重复", Toast.LENGTH_SHORT).show();
                        } else {
                            listIdListDatas.get(edit_position).setImsi(imsi);
                            listIdListDatas.get(edit_position).setName(name);
                            idListSetAdapter.changeItem(edit_position);

                            SQLiteDatabase updateDB = idListSetDB.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("imsi", imsi);
                            cv.put("name", name);
                            String imsi_old_str = String.format("%d", imsi_old);
                            updateDB.update(IdListSetDB.TABLE_NAME, cv, "imsi=?", new String[]{imsi_old_str});
                            updateDB.close();
                        }
                    }
                }
                break;
            case 2:  //取消 返回
                break;
        }
    }

    public int uniqueCheck(long imsi){
        int count = 0;
        for (IdListDatas idListDatas:listIdListDatas){
            if (idListDatas.getImsi()==imsi){
                count = 1;
                break;
            }
        }

        return count;
    }

    public void init_toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.idListSet_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 标题上的回退按钮的点击处理
                finish();
            }
        });
    }

    public void initIdList(){
        position = 0;
        position_selected = -1;

        // 设置名单编辑界面中的 RecyclerView 的布局
        rv = (RecyclerView) findViewById(R.id.rv_id_list_set_id);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        listIdListDatas = new ArrayList<>();
        idListSetAdapter = new IdListSetAdapter(this, listIdListDatas);
        rv.setAdapter(idListSetAdapter);

        //设置 RecyclerView 的点击事件
        idListSetAdapter.setRVItemClickListenerInterface(new IdListSetAdapter.RVItemClickListenerInterface() {
            @Override
            public void onClick(View v, int position) {
                if (view_selected!=null){
                    view_selected.setBackgroundColor(ContextCompat.getColor(IdListSetActivity.this, R.color.white));
                }
                v.setBackgroundColor(ContextCompat.getColor(IdListSetActivity.this, R.color.gray));
                view_selected = v;
                position_selected = position;
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        });

        idListSetAdapter.addItem(position);
        position = listIdListDatas.size();
        rv.scrollToPosition(position-1);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (RealTimeService.MyBinder) service;
        myBinder.getRealTimeService().setCallbackIdListSet(new RealTimeService.CallbackIdListSet() {
            @Override
            public void onConnect(List<IdListDatas> listIdListDatas, IdListSetDB idListSetDB) {
                IdListSetActivity.this.listIdListDatas = listIdListDatas;
                IdListSetActivity.this.idListSetDB = idListSetDB;
            }
        });

        myBinder.onIdListSetInit();
        initIdList();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 将名单导出到文件中
     */
    private String SD_PATH = Environment.getExternalStorageDirectory().toString();
    private void export2file(){
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileDir = SD_PATH + File.separator + "信号采集";
        String fileName = fileDir + File.separator + "名单_" + dateStr + ".txt";

        File dir = new File(fileDir);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("名称,IMSI");
            bufferedWriter.newLine();

            for (IdListDatas item:listIdListDatas){
                String line = item.getName() + "," + item.getImsi();
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();

            Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
