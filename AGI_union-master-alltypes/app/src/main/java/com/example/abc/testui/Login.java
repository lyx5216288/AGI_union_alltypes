package com.example.abc.testui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.abc.testui.R;
import com.example.abc.testui.src.UserPwdDeal;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

public class Login extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private EditText etName;
    private EditText etPasswd;
    private RealTimeService.MyBinder myBinder;  //通过该变量，可以向绑定的 service 中直接传递数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar_id);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.et_login_usr_name);
        etPasswd = (EditText) findViewById(R.id.et_login_password);

        findViewById(R.id.btn_login_ok).setOnClickListener(this);
        findViewById(R.id.btn_login_cancel).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_ok:
                LoginData loginData = new LoginData();
                loginData.setName(etName.getText().toString());
                loginData.setPasswd(etPasswd.getText().toString());
                myBinder.checkin(loginData);
                break;
            case R.id.btn_login_cancel:
                setResult(1, new Intent(this, MainActivity.class));
                unbindService(this);
                finish();
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (RealTimeService.MyBinder) service;

        myBinder.getRealTimeService().setCallbackLogin(new RealTimeService.CallbackLogin() {
            @Override
            public void onAck(int isOK) {
                Bundle b = new Bundle();
                b.putInt("isOK", isOK);

                Message msg = new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });

       if (0!=UserPwdDeal.Init()){
//            Toast.makeText(this, "账户文件初始化失败", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBackPressed() {
        setResult(1, new Intent(this, MainActivity.class));
        unbindService(this);
        finish();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int isOK = msg.getData().getInt("isOK");
//            if (isOK==0){
//                //验证失败
//                Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                //验证成功
//                finish();
//            }
            if (isOK==2){
                //进入 一般操作员 界面
                finish();
            }
            else if (isOK==3){
                Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
            else if (isOK==4){
                Toast.makeText(Login.this, "用户不存在", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
