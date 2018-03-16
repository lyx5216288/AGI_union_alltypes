package com.hardware.board.board_C1;

import com.adapter.CommonClass.StatusReport;
import com.adapter.Constants_Board_Model;
import com.adapter.event.StatusReport.StatusReportManager;
import com.adapter.event.logprint.LogPrintManager;
import com.hardware.board.Global;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_HB;

public class PingThread extends Thread {

    private String Tag = "PingThread";
    private int index;
    private String ipaddr;

    private boolean isover;


    public PingThread(int i, String _ipaddr) {
        this.index = i;
        this.ipaddr = _ipaddr;
    }

    private void SendHBtoUp(){
        int statustype = StatusType_BoardC1_HB;

        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;
        //wrFLEnbToLmtSysInitInformInd hb = (wrFLEnbToLmtSysInitInformInd) cmdObject.getO();

        boolean issuccess = true;
        int errcode = 0;
        String msg = "收到下位机心跳";


        Object obj = null;

        StatusReport sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);



        StatusReport sr = sta;
        sr.setIndex(this.index);
        List<StatusReport> lists = new ArrayList<>();
        lists.add(sr);
        StatusReportManager.getInstance().fireEvent(lists);
    }

    public void SetOver(){
        LogPrintManager.Print("info", Tag, this.index+" in Set Over");
        this.isover = true;
        this.interrupt();

        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogPrintManager.Print("info", Tag, this.index+" out Set Over");
    }


    private byte[] GetQueryBytes(){

        byte[]b = new byte[12];
        b[0] = (byte)0xAA;
        b[1] = (byte)0xAA;
        b[2] = (byte)0x55;
        b[3] = (byte)0x55;

        b[4] = (byte)0x31;
        b[5] = (byte)0xF0;

        b[6] = (byte)0x0C;
        b[7] = (byte)0x00;
        b[8] = (byte)0x00;
        b[9] = (byte)0x00;
        b[10] = (byte)0x00;
        b[11] = (byte)0xFF;

        return b;

    }

    @Override
    public void run() {

        LogPrintManager.Print("info", Tag, "ping thread start....");
        boolean status = false;
        int timeOut = 3000;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (isover == false) {

            try
            {
//                status = InetAddress.getByName(this.ipaddr).isReachable(timeOut);
//
//                if(status){
//
//                    LogPrintManager.Print("info", Tag, this.index+" ping thread hb");
//                    SendHBtoUp();
//                }


                byte[] bytes = GetQueryBytes();
                Global.udpmachine.SendBytes(this.index, bytes);
                LogPrintManager.Print("info", Tag, this.index+" cycle thread query power and gain");

                Thread.sleep(10000);
            }
            catch(Exception e)
            {
                LogPrintManager.Print("error", Tag, e.getMessage());
            }




        }

        LogPrintManager.Print("info", Tag, "ping thread over");
    }
}
