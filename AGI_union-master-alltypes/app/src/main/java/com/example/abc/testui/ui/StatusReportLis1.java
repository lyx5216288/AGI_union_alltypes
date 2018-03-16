package com.example.abc.testui.ui;

import com.adapter.CommonClass.StatusReport;
import com.adapter.event.StatusReport.StatusReportEvent;
import com.adapter.event.StatusReport.StatusReportListener;
import com.hardware.board.board_C1.MeasUeInfoObject;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLEnbToLmtScanCellInfoRpt;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_ARFCNPCIRPT;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_CELLSTATERPT;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_HB;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_MEASUE_RPT;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_REBOOT_ACK;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_SCANFREQ;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_STATION_RPT;


/**
 * Created by john on 2017/7/18.
 */
public class StatusReportLis1 implements StatusReportListener{


    private int ueflag = 0;

    public static String FormatStrStatusReport(StatusReport report){
        StatusReport statusReport = report;
        String str = String.format("index = %d  issuc = %s errcode = %d  msg = %s\n",
                statusReport.getIndex(),
                ((Boolean)statusReport.isIssuccess()).toString(), statusReport.getErrcode(), statusReport.getMsg());
        return str;
    }


    //希望这里面的实现是不耗时的，比如启动一个线程之类的。
    @Override
    public void StatusReportHandle(StatusReportEvent event) {


        //这里你应该启动一个线程 将msg 显示到 日志流水框中


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //模拟状态数据、能力值产生
//                while (true){
//                    if (callback!=null) {
//                        addANewState("this is state values");
//                        callback.onStateValuesChange(listStateValues.size());
//
//                        Random random = new Random();
//                        int i = random.nextInt(100);
//                        int value = addANewValue(i);
//                        callback.onEnergyValueChange(value);
//
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();


        List<StatusReport> lists = event.getLists();
        for(int i=0; i<lists.size();i++) {
            StatusReport statusReport = lists.get(i);


            if(StatusType_BoardC1_MEASUE_RPT == statusReport.getStatustype()){
                //UE测量值上报

                MeasUeInfoObject meobj = (MeasUeInfoObject)statusReport.getObj();
                if(meobj != null)
                {


                    Global.service.addANewValue(meobj.getMeasval());



                    Global.uenewtime = new Date();

                    Global.UErecv = true;

                    if(Global.UErecvoffline){

                        if(Global.activity!=null){
                            if(Global.activity.voiceenable)
                            {
                                Global.soundPlayer.playSoundOnLine();

                            }

                        }
                        Global.service.setTab(1);
                        Global.UErecvoffline = false;
                    }


                }

            }
            else if(statusReport.getStatustype() == StatusType_BoardC1_REBOOT_ACK){
                Global.HBrecv = false;
                String msg = statusReport.getMsg();
                Global.service.addANewState(msg);
            }
            else if(statusReport.getStatustype() == StatusType_BoardC1_HB ){
                //这里要处理只能只显示一次心跳的问题；
                if(Global.HBrecv == false)
                {

                    //并且还要使能 按钮等


                    String modeltype = com.hardware.board.Global.ModelType;
                    //设置主界面中的 模式选择 下拉框
                    // index==0, TDD；index==1，FDD
                    if(modeltype.equals("TDD")){
                        Global.service.setMode(0);
                        Global.service.setMainActivityTitle("主动定位 TDD");
                    }
                    else if(modeltype.equals("FDD")){
                        Global.service.setMode(1);
                        Global.service.setMainActivityTitle("主动定位 FDD");
                    }


                    Global.HBrecv = true;
                    String msg = statusReport.getMsg();
                    msg = msg + " "+modeltype;
                    Global.service.addANewState(msg);


                    Global.service.changeHeartState(1);


                    //发送基站状态查询命令
                    (new QueryStateThread()).start();
                    (new QueryArfcnThread()).start();

                }

                Global.service.setBtnStartEnable(1);
                Global.service.setBtnStopEnable(1);

            }
            else if(statusReport.getStatustype() == StatusType_BoardC1_STATION_RPT){

                int val = statusReport.getErrcode();

                if(val == 6){
                    //小区已建立
                    Global.service.changeConnState(1);

                    Global.cellsetup = true;
                }
                else if(val == 8){
                   //小区去激活

                    if(Global.cellstopoverthenchangemode == true){
//                        Toast.makeText(getApplicationContext(),"设置模式 "+ mode, Toast.LENGTH_SHORT).show();
//                        (new ChangeModeThread(mode)).start();
                        //change mode

                        if(Global.service!=null){
                            Global.service.SendModeChange();
                        }

                        Global.cellstopoverthenchangemode = false;
                    }

                    Global.service.changeConnState(0);
                    Global.cellsetup = false;
                }
                else if(val==0 || val==1|| val==7|| val==8|| val==9|| val==12|| val==13 ){
                    Global.service.changeConnState(0);
                    Global.cellsetup = false;
                }


                String msg = statusReport.getMsg();
                Global.service.addANewState(msg);
            }
            else if(statusReport.getStatustype() == StatusType_BoardC1_CELLSTATERPT){
                //查询基站状态上传
                int code = statusReport.getErrcode();
                if(code == 3){
                    Global.service.changeConnState(1);

                    Global.cellsetup = true;
                }
                else {
                    Global.service.changeConnState(0);

                    Global.cellsetup = false;
                }
            }
            else if(statusReport.getStatustype() == StatusType_BoardC1_SCANFREQ){
                //扫频
                Object o = statusReport.getObj();
                if(o!=null){

                    Object[] olist = (Object[])o;

                    Global.service.setToast("有扫频回包了");

                    StringWriter writer = new StringWriter();
                    PrintWriter bufwrite = new PrintWriter(writer);


                    //bufwrite.println("显示收到的字符串");
                    byte buf[] = (byte[]) olist[1];


                    for (int q = 0; q<buf.length; q++){
                        int val11 = 0x000000FF & buf[q];
                        bufwrite.printf("%02X ", val11);

                        //if()
                    }


                    Global.service.AddANewFreqStr(writer.toString());
                    wrFLEnbToLmtScanCellInfoRpt rpt = (wrFLEnbToLmtScanCellInfoRpt)olist[0];
                    List<ScanFreqDisplay> dis = ScanFreqDisplay.GetFromOrigin(rpt);

                    //Global.service.ClearScanData();
                    Global.service.SetBtnEnable();

                    if(dis!=null && dis.size()!=0){
                        for (int k=0; k<dis.size(); k++){
                            int arfcn = dis.get(k).arfcn;
                            int priority = dis.get(k).priority;
                            int pci = dis.get(k).pci;
                            try {
                                Thread.sleep(100);
                            }
                            catch (Exception e)
                            {
                                ;
                            }

                            Global.service.AddANewFreq(arfcn, priority, pci);
                        }

                    }
                }
            }
            else if(StatusType_BoardC1_ARFCNPCIRPT == statusReport.getStatustype()){
                //收到 earfcn 和 pci
                Object o = statusReport.getObj();
                Object[] olist = (Object[])o;
                int earfcn = (int)olist[0];
                int pci = (int)olist[1];

                Global.service.setArfcn_Pci(earfcn, pci);

            }
            else {
                String msg = statusReport.getMsg();
                Global.service.addANewState(msg);
            }



//            if(statusReport.getStatustype() != StatusType_BoardC1_HB){
//                String str = FormatStrStatusReport(statusReport);
//
//                System.out.println(str);
//            }

        }





    }
}
