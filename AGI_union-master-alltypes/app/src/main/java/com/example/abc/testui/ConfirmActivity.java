package com.example.abc.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi_union.R;

//import com.example.abc.testui.R;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
//        setContentView(android.R.layout.a);
        int flag = getIntent().getIntExtra("flag", 0);
        TextView tv = (TextView) findViewById(R.id.tv_confirm_title_id);
        if (flag==1){
            tv.setText("是否设置模式");
        }
        else if (flag==2){
            tv.setText("是否重启");
        }
        else if (flag==3){
            tv.setText("是否退出程序");
        }
        else{
            Toast.makeText(this, "unknown flag", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btn_confirm_ok_id).setOnClickListener(this);
        findViewById(R.id.btn_confirm_cancle_id).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (v.getId()){
            case R.id.btn_confirm_ok_id:
                setResult(1, intent);
                finish();
                break;
            case R.id.btn_confirm_cancle_id:
                setResult(0, intent);
                finish();
                break;
        }
    }
}
