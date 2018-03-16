package com.example.abc.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jbtang.agi_union.R;

//import com.example.abc.testui.R;

public class TargetSelected2ServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_imsi;
    private long imsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_selected2_service);

        tv_imsi = (TextView) findViewById(R.id.tv_target2service_imsi_id);
        findViewById(R.id.btn_ok_rtarget2service_id).setOnClickListener(this);
        findViewById(R.id.btn_cancel_target2service_id).setOnClickListener(this);

        imsi = getIntent().getLongExtra("imsi", 0);
        tv_imsi.setText(""+imsi);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("imsi", imsi);
        switch (v.getId()){
            case R.id.btn_ok_rtarget2service_id:
                setResult(1, intent);
                finish();
                break;
            case R.id.btn_cancel_target2service_id:
                setResult(2, intent);
                finish();
                break;
        }
    }
}
