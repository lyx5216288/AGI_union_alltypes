package com.example.abc.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.abc.testui.R;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParamSetActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String[] PLMN_DATAS = {"中国移动(46000)", "中国联通(46001)", "中国电信(46003)", "电信4G(46011)"};
    public static final int[] PLMN_DATAS_INT = {46000, 46001, 46003, 46011};
    public static final int[] EARFCN_DATAS = {38400, 38544, 37900, 38098, 38950, 40936, 39147, 1650, 1825, 100};
    public static final String PCI_DEFAULT = "88";
    public static final int TAC_MAX = 65535;
    public static final int CELLID_MAX = 268435455;
//    public static final String[] POWER_DATAS = {"5档(0)", "4档(12)", "3档(24)", "2档(36)", "1档(48)"};
//    public static final int[] POWER_DATAS_INT = {0, 12, 24, 36, 48};

    private SpinnerEditText<BaseBean> plmn_spinner_edittext;
    private SpinnerEditText<BaseBean> earfcn_spinner_edittext;
    private EditText pci_edittext;
    private TextView tac_textview;
    private TextView cellid_textview;
    private SpinnerEditText<BaseBean> power_spinner_edittext;

    private ParamSetValue paramSetValueBack;

    private int earfcn2scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);

        if (Global.UI_FORMAT == Global.UI_V1){
            setContentView(R.layout.activity_param_set);
        }
        else{
//            setContentView(R.layout.activity_param_set_v2);
        }

        init_toolbar();

        Intent intent = getIntent();
        ParamSetValue paramSetValue = intent.getParcelableExtra("paramSetValue");

        plmn_spinner_edittext = (SpinnerEditText<BaseBean>) findViewById(R.id.plmn_spinner_edittext_id);
        earfcn_spinner_edittext = (SpinnerEditText<BaseBean>) findViewById(R.id.earfcn_spinner_edittext_id);
        pci_edittext = (EditText) findViewById(R.id.pci_edittext_id);
        tac_textview = (TextView) findViewById(R.id.tac_textview_id);
        cellid_textview = (TextView) findViewById(R.id.cellid_textview_id);
//        power_spinner_edittext = (SpinnerEditText<BaseBean>) findViewById(R.id.power_spinner_edittext_id);

        plmn_spinner_edittext.setRightCompoundDrawable(R.drawable.vector_drawable_arrowdown);
        List<BaseBean> plmn_baseBeanList = new ArrayList<>();
        for (int i=0; i<PLMN_DATAS.length; i++){
            BaseBean baseBean = new BaseBean();
            baseBean.data = PLMN_DATAS[i];
            plmn_baseBeanList.add(baseBean);
        }
        plmn_spinner_edittext.setNeedShowSpinner(true);
        plmn_spinner_edittext.setList(plmn_baseBeanList);
        plmn_spinner_edittext.setText(""+paramSetValue.getPlmn());

        earfcn_spinner_edittext.setRightCompoundDrawable(R.drawable.vector_drawable_arrowdown);
        List<BaseBean> earfcn_baseBeanList = new ArrayList<>();
        for (int i=0; i<EARFCN_DATAS.length; i++){
            BaseBean baseBean = new BaseBean();
            baseBean.data = ""+EARFCN_DATAS[i];
            earfcn_baseBeanList.add(baseBean);
        }
        earfcn_spinner_edittext.setNeedShowSpinner(true);
        earfcn_spinner_edittext.setList(earfcn_baseBeanList);
        earfcn_spinner_edittext.setText(""+paramSetValue.getEarfcn());

        earfcn2scan = paramSetValue.getEarfcn();

        pci_edittext.setText(""+paramSetValue.getPci());

        Random random = new Random();
        String tac_data = String.format("%d", random.nextInt(TAC_MAX));
        tac_textview.setText(tac_data);
        String cellid_data = String.format("%d",random.nextInt(CELLID_MAX));
        cellid_textview.setText(cellid_data);

//        power_spinner_edittext.setRightCompoundDrawable(R.drawable.vector_drawable_arrowdown);
//        List<BaseBean> power_baseBeanList = new ArrayList<>();
//        for (int i=0; i<POWER_DATAS.length; i++){
//            BaseBean baseBean = new BaseBean();
//            baseBean.data = POWER_DATAS[i];
//            power_baseBeanList.add(baseBean);
//        }
//        power_spinner_edittext.setNeedShowSpinner(true);
//        power_spinner_edittext.setList(power_baseBeanList);
//        power_spinner_edittext.setText(""+paramSetValue.getPower());

        findViewById(R.id.btn_param_set_ok_id).setOnClickListener(this);
        findViewById(R.id.btn_param_set_reset_id).setOnClickListener(this);
        findViewById(R.id.btn_param_set_cancel_id).setOnClickListener(this);
//        findViewById(R.id.btn_param_set_scan_id).setOnClickListener(this);

        paramSetValueBack = new ParamSetValue();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);

        switch (v.getId()){
            case R.id.btn_param_set_ok_id:
                getParamValues();
                intent.putExtra("paramSetValue", paramSetValueBack);
                setResult(1, intent);
                finish();
                break;
            case R.id.btn_param_set_reset_id:
                plmn_spinner_edittext.setText(PLMN_DATAS[0]);
                earfcn_spinner_edittext.setText(""+EARFCN_DATAS[3]);
                pci_edittext.setText(PCI_DEFAULT);
//                power_spinner_edittext.setText(POWER_DATAS[0]);
                break;
            case R.id.btn_param_set_cancel_id:
                setResult(2, intent);
                finish();
                break;
//            case R.id.btn_param_set_scan_id:
//                intent = new Intent(this, ScanActivity.class);
//                intent.putExtra("earfcn", earfcn2scan);
//                startActivity(intent);
//                break;
        }
    }

    public static class BaseBean {
        public String data;

        @Override
        public String toString() {
            return data;
        }
    }

    public void init_toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.paramSet_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 标题上的回退按钮的点击处理
                //setResult(2, new Intent(ParamSetActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void getParamValues(){
        String tmpStr;
        int tmpInt = -1;

        tmpStr = plmn_spinner_edittext.getText().toString();
        for (int i=0; i<PLMN_DATAS.length; i++){
            if (tmpStr.equals(PLMN_DATAS[i])){
                tmpInt = PLMN_DATAS_INT[i];
                break;
            }
        }
        if (tmpInt==-1){
            tmpInt = Integer.parseInt(tmpStr);
        }
        paramSetValueBack.setPlmn(tmpInt);

        paramSetValueBack.setEarfcn(Integer.parseInt(earfcn_spinner_edittext.getText().toString()));
        paramSetValueBack.setPci(Integer.parseInt(pci_edittext.getText().toString()));
        paramSetValueBack.setTac(Integer.parseInt(tac_textview.getText().toString()));
        paramSetValueBack.setCellId(Integer.parseInt(cellid_textview.getText().toString()));

//        tmpInt = -1;
//        tmpStr = power_spinner_edittext.getText().toString();
//        for (int i=0; i<POWER_DATAS.length; i++){
//            if (tmpStr.equals(POWER_DATAS[i])){
//                tmpInt = POWER_DATAS_INT[i];
//                break;
//            }
//        }
//        if (tmpInt==-1){
//            tmpInt = Integer.parseInt(tmpStr);
//        }
//        paramSetValueBack.setPower(tmpInt);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(2, new Intent(ParamSetActivity.this, MainActivity.class));
        finish();
    }
}
