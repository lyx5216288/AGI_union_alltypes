package com.example.jbtang.agi_union.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi_union.R;
import com.example.jbtang.agi_union.core.Global;
import com.example.jbtang.agi_union.core.Status;
import com.example.jbtang.agi_union.dao.OrientationInfos.OrientationInfoDAO;
import com.example.jbtang.agi_union.dao.OrientationInfos.OrientationInfoManager;
import com.example.jbtang.agi_union.device.DeviceManager;
import com.example.jbtang.agi_union.device.MonitorDevice;
import com.example.jbtang.agi_union.external.MonitorApplication;
import com.example.jbtang.agi_union.external.MonitorHelper;
import com.example.jbtang.agi_union.service.BluetoothService;
import com.example.jbtang.agi_union.service.OrientationFinding;
import com.example.jbtang.agi_union.utils.BarChartView;
import com.google.android.gms.fitness.data.DataSource;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.fmaster.LTEServCellMessage;

/**
 * Created by jbtang on 11/7/2015.
 */
public class OrientationFindingActivity extends AppCompatActivity {
    private static final String TAG = "OrientationActivity";
    private static final int RSRP_LIST_MAX_SIZE = 4;
    public static boolean startToFind;

    private TextView currentPCi;
    private TextView targetStmsiTextView;
    private RadioGroup triggerTypeRG;
    private int temSMSInterval;
    private int temSilenceTimer;
    public static Button startButton;
    public static Button stopButton;
    private ListView resultListView;
    private LinearLayout resultGraphLayout,mutilineResultLayout;
    private myHandler handler;
    private List<OrientationFinding.OrientationInfo> orientationInfoList;
    private TextView cellConfirmColorOne;
    private TextView cellConfirmColorTwo;
    private TextView cellConfirmColorThree;
    private TextView cellConfirmColorFour;
    private TextView cellRsrpOne;
    private TextView cellRsrpTwo;
    private TextView cellRsrpThree;
    private TextView cellRsrpFour;
    private TextView pciNumOne;
    private TextView pciNumTwo;
    private TextView pciNumThree;
    private TextView pciNumFour;

    private TextView earfcnNumOne;
    private TextView earfcnNumTwo;
    private TextView earfcnNumThree;
    private TextView earfcnNumFour;

    private RadioButton statusone,statusmulti;
    private RadioGroup group,linetypegroup;
    private ImageView lineTop,lineLeft,lineRight,lineDown;
    private TextView  dataTop,dataLeft,dataRight,dataDown,lineCenter;


    private MonitorHelper monitorHelper;
    public static List<String> options = Arrays.asList("", "", "", "", "", "");
    private BarChartView view;
    private TextToSpeech textToSpeech;
    private OrientationInfoManager orientationInfoManager;
    private static final String[] m={"正前","前右","正后","前左","全向"};
    public static Spinner spinner;
    public static boolean  isdirect;

    public static int shunxuindex;
    public static int linetype;
    public int currentdirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_finding);

        startToFind = false;
        isdirect=false;
        currentdirect=-1;
        OrientationFinding.getInstance().targetStmsi = Global.TARGET_STMSI;
        orientationInfoList = new ArrayList<>();
        init();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent=new Intent(OrientationFindingActivity.this,MainMenuActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        orientationInfoManager.clear();
        orientationInfoManager.add(orientationInfoList);
        orientationInfoManager.closeDB();
        if (startToFind) {
            OrientationFinding.getInstance().stop();
            startToFind = false;
        }
        Global.Configuration.triggerInterval = temSMSInterval;
        Global.Configuration.silenceCheckTimer = temSilenceTimer;
        unregisterReceiver(receiver);
        monitorHelper.unbindservice(OrientationFindingActivity.this);
        textToSpeech.shutdown();
        Log.e("Orientation", "orientation is onDestory");
        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orientation_find, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_find_stmsi_save) {
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.orientation_find_layout_cell_status_bar);
        LinearLayout cellStatusBar = (LinearLayout) inflater.inflate(R.layout.cell_status_bar, null).findViewById(R.id.cell_status_bar_linearlayout);
        linearLayout.addView(cellStatusBar);


        currentPCi = (TextView) findViewById(R.id.orientation_current_pci);
        targetStmsiTextView = (TextView) findViewById(R.id.orientation_find_target_stmsi);
        triggerTypeRG = (RadioGroup) findViewById(R.id.orientation_find_trigger_type);
        startButton = (Button) findViewById(R.id.orientation_find_start);
        stopButton = (Button) findViewById(R.id.orientation_find_stop);
        cellConfirmColorOne = (TextView) findViewById(R.id.cell_status_bar_confirm_background_one);
        cellConfirmColorTwo = (TextView) findViewById(R.id.cell_status_bar_confirm_background_two);
        cellConfirmColorThree = (TextView) findViewById(R.id.cell_status_bar_confirm_background_three);
        cellConfirmColorFour = (TextView) findViewById(R.id.cell_status_bar_confirm_background_four);
        cellRsrpOne = (TextView) findViewById(R.id.cell_status_bar_rsrp_one);
        cellRsrpTwo = (TextView) findViewById(R.id.cell_status_bar_rsrp_two);
        cellRsrpThree = (TextView) findViewById(R.id.cell_status_bar_rsrp_three);
        cellRsrpFour = (TextView) findViewById(R.id.cell_status_bar_rsrp_four);
        pciNumOne = (TextView) findViewById(R.id.cell_status_bar_pci_num_one);
        pciNumTwo = (TextView) findViewById(R.id.cell_status_bar_pci_num_two);
        pciNumThree = (TextView) findViewById(R.id.cell_status_bar_pci_num_three);
        pciNumFour = (TextView) findViewById(R.id.cell_status_bar_pci_num_four);


        earfcnNumOne= (TextView) findViewById(R.id.cell_status_bar_confirm_earfcn_one);
        earfcnNumTwo= (TextView) findViewById(R.id.cell_status_bar_confirm_earfcn_two);
        earfcnNumThree= (TextView) findViewById(R.id.cell_status_bar_confirm_earfcn_three);
        earfcnNumFour= (TextView) findViewById(R.id.cell_status_bar_confirm_earfcn_four);

        lineTop= (ImageView) findViewById(R.id.line_top);
        lineLeft= (ImageView) findViewById(R.id.line_left);
        lineRight= (ImageView) findViewById(R.id.line_right);
        lineDown= (ImageView) findViewById(R.id.line_down);
        lineCenter= (TextView) findViewById(R.id.line_center);

        dataTop= (TextView) findViewById(R.id.text_top);
//        dataCenter= (TextView) findViewById(R.id.text_center);
        dataLeft= (TextView) findViewById(R.id.text_left);
        dataRight= (TextView) findViewById(R.id.text_right);
        dataDown= (TextView) findViewById(R.id.text_down);

        lineCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shunxuindex=4;
                if (isdirect){
                    if (currentdirect==4){
                        isdirect=false;

                    }else {
                        currentdirect=4;
                        lineCenter.setBackgroundResource(R.drawable.red);
                        lineTop.setBackgroundResource(R.drawable.greentop);
                        lineDown.setBackgroundResource(R.drawable.greendown);
                        lineLeft.setBackgroundResource(R.drawable.greenleft);
                        lineRight.setBackgroundResource(R.drawable.greenright);
                    }

                }else {
                    isdirect=true;
                    currentdirect=4;
                    lineCenter.setBackgroundResource(R.drawable.red);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                }
            }
        });

        lineTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shunxuindex=0;
                if (isdirect){
                    if (currentdirect==0){
                        isdirect=false;
                        shunxuindex=4;
                        lineTop.setBackgroundResource(R.drawable.greentop);
                        lineCenter.setBackgroundResource(R.drawable.red);
                    }else {
                        currentdirect=0;
                        lineTop.setBackgroundResource(R.drawable.redtop);
                        lineCenter.setBackgroundResource(R.drawable.green);
                        lineDown.setBackgroundResource(R.drawable.greendown);
                        lineLeft.setBackgroundResource(R.drawable.greenleft);
                        lineRight.setBackgroundResource(R.drawable.greenright);
                    }

                }else {
                    isdirect=true;
                    currentdirect=0;
                    lineTop.setBackgroundResource(R.drawable.redtop);
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                }
            }
        });
        lineLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shunxuindex=3;
                if (isdirect){
                    if (currentdirect==3){
                        isdirect=false;
                        shunxuindex=4;
                        lineLeft.setBackgroundResource(R.drawable.greenleft);
                        lineCenter.setBackgroundResource(R.drawable.red);
                    }else {
                        currentdirect=3;
                        lineLeft.setBackgroundResource(R.drawable.redleft);
                        lineCenter.setBackgroundResource(R.drawable.green);
                        lineTop.setBackgroundResource(R.drawable.greentop);
                        lineDown.setBackgroundResource(R.drawable.greendown);
                        lineRight.setBackgroundResource(R.drawable.greenright);
                    }

                }else {
                    isdirect=true;
                    currentdirect=3;
                    lineLeft.setBackgroundResource(R.drawable.redleft);
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                }
            }
        });
        lineRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shunxuindex=1;
                if (isdirect){
                    if (currentdirect==1){
                        isdirect=false;
                        shunxuindex=4;
                        lineRight.setBackgroundResource(R.drawable.greenright);
                        lineCenter.setBackgroundResource(R.drawable.red);
                    }else {
                        currentdirect=1;
                        lineRight.setBackgroundResource(R.drawable.redright);
                        lineCenter.setBackgroundResource(R.drawable.green);
                        lineTop.setBackgroundResource(R.drawable.greentop);
                        lineDown.setBackgroundResource(R.drawable.greendown);
                        lineLeft.setBackgroundResource(R.drawable.greenleft);
                    }
                }else {
                    isdirect=true;
                    currentdirect=1;
                    lineRight.setBackgroundResource(R.drawable.redright);
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                }
            }
        });
        lineDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shunxuindex=2;
                if (isdirect){
                    if (currentdirect==2){
                        isdirect=false;
                        shunxuindex=4;
                        lineDown.setBackgroundResource(R.drawable.greendown);
                        lineCenter.setBackgroundResource(R.drawable.red);
                    }else{
                        currentdirect=2;
                        lineDown.setBackgroundResource(R.drawable.reddown);
                        lineCenter.setBackgroundResource(R.drawable.green);
                        lineTop.setBackgroundResource(R.drawable.greentop);
                        lineLeft.setBackgroundResource(R.drawable.greenleft);
                        lineRight.setBackgroundResource(R.drawable.greenright);
                    }

                }else {isdirect=true;
                    currentdirect=2;
                    lineDown.setBackgroundResource(R.drawable.reddown);
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                }
            }
        });




        spinner= (Spinner) findViewById(R.id.spinner1);
        SpinnerAdapter adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,m);
        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    BluetoothService.getInstance().start(OrientationFindingActivity.this);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        resultGraphLayout = (LinearLayout) findViewById(R.id.orientation_find_layout_result_graph);
        mutilineResultLayout= (LinearLayout) findViewById(R.id.orientation_find_mutiline);

        statusone= (RadioButton) findViewById(R.id.orientation_find_status_one);
        statusmulti= (RadioButton) findViewById(R.id.orientation_find_status_multi);
        group= (RadioGroup) findViewById(R.id.orientation_find_status);

        group.check(R.id.orientation_find_status_multi);
        Global.Configuration.OritationModle=1;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==statusone.getId()){
                    Global.Configuration.OritationModle=0;
                }else if (checkedId==statusmulti.getId()){
                    Global.Configuration.OritationModle=1;
                }
            }
        });
        linetype=0;
        linetypegroup= (RadioGroup) findViewById(R.id.orientation_line_type);
        linetypegroup.check(R.id.orientation_line_simple);
        linetypegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.orientation_line_simple){
                    linetype=0;
                    mutilineResultLayout.setVisibility(View.GONE);
                    resultGraphLayout.setVisibility(View.VISIBLE);
                }else if(checkedId==R.id.orientation_line_muti){
                    linetype=1;
                    resultGraphLayout.setVisibility(View.GONE);
                    mutilineResultLayout.setVisibility(View.VISIBLE);
                    try {
                        BluetoothService.getInstance().start(OrientationFindingActivity.this,0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        orientationInfoManager = new OrientationInfoManager(this);
        orientationInfoList = getOrientationInfoList();

        resultListView = (ListView) findViewById(R.id.orientation_find_result_list);
        resultListView.setAdapter(new MyAdapter(this));
        ((MyAdapter) resultListView.getAdapter()).notifyDataSetChanged();

        refreshBarChart();

        targetStmsiTextView.setText(OrientationFinding.getInstance().targetStmsi);

        temSMSInterval = Global.Configuration.triggerInterval;
        temSilenceTimer = Global.Configuration.silenceCheckTimer;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    textToSpeech.speak("开始侧向", TextToSpeech.QUEUE_FLUSH, null);
                }
                if (startToFind || DeviceManager.getInstance().getDevices().size() == 0 || Global.TARGET_STMSI == null)
                    return;
                if (triggerTypeRG.getCheckedRadioButtonId() == R.id.orientation_find_trigger_continue) {
                    Global.Configuration.triggerInterval = 4;//近模式连续触发间隔
                    Global.Configuration.silenceCheckTimer = 0;
                }else if (triggerTypeRG.getCheckedRadioButtonId() == R.id.orientation_find_trigger_medium){
                    Global.Configuration.triggerInterval = 8;//中模式连续触发间隔
                    Global.Configuration.silenceCheckTimer = 0;
                } else {
                    Global.Configuration.triggerInterval = temSMSInterval;
                    Global.Configuration.silenceCheckTimer = temSilenceTimer;
                }
                OrientationFinding.getInstance().start(OrientationFindingActivity.this);
                startToFind = true;
                shunxuindex=0;
                orientationInfoList.clear();
                for (int i = 0; i < RSRP_LIST_MAX_SIZE + 1; i++) {
                    options.set(i, "");
                }
                if (linetypegroup.getCheckedRadioButtonId()!=R.id.orientation_line_muti){
                    refreshBarChart();
                }

                startButton.setEnabled(false);
                stopButton.setEnabled(false);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        OrientationFindingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                startButton.setEnabled(true);
                                stopButton.setEnabled(true);
                            }
                        });
                    }
                }, 5000);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrientationFinding.getInstance().stop();
                if (startToFind) {
                    startToFind = false;
                    stopButton.setEnabled(false);
                    //Toast.makeText(OrientationFindingActivity.this,"设备停止中，请稍后！",Toast.LENGTH_LONG).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            OrientationFindingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setEnabled(true);
                                    stopButton.setEnabled(true);
                                }
                            });
                        }
                    }, 5000);
                }
            }
        });

        triggerTypeRG.check(R.id.orientation_find_trigger_single);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MonitorApplication.BROAD_TO_MAIN_ACTIVITY);
        filter.addAction(MonitorApplication.BROAD_FROM_MAIN_MENU_DEVICE);
        registerReceiver(receiver, filter);
        handler = new myHandler(this);
        Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (startToFind) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }, 1, 3, TimeUnit.SECONDS);
        OrientationFinding.getInstance().setOutHandler(handler);
        monitorHelper = new MonitorHelper();
        monitorHelper.bindService(OrientationFindingActivity.this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED
                            || result == TextToSpeech.LANG_MISSING_DATA) {
                        Toast.makeText(OrientationFindingActivity.this, "数据丢失或不支持", Toast.LENGTH_LONG).show();
                    } else {
                        textToSpeech.setPitch(1.5f);
                    }
                }
            }
        });
    }

    private List<OrientationFinding.OrientationInfo> getOrientationInfoList() {
        List<OrientationInfoDAO> orientationInfoDAOList = orientationInfoManager.listDB();
        List<OrientationFinding.OrientationInfo> orientationInfoDBList = new ArrayList<>();
        for (OrientationInfoDAO dao : orientationInfoDAOList) {
            OrientationFinding.OrientationInfo orientationInfo = new OrientationFinding.OrientationInfo();
            //orientationInfo.PUSCHRsrp = dao.pusch.equals(Double.NaN)? Double.NaN:Double.parseDouble(dao.pusch);
            orientationInfo.PUSCHRsrp = Double.parseDouble(dao.pusch);
            orientationInfo.pci = dao.pci;
            orientationInfo.earfcn = dao.earfcn;
            orientationInfo.timeStamp = dao.time;
            orientationInfoDBList.add(orientationInfo);

        }
        return orientationInfoDBList;
    }

    private void refresh(String type) {
        final String temtype = type;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (temtype.equals("all")) {
                    ((MyAdapter) resultListView.getAdapter()).notifyDataSetChanged();
                    resultListView.setSelection(orientationInfoList.size() - 1);
                    refreshBarChart();
                }
                refreshCellStatusBar();
            }
        });
    }

    private void refreshBarChart() {
        resultGraphLayout.removeAllViews();
        view = new BarChartView(OrientationFindingActivity.this);

        int from = orientationInfoList.size() < RSRP_LIST_MAX_SIZE ? 0 : orientationInfoList.size() - RSRP_LIST_MAX_SIZE;
        int to = orientationInfoList.size();
//        int[] pucchList = new int[RSRP_LIST_MAX_SIZE];
        int[] puschList = new int[RSRP_LIST_MAX_SIZE];
        int rsrpIndex = RSRP_LIST_MAX_SIZE - 1;
        for (; to > from; to--, rsrpIndex--) {
//            pucchList[rsrpIndex] = orientationInfoList.get(to - 1).getStandardPucch();
            int pusch = orientationInfoList.get(to - 1).getStandardPusch();
            puschList[rsrpIndex] = pusch;
            options.set(rsrpIndex + 1, orientationInfoList.get(to - 1).timeStamp);
            if (pusch > 25 && !Global.Configuration.targetPhoneNum.equals(Global.LogInfo.phone)) {
                Global.LogInfo.phone = Global.Configuration.targetPhoneNum;
                Global.LogInfo.findStartTime = new Date().toString();
            } else if (pusch > 25) {
                Global.LogInfo.targetSTMSI = Global.TARGET_STMSI;
                Global.LogInfo.findEndTime = new Date().toString();
            }
            Log.e(TAG, "LogInfo.phone" + Global.LogInfo.phone + ",LogInfo.targetSTMSI" + Global.LogInfo.targetSTMSI + ",LogInfo.findStartTime" + Global.LogInfo.findStartTime + ",LogInfo.findEndTime" + Global.LogInfo.findEndTime);
        }
        for (; to > from; to--, rsrpIndex--) {
            puschList[rsrpIndex] = 0;
        }
        view.initData(puschList, options, "");
        resultGraphLayout.addView(view.getBarChartView());
            switch (shunxuindex%5){
                case 4:{
                    lineCenter.setText(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]));
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineCenter.setBackgroundResource(R.drawable.red);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                    break;
                }
                case 0:{
                    dataTop.setText(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]));
                    lineRight.setBackgroundResource(R.drawable.greenright);
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.redtop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    break;
                }
                case 1:{
                    dataRight.setText(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]));
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.redright);
                    break;
                }
                case 2:{
                    dataDown.setText(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]));
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.reddown);
                    lineLeft.setBackgroundResource(R.drawable.greenleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                    break;
                }
                case 3:{
                    dataLeft.setText(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]));
                    lineCenter.setBackgroundResource(R.drawable.green);
                    lineTop.setBackgroundResource(R.drawable.greentop);
                    lineDown.setBackgroundResource(R.drawable.greendown);
                    lineLeft.setBackgroundResource(R.drawable.redleft);
                    lineRight.setBackgroundResource(R.drawable.greenright);
                    break;
                }
            }

//        if(linetypegroup.getCheckedRadioButtonId()==R.id.orientation_line_muti){
//            OrientationFindingActivity.shunxuindex++;
//            BluetoothService.getInstance().sendOrienMsg();
//            Log.e("send","shormessage!!!!");
//        }

        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.speak(String.valueOf(puschList[RSRP_LIST_MAX_SIZE - 1]), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void refreshCellStatusBar() {
        int position = 0;
        for (MonitorDevice device : DeviceManager.getInstance().getAllDevices()) {
            if (device.getStatus() != Status.DeviceStatus.DISCONNECTED) {
                if (device.getIsReadyToMonitor()) {
                    if (device.getWorkingStatus() == Status.DeviceWorkingStatus.NORMAL) {
                        String rsrp = String.format("%.2f", device.getCellInfo().rsrp);
                        setCellStatusBar(position, Color.GREEN, rsrp, device.getCellInfo().pci + "",device.getCellInfo().earfcn+"");
                    } else if (device.getWorkingStatus() != Status.DeviceWorkingStatus.NORMAL) {
                        setCellStatusBar(position, Color.YELLOW, "N/A", device.getCellInfo().pci + "",device.getCellInfo().earfcn+"");
                    }
                } else {
                    setCellStatusBar(position, Color.RED, "", "","");
                }
            } else {
                setCellStatusBar(position, getResources().getColor(R.color.default_color), "", "","");
            }
            position++;
        }
    }

    private void setCellStatusBar(int position, int color, String text, String pci,String earfcn) {
        switch (position) {
            case 0: {
                cellConfirmColorOne.setBackgroundColor(color);
                cellRsrpOne.setText(text);
                pciNumOne.setText(pci);
                earfcnNumOne.setText(earfcn);
                break;
            }
            case 1: {
                cellConfirmColorTwo.setBackgroundColor(color);
                cellRsrpTwo.setText(text);
                pciNumTwo.setText(pci);
                earfcnNumTwo.setText(earfcn);
                break;
            }
            case 2: {
                cellConfirmColorThree.setBackgroundColor(color);
                cellRsrpThree.setText(text);
                pciNumThree.setText(pci);
                earfcnNumThree.setText(earfcn);
                break;
            }
            case 3: {
                cellConfirmColorFour.setBackgroundColor(color);
                cellRsrpFour.setText(text);
                pciNumFour.setText(pci);
                earfcnNumFour.setText(earfcn);
                break;
            }
            default:
                break;
        }
    }

    static class myHandler extends Handler {
        private final WeakReference<OrientationFindingActivity> mOuter;

        public myHandler(OrientationFindingActivity activity) {
            mOuter = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                OrientationFinding.OrientationInfo info = (OrientationFinding.OrientationInfo) msg.obj;
                //if(!Double.isNaN(info.PUSCHRsrp)) {
                mOuter.get().orientationInfoList.add(info);
                mOuter.get().refresh("all");
                //}
            } else if (msg.what == 2) {
                mOuter.get().refresh("");
            }

        }
    }

    /**
     * for ListView
     */
    private final class ViewHolder {
        public TextView num;
        public TextView pusch;
        public TextView pci;
        public TextView earfcn;
        public TextView time;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return orientationInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.orientation_finding_list_item, null);
                holder = new ViewHolder();
                holder.num = (TextView) convertView.findViewById(R.id.orientation_find_list_item_num);
                holder.pusch = (TextView) convertView.findViewById(R.id.orientation_find_list_item_pusch);
                holder.pci = (TextView) convertView.findViewById(R.id.orientation_find_list_item_pci);
                holder.earfcn = (TextView) convertView.findViewById(R.id.orientation_find_list_item_earfcn);
                holder.time = (TextView) convertView.findViewById(R.id.orientation_find_list_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.num.setText(String.valueOf(position + 1));
            holder.pusch.setText(String.format("%.2f", orientationInfoList.get(position).PUSCHRsrp));
            holder.pci.setText(orientationInfoList.get(position).pci);
            holder.earfcn.setText(orientationInfoList.get(position).earfcn);
            holder.time.setText(orientationInfoList.get(position).timeStamp);
            return convertView;
        }
    }


    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("")) {
                return;
            }
            if (intent.getAction().equals(MonitorApplication.BROAD_TO_MAIN_ACTIVITY)) {
                refreshServerCell(intent);
            } else if (intent.getAction().equals(MonitorApplication.BROAD_FROM_MAIN_MENU_DEVICE)) {
                //refreshDeviceStatus(intent);
            }
        }
    }

    private void refreshServerCell(Intent intent) {
        int flag = intent.getFlags();
        Bundle bundle = intent.getExtras();
        switch (flag) {
            case MonitorApplication.SERVER_CELL_FLAG:
                LTEServCellMessage myServCellMessage = bundle.getParcelable("msg");
                if (myServCellMessage != null) {
                    currentPCi.setText(String.valueOf(myServCellMessage.getPCI()));
                }
                break;
            default:
                break;
        }
    }

    private void refreshDeviceStatus(Intent intent) {
        Bundle bundle = intent.getExtras();
        int colorOne = bundle.getInt("colorOne");
        int colorTwo = bundle.getInt("colorTwo");
        int colorThree = bundle.getInt("colorThree");
        int colorFour = bundle.getInt("colorFour");
        if (colorOne == Color.RED) {
            cellConfirmColorOne.setBackgroundColor(Color.RED);
        }
        if (colorTwo == Color.RED) {
            cellConfirmColorTwo.setBackgroundColor(Color.RED);
        }
        if (colorThree == Color.RED) {
            cellConfirmColorThree.setBackgroundColor(Color.RED);
        }
        if (colorFour == Color.RED) {
            cellConfirmColorFour.setBackgroundColor(Color.RED);
        }
    }

//    private void refreshView(Intent intent) {
//
//        int flag = intent.getFlags();
//        Bundle bundle = intent.getExtras();
//        switch (flag) {
//            case MonitorApplication.STMSI:
//                String stmsi = bundle.getString("msg");
//                myStmsiTextView.setText(stmsi);
//                break;
//        }
//    }
}
