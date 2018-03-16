package com.example.jbtang.agi_union.service;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.print.PrinterCapabilitiesInfo;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.example.jbtang.agi_union.ui.OrientationFindingActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by 刘洋旭 on 2017/5/11.
 */
public class BluetoothService {
    private static final String TAG="BluetoothService";
    private static final BluetoothService instance=new BluetoothService();
    private static final boolean D=true;
    private Activity currentActivity;
    private static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private List<String> lstDevices=new ArrayList();
    private BluetoothAdapter mAdapter=null;
    private Activity currentActicity;
    private byte[]  byteall=new byte[]{0x7E,(byte) 0xF5,(byte) 0xFF, 0x55,0x55, 0x55,(byte) 0xF5, 0x7E};
    private byte[]  byteb=new byte[]{0x7E,(byte) 0xF1,(byte) 0xFF, 0x11,0x11, 0x11,(byte) 0xF1, 0x7E};
    private byte[]  bytea=new byte[]{0x7E,(byte) 0xF3,(byte) 0xFF, 0x33,0x33, 0x33,(byte) 0xF3, 0x7E};
    private byte[]  bytel=new byte[]{0x7E,(byte) 0xF4,(byte) 0xFF, 0x44,0x44, 0x44,(byte) 0xF4, 0x7E};
    private byte[]  byter=new byte[]{0x7E,(byte) 0xF2,(byte) 0xFF, 0x22,0x22, 0x22,(byte) 0xF2, 0x7E};
    private byte[][] mbytes={byteb,byter,bytea,bytel,byteall};
    private BluetoothDevice mydevice;
    private BluetoothSocket btsocket;
    private readThread mreadThread;
    private ConnectThread connectthread;
    public boolean isconnect;
    public int index;
    private final Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    private BluetoothService(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        isconnect=false;
        index=-1;
    }
    public static BluetoothService getInstance() {
        return instance;
    }


    public void start(Activity activity,int index) throws IOException {
        currentActivity=activity;
        this.index=index;
        if (!mAdapter.isEnabled()){
            Toast.makeText(currentActivity, "蓝牙未打开！", Toast.LENGTH_SHORT).show();
            return;
        }
        Set<BluetoothDevice> devices= mAdapter.getBondedDevices();
        for (BluetoothDevice xx:devices){
            if (xx.getName().equals("HC-06")){
                mydevice=xx;

            }
        }
        if (mydevice!=null){
            connectthread=new ConnectThread();
            connectthread.start();
        }
    }

    private class ConnectThread extends Thread{
        @Override
        public void run() {
            try {
            if (btsocket!=null&&btsocket.isConnected()){
                mreadThread=new readThread();
                mreadThread.start();
            }else {
                btsocket = mydevice.createRfcommSocketToServiceRecord(MY_UUID);
                btsocket.connect();
                Looper.prepare();
                Toast.makeText(currentActivity, "已连接！", Toast.LENGTH_SHORT).show();
                Looper.loop();
                isconnect=true;
                sendOrienMsg();
            }
            }catch (IOException e){
                Looper.prepare();
                Toast.makeText(currentActivity, "蓝牙连接异常！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }
    public void sendOrienMsg(){
        if (isconnect){
            mreadThread=new readThread();
            mreadThread.start();
        }else {
            Toast.makeText(currentActivity, "蓝牙未连接！", Toast.LENGTH_SHORT).show();
        }
    }


    private class readThread extends Thread{
        @Override
        public void run() {
            if (btsocket==null){
                Looper.prepare();
                Toast.makeText(currentActicity, "蓝牙未连接！", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return;
            }
            OutputStream os = null;
            InputStream is=null;
            try{
                os=btsocket.getOutputStream();
                int x= OrientationFindingActivity.spinner.getSelectedItemPosition();
                Log.e("send","DATA!!!!!!");
                os.write(mbytes[index]);
                StringBuffer str=new StringBuffer();
                byte[] read=new byte[1024];
                is=btsocket.getInputStream();
                while(is.read(read)!=-1){
                    str.append(new String(read));
                }
                Looper.prepare();
                Toast.makeText(currentActicity, ""+str, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }catch (IOException e){

            }finally {
                try {
                    if (os!=null){os.close();}
                    if (is!=null){is.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

//    public BluetoothService() throws IOException {
//        mAdapter = BluetoothAdapter.getDefaultAdapter();
//        mAdapter.listenUsingRfcommWithServiceRecord("hello",MY_UUID);
//        mAdapter.startDiscovery();
//        Set<BluetoothDevice> devices= mAdapter.getBondedDevices();
//        mReceiver=new myBroadReceiver();
//        byte[] bytes="7EF1FF111111F17E".getBytes();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        currentActivity.registerReceiver(mReceiver, filter);
//        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        currentActivity.registerReceiver(mReceiver, filter);
//        BluetoothDevice device=mAdapter.getRemoteDevice("keyname");
//        BluetoothSocket btsocket=device.createRfcommSocketToServiceRecord(MY_UUID);
//        InputStream is=null;
//        OutputStream os=null;
//        try{
//            btsocket.connect();
//            is=btsocket.getInputStream();
//            os=btsocket.getOutputStream();
//            os.write(bytes);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if (is!=null){is.close();}
//            if (os!=null){os.close();}
//        }
//    }
    public void  init(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mAdapter.isEnabled()){
            Toast.makeText(currentActicity, "请打开蓝牙并配对！", Toast.LENGTH_SHORT).show();
        }

    }



}
