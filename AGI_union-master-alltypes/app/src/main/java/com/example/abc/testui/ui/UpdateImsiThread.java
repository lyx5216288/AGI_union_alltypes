package com.example.abc.testui.ui;


/**
 * Created by usr on 2017/9/6.
 */

public class UpdateImsiThread extends Thread {

    private long  imsi;
    private int istarget;
    private int rssi;

    public UpdateImsiThread(long imsi_, int istarget_, int rssi_) {
        this.imsi = imsi_;
        this.istarget = istarget_;
        this.rssi = rssi_;
    }



    @Override
    public void run(){

        if(Global.service !=null){
            //Global.service.AddANewData_2(imsi, istarget);


            Global.service.addANewData(imsi, istarget, rssi);

        }


    }



}
