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
import android.view.View;
import android.widget.TextView;

//import com.example.abc.testui.R;
import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class EnergyShow extends AppCompatActivity implements ServiceConnection {

    private RealTimeService.MyBinder myBinder;

    private TextView tv_imsi;
    private TextView tv_earfcn;
    private TextView tv_pci;
    private TextView tv_bigEnergyValue;
    private TextView tv_time;

    private ParamSetValue paramSetValue;
    private long target2service_imsi;
    private List<Integer> listEnergyValue;

    //能量图标
    private LineChartView chart;
    private LineChartData data;

    int numindex = 0;
    int []num = new int[10];
    private int numberOfPoints = 10;
    private int numberOfLines = 1;

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = true;
    private boolean hasLabels = true;
    private boolean isCubic = true;
    private boolean hasLabelForSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Global.UI_FORMAT == Global.UI_V2){
            setTheme(R.style.GrayTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy_show);

        Intent intent = new Intent(this, RealTimeService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        paramSetValue = getIntent().getParcelableExtra("paramSetValue");
        target2service_imsi = getIntent().getLongExtra("target2service_imsi", 0);
        listEnergyValue = getIntent().getIntegerArrayListExtra("listEnergyValue");

        init_toolbar();
        init_tv();

        //图表
        chart = (LineChartView) findViewById(R.id.chart);
        ResetChart();

        initChart();
    }

    public void init_toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.energyShow_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 标题上的回退按钮的点击处理
                finish();
            }
        });
    }

    public void init_tv(){
        tv_imsi = (TextView) findViewById(R.id.tv_energyshow_imsi_id);
        tv_earfcn = (TextView) findViewById(R.id.tv_energyshow_earfcn_id);
        tv_pci = (TextView) findViewById(R.id.tv_energyshow_pci_id);
        tv_bigEnergyValue = (TextView) findViewById(R.id.textView_loc);
        tv_time = (TextView) findViewById(R.id.tv_energyshow_time_id);

        if (target2service_imsi==0){
            tv_imsi.setText("IMSI : ");
        }
        else {
            tv_imsi.setText("IMSI : " + target2service_imsi);
        }
        tv_earfcn.setText("EARFCN : "+paramSetValue.getEarfcn());
        tv_pci.setText("PCI : "+paramSetValue.getPci());
        if (listEnergyValue.size()>0)
            tv_bigEnergyValue.setText(""+listEnergyValue.get(listEnergyValue.size()-1).intValue());

        tv_time.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    private void initChart(){
        for (int i=0; i<listEnergyValue.size(); i++){
            AddOneDataToChart(listEnergyValue.get(i));
        }
    }

    private void ResetChart(){
        numindex = 0;
        for (int i = 0; i<numindex; i++){
            num[i] = -1;
        }

        DisplayChart();
    }

    private void DisplayChart(){
        // Generate some random values.
        //generateValues();

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numindex; ++j) {
                //values.add(new PointValue(j, randomNumbersTab[i][j]));
                values.add(new PointValue(j, num[j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[3]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            // line.setHasGradientToTransparent(hasGradientToTransparent);
//            if (pointsHaveDifferentColor){
//                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            //data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void AddOneDataToChart(int val){

        if(numindex<10){
            num[numindex] = val;
            numindex++;
        }
        else {

            int i = 0;
            int j = 1;
            while (i<10&&j<10){
                num[i] = num[j];
                i++;
                j++;
            }
            num[9] = val;
        }

        DisplayChart();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (RealTimeService.MyBinder) service;
        myBinder.getRealTimeService().setCallbackEnergyShow(new RealTimeService.CallbackEnergyShow() {
            @Override
            public void onEnergyValueChange(int value) {
                AddOneDataToChart(value);

                Bundle b = new Bundle();
                b.putInt("value", value);
                Message msg = new Message();
                msg.setData(b);
                handler_setBigEnergyValue.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private Handler handler_setBigEnergyValue = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            tv_bigEnergyValue.setText(""+msg.getData().getInt("value"));
            tv_time.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
    };
}
