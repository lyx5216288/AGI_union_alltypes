package com.example.jbtang.agi_union.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi_union.R;
import com.example.jbtang.agi_union.core.CellInfo;
import com.example.jbtang.agi_union.core.Global;
import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.MybytesUtil;
import com.example.jbtang.agi_union.core.Status;
import com.example.jbtang.agi_union.core.type.U32;
import com.example.jbtang.agi_union.core.type.U8;
import com.example.jbtang.agi_union.device.DeviceManager;
import com.example.jbtang.agi_union.device.MonitorDevice;
import com.example.jbtang.agi_union.messages.GetFrequentlyUsedMsg;
import com.example.jbtang.agi_union.messages.MessageDispatcher;
import com.example.jbtang.agi_union.messages.ag2pc.L2P_MAC_DATA_STRU;
import com.example.jbtang.agi_union.messages.ag2pc.L2P_MAC_PDU_SUBHEADER_STRU;
import com.example.jbtang.agi_union.messages.ag2pc.L2P_PROTOCOL_DATA;
import com.example.jbtang.agi_union.messages.ag2pc.L2P_RRCNAS_MSG_STRU;
import com.example.jbtang.agi_union.messages.ag2pc.MMEC;
import com.example.jbtang.agi_union.messages.ag2pc.MsgCRS_RSRPQI_INFO;
import com.example.jbtang.agi_union.messages.ag2pc.MsgL1_PHY_COMMEAS_IND;
import com.example.jbtang.agi_union.messages.ag2pc.MsgL2P_AG_CELL_CAPTURE_IND;
import com.example.jbtang.agi_union.messages.ag2pc.MsgL2P_AG_UE_CAPTURE_IND;
import com.example.jbtang.agi_union.messages.ag2pc.PagingRecord;
import com.example.jbtang.agi_union.messages.ag2pc.PagingRecordList;
import com.example.jbtang.agi_union.messages.ag2pc.PagingUE_Identity;
import com.example.jbtang.agi_union.messages.ag2pc.S_TMSI;
import com.example.jbtang.agi_union.messages.base.MsgTypes;
import com.example.jbtang.agi_union.trigger.PhoneTrigger;
import com.example.jbtang.agi_union.trigger.SMSTrigger;
import com.example.jbtang.agi_union.trigger.Trigger;
import com.example.jbtang.agi_union.ui.FindSTMSIActivity;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.opengles.GL;


/**
 * Created by jbtang on 11/1/2015.
 */
public class FindSTMSI {
    private static final String TAG = "FindSTMSI";
    private static final FindSTMSI instance = new FindSTMSI();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final Integer DoubtfulTime = 500;//疑似目标时间间隔
    private Activity currentActivity;
    private boolean starflag;
    public static Map<String, CountSortedInfo> sTMSI2Count;
    private List<CountSortedInfo> countSortedInfoList;
    private myHandler handler;
    private Trigger trigger;
    private CheckBox filterCheckBox;
    private Status.Service service;
    public int stmsiCount;
    public int sumCount;
    public int nullCount;
    private Map<String, Timer> timerMap;
    private Map<String, String> FnMap;
    private HashSet<String> paginglist;
    private static int  connindex=0;
    private List<CellInfo> cellInfoList;
//    private String pagingstmsi;
//    private Map<String, Timer> stmsiMap;
    private Map<String, Timer> protocolMap;
    private Handler hander1=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
                final MonitorDevice monitorDevice11= (MonitorDevice) msg.obj;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.e("Device",monitorDevice11.getName()+"restar!!");
                        monitorDevice11.startMonitor(Status.Service.FINDSTMIS);
                        monitorDevice11.setStartAgain(true);
                        if(timerMap.get(monitorDevice11.getName())!=null){
                            timerMap.get(monitorDevice11.getName()).cancel();
                            timerMap.remove(monitorDevice11.getName());
                            timerMap.put(monitorDevice11.getName(), new Timer());
                            timerMap.get(monitorDevice11.getName()).schedule(new MyTimerTask(monitorDevice11.getName()), 10000);
                            currentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(currentActivity, String.format("%s下行同步丢失，二次同步中...", monitorDevice11.getName()), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                },5000);
            return false;
        }
    });

    public List<CountSortedInfo> getCountSortedInfoList() {
        countSortedInfoList.clear();
        CountSortedInfo info;
        Set<Map.Entry<String, CountSortedInfo>> sortedStmsi = getSortedSTMSI();
        for (Map.Entry<String, CountSortedInfo> entry : sortedStmsi) {
            info = entry.getValue();
            try {
                Log.e(TAG, info.stmsi + " 发送时间差值:" + (info.exactTime.getTime() - Global.sendTime.getTime()) + "发送至短信中心时间差值:" + (info.exactTime.getTime() - Global.smsCenterTime.getTime()) + "到达时间差值:" + (info.exactTime.getTime() - Global.receiveTime.getTime()));
                if (Math.abs(Global.receiveTime.getTime() - info.exactTime.getTime()) <= DoubtfulTime) {
                    info.doubtful = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            countSortedInfoList.add(info);
            if(info.changed < 0)
                info.changed++;
        }
        return countSortedInfoList;
    }


    private FindSTMSI() {
        sTMSI2Count = new HashMap<>();
        countSortedInfoList = new ArrayList<>();
        handler = new myHandler(this);
        //trigger = Global.Configuration.type == Status.TriggerType.SMS? SMSTrigger.getInstance(): PhoneTrigger.getInstance();
        trigger = SMSTrigger.getInstance();
        timerMap = new HashMap<>();
        cellInfoList=new ArrayList<>();
//        stmsiMap=new HashMap<>();
        starflag=false;
        protocolMap=new HashMap<>();

    }

    public static FindSTMSI getInstance() {
        return instance;
    }

    private static int handlerCount = 0;

    static class myHandler extends Handler {
        private final WeakReference<FindSTMSI> mOuter;

        public myHandler(FindSTMSI findSTMSI) {
            mOuter = new WeakReference<>(findSTMSI);
        }

        @Override
        public void handleMessage(Message msg) {
            Global.GlobalMsg globalMsg = (Global.GlobalMsg) msg.obj;
            switch (msg.what) {
                case MsgTypes.L2P_AG_UE_CAPTURE_IND_MSG_TYPE:
                    mOuter.get().resolveUECaptureMsg(globalMsg);
                    break;
                case MsgTypes.L2P_AG_CELL_CAPTURE_IND_MSG_TYPE:
                    mOuter.get().resolveCellCaptureMsg(globalMsg);
                    break;
                case MsgTypes.L1_PHY_COMMEAS_IND_MSG_TYPE:
                    mOuter.get().resolvePhyCommeasIndMsg(globalMsg);
                    Log.e(TAG, "L1_PHY_COMMEAS_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case MsgTypes.L2P_PROTOCOL_DATA_MSG_TYPE:
                    mOuter.get().resolveProtocolMsg(globalMsg);
//                    Log.e(TAG, "L2P_PROTOCOL_DATA_MSG_TYPE captured：：：：：：：：");
                    break;
                default:
                    break;
            }
        }
    }

    public void start(Activity activity) {
        Global.smsCenterTime = new Date();
        Global.receiveTime = new Date();
        MessageDispatcher.getInstance().RegisterHandler(handler);
        currentActivity = activity;
        stmsiCount = 0;
        sumCount = 0;
        nullCount = 0;
        FnMap=new HashMap<>();
        paginglist=new HashSet<>();
        Log.e("Test", activity.getLocalClassName());
        filterCheckBox = (CheckBox) activity.findViewById(R.id.find_stmsi_filter_checkbox);
        service = Status.Service.FINDSTMIS;
//        trigger.start(activity, service);
        if (starflag){
            Log.e(TAG,"restar");
            sTMSI2Count = new HashMap<>();
            countSortedInfoList = new ArrayList<>();
//            sTMSI2Count.clear();
//            countSortedInfoList.clear();
            trigger.restar(activity, service);
            
        }else{
            Log.e(TAG,"star");
            try {
                trigger.start(activity, service);
            } catch (IOException e) {
                e.printStackTrace();
            }
            starflag=true;
            sTMSI2Count.clear();
            countSortedInfoList.clear();
            for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                if (device.getCellInfo() != null) {
                    timerMap.put(device.getName(), new Timer());
//                    stmsiMap.put(device.getName(),new Timer());
                    device.getCellInfo().stmsiCount = 0;
                }
                device.setStartAgain(false);
                device.setWorkingStatus(Status.DeviceWorkingStatus.ABNORMAL);
            }
            for (Map.Entry<String, Timer> entry : timerMap.entrySet()) {
                entry.getValue().schedule(new MyTimerTask(entry.getKey()), 10000);
            }


            Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                        if (device.getCellInfo() != null && device.getStatus() == Status.DeviceStatus.DISCONNECTED) {
                            device.setWorkingStatus(Status.DeviceWorkingStatus.ABNORMAL);
                            changeDevice(device.getName());
                        }
                    }
                }
            }, 3, 3, TimeUnit.SECONDS);
        }


    }


    private class MyTimerTask extends TimerTask {
        private String mName;

        public MyTimerTask(String name) {
            mName = name;
        }

        @Override
        public void run() {
            changeDevice(mName);
        }
    }

    private void changeDevice(final String deviceName) {
        final MonitorDevice temDevice = DeviceManager.getInstance().getDevice(deviceName);
        if (temDevice == null)
            return;
        if (!temDevice.isStartAgain()) {
            temDevice.send(GetFrequentlyUsedMsg.protocalTraceRelMsg);
            Message msg1=new Message();
            msg1.what=(int)(deviceName.charAt(deviceName.length()-1));
            msg1.obj=temDevice;
            hander1.sendMessage(msg1);
            return;
        }
        temDevice.reboot();
        DeviceManager.getInstance().remove(temDevice.getName());
        Log.e(TAG, "Device Status After Reboot" + temDevice.getStatus());
        timerMap.get(deviceName).cancel();
        timerMap.remove(deviceName);
//        timerMap.put(deviceName, new Timer());
        CellInfo cellInfo = temDevice.getCellInfo();
//        temDevice.setCellInfo(null);
        String nextDeviceName = "";
        for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
//            if (!device.getIsReadyToMonitor() && device.getType() == temDevice.getType() && device.isReady()) {
            if (!device.getIsReadyToMonitor()&& device.isReady()) {
                device.setCellInfo(cellInfo);
                timerMap.put(device.getName(), new Timer());
                timerMap.get(device.getName()).schedule(new MyTimerTask(device.getName()), 10000);
                device.startMonitor(Status.Service.FINDSTMIS);
                nextDeviceName = device.getName();
                break;
            }
        }
        temDevice.setCellInfo(null);
        final Short pci = cellInfo.pci;
        final String nextName = nextDeviceName;

        for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
            if(device.getCellInfo() != null) {
                Log.e("Test"+TAG, device.getName() + ":" + device.getCellInfo().earfcn + " " + device.getCellInfo().pci);
            } else {
                Log.e("Test"+TAG, device.getName() + ":null");
            }
        }
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (nextName != "") {
                    Toast.makeText(currentActivity, String.format("%s下行同步丢失，切换至设备%s！", deviceName, nextName), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(currentActivity, String.format("%s出现异常，正在恢复初始状态！", deviceName), Toast.LENGTH_LONG).show();
                    Toast.makeText(currentActivity, String.format("%s下行同步丢失，重新同步中...", deviceName), Toast.LENGTH_LONG).show();
                }
//                if (timerMap.isEmpty()) {
//                    trigger.stop();
//                    Toast.makeText(currentActivity, "搜索已停止！", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    public void stop() {
        if (starflag){
            trigger.stop();
        }
        starflag=false;
        for (Map.Entry<String, Timer> entry : timerMap.entrySet()) {
            entry.getValue().cancel();
            entry.setValue(null);
        }
//        for (Map.Entry<String, Timer> entry : stmsiMap.entrySet()) {
//            entry.getValue().cancel();
//            entry.setValue(null);
//        }
//        stmsiMap.clear();
        for (MonitorDevice device:DeviceManager.getInstance().getDevices()){
//            device.getStatuTimer().cancel();
//            device.getStatuTimer().purge();
//            device.setStatuTimer(new Timer());

            device.protocolTimer.cancel();
            device.protocolTimer.purge();
            device.protocolTimer=new Timer();
        }
        timerMap.clear();

    }

    private void resolveCellCaptureMsg(Global.GlobalMsg globalMsg) {
        MsgL2P_AG_CELL_CAPTURE_IND msg = new MsgL2P_AG_CELL_CAPTURE_IND(globalMsg.getBytes());
        Status.DeviceWorkingStatus status = msg.getMu16TAC() == 0 ? Status.DeviceWorkingStatus.ABNORMAL : Status.DeviceWorkingStatus.NORMAL;
        Float rsrp = msg.getMu16Rsrp() * 1.0F;
        final String deviceName = globalMsg.getDeviceName();
        MonitorDevice monitorDevice = DeviceManager.getInstance().getDevice(deviceName);
        if (monitorDevice == null)
            return;
        monitorDevice.setWorkingStatus(status);
        monitorDevice.getCellInfo().rsrp = rsrp;
        Log.e(TAG, String.format("==========status : %s, rsrp : %f ============DEVICE："+deviceName, status.name(), rsrp));
        Log.e("cell_capture", "mu16PCI:" + msg.getMu16PCI() + " mu16EARFCN:" + msg.getMu16EARFCN() + " mu16TAC:" + msg.getMu16TAC() + " mu16Rsrp:" + msg.getMu16Rsrp() + " mu16Rsrq:" + msg.getMu16Rsrq());
        Log.e(TAG,"devicestatus"+deviceName+msg.getMu16TAC());
        if (timerMap.get(monitorDevice.getName()) != null)
            timerMap.get(monitorDevice.getName()).cancel();
        if (status==Status.DeviceWorkingStatus.NORMAL){
//            stmsiMap.get(deviceName).schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    DeviceManager.getInstance().getDevice(deviceName).stopMonitor();
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    DeviceManager.getInstance().getDevice(deviceName).startMonitor(Status.Service.FINDSTMIS);
//                    DeviceManager.getInstance().getDevice(deviceName).setWorkingStatus(Status.DeviceWorkingStatus.ABNORMAL);
//                }
//            },15000);
//            if(stmsiMap.get(deviceName)!=null){
//                stmsiMap.get(deviceName).schedule(new MyTimerTask(monitorDevice.getName()),10000 );
//            }
        }

        if (status == Status.DeviceWorkingStatus.ABNORMAL) {
            if (!monitorDevice.isStartAgain()) {
                monitorDevice.send(GetFrequentlyUsedMsg.protocalTraceRelMsg);
                Message msg1=new Message();
                msg1.what=(int)(deviceName.charAt(deviceName.length()-1));
                msg1.obj=monitorDevice;
                hander1.sendMessage(msg1);
                return;
            }
            timerMap.remove(deviceName);
//            if (timerMap.isEmpty()) {
////                trigger.stop();
////                Toast.makeText(currentActivity, "搜索已停止！", Toast.LENGTH_LONG).show();
//            } else {
                Toast.makeText(currentActivity, String.format("%d小区信号过弱！"+msg.getMu16EARFCN(), msg.getMu16PCI()), Toast.LENGTH_LONG).show();
//            }
        } else {
            monitorDevice.setStartAgain(false);
        }
    }

    private synchronized void resolveUECaptureMsg(Global.GlobalMsg globalMsg) {
        try {
            stmsiCount++;
            sumCount++;
            MonitorDevice temDevice = DeviceManager.getInstance().getDevice(globalMsg.getDeviceName());
            if (temDevice != null)
                temDevice.getCellInfo().stmsiCount++;
            long sendDifTime;
            Date currentTime = new Date();

            MsgL2P_AG_UE_CAPTURE_IND msg = new MsgL2P_AG_UE_CAPTURE_IND(globalMsg.getBytes());
            String stmsi = "";
            byte mec = 0;
            int mu8EstCause = 0;
            if ((msg.getMstUECaptureInfo().getMu8UEIDTypeFlg() & 0x20) == 0x20) {
                mec = msg.getMstUECaptureInfo().getMau8GUTIDATA()[5].getBytes()[0];
                byte[] stmsiBytes = new byte[4];
                stmsiBytes[3] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[6].getBytes()[0];
                stmsiBytes[2] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[7].getBytes()[0];
                stmsiBytes[1] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[8].getBytes()[0];
                stmsiBytes[0] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[9].getBytes()[0];
                mu8EstCause = msg.getMstUECaptureInfo().getMu8Pading1();
                stmsi = new StringBuilder().append(padLeft(String.format("%X", mec), "0", 2))
                        .append(padLeft(String.format("%X", stmsiBytes[0]), "0", 2))
                        .append(padLeft(String.format("%X", stmsiBytes[1]), "0", 2))
                        .append(padLeft(String.format("%X", stmsiBytes[2]), "0", 2))
                        .append(padLeft(String.format("%X", stmsiBytes[3]), "0", 2))
                        .toString();
            }
            if (stmsi.equals("")) {
                nullCount++;
                return;
            }

//            int sfn;
//            int fn=-1;
//            sfn=(128/128)*(int)((Long.parseLong(Global.Configuration.targetPhoneImsi)%1024)%128);
//            fn= Integer.parseInt(FnMap.get(msg.getMstUECaptureInfo().getMu16CRNTIDATA()+""));
////            int modfn=(fn-sfn)%128;
//            Toast.makeText(currentActivity, "SFN:--"+sfn+"--FN:--"+fn+"--", Toast.LENGTH_SHORT).show();
//            Log.e(TAG,"SFN:--"+sfn+"--FN:--"+fn+"--STMSI:-"+ stmsi+"device:"+temDevice.getName());
//            if (fn!=-1){
//                boolean flag=true;
//                if (Global.Configuration.targetPhoneImsi.length()>0&&Global.Configuration.imsiflag){
//
//                    if(modfn>0){
//                        if (0<=modfn&&modfn <16){
//                            flag=false;
//                        }
//                    }else{
//                        if(0<=modfn+128&&modfn+128<16){
//                            flag=false;
//                        }
//                    }
//                    if (flag){
//                        return;
//                    }
//                    if (Global.Configuration.smsType == Status.TriggerSMSType.INSIDE) {
//                        if (currentTime.getTime() - Global.smsCenterTime.getTime() > Global.Configuration.filterInterval * 1000*2)
//                            return;
//                        if (Global.receiveTime.getTime() >= Global.smsCenterTime.getTime() && currentTime.getTime() > Global.receiveTime.getTime())
//                            return;
//                    }
//                }else{//                    if (Global.Configuration.smsType == Status.TriggerSMSType.INSIDE) {
//                        if (currentTime.getTime() - Global.smsCenterTime.getTime() > Global.Configuration.filterInterval * 1000)
//                            return;
//                        if (Global.receiveTime.getTime() >= Global.smsCenterTime.getTime() && currentTime.getTime() > Global.receiveTime.getTime())
//                            return;
//                    }
//                }
//
//            }
//            Log.e("UEcaptionstmsi:",""+stmsi);
            if (Global.Configuration.smsType == Status.TriggerSMSType.INSIDE) {
                if (currentTime.getTime() - Global.smsCenterTime.getTime() > Global.Configuration.filterInterval * 1000)
                    return;
                if (Global.receiveTime.getTime() >= Global.smsCenterTime.getTime() && currentTime.getTime() > Global.receiveTime.getTime())
                    return;
            }


//            boolean ueflag=true;
//            for (String s:paginglist){
//                if(stmsi.equals(s)){
//                    ueflag=false;
//                }
//            }
//            if (ueflag){
//                return;
//            }

            Log.e(TAG, DATE_FORMAT.format(Global.smsCenterTime) + " " + DATE_FORMAT.format(Global.receiveTime) + " " + DATE_FORMAT.format(currentTime));
            //Log.e(TAG, String.format("---------Find STMSI :%s Type :%d-----------", stmsi, mu8EstCause));

            if (!(mu8EstCause == 0x02)) {
                return;
            }
            if (filterCheckBox.isChecked() && Global.filterStmsiMap.containsKey(stmsi))
                return;


            Log.e("UEcaptionstmsi11:", "" + stmsi);
            int count = 0;
            CountSortedInfo info;
            if (sTMSI2Count.containsKey(stmsi)) {
                Log.e("ue", stmsi + "baohan");
                info = sTMSI2Count.get(stmsi);
                if(sTMSI2Count.get(stmsi).count!=null){
                    count = Integer.parseInt(sTMSI2Count.get(stmsi).count);
                }else {
                    info.count="0";
                }
                if (sTMSI2Count.get(stmsi).pagingcount==null){
                    info.pagingcount="0";
                }
                info.doubtful = sTMSI2Count.get(stmsi).doubtful;
            } else {
                Log.e("ue", stmsi + "bubaohan");
                info = new CountSortedInfo();
                info.pagingcount="0";
            }
            Log.e("ue", stmsi + "11111");
            info.stmsi = stmsi;
            info.count = String.valueOf(count + 1);
            info.time = DATE_FORMAT.format(currentTime.getTime());
            info.exactTime = currentTime;

            if (temDevice == null) {
                Log.e("ue", stmsi + "returndevicenull");
                return;
            }
            info.pci = String.valueOf(temDevice.getCellInfo().pci);
            info.earfcn = String.valueOf(temDevice.getCellInfo().earfcn);
            info.ecgi = String.valueOf(temDevice.getCellInfo().tai);
            if (temDevice.getCellInfo().tai == Short.MAX_VALUE)
                info.ecgi = CellInfo.NULL_VALUE;
            if (sTMSI2Count.containsKey(stmsi)) {
                if (!sTMSI2Count.get(stmsi).pci.equals(info.pci) || !sTMSI2Count.get(stmsi).earfcn.equals(info.earfcn)) {
                    info.changed = -4;
                }
            }

            Log.e("ue", stmsi + "jiaru");
            sTMSI2Count.put(stmsi, info);
        }catch (Exception e){
            Log.e("ue",""+e.getMessage());
            e.printStackTrace();
        }

    }

    private void resolvePhyCommeasIndMsg(Global.GlobalMsg globalMsg) {
        /*if (!needToCount) {
            return;
        }*/
        MonitorDevice temDevice = DeviceManager.getInstance().getDevice(globalMsg.getDeviceName());
//        if (stmsiMap.get(temDevice.getName()) != null){
//            stmsiMap.get(temDevice.getName()).cancel();
//            stmsiMap.remove(temDevice.getName());
//            stmsiMap.put(temDevice.getName(),new Timer());
//            stmsiMap.get(temDevice.getName()).schedule(new MyTimerTask(temDevice.getName()),10000);
//        }
        if (connindex==2){
            Toast.makeText(currentActivity, "目标疑似通联中！", Toast.LENGTH_SHORT).show();
//            connindex=0;
        }
        MsgL1_PHY_COMMEAS_IND msg = new MsgL1_PHY_COMMEAS_IND(globalMsg.getBytes());
        if (isCRSChType(msg.getMstL1PHYComentIndHeader().getMu32MeasSelect())) {
            MsgCRS_RSRPQI_INFO crs_rsrpqi_info = new MsgCRS_RSRPQI_INFO(
                    MsgSendHelper.getSubByteArray(globalMsg.getBytes(), MsgL1_PHY_COMMEAS_IND.byteArrayLen, MsgCRS_RSRPQI_INFO.byteArrayLen));
            if (globalMsg.getDeviceName() != null&&DeviceManager.getInstance().getDevice(globalMsg.getDeviceName())!=null&&
                    DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo()!=null) {
                DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo().rsrp = crs_rsrpqi_info.getMstCrs0RsrpqiInfo().getMs16CRS_RP() * 0.125F;
            }

        }

    }
    private int fn=-1;
    private int smsindex=0;
    private  void resolveProtocolMsg(Global.GlobalMsg globalMsg){
        MonitorDevice temDevice = DeviceManager.getInstance().getDevice(globalMsg.getDeviceName());
        if (temDevice==null){
            return;
        }
        try{
            temDevice.protocolTimer.cancel();
            temDevice.protocolTimer.purge();
        }catch (Exception e){
            e.printStackTrace();
        }
        temDevice.protocolTimer=new Timer();
        temDevice.protocolTimer.schedule(temDevice.getTimer(),10000);

        StringBuffer str=new StringBuffer();
        str.append(temDevice.getName()+"L2P_PROTOCOL_DATA:::::");
        int pos=0;
        str.append(globalMsg.getBytes().length+"--protocoldatalengh:"+L2P_PROTOCOL_DATA.byteArrayLen);
        L2P_PROTOCOL_DATA stru_L2P_Protocol_Data=new L2P_PROTOCOL_DATA(MsgSendHelper.getSubByteArray(globalMsg.getBytes(),pos,L2P_PROTOCOL_DATA.byteArrayLen));
        str.append("fn:--"+stru_L2P_Protocol_Data.mstProtocolDataHeader.getMu16FrameNumber().getBase()+"--subfn:--"+
                stru_L2P_Protocol_Data.mstProtocolDataHeader.getMu8SubFrameNumber().getBase()+"--");
        long rnti=stru_L2P_Protocol_Data.getMstProtocolDataHeader().getMu32RntiValue().getBase();
//        FnMap.put(""+rnti,afn+"");
//        str.append("--datatype::"+stru_L2P_Protocol_Data.getMu16DataType().getBase()+"--fn"+afn+"--");
        Log.e(TAG,str.toString());
        pos+=L2P_PROTOCOL_DATA.byteArrayLen;
        switch (stru_L2P_Protocol_Data.getMu16DataType().getBase()){
            case 1666:
                L2P_MAC_DATA_STRU data_stru=new L2P_MAC_DATA_STRU(MsgSendHelper.getSubByteArray(globalMsg.getBytes(),pos, L2P_MAC_DATA_STRU.byteArrayLen));
                pos+=L2P_MAC_DATA_STRU.byteArrayLen;
                str.append("case 5 L2P_MAC_DATA_STRU");
                int Location=L2P_PROTOCOL_DATA.byteArrayLen+L2P_MAC_DATA_STRU.byteArrayLen;
//                int temp=0;
                for(int i=0;i<data_stru.getMu8TbNum().getBase();i++){
//                    byte[] subdata=new byte[data_stru.mastMacTbInfo[i].mu16SubHeaderLen.getBase()*4];
                    byte[] subdata=new byte[0];
//                    subdata=MsgSendHelper.getSubByteArray(globalMsg.getBytes(),Location,subdata.length);
//                    temp+=subdata.length;
                    if (stru_L2P_Protocol_Data.mstProtocolDataHeader.getMu8RntiType().getBase()==2){

                    }else{

                        byte[] subheaddata=new byte[136]; //L2P_MAC_PDU_SUBHEADER_STRU.size=136
                        System.arraycopy(subdata,0,subheaddata,0,subdata.length);
                        L2P_MAC_PDU_SUBHEADER_STRU pdu_subheader_stru=new L2P_MAC_PDU_SUBHEADER_STRU(subheaddata);

                        for (int j=0;j<32;j++){
                            str.append("lcid"+(j+1)).append("--"+pdu_subheader_stru.getPduSubHeaderInfo()[j].getU8Lcid().getBase()+"--!!");
                            str.append("f"+(j+1)).append("--"+pdu_subheader_stru.getPduSubHeaderInfo()[j].getU8F().getBase()+"--!!");
                            str.append("L"+(j+1)).append("--"+pdu_subheader_stru.getPduSubHeaderInfo()[j].getU16L().getBase()+"--!!");
                        }
//                        Log.e(TAG,str.toString());
                    }
                    Location+=subdata.length;
                }
                break;
            case 8:
                    Log.e("Paging","case8");
                    L2P_RRCNAS_MSG_STRU l2P_rrcnas_msg_stru=new L2P_RRCNAS_MSG_STRU(MsgSendHelper.getSubByteArray(globalMsg.getBytes(),pos,L2P_RRCNAS_MSG_STRU.byteArrayLen));
                    pos+=L2P_RRCNAS_MSG_STRU.byteArrayLen;
                    int mu16PduLength=l2P_rrcnas_msg_stru.getMu16PduLength().getBase();
                    int lchtype=l2P_rrcnas_msg_stru.getMu8LchType().getBase();
                    if(lchtype==2){
                        Log.e("Paging","lchtype=2");
                        if (l2P_rrcnas_msg_stru.getmu16RrcOrNas().getBase()==0){
                                paginglist.clear();
                                paginglist=new HashSet<>();
//                                Global.pagingflag=false;
                                Log.e("Paging","mu16RrcOrNas=0");
                                int rrcpos=0;
                                byte[] data=MsgSendHelper.getSubByteArray(globalMsg.getBytes(),pos,((mu16PduLength+3)/4)*4);
                                Log.e("Paging",""+data);
//                              String bitdata=MybytesUtil.bytesToString(data);
                                StringBuffer strbuf=new StringBuffer();
                                for (int i=0;i<data.length;i++){
                                    strbuf.append(MybytesUtil.byteToBit(data[i]).trim());
                                }
                                String bitdata=strbuf.toString();
                                Log.e("pagingdata",bitdata);
                                createFileWithByte(bitdata.getBytes());
                                int strpos=0;
                                if (bitdata.substring(strpos,strpos+1).equals("0")) {
                                    strpos += 1;
                                    Log.e("Paging", "PCCHMESSAGE！");
                                    if (bitdata.substring(strpos, strpos + 1).equals("1")) {
                                        Log.e("Paging", "RecordList！");
                                        strpos += 4;
                                        int recordlistnum1 = Integer.parseInt(bitdata.substring(strpos, strpos + 4), 2) + 1;
                                        strpos += 4;
                                        List<PagingRecord> list = new ArrayList<PagingRecord>();

                                        for (int i = 0; i < recordlistnum1; i++) {
                                            PagingRecord record = new PagingRecord();
                                            PagingUE_Identity pagingUE_identity=new PagingUE_Identity();
//                                          record.pagingUE_identity=pagingUE_identity;
                                            strpos += 2;
                                            if (bitdata.substring(strpos, strpos + 1).equals("0")) {
                                                strpos += 1;
                                                S_TMSI s_tmsi=new S_TMSI();
                                                s_tmsi.mmec=Integer.parseInt(bitdata.substring(strpos, strpos + 8), 2);

//                                          record.pagingUE_identity.s_tmsi.mmec = Integer.parseInt(bitdata.substring(strpos, strpos + 8), 2);
                                                strpos += 8;
                                                int[] x=new int[4];
                                                for (int j = 0; j < 4; j++) {
                                                    x[j] = Integer.parseInt(bitdata.substring(strpos, strpos + 8), 2);
                                                    strpos += 8;
                                                }
                                                s_tmsi.s_tmsi_m_tmsi=x;


                                                pagingUE_identity.s_tmsi=s_tmsi;
                                                record.pagingUE_identity=pagingUE_identity;
                                                record.cn_Domain = Integer.parseInt(bitdata.substring(strpos, strpos + 1), 2);
                                                strpos += 1;
                                            } else if (bitdata.substring(strpos, strpos + 1).equals("1")) {
                                                strpos += 1;
                                                int imsinum = Integer.parseInt(bitdata.substring(strpos, strpos + 4),2) + 6;
                                                strpos += 4;
                                                int[] y=new int[imsinum];
                                                for (int a = 0; a < imsinum; a++) {
                                                    y[a]=Integer.parseInt(bitdata.substring(strpos, strpos + 4), 2);
//                                                  record.pagingUE_identity.imsi[a] = Integer.parseInt(bitdata.substring(strpos, strpos + 4), 2);
                                                    strpos += 4;
                                                }
                                                pagingUE_identity.imsi=y;
                                                record.pagingUE_identity=pagingUE_identity;
                                                record.cn_Domain = Integer.parseInt(bitdata.substring(strpos, strpos + 1), 2);
                                                strpos += 1;

                                            }
                                            list.add(record);
                                        }
                                        Log.e("Paging","listsize:"+list.size());
                                        for (int b=0;b<list.size();b++){
                                            StringBuffer stmsi=new StringBuffer();
                                            if (list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi!=null&&list.get(b).pagingUE_identity.s_tmsi!=null){
                                                if(list.get(b).pagingUE_identity.s_tmsi.mmec==0){
                                                    stmsi.append("00");
                                                }else if (0<list.get(b).pagingUE_identity.s_tmsi.mmec&&list.get(b).pagingUE_identity.s_tmsi.mmec<16){
                                                    stmsi.append("0");
                                                    stmsi.append(Integer.toHexString(list.get(b).pagingUE_identity.s_tmsi.mmec));
                                                }else {
                                                    stmsi.append(Integer.toHexString(list.get(b).pagingUE_identity.s_tmsi.mmec));
                                                }
                                                for (int c=0;c<4;c++){
                                                    if (list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi[c]==0){
                                                        stmsi.append("00");
                                                    }else if(0<list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi[c]&&list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi[c]<16){
                                                        stmsi.append("0");
                                                        stmsi.append(Integer.toHexString(list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi[c]));
                                                    }else{
                                                        stmsi.append(Integer.toHexString(list.get(b).pagingUE_identity.s_tmsi.s_tmsi_m_tmsi[c]));
                                                    }
                                                }
                                            }

                                            StringBuffer imsi=new StringBuffer();
                                            if (list.get(b).pagingUE_identity.imsi!=null){
                                                for (int d=0;d<list.get(b).pagingUE_identity.imsi.length;d++){

                                                    imsi.append(list.get(b).pagingUE_identity.imsi[d]);
                                                }
                                            }
                                            if(list.get(b).cn_Domain==0){

                                                String  STMSI=stmsi.toString().toUpperCase();
                                                Date currenttime=new Date();
                                                    if (Global.Configuration.smsType == Status.TriggerSMSType.INSIDE) {
                                                        if (currenttime.getTime() - Global.smsCenterTime.getTime() > 2500) {
//                                                            connindex++;
                                                            return;
                                                        }
                                                        if (Global.receiveTime.getTime() >= Global.smsCenterTime.getTime() && currenttime.getTime() > Global.receiveTime.getTime())
                                                            return;
//                                                        connindex=0;
                                                    }

//                                                Log.e("时间差",STMSI+"和发送时刻时间差："+(currenttime.getTime()-Global.sendTime.getTime())+"和发送回执时间差："+(currenttime.getTime()-Global.smsCenterTime.getTime())+"发送成功和接收成功时间差："+(Global.smsCenterTime.getTime()- Global.receiveTime.getTime()));
                                                Log.e("Paging","pagingstmsi:"+STMSI);
                                                paginglist.add(STMSI);
                                                Log.e("Paginglist.size",""+paginglist.size());
                                                for(String s:paginglist){
                                                    Log.e("paginglist:",""+s);
                                                }
                                                int afn=stru_L2P_Protocol_Data.getMstProtocolDataHeader().getMu16FrameNumber().getBase();
//                                                if(smsindex==0){
//                                                    smsindex=Global.pagingindex;
//                                                }else{
//                                                    if (smsindex==Global.pagingindex){
//                                                        if (fn==afn) {
//                                                            Log.e("paginglist",STMSI+"lanjiedao");
//                                                            return;
//                                                        }
//                                                    }else{
//                                                        smsindex=Global.pagingindex;
//                                                    }
//                                                }
//                                                fn=afn;
                                                int pagingcount = 0;
                                                CountSortedInfo info ;
                                                if (sTMSI2Count.containsKey(STMSI)) {
                                                    Log.e("pl",STMSI+"baohan");
                                                    info=sTMSI2Count.get(STMSI);
                                                    info.pagingflag=false;
                                                    if (sTMSI2Count.get(STMSI).pagingcount!=null){
                                                        pagingcount = Integer.parseInt(sTMSI2Count.get(STMSI).pagingcount);
                                                    }
                                                    if(sTMSI2Count.get(STMSI).count==null){
                                                        info.count="0";
                                                    }
                                                    if (!info.pagingflag){

                                                        info.pagingcount = String.valueOf(pagingcount + 1);
                                                        Log.e("paginglist",STMSI+"shouci+1");
                                                        info.pagingflag=true;
                                                    }else{
                                                        Log.e("paginglist",STMSI+"lanjiedao");
//
                                                    }
                                                    Log.e("pl",STMSI+"count"+pagingcount);
//                                                    info.doubtful = sTMSI2Count.get(STMSI).doubtful;
                                                }else{
                                                    Log.e("pl",STMSI+"bubaohan");
                                                    info= new CountSortedInfo();
                                                    info.pagingcount = String.valueOf(pagingcount + 1);
                                                    info.count="0";
                                                    Log.e("paginglist",STMSI+"shouci+1");
                                                    info.pagingflag=true;
                                                }
                                                info.stmsi = STMSI;
                                                info.time = DATE_FORMAT.format(currenttime.getTime());
                                                info.exactTime = currenttime;

                                                sTMSI2Count.put(STMSI, info);
                                            }
                                        }
                                    }
                                }
                        }
                    }

                break;
        }

    }

    public static void initpaging(){
        Iterator iterator=sTMSI2Count.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            CountSortedInfo info= (CountSortedInfo) entry.getValue();
            info.pagingflag=false;
        }
    }


    private void createFileWithByte(byte[] bytes) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        File file = new File(Environment.getExternalStorageDirectory(),
                "pagingdata.doc");
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }




    private boolean isCRSChType(long type) {
        return (type & 0x2000) == 0x2000;
    }

    private Set<Map.Entry<String, CountSortedInfo>> getSortedSTMSI() {
        Set<Map.Entry<String, CountSortedInfo>> sortedSTMSI = new TreeSet<>(
                new Comparator<Map.Entry<String, CountSortedInfo>>() {
                    public int compare(Map.Entry<String, CountSortedInfo> o1, Map.Entry<String, CountSortedInfo> o2) {
                        Integer d1 = Integer.parseInt(o1.getValue().pagingcount);
                        Integer d2 = Integer.parseInt(o2.getValue().pagingcount);
                        int r = d2.compareTo(d1);
                        if (r != 0) {
                            return r;
                        } else {
                            Integer d11 = Integer.parseInt(o1.getValue().count);
                            Integer d21 = Integer.parseInt(o2.getValue().count);
                            int r1 = d21.compareTo(d11);
                            if (r1!=0){
                                return r1;
                            }else {
                                return o2.getKey().compareTo(o1.getKey());
                            }

                        }
                    }
                }
        );
        sortedSTMSI.addAll(sTMSI2Count.entrySet());
        return sortedSTMSI;

    }

    private String padLeft(String src, String pad, int len) {
        StringBuilder builder = new StringBuilder();
        len -= src.length();
        while (len-- > 0) {
            builder.append(pad);
        }
        builder.append(src);
        return builder.toString();
    }

    public static class CountSortedInfo {
        public String stmsi;
        public String count;
        public String time;
        public String pci;
        public String earfcn;
        public String ecgi;
        public Date exactTime;
        public boolean doubtful;
        public int changed;
        public String pagingcount;
        public boolean pagingflag;
    }

}
