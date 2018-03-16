package com.example.abc.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.jbtang.agi_union.R;

//import com.example.abc.testui.R;

public class IdListAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_imsi;
    private EditText et_name;
    int requestType;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_list_add);

        et_imsi = (EditText) findViewById(R.id.et_imsi_id_list_add_id);
        et_name = (EditText) findViewById(R.id.et_name_id_list_add_id);

        findViewById(R.id.btn_ok_id_list_add_id).setOnClickListener(this);
        findViewById(R.id.btn_cancel_id_list_add_id).setOnClickListener(this);

        Intent intent = getIntent();
        requestType = intent.getIntExtra("requestType", 0);
        position = intent.getIntExtra("position", 0);
        switch (requestType){
            case 1:
                setTitle("新增");
                break;
            case 2:
                setTitle("编辑");
                long imsi = intent.getLongExtra("imsi", 0);
                String name = intent.getStringExtra("name");
                et_imsi.setText(""+imsi);
                et_name.setText(name);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, IdListSetActivity.class);

        switch (v.getId()){
            case R.id.btn_ok_id_list_add_id:
                if (et_imsi.getText().toString().isEmpty()==false){
                    long imsi = Long.parseLong(et_imsi.getText().toString());
                    String name = et_name.getText().toString();
                    intent.putExtra("imsi", imsi);
                    intent.putExtra("name", name);
                    intent.putExtra("position", position);
                    setResult(1, intent);
                    finish();
                }
                break;
            case R.id.btn_cancel_id_list_add_id:
                setResult(2, intent);
                finish();
                break;
        }
    }
}
