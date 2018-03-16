package com.example.abc.testui;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.abc.testui.R;
import com.example.abc.testui.ui.Global;
import com.example.abc.testui.ui.RegDeal;
import com.example.jbtang.agi_union.R;

public class RegisterActivity extends AppCompatActivity implements ServiceConnection, View.OnClickListener {

    private RealTimeService.MyBinder myBinder;
    private String registerMsg;
    private String machineCode;
    private String registerCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btn_register_ok_id).setOnClickListener(this);
        findViewById(R.id.btn_register_cancel_id).setOnClickListener(this);


        ((EditText)findViewById(R.id.et_register_registerCode_id)).addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                Log.e("输入过程中执行该方法", "文字变化");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                Log.e("输入前确认执行该方法", "开始输入");

            }


            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                Log.e("输入结束执行该方法", "输入结束");
                String s1 = s.toString();
                System.out.println(s);

            }
        });


        init_toolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.isopenreg = false;

        int ret = RegDeal.Judge();
        if(ret!=0){
            System.exit(0);
        }

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (RealTimeService.MyBinder) service;
        registerMsg = myBinder.getRegisterMsg();
        machineCode = myBinder.getMachineCode();
        init_tv();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register_ok_id:
                doProcess();
                unbindService(this);
                finish();
                break;
            case R.id.btn_register_cancel_id:
                unbindService(this);
                finish();
                break;
        }
    }

    public void init_tv(){
        TextView tv_registerMsg;
        final TextView tv_machineCode;

        tv_registerMsg = (TextView) findViewById(R.id.tv_register_msg_id);
        tv_machineCode = (TextView) findViewById(R.id.tv_register_machineCode_id);

        tv_registerMsg.setText(registerMsg);
        tv_machineCode.setText(machineCode);

        tv_machineCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv_machineCode.getText().toString());
                return false;
            }
        });
    }

    //将 注册码 编辑框中的值传递给 service
    public void doProcess(){
        EditText et_registerCode = (EditText) findViewById(R.id.et_register_registerCode_id);
        registerCode = et_registerCode.getText().toString();
        myBinder.onRegisterCode(registerCode);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        unbindService(this);
        finish();
    }

    public void init_toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 标题上的回退按钮的点击处理
                unbindService(RegisterActivity.this);
                finish();
            }
        });
    }
}
