package com.example.abc.testui;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
//import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.adapter.CommonClass.Configure;
import com.adapter.CommonClass.StatusReport;
import com.adapter.Constants_Board_Model;
import com.adapter.Machine;
import com.adapter.Parameter.BoardC1Set;
import com.adapter.Parameter.BoardC1SetEnum;
import com.example.abc.testui.src.UserPwdDeal;
import com.example.abc.testui.src.WriteBLogDeal;
import com.example.abc.testui.ui.ChangeModeThread;
import com.example.abc.testui.ui.RegDeal;
import com.example.abc.testui.ui.RegLib;
import com.example.abc.testui.ui.StatusReportLis1;
import com.example.abc.testui.ui.UECycle;
import com.example.abc.testui.ui.UIPrint;
import com.example.abc.testui.ui.Global;
import com.example.abc.testui.ui.SetParaThread;
import com.example.abc.testui.ui.StreamReportLis1;
import com.example.abc.testui.ui.regcycle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RealTimeService extends Service {

    public static final String TAG = "Service";


    private IdListSetLog idListSetLog;

    private List<RealTimeDatas> listRealTimeDatas;  //向 MainActivity 传递的实时流水数据
	private List<RealTimeDatas> listTargetDatas;  //中标数据
    private List<StateValues> listStateValues;  //向 MainActivity 传递的状态数据

    private List<IdListDatas> listIdListDatas;  //向 IdListSetActivity 传递数据库中的数据
    private IdListSetDB idListSetDB;

    private List<ScanData> listScanDatas;  //向 ScanActivity 传递数据


    private String registerMsg = "1234567890abcdefg";  //注册信息
    private String machineCode = "ABCDEFGHIJK12345";  //机器码
    private String registerCode;  //注册码
    private RegisterDB registerDB;  //注册数据库


    public RealTimeService() {
    }

    @Override
    public void onDestroy(){


        //Service退出要注销一下
        Machine.UnInit();


        if(Global.uecycle !=null){
            Global.uecycle.SetOver();
        }
        if(Global.recycle !=null){
            Global.recycle.SetOver();
        }

        Log.d(TAG, "onDestroy: ");

    }



    private void SetListToDown(long[] lists){


        StatusReport report;
        Configure cfg = null;
        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;

        BoardC1Set set = null;

        int earfcn = 38400;
        int pci = 88;
        int PLMN = 46000;
        long targetnum = 123456789012345L;
        int powerval = 0;
        int gain = 52;
        int tac = (new Random()).nextInt(65535);
        int cellid = (new Random()).nextInt(65535);
        int event;


        //仅设置功率
        event = BoardC1SetEnum.Event_BlackList;
        set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);;
        cfg = new Configure(0, set);
        ///Machine.SetPara(cfg);

        (new SetParaThread(cfg)).start();

    }


    @Override
    public void onCreate() {
        super.onCreate();

        idListSetLog = new IdListSetLog();
        //主动注册三个监听事件
        StatusReportLis1 statusReportLis1 = new StatusReportLis1();
        StreamReportLis1 streamReportLis1 = new StreamReportLis1();
        UIPrint uiprint = new UIPrint();
        Machine.RegisterEventListener(statusReportLis1, streamReportLis1, uiprint);
        Machine.Init();


       // setLastruntime();
        //setRegisterCode("123456jfldjlf");


        listRealTimeDatas = new ArrayList<>();
		listTargetDatas = new ArrayList<>();
        listStateValues = new ArrayList<>();
        listIdListDatas = new ArrayList<>();
        listScanDatas = new ArrayList<>();
        initIdListDatas();

//        initRegisterTest();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //模拟实时流水数据产生
//                String str = "111111111";
//                while (true){
//                    if (callbackScan!=null) {
//                        Random random = new Random();
//                        long i = random.nextInt(30)*10 + 100000000000000L ;
//                        int j = random.nextInt(10);
//                        if (j>5) j=1;
//                        else j=0;
//
////                        addANewData(i, j, 99);
////                        AddANewFreq(100,11,99);
//                        str+="22222222\n";
//                        AddANewFreqStr(str);
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //模拟状态数据、能力值产生
//                while (true){
//                    if (callback!=null) {
//                        addANewState("this is state values");
//
//                        Random random = new Random();
//                        int i = random.nextInt(100);
//                        addANewValue(i);
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


        //向一个全局类 登记 本service  这种方式很ugly，没法子了 为了方便了。
        Global.service = this;



        //初始化时候要下发名单
        if(listIdListDatas!=null){
            if(listIdListDatas.size()!=0){

                long[] idlisttolongarray = idlisttolongarray(listIdListDatas);
                //组装命令下发
                SetListToDown(idlisttolongarray);
            }
        }



// ue超时线程
        UECycle ueCycle = new UECycle();
        ueCycle.start();
        Global.uecycle = ueCycle;

//设置 注册信息


//        Global.recycle = new regcycle();
//        Global.recycle.start();
//
//
//        RegDeal.Init();


//        Global.reglib = new RegLib();
//        Global.reglib.setMcode_now((new RegLib()).imeiToMcode(getImei(Global.activity.imeistr)));
//        Global.reglib.setRcode_now(this.registerCode);
//
//
//
//        setRegisterMsgandMachineCode(Global.reglib.GetRegInfo(), Global.reglib.getMcode_now());





    }

    public void initIdListDatas(){
        // 从sqlite数据库中读取数据数目
        idListSetDB = new IdListSetDB(this, IdListSetDB.DB_NAME, null, 1);

        int count = 1;
        // 从数据库中读取名单列表作为初始显示
        SQLiteDatabase readDB = idListSetDB.getReadableDatabase();
        try {
            //Cursor cursor = readDB.query(IdListSetDB.TABLE_NAME, null, null, null, null, null, null);
            Cursor cursor = readDB.query(IdListSetDB.TABLE_NAME, null, null, null, null, null, "imsi asc");
            while (cursor.moveToNext()){
                long imsi = cursor.getLong(cursor.getColumnIndex("imsi"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                IdListDatas idListDatas = new IdListDatas();
                idListDatas.setNo(count++);
                idListDatas.setImsi(imsi);
                idListDatas.setName(name);
                listIdListDatas.add(idListDatas);
            }
            readDB.close();
            if (com.example.jbtang.agi_union.core.Global.Configuration.targetPhoneImsi!=null) {
                String myimsi = com.example.jbtang.agi_union.core.Global.Configuration.targetPhoneImsi;
                long imsi=Long.parseLong(myimsi);
                if (uniqueCheck(imsi)!=0){
//                    Toast.makeText(this, "IMSI已添加", Toast.LENGTH_SHORT).show();
                }
                else {
                    IdListDatas idListDatas = new IdListDatas();
                    idListDatas.setNo(listIdListDatas.size()+1);
                    idListDatas.setImsi(imsi);
                    idListDatas.setName("" + com.example.jbtang.agi_union.core.Global.Configuration.targetPhoneNum);
                    listIdListDatas.add(idListDatas);

                    SQLiteDatabase writeDB = idListSetDB.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("imsi", imsi);
                    cv.put("name", "" + com.example.jbtang.agi_union.core.Global.Configuration.targetPhoneNum);
                    writeDB.insert(IdListSetDB.TABLE_NAME, null, cv);
                    writeDB.close();

//                        idListSetLog.writeLog(IdListSetLog.TYPE_ADD, idListDatas);
                    idListSetLog.writeBLog(new WriteBLogDeal().GetBytes(Global.username, imsi));
                    Global.service.SetDownList(listIdListDatas);

                }

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public int uniqueCheck(long imsi){
        int count = 0;
        for (IdListDatas idListDatas:listIdListDatas){
            if (idListDatas.getImsi()==imsi){
                count = 1;
                break;
            }
        }

        return count;
    }




    private long[] idlisttolongarray(List<IdListDatas> lists){
        if(lists == null) return null;
        long []ret = new long[lists.size()];
        for (int i=0; i<lists.size(); i++){
            long one = lists.get(i).getImsi();
            ret[i] = one;
        }
        return ret;
    }

    public void SetDownList(List<IdListDatas> listIdListDatas) {

        long[] idlisttolongarray = idlisttolongarray(listIdListDatas);

        SetListToDown(idlisttolongarray);

    }


    public void SetModeSendCmd(){
        String mode = Global.cellstopwillchangemode;
        Toast.makeText(Global.activity,"设置模式 "+ mode, Toast.LENGTH_SHORT).show();
        (new ChangeModeThread(mode)).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }




    class MyBinder extends Binder{
        public List<RealTimeDatas> getListRealTimeDatas(){
            return listRealTimeDatas;
		}

        public List<RealTimeDatas> getListTargetDatas(){
            return listTargetDatas;
        }

        public List<StateValues> getListStateValues(){
            return listStateValues;
        }

        public List<ScanData> getListScanDatas(){
            return listScanDatas;
        }

        public RealTimeService getRealTimeService(){
            return RealTimeService.this;
        }

        public void onIdListSetInit(){
            callbackIdListSet.onConnect(listIdListDatas, idListSetDB);
        }

        // MainActivity 向 service 传递数据通道
        // tag：标识事件源
        // realTimeUIParamValues：从UI界面传过来的参数
        // listIdListDatas：保存了 名单编辑 界面中的数据，注意，只有 建立 按钮点击才会传递过来，其他按钮点击传 null 值
        public void onMainActivityBtnClickListener(int tag, RealTimeUIParamValues realTimeUIParamValues, List<IdListDatas> listIdListDatas){
            BoardC1Set set = null;
            int earfcn = 38400;
            int pci = 88;
            int PLMN = 46000;
            long targetnum = 123456789012345L;
            int powerval = 0;
            int gain = 52;
            int tac = (new Random()).nextInt(65535);
            int cellid = (new Random()).nextInt(65535);
            int event;
            Configure cfg = null;
            long[] lists = new long[2];
            lists[0] = (123456789012345L);
            lists[1] = (412345678901234L);
            if (RealTimeUIParamValues.BTNCLICKEDTAG[0] == tag){
                //模式确定 按钮点击
                // 通过下一行代码获取界面上选择的模式是 FDD 或 TDD
                String mode = realTimeUIParamValues.getMode();

                if(Global.cellsetup){
                    Toast.makeText(getApplicationContext(),"检测到小区已建立", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"检测到小区没有建立", Toast.LENGTH_SHORT).show();
                }


                if(Global.cellsetup==false){
                    Toast.makeText(getApplicationContext(),"设置模式 "+ mode, Toast.LENGTH_SHORT).show();
                    (new ChangeModeThread(mode)).start();
                }
                else {

                    Global.cellstopoverthenchangemode = true;
                    Global.cellstopwillchangemode = mode;


                    System.out.println("去激活");
                    event = BoardC1SetEnum.Event_CellStop;
                    set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
                    cfg = new Configure(0, set);
                    //Machine.SetPara(cfg);


                    (new SetParaThread(cfg)).start();

                }



            }
            else if (RealTimeUIParamValues.BTNCLICKEDTAG[1] == tag) {
                //重启 按钮点击

                //        //复位按钮被按下
            event = BoardC1SetEnum.Event_Reset;
            set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
            cfg = new Configure(0, set);
            //Machine.SetPara(cfg);
                (new SetParaThread(cfg)).start();
            //System.out.println(StatusReportLis1.FormatStrStatusReport(report));

                //Global.HBrecv = false;


            }
            else if (RealTimeUIParamValues.BTNCLICKEDTAG[2] == tag){
                //建立 按钮点击
                ParamSetValue paramSetValue = realTimeUIParamValues.getParamSetValue();
                if (paramSetValue!=null){
                    //参数设置 界面 确定按钮返回
                    //paramSetValue 中保存了 参数界面 中的参数
                    System.out.println("小区配置");
                    //小区配置按钮被按下
                    event = BoardC1SetEnum.Event_CellBtnPress;

                    if(Global.cellsetup == false)
                    {
                        event = BoardC1SetEnum.Event_CellBtnPress;
                    }
                    else {
                        event = BoardC1SetEnum.Event_CellMod;
                    }

                    earfcn = paramSetValue.getEarfcn();
                    pci = paramSetValue.getPci();
                    PLMN = paramSetValue.getPlmn();
                    targetnum = 0;
                    powerval = paramSetValue.getPower();
                    //gain = paramSetValue.getGain();
                    tac = paramSetValue.getTac();
                    cellid = paramSetValue.getCellId();
                    lists = idlisttolongarray(listIdListDatas);
                    set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
                    cfg = new Configure(0, set);


                    if(Global.cellsetup == false)
                    {
                        Toast.makeText(getApplicationContext(),"频点配置命令下发 "+ com.hardware.board.Global.ModelType,Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"动态频点配置命令下发 "+ com.hardware.board.Global.ModelType,Toast.LENGTH_SHORT).show();

                    }

                    (new SetParaThread(cfg)).start();


                }
                else{
                    //参数设置 界面 取消按钮返回
                    //这里不需要填充什么
                }
            }
            else if (RealTimeUIParamValues.BTNCLICKEDTAG[3] == tag){
                //停止 按钮点击
                System.out.println("去激活");
                event = BoardC1SetEnum.Event_CellStop;
                set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
                cfg = new Configure(0, set);
                //Machine.SetPara(cfg);
                (new SetParaThread(cfg)).start();
            }
        }

        // 中标流水 下发定位命令，传过来的 imsi
        public void onGetTargetItem(long imsi){

            BoardC1Set set = null;
            int earfcn = 38400;
            int pci = 88;
            int PLMN = 46000;
            long targetnum = 123456789012345L;
            int powerval = 0;
            int gain = 52;
            int tac = (new Random()).nextInt(65535);
            int cellid = (new Random()).nextInt(65535);
            int event;
            Configure cfg = null;
            long[] lists = new long[2];

            targetnum = imsi;
            //仅设置定位目标
            event = BoardC1SetEnum.Event_TargetChanged;
            set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
            cfg = new Configure(0, set);
            //Machine.SetPara(cfg);
            //System.out.println(StatusReportLis1.FormatStrStatusReport(report));
            (new SetParaThread(cfg)).start();

        }

        public String getRegisterMsg(){
            return registerMsg;
        }

        public String getMachineCode(){
            return machineCode;
        }

        // 注册界面 发送 注册码 过来
        public void onRegisterCode(String registerCode){
            RealTimeService.this.registerCode = registerCode;


            RegDeal.SetRegCode(registerCode);
//            if (registerDB!=null) {
//                registerDB.setValue("registerCode", registerCode);
////                Log.e("4444444444444====", registerDB.getValue("registerCode"));
//            }
//            else{
//                Log.e("onRegisterCode", "registerDB is null");
//            }
        }


        //从 登陆界面 传 用户名、密码 到 service，进行验证
        public void checkin(LoginData loginData){
            //用户名 loginData.getName();
            //密码 loginData.getPasswd();
            String name = loginData.getName();
            String passwd = loginData.getPasswd();

            if (3==UserPwdDeal.Judge(name, passwd)){
                ackLogin(3);
            }
            else if (4==UserPwdDeal.Judge(name, passwd)){
                ackLogin(4);
            }
            else if (1==UserPwdDeal.Judge(name, passwd)){
                Global.username = name;
                //打开名单日志展现界面Activity
                Intent intent = new Intent(RealTimeService.this, IdListShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if (2==UserPwdDeal.Judge(name, passwd)){
                Global.username = name;
                ackLogin(2);
            }

//            if(name.equals("admin") && passwd.equals("123456")){
//                ackLogin(1);
//            }
//            else if(name.equals("root") && passwd.equals("root@123")){
//                //TODO
//
//                //打开名单日志展现界面Activity
//                Intent intent = new Intent(RealTimeService.this, IdListShowActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//            else {
//                ackLogin(0);
//            }


        }

        //从 扫频界面 传频点数据过来
        public void onGetFreq(int freq){

            int arfcn = freq;

            BoardC1Set set = null;
            int earfcn = arfcn;
            int pci = 88;
            int PLMN = 46000;
            long targetnum = 123456789012345L;
            int powerval = 0;
            int gain = 52;
            int tac = (new Random()).nextInt(65535);
            int cellid = (new Random()).nextInt(65535);
            int event;
            Configure cfg = null;
            long[] lists = new long[2];

            //扫频
            event = BoardC1SetEnum.Event_ScanFreq;
            set = new BoardC1Set(earfcn, pci, PLMN, targetnum, powerval, gain, tac, cellid, event, lists);
            cfg = new Configure(0, set);

            Toast.makeText(getApplicationContext(),"扫频命令已经发送，请耐心等待1分钟", Toast.LENGTH_SHORT).show();
            (new SetParaThread(cfg)).start();

        }
    }

//    public void initRegisterTest(){
//        long ll = new Date().getTime();
//        registerDB = new RegisterDB(this);
//        registerDB.setValue("registerCode", "12345678901212");
//        registerDB.setValue("lastruntime", ""+ll);
//
//        Log.e("1111111111111====", registerDB.getValue("registerCode"));
//        Log.e("2222222222222====", registerDB.getValue("lastruntime"));
//        Log.e("3333333333333====", ""+ll);
//    }

    //通过该接口，向 登陆界面 发送返回值
    private CallbackLogin callbackLogin;
    public void setCallbackLogin(CallbackLogin callbackLogin){
        this.callbackLogin = callbackLogin;
    }
    interface CallbackLogin{
        void onAck(int isOK);
    }

    // 通过该接口，向 MainActivity 发送数据
    private Callback callback;
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    interface Callback{
        void onTargetChange(int position);
        void onStateValuesChange(int position);
        void onEnergyValueChange(int value);
        void onHeartChange(int flag);
        void onConnChange(int flag);
        void onModeSet(int index);
        void onBtnStartEnable(int isEnable);
        void onBtnStopEnable(int isEnable);
        void onTabChange(int tag);
        void onTitleSet(String title);
        void onPopRegister();
        void onSendModeChange();
        void onSetToast(String str);

        void setArfcn_Pci(int earfcn, int pci);
    }

    // 通过该接口，向 IdListSetActivity 发送数据库数据，来初始化界面
    private CallbackIdListSet callbackIdListSet;
    public void setCallbackIdListSet(CallbackIdListSet callbackIdListSet) {
        this.callbackIdListSet = callbackIdListSet;
    }
    interface CallbackIdListSet{
        void onConnect(List<IdListDatas> listIdListDatas, IdListSetDB idListSetDB);
    }


    // 通过该接口，向 能量显示 界面发送能量值
    interface CallbackEnergyShow{
        void onEnergyValueChange(int value);
    }
    private CallbackEnergyShow callbackEnergyShow;
    public void setCallbackEnergyShow(CallbackEnergyShow callbackEnergyShow){
        this.callbackEnergyShow = callbackEnergyShow;
    }


    // 向主界面发送实时流水数据
    public void addANewData(long imsi, int isTarget, int rssi){
        int position = 0;
        int length = listRealTimeDatas.size();
        int isAgain = 0;
        Date date = new Date();




        for (int i=0; i<length; i++){
            if (imsi==listRealTimeDatas.get(i).getImsi()){
                position = i;
                isAgain = 1;
                break;
            }
        }

        if (isAgain==0){  //不重复
            RealTimeDatas realTimeDatas = new RealTimeDatas();

            position = length;

            realTimeDatas.setSequence(position+1);
            realTimeDatas.setImsi(imsi);
            realTimeDatas.setIsTarget(isTarget);
            realTimeDatas.setCount(1);
            realTimeDatas.setBeginDate(date);
            realTimeDatas.setEndDate(date);
            realTimeDatas.setRssi(rssi);

            listRealTimeDatas.add(realTimeDatas);
            if (callback!=null){

            }
        }
        else{  //重复
			int count;
			
            count = listRealTimeDatas.get(position).getCount();
            listRealTimeDatas.get(position).setCount(count+1);
            listRealTimeDatas.get(position).setEndDate(date);
        }

        if (isTarget==1){
            int position_target = 0;
            int length_target = listTargetDatas.size();
            int isAgain_target = 0;

            for (int i=0; i<length_target; i++){
                if (imsi==listTargetDatas.get(i).getImsi()){
                    position_target = i;
                    isAgain_target = 1;
                    break;
                }
            }

            if (isAgain_target==0){  // 不重复
                RealTimeDatas targetDatas = new RealTimeDatas();
                targetDatas.setSequence(listTargetDatas.size()+1);
                targetDatas.setImsi(imsi);
                targetDatas.setIsTarget(isTarget);
                targetDatas.setCount(1);
                targetDatas.setBeginDate(date);
                targetDatas.setEndDate(date);
                targetDatas.setRssi(rssi);
                listTargetDatas.add(targetDatas);
                if (callback!=null){
                    callback.onTargetChange(listTargetDatas.size());
                }

                if(Global.activity!=null){
                    if(Global.activity.voiceenable)
                    {
                        Global.soundPlayer.playSoundOnLine();

                    }


                }
                Global.service.setTab(1);

            }
            else{  // 重复
                int count;

                count = listTargetDatas.get(position_target).getCount();
                listTargetDatas.get(position_target).setCount(count+1);
                listTargetDatas.get(position_target).setEndDate(date);
                //listTargetDatas.add(targetDatas);
                if (callback!=null){
                    callback.onTargetChange(listTargetDatas.size());
                }



            }
        }

        // listRealTimeDatas 的引用在 MainActivity 中，通过 MyBinder 对象被获取了
        // 并与对应的 RecyclerView 适配器绑定了
        // 所以直接通过适配器发布更新广播即可
    }

    //将 msg 组装为 StateValues 对象，保存到 listStateValues 对象变量中
    public void addANewState(String msg){
        StateValues stateValues = new StateValues();
        stateValues.setDate(new Date());
        stateValues.setMsg(msg);

        listStateValues.add(stateValues);

        if (callback!=null) {
            callback.onStateValuesChange(listStateValues.size());
        }
    }

    //更新能量值
    public void addANewValue(int value){
        if (callback!=null) {
            callback.onEnergyValueChange(value);
        }
        if (callbackEnergyShow!=null){
            callbackEnergyShow.onEnergyValueChange(value);
        }
    }

    //更新 heart 状态
    // flag==1，保持心跳；flag==0，心跳断开
    public void changeHeartState(int flag){
        if (callback!=null){
            callback.onHeartChange(flag);
        }
    }


    public void SendModeChange(){
        if (callback!=null){
            callback.onSendModeChange();
        }
    }


    //更新 connect 状态
    // flag==1, 连接；flag==0，未连接
    public void changeConnState(int flag){
        if (callback!=null){
            callback.onConnChange(flag);
        }
    }

    //设置主界面中的 模式选择 下拉框
    // index==0, TDD；index==1，FDD
    public void setMode(int index){
        if (callback!=null){
            callback.onModeSet(index);
        }
    }

    //设置主界面中的 频点和PCI
    //
    public void setArfcn_Pci(int earfcn, int pci){
        if (callback!=null){
            //callback.onModeSet(index);
            callback.setArfcn_Pci(earfcn, pci);
        }
    }

    //设置主界面 建立 按钮的使能
    //isEnable==1，使能；isEnable==0，不使能
    public void setBtnStartEnable(int isEnable){
        if (callback!=null){
            callback.onBtnStartEnable(isEnable);
        }
    }

    //设置主界面 停止 按钮的使能
    //isEnable==1，使能；isEnable==0，不使能
    public void setBtnStopEnable(int isEnable){
        if (callback!=null){
            callback.onBtnStopEnable(isEnable);
        }
    }

    //
    //主界面上面Toast
    public void setToast(String str){
        if (callback!=null){
            callback.onSetToast(str);
        }
    }

    //设置 主界面 tab 页切换
    //tag==0，实时流水；tag==1，中标流水
    public void setTab(int tag){
        if (callback!=null){
            callback.onTabChange(tag);
        }
    }

    // 获取名单列表
    public List<IdListDatas> getIdList(){
        return listIdListDatas;
    }

    // 设置 注册界面 需要读取的全局变量
    public void setRegisterMsgandMachineCode(String registerMsg, String machineCode){
        this.registerMsg = registerMsg;
        this.machineCode = machineCode;
    }

    //读取 IMEI 的值
    public long getImei(String imeistr){
        if (imeistr==null){
            return 0;
        }
        StringBuffer imeires = new StringBuffer();
        imeires.append(imeistr);

        int offset = 0;
        if (imeistr.length()<15){  //不足15位，后面补0
            offset = 15-imeistr.length();
            for (int i=0; i<offset; i++){
                imeires.append("0");
            }
        }
        else if (imeistr.length()>15){  //超过15位，删除后面多余的
            imeires.delete(15, imeistr.length());
        }

        long imei = Long.parseLong(imeires.toString());
        return imei;
    }

    // 弹出 注册界面
    public void startRegisterActivity(){
        if (callback!=null){
            callback.onPopRegister();
        }
    }

    //设置 主界面 标题栏 文字
    public void setMainActivityTitle(String title){
        if (callback!=null){
            callback.onTitleSet(title);
        }
    }

    //向 登陆界面 发送返回值
    //isOK==1，验证通过；isOK==0，验证失败
    public void ackLogin(int isOK){
        if (callbackLogin!=null){
            callbackLogin.onAck(isOK);
        }
    }

    //通过该接口，向 扫频界面 发送数据
    private CallbackScan callbackScan;
    public void setCallbackScan(CallbackScan callbackScan){
        this.callbackScan = callbackScan;
    }
    interface CallbackScan{
        void onScanData(int position);
        void onScanTextData(String info);

        void onSetBtnEnable();

    }

    //向 扫频界面 发送数据
    public void AddANewFreq(int earfcn, int priority, int pci){

        int length = listScanDatas.size();
        ScanData scanData = new ScanData();
        scanData.setSeq(length);
        scanData.setEarfcn(earfcn);
        scanData.setPriority(priority);
        scanData.setPci(pci);

        listScanDatas.add(scanData);
        if (callbackScan!=null){
            callbackScan.onScanData(length);
        }
    }

    //向 扫频界面 TextView 发送数据
    public void AddANewFreqStr(String str){
        if (callbackScan!=null){
            callbackScan.onScanTextData(str);
        }
    }


    public void SetBtnEnable() {
        if (callbackScan!=null){
            callbackScan.onSetBtnEnable();
        }
    }
}
