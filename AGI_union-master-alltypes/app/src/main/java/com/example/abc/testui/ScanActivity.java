package com.example.abc.testui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
//import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.abc.testui.R;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity implements ServiceConnection, View.OnClickListener {

    private List<ScanData> listScanDatas;
    private RealTimeService.MyBinder myBinder;
    private SpinnerEditText spinnerEditText;

    private ScanAdapter scanAdapter;
    private RecyclerView rv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);

        if (Global.UI_FORMAT == Global.UI_V1){
            setContentView(R.layout.activity_scan);
        }
        else{
//            setContentView(R.layout.activity_scan_v2);
        }

//        int earfcn_init = getIntent().getIntExtra("earfcn", 0);

        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btn_scan_id).setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv_scan_show_id);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        spinnerEditText = (SpinnerEditText) findViewById(R.id.scan_spinner_edittext_id);
        List<ParamSetActivity.BaseBean> earfcn_baseBeanList = new ArrayList<>();
        for (int i=0; i<ParamSetActivity.EARFCN_DATAS.length; i++){
            ParamSetActivity.BaseBean baseBean = new ParamSetActivity.BaseBean();
            baseBean.data = ""+ParamSetActivity.EARFCN_DATAS[i];
            earfcn_baseBeanList.add(baseBean);
        }
        spinnerEditText.setList(earfcn_baseBeanList);
//        spinnerEditText.setText(""+earfcn_init);
        spinnerEditText.setText(""+ParamSetActivity.EARFCN_DATAS[0]);
        spinnerEditText.setRightCompoundDrawable(R.drawable.vector_drawable_arrowdown);
        spinnerEditText.setNeedShowSpinner(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.scan_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (RealTimeService.MyBinder) service;
        listScanDatas = myBinder.getListScanDatas();
        initRV();

        myBinder.getRealTimeService().setCallbackScan(new RealTimeService.CallbackScan() {
            @Override
            public void onScanData(int position) {
                Bundle b = new Bundle();
                b.putInt("position", position);

                Message msg = new Message();
                msg.setData(b);
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onScanTextData(String info) {
                Bundle b = new Bundle();
                b.putString("info", info);

                Message msg = new Message();
                msg.setData(b);
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onSetBtnEnable() {
                //Bundle b = new Bundle();
                //b.putString("info", info);

                Message msg = new Message();
                //msg.setData(b);
                msg.what = 3;
                handler.sendMessage(msg);
            }


        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void initRV(){
        rv = (RecyclerView) findViewById(R.id.rv_scan_id);
        rv.setLayoutManager(new LinearLayoutManager(this));
        scanAdapter = new ScanAdapter(this, listScanDatas);
        rv.setAdapter(scanAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_scan_id:

                findViewById(R.id.btn_scan_id).setEnabled(false);
                listScanDatas.removeAll(listScanDatas);
                scanAdapter.refresh();
                myBinder.onGetFreq(Integer.parseInt(spinnerEditText.getText().toString()));
                break;
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);

            if (msg.what==1) {
                int position = msg.getData().getInt("position");
                scanAdapter.addItem(position);
                // rv.scrollToPosition(position);
            }
            else if (msg.what==2){
                tv.setText(msg.getData().getString("info"));
                int offset = tv.getLineCount() * tv.getLineHeight();
                if (offset>tv.getHeight()){
                    tv.scrollTo(0, offset-tv.getHeight());
                }
            }
            else if(msg.what == 3){
                findViewById(R.id.btn_scan_id).setEnabled(true);
            }
        }
    };
}
