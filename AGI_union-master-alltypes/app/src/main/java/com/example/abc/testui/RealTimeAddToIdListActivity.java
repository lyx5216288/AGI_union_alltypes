package com.example.abc.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jbtang.agi_union.R;

//import com.example.abc.testui.R;

public class RealTimeAddToIdListActivity extends AppCompatActivity implements View.OnClickListener {

    private long imsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_add_to_id_list);

        imsi = getIntent().getLongExtra("imsi", 0);
        TextView tv_imsi = (TextView) findViewById(R.id.tv_rt2idlist_imsi_id);
        tv_imsi.setText(""+imsi);

        findViewById(R.id.btn_ok_rt2idlist_id).setOnClickListener(this);
        findViewById(R.id.btn_cancel_rt2idlist_id).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RealTimeAddToIdListActivity.this, MainActivity.class);

        intent.putExtra("imsi", imsi);
        switch (v.getId()){
            case R.id.btn_ok_rt2idlist_id:
                setResult(1, intent);
                finish();
                break;
            case R.id.btn_cancel_rt2idlist_id:
                setResult(2, intent);
                finish();
                break;
        }
    }
}
