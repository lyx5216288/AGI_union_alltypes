package com.example.jbtang.agi_union.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.jbtang.agi_union.R;
import com.example.jbtang.agi_union.dao.BaseStationinfo;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDAO;
import com.example.jbtang.agi_union.dao.cellinfos.CellInfoDBManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 刘洋旭 on 2016/11/28.
 */
public class LBSActivity extends Activity {
    public MapView map;
    public BaiduMap mymap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener;
    LatLng point1, point2;
    //List<Point> list;
//    Marker[] markarr;
    public List<Marker>  markarr=new ArrayList<>();
    public List<BaseStationinfo> bsinfolist=new ArrayList<>();
    public BaseStationinfo bsinfo;
    private CellInfoDBManager cellInfoDBManager;

    private EditText tai,ecgi;
    private Button select;
    private Retrofit r;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.acticity_map);
        r = new Retrofit.Builder().baseUrl("http://api.cellocation.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        map = (MapView) findViewById(R.id.mymapview);
        mymap = map.getMap();
        tai= (EditText) findViewById(R.id.lbsmap_tai);
        ecgi= (EditText) findViewById(R.id.lbsmap_ecgi);
        select= (Button) findViewById(R.id.lbsmap_select);
        final InputMethodManager imm1 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imm1!=null){
                    imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);//从控件所在的窗口中隐藏
                }
                if(tai.getText().toString().length()>0&&ecgi.getText().toString().length()>0){
                    boolean flag=true;
                    for (int i=0;i<bsinfolist.size();i++){
                        if (ecgi.getText().toString().equals(bsinfolist.get(i).ecgi)){
                            LatLng l=new LatLng(Double.parseDouble(bsinfolist.get(i).getLat()),Double.parseDouble(bsinfolist.get(i).getLon()));
                            CoordinateConverter converter= new CoordinateConverter();
                            converter.from(CoordinateConverter.CoordType.GPS);
                            // l待转换坐标
                            converter.coord(l);
                            LatLng desLatLng = converter.convert();
                            //构建Marker图标
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(desLatLng)
                                    .zoom(18)
                                    .build();
                            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                            //改变地图状态
                            mymap.setMapStatus(mMapStatusUpdate);
                            flag=false;
                        }
                    }
                    if (flag){
                        GetMap service=r.create(GetMap.class);
                        Call<BaseStationinfo> call=service.getLbs(460,1,Integer.parseInt(tai.getText().toString()),Integer.parseInt(ecgi.getText().toString()),"json");
                        call.enqueue(new Callback<BaseStationinfo>() {
                            @Override
                            public void onResponse(Call<BaseStationinfo> call, Response<BaseStationinfo> response) {
                                bsinfo=response.body();
                                if (bsinfo.getErrcode()==0){
//                                    bsinfo.setEarfcn(cellinfo.earfcn+"");
                                    bsinfo.setEcgi(ecgi.getText().toString()+"");
//                                    bsinfo.setPci(cellinfo.pci+"");
                                    bsinfo.setTai(tai.getText().toString()+"");
                                    bsinfolist.add(bsinfo);
                                    LatLng l=new LatLng(Double.parseDouble(bsinfo.getLat()),Double.parseDouble(bsinfo.getLon()));
                                    CoordinateConverter converter= new CoordinateConverter();
                                    converter.from(CoordinateConverter.CoordType.GPS);
                                    // l待转换坐标
                                    converter.coord(l);
                                    LatLng desLatLng = converter.convert();
                                    //构建Marker图标
                                    MapStatus mMapStatus = new MapStatus.Builder()
                                            .target(desLatLng)
                                            .zoom(18)
                                            .build();
                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                    //改变地图状态
                                    mymap.setMapStatus(mMapStatusUpdate);
                                    BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                                            .fromResource(R.drawable.pushpins);
                                    //构建MarkerOption，用于在地图上添加Marker
                                    OverlayOptions option1 = new MarkerOptions()
                                            .position(desLatLng)
                                            .icon(bitmap1);
                                    //在地图上添加Marker，并显示
                                    markarr.add((Marker) mymap.addOverlay(option1));
                                }else if (bsinfo.getErrcode()==10000){
                                    Toast.makeText(LBSActivity.this, "参数错误,请输入正确参数！", Toast.LENGTH_SHORT).show();
                                }else if (bsinfo.getErrcode()==10001){
                                    Toast.makeText(LBSActivity.this, "无查询结果", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseStationinfo> call, Throwable t) {
                                Toast.makeText(LBSActivity.this, "查询失败，错误代码"+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(LBSActivity.this, "请输入正确的参数！！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //getBsinfolist();
        //Toast.makeText(LBSActivity.this, "123123123", Toast.LENGTH_SHORT).show();
//        testData();
        mLocationClient = new LocationClient(this.getApplicationContext());
        LocationClientOption locoption = new LocationClientOption();
        locoption.setOpenGps(true);        //是否打开GPS
        locoption.setCoorType("bd09ll");        //设置返回值的坐标类型。
        locoption.setPriority(LocationClientOption.NetWorkFirst);    //设置定位优先级
        //locoption.setProdName(this.getApplicationContext().getString(R.string.app_name));    //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        //locoption.setScanSpan(100);//设置定时定位的时间间隔。单位毫秒
        mLocationClient.setLocOption(locoption);
        myListener = new MyLocationListener();//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
        mLocationClient.requestLocation();
        mLocationClient.requestNotifyLocation();

        mymap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < markarr.size(); i++) {
                    if (marker.getPosition().longitude == markarr.get(i).getPosition().longitude) {
                        if (marker.getPosition().latitude == markarr.get(i).getPosition().latitude) {
                            View v1= View.inflate(LBSActivity.this, R.layout.baselocation_info,null);
                            TextView pci= (TextView) v1.findViewById(R.id.baselocation_pci);
                            pci.setText(bsinfolist.get(i).getPci());
                            TextView tai= (TextView) v1.findViewById(R.id.baselocation_tai);
                            tai.setText(bsinfolist.get(i).getTai());
                            TextView earfcn= (TextView) v1.findViewById(R.id.baselocation_earfcn);
                            earfcn.setText(bsinfolist.get(i).getEarfcn());
                            TextView ecgi= (TextView) v1.findViewById(R.id.baselocation_ecgi);
                            ecgi.setText(bsinfolist.get(i).getEcgi());
                            TextView address= (TextView) v1.findViewById(R.id.baselocation_address);
                            address.setText(bsinfolist.get(i).getAddress());
                            AlertDialog.Builder builder = new AlertDialog.Builder(LBSActivity.this);
                            AlertDialog d = builder.setTitle("基站信息").setView(v1).setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
                            d.show();
                        }
                    }
                }

                return false;
            }
        });

    }
    public Boolean isGetInfo=true;



    @Override
    public void onStart() {
        super.onStart();
    }

    public   static   void  removeDuplicate(List<CellInfoDAO> list)   {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )   {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )   {
                if  (list.get(j).ecgi==list.get(i).ecgi)   {
                    list.remove(j);
                }
            }
        }
//        System.out.println(list);
    }

    public   static   void  removeDuplicate1(List<BaseStationinfo> list)   {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )   {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )   {
                if  (list.get(j).ecgi.equals(list.get(i).ecgi))   {
                    list.remove(j);
                }
            }
        }
//        System.out.println(list);
    }



    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mymap.setMyLocationEnabled(true);
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(10).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            //116.422666,40.039669
            // 设置定位数据
            mymap.setMyLocationData(locData);
            LatLng ll = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());

            //float f = mymap.getMaxZoomLevel();//19.0
            // float m = mBaiduMap.getMinZoomLevel();//3.0
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
            mymap.animateMapStatus(u);
            MapStatusUpdate u1= MapStatusUpdateFactory.zoomTo(19f);
            mymap.setMapStatus(u1);

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.myloca1);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mymap.setMyLocationConfigeration(config);
            // 当不需要定位图层时关闭定位图层
            mymap.setMyLocationEnabled(false);

            //定义Maker坐标点
            LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.myloca1);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Marker marke= (Marker) mymap.addOverlay(option);
            cellInfoDBManager = new CellInfoDBManager(getApplicationContext());
            final List<CellInfoDAO>  cellInfoDAOList = cellInfoDBManager.listDB();
            removeDuplicate(cellInfoDAOList);
            GetMap service=r.create(GetMap.class);
                for(final CellInfoDAO cellinfo:cellInfoDAOList){

                    Call<BaseStationinfo> call=service.getLbs(460,1,cellinfo.tai,cellinfo.ecgi,"json");
                    call.enqueue(new Callback<BaseStationinfo>() {
                        @Override
                        public void onResponse(Call<BaseStationinfo> call, Response<BaseStationinfo> response) {
                            bsinfo=response.body();
                            if (bsinfo.getErrcode()==0){
                                bsinfo.setEarfcn(cellinfo.earfcn+"");
                                bsinfo.setEcgi(cellinfo.ecgi+"");
                                bsinfo.setPci(cellinfo.pci+"");
                                bsinfo.setTai(cellinfo.tai+"");
                                bsinfolist.add(bsinfo);
                                LatLng l=new LatLng(Double.parseDouble(bsinfo.getLat()),Double.parseDouble(bsinfo.getLon()));
                                CoordinateConverter converter= new CoordinateConverter();
                                converter.from(CoordinateConverter.CoordType.GPS);
                                // l待转换坐标
                                converter.coord(l);
                                LatLng desLatLng = converter.convert();
                                //构建Marker图标
                                BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                                    .fromResource(R.drawable.pushpins);
                                //构建MarkerOption，用于在地图上添加Marker
                                OverlayOptions option1 = new MarkerOptions()
                                    .position(desLatLng)
                                    .icon(bitmap1);
                                //在地图上添加Marker，并显示
                                markarr.add((Marker) mymap.addOverlay(option1));
                        }else if (bsinfo.getErrcode()==10000){
                            Toast.makeText(LBSActivity.this, "参数错误,请输入正确参数！", Toast.LENGTH_SHORT).show();
                        }else if (bsinfo.getErrcode()==10001){
                            Toast.makeText(LBSActivity.this, "无查询结果", Toast.LENGTH_SHORT).show();
                        }


                        }

                        @Override
                        public void onFailure(Call<BaseStationinfo> call, Throwable t) {
                            Toast.makeText(LBSActivity.this, "查询失败，错误代码"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }



    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        map.onPause();
    }
}

