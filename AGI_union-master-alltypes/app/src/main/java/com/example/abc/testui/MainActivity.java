package com.example.abc.testui;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.testui.src.WriteBLogDeal;
import com.example.abc.testui.ui.RegDeal;import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection, ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String FIX_IP = "192.168.2.11";

    private ActionBar actionBar;
    private TabHost tabHost;
    private Spinner spinner_mode;
    private Button btnStart;
    private Button btnStop;
    private Button btn_stopScroll;
    private ImageView iv_heart;
    private ImageView iv_conn;
    private TextView tv_totalNum;
    private TextView tv_earfcn;
    private TextView tv_pci;

    private RecyclerView recyclerView_realtime;
    private RecyclerView recyclerView_target;
    private RecyclerView recyclerView_state;
    private RealTimeAdapter realTimeAdapter;
	private TargetAdapter targetAdapter;
    private StateAdapter stateAdapter;

    private boolean isStopScrolToPosition;
    private boolean isPermissionOK = false;
    private SoundPlayer soundPlayer;  //加载语音的线程类

    private ParamSetValue paramSetValue;  //保存从“参数设置”界面传回来的参数值
    private long target2service_imsi = 0;  //长按 中标流水 向 service 下发定位命令的 imsi
    private List<Integer> listEnergyValue;  //保存10个最新的能量值，用于传递给 能量显示 界面显示

    private List<RealTimeDatas> listRealTimeDatases;  //实时流水中的行数据，从 service 中传过来
	private List<RealTimeDatas> listTargetDatas;  //从 listRealTimeDatas 抽取出的中标数据
    private List<StateValues> listStateValues;  //状态流水中的行数据，从 service 中传过来
    private List<IdListDatas> listIdListDatas;  //从 service 中传过来的，保存数据库中所有数据
    private IdListSetDB idListSetDB;
    private RealTimeUIParamValues realTimeUIParamValues;  //保存UI界面参数的值的对象
    private RealTimeService.MyBinder myBinder;  //通过该变量，可以向绑定的 service 中直接传递数据

    private IdListSetLog idListSetLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("111","111");
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);

        if (Global.UI_FORMAT == Global.UI_V1){
            setContentView(R.layout.activity_main);
        }
        else{
//            setContentView(R.layout.activity_main_v2);
        }

      //  Intent intent = new Intent(this, RealTimeService.class);
      //  bindService(intent, this, Context.BIND_AUTO_CREATE);

        realTimeUIParamValues = new RealTimeUIParamValues();
        isStopScrolToPosition = false;
        listEnergyValue = new ArrayList<>();

        doReadIMEI();  //读取 imei，保存到 imeistr 中，然后在 onServiceConnected 中传回 service

        init_paramSetValue();

        init_toolbar();

        init_spinner();



        if (Global.UI_FORMAT == Global.UI_V1) {
            init_tabhost();
        }

        init_stateBar(); //初始化界面最底部的状态栏
        //服务绑定成功后，再调用 init_recyclerView()
        //所以 init_recyclerView() 在 onServiceConnected 中调用

        init_view();


        idListSetLog = new IdListSetLog();


        // 服务的绑定，在权限询问 onRequestPermissionsResult 之后
        while (!isPermissionOK){
            try {
                Thread.sleep(500);
                Log.e("hhhhhhhhhhhh","aaaaaaaaaaaa");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);




        Global.activity = this;

        //语音播报
        soundPlayer = new SoundPlayer(); //创建线程，在线程中初始化
        Global.soundPlayer = soundPlayer;


        btnStart.setEnabled(false);
        btnStop.setEnabled(false);
        tv_earfcn.setText("");
        tv_pci.setText("");


        //boolean ret = setIpWithStaticIp(0);  //static ip
       // System.out.println(ret);
        iv_heart.setImageResource(R.drawable.ic_heart_none);
        iv_conn.setImageResource(R.drawable.ic_action_name_cellnone);

//        if(Global.UI_FORMAT == Global.UI_V1){
//            startActivityForResult(new Intent(this, Login.class), 7);//ppp
//        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    public void init_paramSetValue(){
        paramSetValue = new ParamSetValue();
        paramSetValue.setPlmn(ParamSetActivity.PLMN_DATAS_INT[0]);
        paramSetValue.setEarfcn(ParamSetActivity.EARFCN_DATAS[3]);
        paramSetValue.setPci(88);
//        paramSetValue.setPower(ParamSetActivity.POWER_DATAS_INT[0]);

        tv_earfcn = (TextView) findViewById(R.id.tv_mainactivity_earfcn_id);
        tv_pci = (TextView) findViewById(R.id.tv_mainactivity_pci_id);

        tv_earfcn.setText(""+paramSetValue.getEarfcn());
        tv_pci.setText(""+paramSetValue.getPci());
    }

    public void init_toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar_id);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
    }

    public void init_spinner(){
        spinner_mode = (Spinner) findViewById(R.id.spinner_mode_id);
        spinner_mode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RealTimeUIParamValues.SPINNERDATAS));
        spinner_mode.setSelection(0);

        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                realTimeUIParamValues.setMode(RealTimeUIParamValues.SPINNERDATAS[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void init_tabhost(){
        // 初始化 TabHost，添加 tab 选项卡，设置对应的页面布局
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(R.layout.tab1, tabHost.getTabContentView());
        layoutInflater.inflate(R.layout.tab2, tabHost.getTabContentView());

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("实时流水")
                .setContent(R.id.tab1_id));

        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("中标流水")
                .setContent(R.id.tab2_id));

        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i=0; i<tabWidget.getChildCount(); i++){
            tabWidget.getChildAt(i).getLayoutParams().height = 80;
        }


    }

    private View view_realtime_old = null;
    private View view_target_old = null;


    public void init_recyclerView(){
        recyclerView_realtime = (RecyclerView) findViewById(R.id.rv_realtime_id);
        recyclerView_realtime.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        realTimeAdapter = new RealTimeAdapter(this, listRealTimeDatases);
        recyclerView_realtime.setAdapter(realTimeAdapter);
        realTimeAdapter.setRVItemClickListenerInterface(new RealTimeAdapter.RVItemClickListenerInterface() {
            @Override
            public void onClick(View v, int position) {
                if (view_realtime_old!=null){
                    view_realtime_old.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                view_realtime_old = v;
            }

            @Override
            public void onLongClick(View v, int position) {
                if (view_realtime_old!=null){
                    view_realtime_old.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                view_realtime_old = v;


                Intent intent = new Intent(MainActivity.this, RealTimeAddToIdListActivity.class);
                intent.putExtra("imsi", listRealTimeDatases.get(position).getImsi());
                startActivityForResult(intent, 2);
            }
        });




		recyclerView_target = (RecyclerView) findViewById(R.id.rv_target_id);
        recyclerView_target.setLayoutManager(new LinearLayoutManager(this));
        targetAdapter = new TargetAdapter(this, listTargetDatas);
        recyclerView_target.setAdapter(targetAdapter);
        targetAdapter.setTargetOnClickListenerInterface(new TargetAdapter.TargetOnClickListenerInterface() {
            @Override
            public void onClick(View v, int position) {
                if (view_target_old!=null){
                    view_target_old.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                view_target_old = v;
            }

            @Override
            public void onLongClick(View v, int position) {
                if (view_target_old!=null){
                    view_target_old.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                view_target_old = v;

                Intent intent = new Intent(MainActivity.this, TargetSelected2ServiceActivity.class);
                intent.putExtra("imsi", listTargetDatas.get(position).getImsi());
                startActivityForResult(intent, 3);
            }
        });

        recyclerView_state = (RecyclerView) findViewById(R.id.rv_state_id);
        recyclerView_state.setLayoutManager(new LinearLayoutManager(this));
        stateAdapter = new StateAdapter(this, listStateValues);
        recyclerView_state.setAdapter(stateAdapter);
    }

    public void init_view(){
        findViewById(R.id.btn_mode_id).setOnClickListener(this);
        findViewById(R.id.btn_reboot_id).setOnClickListener(this);
        findViewById(R.id.btn_clear_id).setOnClickListener(this);
        findViewById(R.id.btn_showEnergy_id).setOnClickListener(this);

        btnStart = (Button) findViewById(R.id.btn_start_id);
        btnStart.setOnClickListener(this);

        btnStop = (Button) findViewById(R.id.btn_stop_id);
        btnStop.setOnClickListener(this);

        btn_stopScroll = (Button) findViewById(R.id.btn_stopScroll_id);
        btn_stopScroll.setOnClickListener(this);
    }

    public void init_stateBar(){
        iv_heart = (ImageView) findViewById(R.id.iv_heart_id);
        iv_conn = (ImageView) findViewById(R.id.iv_connected_id);
        tv_totalNum = (TextView) findViewById(R.id.tv_totalNum_id);

        iv_heart.setImageResource(R.mipmap.ic_heart_dark);
        iv_conn.setImageResource(R.mipmap.ic_unconnected);
        tv_totalNum.setTextSize(30);
        tv_totalNum.setText("0");
    }


    private Menu mm;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        mm = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public boolean voiceenable = false;

    private void testdb(){


        long l = RegDeal.GetLastRunTime();
        String s = RegDeal.GetRegCode();


        RegDeal.SetLastRunTime((new Date()).getTime());
        RegDeal.SetRegCode("regcode");

        long a = RegDeal.GetLastRunTime();
        String b = RegDeal.GetRegCode();

        System.out.println(l +s+a+b);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.IdListSet_id:  //名单编辑
                startActivity(new Intent(this, IdListSetActivity.class));
                break;
            case R.id.action_setting_voice:  //静音 按钮
                voiceenable = !voiceenable;

                if(voiceenable){
                    item.setIcon(R.mipmap.audio);
                    Toast.makeText(this, "开启声音", Toast.LENGTH_SHORT).show();
                }
                else {
                    item.setIcon(R.mipmap.noaudio);
                    Toast.makeText(this, "关闭声音", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_setting_debug:
                //playSoundOffLine();
                //playSoundOffLine();

               // Global.service.changeHeartState(1);
                //iv_conn.setImageResource(R.drawable.ic_action_name_cellhave);




                testdb();



                break;
//            case R.id.Register_id:  //注册
//                startActivity(new Intent(this, RegisterActivity.class));
//                break;
            case R.id.netFixorFree_id:  //释放网络/建立网络
                if (item.getTitle().equals("释放网络")){
                    item.setTitle("建立网络");
                    setIpWithStaticIp(0); // static ip
                }
                else{
                    item.setTitle("释放网络");
                    setIpWithStaticIp(1);  //dhcp
                }
                break;
            case R.id.ScanActivity_id:
                Intent intent = new Intent(this, ScanActivity.class);
//                intent.putExtra("earfcn", earfcn2scan);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_mode_id:  //“设置模式”按钮
                intent = new Intent(this, ConfirmActivity.class);
                intent.putExtra("flag", 1);  //指示 设置按钮 触发
                startActivityForResult(intent, 4);
                // 返回 确认，才执行真正的操作
                break;
            case R.id.btn_reboot_id:  //“重启”按钮
                intent = new Intent(this, ConfirmActivity.class);
                intent.putExtra("flag", 2);  //指示 设置按钮 触发
                startActivityForResult(intent, 5);
                // 返回 确认，才执行重启
                break;
            case R.id.btn_start_id:  //“建立”按钮
                intent = new Intent(this, ParamSetActivity.class);
                intent.putExtra("paramSetValue", paramSetValue);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_stop_id:  //“停止”按钮
                myBinder.onMainActivityBtnClickListener(RealTimeUIParamValues.BTNCLICKEDTAG[3], null, null);
                break;
            case R.id.btn_clear_id:  //“清空”按钮
                listRealTimeDatases.removeAll(listRealTimeDatases);
                realTimeAdapter.changeItem(listRealTimeDatases.size());//123123
                realTimeAdapter.setIsSelected(-1);
                break;
            case R.id.btn_stopScroll_id:
                if (isStopScrolToPosition==false){
                    isStopScrolToPosition=true;
                    btn_stopScroll.setText("开始滚动");
                }
                else{
                    isStopScrolToPosition=false;
                    btn_stopScroll.setText("停止滚动");
                }
                break;
            case R.id.btn_showEnergy_id:  //详细能量图 按钮
                Intent intent_showEnergy = new Intent(this, EnergyShow.class);
                intent_showEnergy.putExtra("paramSetValue", paramSetValue);
                intent_showEnergy.putExtra("target2service_imsi", target2service_imsi);
                intent_showEnergy.putIntegerArrayListExtra("listEnergyValue", (ArrayList<Integer>) listEnergyValue);
                startActivity(intent_showEnergy);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:  //“建立”按钮返回
                if (resultCode==1) {  //参数设置 界面 确定 按钮返回
                    paramSetValue = data.getParcelableExtra("paramSetValue");
                    realTimeUIParamValues.setParamSetValue(paramSetValue);
                    myBinder.onMainActivityBtnClickListener(RealTimeUIParamValues.BTNCLICKEDTAG[2], realTimeUIParamValues, listIdListDatas);

                    tv_earfcn.setText(""+paramSetValue.getEarfcn());
                    tv_pci.setText(""+paramSetValue.getPci());
                }
                else if (resultCode==2){  //参数设置 界面 取消 按钮或 回退图标 返回
                    // paramSetValue 的值保持原值
                    myBinder.onMainActivityBtnClickListener(RealTimeUIParamValues.BTNCLICKEDTAG[2], realTimeUIParamValues, listIdListDatas);
                }
                break;
            case 2: //长按 实时流水条目，是否将该条目的 imsi 添加到 名单列表中
                if (resultCode==1){  //确定 返回
                    long imsi = data.getLongExtra("imsi", 0);
                    if (uniqueCheck(imsi)!=0){
                        Toast.makeText(this, "IMSI已添加", Toast.LENGTH_SHORT).show();;
                    }
                    else {
                        IdListDatas idListDatas = new IdListDatas();
                        idListDatas.setNo(listIdListDatas.size()+1);
                        idListDatas.setImsi(imsi);
                        idListDatas.setName("" + imsi);
                        listIdListDatas.add(idListDatas);

                        SQLiteDatabase writeDB = idListSetDB.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("imsi", imsi);
                        cv.put("name", "" + imsi);
                        writeDB.insert(IdListSetDB.TABLE_NAME, null, cv);
                        writeDB.close();

//                        idListSetLog.writeLog(IdListSetLog.TYPE_ADD, idListDatas);
                        idListSetLog.writeBLog(new WriteBLogDeal().GetBytes(Global.username, imsi));
                        Global.service.SetDownList(listIdListDatas);

                    }
                }
                else if (resultCode==2){  //取消 返回

                }
                break;
            case 3:  //长按 中标流水条目，是否将该条目的 imsi 传给 service
                if (resultCode==1){  //确定 返回
                    target2service_imsi = data.getLongExtra("imsi", 0);
                    myBinder.onGetTargetItem(target2service_imsi);
                }
                else if (resultCode==2){  //取消 返回

                }
                break;
            case 4:  //设置模式 按钮，确认 返回才真正执行操作
                if (resultCode==1) {
                 //   Toast.makeText(this, "1111111111", Toast.LENGTH_SHORT).show();
                    myBinder.onMainActivityBtnClickListener(RealTimeUIParamValues.BTNCLICKEDTAG[0], realTimeUIParamValues, null);
                }
                break;
            case 5:  //重启 按钮，确认 返回才重启
                if (resultCode==1) {
                    //Toast.makeText(this, "222222222", Toast.LENGTH_SHORT).show();
                    myBinder.onMainActivityBtnClickListener(RealTimeUIParamValues.BTNCLICKEDTAG[1], null, null);
                }
                break;
            case 6:
                if (resultCode==1){
                    finish();
                }
                break;
            case 7:  //登陆界面 取消，退出程序
                if (resultCode==1){
                    finish();
                }
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

    private String Mcode = null;


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.e("111","111");
        myBinder = (RealTimeService.MyBinder)service;
        listRealTimeDatases = myBinder.getListRealTimeDatas();
		listTargetDatas = myBinder.getListTargetDatas();
        listStateValues = myBinder.getListStateValues();

        init_recyclerView();
        Log.e("222","222");



        //startActivityForResult(new Intent(this, Login.class), 7);//ppp



        //Mcode = myBinder.getRealTimeService().getImei(imeistr)+"";

        // 定时器，每隔 0.5 秒更新一次实时流水
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Bundle b = new Bundle();
                b.putInt("position", 0);  //随便发个数据，没有任何意义

                Message msg = new Message();
                msg.setData(b);
                handler_realtime.sendMessage(msg);


//                Bundle b1 = new Bundle();
//                b1.putInt("position", 0);  //随便发个数据，没有任何意义
//
//                Message msg1 = new Message();
//                msg1.setData(b1);
//
//                handler_target.sendMessage(msg1);
            }
        };
        timer.schedule(timerTask, 0, 40);
        Log.e("333","333");
        myBinder.getRealTimeService().setCallback(new RealTimeService.Callback() {
            @Override
            public void onTargetChange(int position) {
                Bundle b = new Bundle();
                b.putInt("position", position);

                Message msg = new Message();
                msg.setData(b);
                handler_target.sendMessage(msg);
            }

            @Override
            public void onStateValuesChange(int position) {
                Bundle b = new Bundle();
                b.putInt("position", position);

                Message msg = new Message();
                msg.setData(b);
                handler_state.sendMessage(msg);
            }

            @Override
            public void onEnergyValueChange(int value) {
                Bundle b = new Bundle();
                b.putInt("value", value);

                Message msg = new Message();
                msg.setData(b);
                handler_energyValue.sendMessage(msg);
            }

            @Override
            public void onHeartChange(int flag) {
                Bundle b = new Bundle();
                b.putInt("flag", flag);

                Message msg = new Message();
                msg.setData(b);
                handler_heartImg.sendMessage(msg);
            }

            @Override
            public void onConnChange(int flag) {
                Bundle b = new Bundle();
                b.putInt("flag", flag);

                Message msg = new Message();
                msg.setData(b);
                handler_connImg.sendMessage(msg);
            }

            @Override
            public void onModeSet(int index) {
                Bundle b = new Bundle();
                b.putInt("index", index);

                Message msg = new Message();
                msg.setData(b);
                handler_modeSet.sendMessage(msg);
            }

            @Override
            public void onBtnStartEnable(int isEnable) {
                Bundle b = new Bundle();
                b.putInt("isEnable", isEnable);

                Message msg = new Message();
                msg.setData(b);
                handler_btnStartEnable.sendMessage(msg);
            }

            @Override
            public void onBtnStopEnable(int isEnable) {
                Bundle b = new Bundle();
                b.putInt("isEnable", isEnable);

                Message msg = new Message();
                msg.setData(b);
                handler_btnStopEnable.sendMessage(msg);
            }

            @Override
            public void onTabChange(int tag) {
                Bundle b = new Bundle();
                b.putInt("tag", tag);

                Message msg = new Message();
                msg.setData(b);
                handler_tabChange.sendMessage(msg);
            }

            @Override
            public void onTitleSet(String title) {
                Bundle b = new Bundle();
                b.putString("title", title);

                Message msg = new Message();
                msg.setData(b);
                handler_title.sendMessage(msg);
            }

            @Override
            public void onPopRegister() {
                handler_startRegisterActivity.sendMessage(new Message());
            }

            @Override
            public void onSendModeChange() {
                handler_SendModeChange.sendMessage(new Message());
            }

            @Override
            public void onSetToast(String str) {
                Bundle b = new Bundle();
                b.putString("msg", str);

                Message msg = new Message();
                msg.setData(b);

                handler_SetToast.sendMessage(msg);
            }

            @Override
            public void setArfcn_Pci(int earfcn, int pci){
                Bundle b = new Bundle();
                //b.putString("msg", str);
                b.putInt("earfcn", earfcn);
                b.putInt("pci", pci);

                Message msg = new Message();
                msg.setData(b);

                handler_Setarfcn_pci.sendMessage(msg);
            }

        });
        Log.e("444","444");

        // 用于长按 实时流水条目，添加条目中 imsi 到 名单列表 中
        myBinder.getRealTimeService().setCallbackIdListSet(new RealTimeService.CallbackIdListSet() {
            @Override
            public void onConnect(List<IdListDatas> listIdListDatas, IdListSetDB idListSetDB) {
                MainActivity.this.listIdListDatas = listIdListDatas;
                MainActivity.this.idListSetDB = idListSetDB;
            }
        });
        Log.e("555","555");
        myBinder.onIdListSetInit();
        Log.e("666","666");
    }

    private Handler handler_realtime  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Position positionRange = reflush(recyclerView_realtime);

            // 更新广播，所有的 RecyclerView 都会更新
//            realTimeAdapter.refreshRealTime();
            int position = listRealTimeDatases.size();
//            realTimeAdapter.addItem(position);  //123123
//            realTimeAdapter.notifyItemRangeChanged(0, position);
            realTimeAdapter.notifyItemRangeChanged(positionRange.getFirstItemPosition(), positionRange.getLastItemPosition()+2);

            if (!isStopScrolToPosition)
                recyclerView_realtime.scrollToPosition(position-1);

            tv_totalNum.setText(""+listRealTimeDatases.size());
        }
    };

    private Handler handler_target  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            Position positionRange = reflush(recyclerView_target);

            // 更新广播，所有的 RecyclerView 都会更新
//            targetAdapter.refreshTarget();
            int position = listTargetDatas.size();
//            int position = msg.getData().getInt("position");
//            targetAdapter.addItem(position);  //123123
            targetAdapter.notifyItemRangeChanged(0, position);
//            targetAdapter.notifyItemRangeChanged(firstItemPosition, lastItemPosition);

            if (!isStopScrolToPosition)
                recyclerView_target.scrollToPosition(position-1);
        }
    };

    private Handler handler_state = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int position = msg.getData().getInt("position");
            stateAdapter.addItem(position);
            if (!isStopScrolToPosition)
                recyclerView_state.scrollToPosition(position-1);
        }
    };

    private int uesoundflag= 0;
    private Handler handler_energyValue = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle b = msg.getData();
            int energyValue = b.getInt("value");
            TextView tv_energyValue = (TextView) findViewById(R.id.tv_energyvalue_id);
            tv_energyValue.setText(""+energyValue);

            if (listEnergyValue.size()>=10){
                listEnergyValue.remove(0);
            }
            listEnergyValue.add(energyValue);


            uesoundflag++;
            if(uesoundflag%2==0){
                //语音播报
                if (voiceenable){
                    if(energyValue>=0 && energyValue<=99)
                    {
                        soundPlayer.playSound(energyValue);
                    }

                }

            }

        }
    };

    private Handler handler_heartImg = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getInt("flag")==0){
                //iv_heart.setImageResource(R.mipmap.ic_heart_dark);
                iv_heart.setImageResource(R.drawable.ic_heart_none);
            }
            else{
                //iv_heart.setImageResource(R.mipmap.ic_heart_light);
                iv_heart.setImageResource(R.drawable.ic_heart_have);
            }
        }
    };

    private Handler handler_connImg = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getInt("flag")==0){
                //iv_conn.setImageResource(R.mipmap.ic_unconnected);
                iv_conn.setImageResource(R.drawable.ic_action_name_cellnone);
            }
            else{
                //iv_conn.setImageResource(R.mipmap.ic_connected);
                iv_conn.setImageResource(R.drawable.ic_action_name_cellhave);
            }
        }
    };

    private Handler handler_modeSet = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            spinner_mode.setSelection(msg.getData().getInt("index"));
        }
    };

    private Handler handler_btnStartEnable = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getInt("isEnable")==0){
                btnStart.setEnabled(false);
            }
            else{
                btnStart.setEnabled(true);
            }
        }
    };

    private Handler handler_SetToast = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String str = msg.getData().getString("msg");

            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

        }
    };

    private Handler handler_Setarfcn_pci = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //String str = msg.getData().getString("msg");
            int earfcn = msg.getData().getInt("earfcn");
            int pci = msg.getData().getInt("pci");

            //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            tv_earfcn.setText(""+earfcn);
            tv_pci.setText(""+pci);

            paramSetValue.setEarfcn(earfcn);
            paramSetValue.setPci(pci);

        }
    };

    private Handler handler_tabChange = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (Global.UI_FORMAT == Global.UI_V1){
                tabHost.setCurrentTab(msg.getData().getInt("tag"));
            }
        }
    };

    private Handler handler_startRegisterActivity = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            startActivity(new Intent(MainActivity.this, RegisterActivity.class));//ppp
        }
    };

    private Handler handler_SendModeChange = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Global.service.SetModeSendCmd();
        }
    };

    private Handler handler_btnStopEnable = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getInt("isEnable")==0){
                btnStop.setEnabled(false);
            }
            else{
                btnStop.setEnabled(true);
            }
        }
    };


    private Handler handler_title = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String str  = msg.getData().getString("title");
            actionBar.setTitle(str);

            if(str.equals("主动定位 TDD")){
                mm.findItem(R.id.ScanActivity_id).setEnabled(true);
            }

        }
    };



    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        finish();
//        Intent intent = new Intent(this, ConfirmActivity.class);
//        intent.putExtra("flag", 3);
//        startActivityForResult(intent, 6);
    }

    //读取 imei 的值
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public String imeistr="";
    public void isOK(){
        int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (osVersion>22){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }else{
                //绑定服务
//                Intent intent = new Intent(this, RealTimeService.class);
//                bindService(intent, this, Context.BIND_AUTO_CREATE);

                isPermissionOK = true;

                getImei();
            }
        }else{
            //如果SDK小于6.0则不去动态申请权限

            //绑定服务
//            Intent intent = new Intent(this, RealTimeService.class);
//            bindService(intent, this, Context.BIND_AUTO_CREATE);

            isPermissionOK = true;

            getImei();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("ggggggggggg", "11111");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            getImei();
            //Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();

            //绑定服务
//            Intent intent = new Intent(this, RealTimeService.class);
//            bindService(intent, this, Context.BIND_AUTO_CREATE);
        }else{
            //Toast.makeText(getApplicationContext(),"授权拒绝",Toast.LENGTH_SHORT).show();
        }

        Log.e("wwwwwwwwwwww","11111");
        isPermissionOK = true;
        Log.e("wwwwwwwwwwww","22222");
    }


    public String mygetid(){
        try{
            String m_szDevIDShort = "86" +
                    Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                    Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                    Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                    Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                    Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                    Build.USER.length()%10 ; //13 位

            return m_szDevIDShort;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public void getImei(){
        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
        String IMEI = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            IMEI = tm.getDeviceId(TelephonyManager.PHONE_TYPE_GSM);
        }
        //String deviceId = tm.getDeviceId(TelephonyManager.PHONE_TYPE_NONE);
        //String deviceId2 = tm.getDeviceId(TelephonyManager.PHONE_TYPE_GSM);
        imeistr = IMEI;
        String mygetid = mygetid();
        String mtype = android.os.Build.MODEL;
        Log.d("Main",mtype);
        //Toast.makeText(this,"IMEI的值为："+imeistr,Toast.LENGTH_SHORT).show();
    }


    private void doReadIMEI() {
        isOK();

        imeistr = mygetid();
        isPermissionOK = true;
    }


    //=============================================================================================================
    /**

     * 设置静态ip地址的方法
     */
    private boolean setIpWithStaticIp(int flag) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiConfiguration wifiConfig = null;
        WifiInfo connectionInfo = wifiManager.getConnectionInfo(); //得到连接的wifi网络

        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();

        if (configuredNetworks==null){
            return false;
        }

        for (WifiConfiguration conf : configuredNetworks) {
            if (conf.networkId == connectionInfo.getNetworkId()) {
                wifiConfig = conf;

                break;
            }
        }


        try {
            if (flag==0) {
                setIpAssignment("STATIC", wifiConfig);

                setIpAddress(InetAddress.getByName(FIX_IP), 24, wifiConfig);
//                wifiManager.reassociate();
                wifiManager.updateNetwork(wifiConfig); // apply the setting

//                wifiManager.setWifiEnabled(false);
//                wifiManager.setWifiEnabled(true);
                wifiManager.disconnect();
                wifiManager.reassociate();

                Log.e("设置ip:", "静态ip设置成功！");
            }
            else if (flag==1){
                setIpAssignment("DHCP", wifiConfig);

//                setIpAddress(InetAddress.getByName(""), 0, wifiConfig);
//                wifiManager.reconnect();

                wifiManager.updateNetwork(wifiConfig); // apply the setting

                wifiManager.reassociate();

                Log.e("设置ip:", "动态ip设置成功！");

            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();

            Log.e("设置ip:","静态ip设置失败！");
            return false;
        }
    }
    private static void setIpAssignment(String assign, WifiConfiguration wifiConf)
    {
        setEnumField(wifiConf, assign, "ipAssignment");
    }

    private static void setEnumField(Object obj, String value, String name)
    {
        Field f = null;
        try {
            f = obj.getClass().getField(name);
            f.set(obj, Enum.valueOf((Class<Enum>)f.getType(), value));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf)
    {
        Object linkProperties = getField(wifiConf, "linkProperties");

        if (linkProperties == null){
            return;
        }

        Class<?> laClass = null;
        try {
            laClass = Class.forName("android.net.LinkAddress");
            Constructor<?> laConstructor = laClass.getConstructor(new Class[]{InetAddress.class, int.class });
            Object linkAddress = laConstructor.newInstance(addr, prefixLength);
            ArrayList<Object> mLinkAddresses = (ArrayList<Object>) getDeclaredField(linkProperties, "mLinkAddresses");

            mLinkAddresses.clear();
            mLinkAddresses.add(linkAddress);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    private static Object getField(Object obj, String name)
    {
        Field f = null;
        Object out = null;
        try {
            f = obj.getClass().getField(name);
            out = f.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return out;
    }
    private static Object getDeclaredField(Object obj, String name)
    {
        Field f = null;
        Object out = null;
        try {
            f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            out = f.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return out;
    }
//=============================================================================================================


    class Position{
        public int getLastItemPosition() {
            return lastItemPosition;
        }

        public void setLastItemPosition(int lastItemPosition) {
            this.lastItemPosition = lastItemPosition;
        }

        public int getFirstItemPosition() {
            return firstItemPosition;
        }

        public void setFirstItemPosition(int firstItemPosition) {
            this.firstItemPosition = firstItemPosition;
        }

        private int lastItemPosition;
        private int firstItemPosition;


    }
    public Position reflush(RecyclerView recyclerView){

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        Position position = new Position();

        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            position.setFirstItemPosition(firstItemPosition);
            position.setLastItemPosition(lastItemPosition);
        }

        return position;
    }

}
