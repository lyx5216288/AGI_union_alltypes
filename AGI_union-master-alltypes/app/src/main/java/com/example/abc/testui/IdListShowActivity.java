package com.example.abc.testui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

//import com.example.abc.testui.R;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

public class IdListShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_list_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idListShow_toolbar_id);
        setSupportActionBar(toolbar);

        initIdList();
    }

    public void initIdList(){
        final TextView tv = (TextView) findViewById(R.id.tv_idlistshow_id);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        IdListSetLog idListSetLog = new IdListSetLog();
//        idListSetLog.readfile(tv);
        idListSetLog.readBLog(tv);
    }
}
