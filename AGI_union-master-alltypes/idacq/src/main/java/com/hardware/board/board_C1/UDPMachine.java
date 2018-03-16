package com.hardware.board.board_C1;

import com.adapter.BlackListUnit;
import com.adapter.CommonClass.StatusReport;
import com.adapter.CommonClass.StreamReport;
import com.adapter.Constants_Board_Model;
import com.adapter.event.StatusReport.StatusReportManager;
import com.adapter.event.StreamReport.StreamReportManager;
import com.adapter.event.logprint.LogPrintManager;
import com.hardware.board.Global;
import com.hardware.board.board_C1.boardc1hardware.*;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLEnbToLmtScanCellInfoRpt;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLLmtToEnbScanCellInfoCfg;
import com.hardware.util.ByteUtil;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_ARFCNPCIRPT;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_HB;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_STATION_RPT;
import static com.adapter.Parameter.BoardC1SetEnum.StatusType_BoardC1_TDDFDDCHANGE_ACK;
import static com.hardware.board.board_C1.ParseBytes.GetMsgStrType;
import static com.hardware.board.board_C1.board2SetPara.ParseRecBlockStatus;
import static com.hardware.board.board_C1.board2SetPara.ParseRecBlockStream;
import static com.hardware.util.ByteUtil.getInt;

/**
 * Created by john on 2017/7/17.
 */
//public class UDPMachine implements Runnable
public class UDPMachine extends Thread
{

    private String Tag = "udpmachine";
    private List<String> remoteipaddr;
    private List<Integer> remoteport;
    private List<Integer> remoteipaddrnum = new ArrayList<>();
    private int localport;
    private Socket sk;
    private boolean isover = false;

    private DatagramSocket server = null;


    //private DatagramSocket ds = null;




    public UDPMachine(List<String> remoteipaddr, List<Integer> remoteport, int localport) throws UnknownHostException, SocketException {



            server = new DatagramSocket(localport);
            server.setReceiveBufferSize(819200);
            server.setSendBufferSize(819200);

            LogPrintManager.Print("info", "UDPMachine", String.format("rece buf size = %d", server.getReceiveBufferSize()));

            this.remoteipaddr = remoteipaddr;
            this.remoteport = remoteport;
            this.localport = localport;

            for (int i=0; i<remoteipaddr.size(); i++){
                InetAddress byName = Inet4Address.getByName(remoteipaddr.get(i));
                byte[] address = byName.getAddress();
                int anInt = getInt(address, 0);

                remoteipaddrnum.add(anInt);
            }


//            for (int i=0; i<4; i++){
//                int mindex = i;
//                String ipaddr = this.remoteipaddr.get(mindex);
//                PingThread pingthread = new PingThread(mindex, ipaddr);
//                pingthread.start();
//                Global.pinglists[mindex] = pingthread;
//            }

            for (int i = 0; i<4; i++) {
                TongjiClassVal tongji = new TongjiClassVal(i);

                Global.tongjilist[i] = tongji;
            }


            //ds = new DatagramSocket();

    }




    public void Adjust()
    {
        //TODO 有可能需要调整位置，在错位的时候


        LogPrintManager.Print("error", Tag, "UDP接收乱序，需要调整顺序");

    }

//    public void SendBlcok(CmdObject cmdObject) throws IOException {
//        //this.SendABlock(this.ds, cmdObject);
//        this.SendABlock(this.server, cmdObject);
//    }


    synchronized public void SendBytes(int index, byte[] buf) throws IOException{


        DatagramPacket dp=
                new DatagramPacket(buf, buf.length, InetAddress.getByName(this.remoteipaddr.get(index)),this.remoteport.get(index));



        this.server.send(dp);


    }


    public void SendBlcok(int index, CmdObject cmdObject) throws IOException {
        //this.SendABlock(this.ds, cmdObject);

        byte[] buf = SendABlockCmdToBytes(cmdObject);


        String printstr = "";
        String tmpstr = "";

        if(cmdObject.getMsgType() != Constants_C1.O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP)
        {
            tmpstr = String.format("Send Msg To " +this.remoteipaddr.get(index)+" "+this.remoteport.get(index) +" "+ GetMsgStrType(buf));
            printstr += tmpstr;
            // 发送前打印
            for (int i=0; i<buf.length;i++) {
                tmpstr = String.format(" %02X ", buf[i]);
                printstr+=tmpstr;
            }
            printstr+="\n";
            LogPrintManager.Print("debug", this.Tag, printstr);
            //System.out.println();
        }
        this.SendBytes(index, buf);


    }


    public byte[] SendABlockCmdToBytes(CmdObject cmdObject) throws IOException {

        byte[] buf = null;
        switch (cmdObject.getMsgType())
        {

            case Constants_C1.O_FL_LMT_TO_ENB_REM_CFG:
                wrFLLmtToEnbScanCellInfoCfg cellcfg = (wrFLLmtToEnbScanCellInfoCfg)cmdObject.getO();
                buf = cellcfg.ToBytes();
                break;

            //频点配置更改
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_ARFCN_MOD:

                wrFLLmtToEnbSysArfcnMod mod = (wrFLLmtToEnbSysArfcnMod)cmdObject.getO();
                buf = mod.ToBytes();

                break;


            //复位下发
            case Constants_C1.O_FL_LMT_TO_ENB_REBOOT_CFG:
                wrFLLmtToEnbRebootcfg reboot = (wrFLLmtToEnbRebootcfg) cmdObject.getO();
                buf = reboot.ToBytes();
                break;

            //频点配置
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_ARFCN_CFG:
                wrFLLmtToEnbSysArfcnCfg cfg = (wrFLLmtToEnbSysArfcnCfg)cmdObject.getO();
                buf = cfg.ToBytes();
                break;

            //小区激活去激活配置
            case Constants_C1.O_FL_LMT_TO_ENB_SET_ADMIN_STATE_CFG:
                wrFLLmtToEnbSetAdminStateCfg scfg = (wrFLLmtToEnbSetAdminStateCfg) cmdObject.getO();
                buf = scfg.ToBytes();
                break;

            // 心跳应答
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP:
                wrFLEnbToLmtSysInitInformRsp rsp = (wrFLEnbToLmtSysInitInformRsp) cmdObject.getO();
                buf = rsp.ToBytes();
                break;

            // 接收增益配置
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_RxGAIN_CFG:
                wrFLLmtToEnbSysRxGainCfg gainCfg = (wrFLLmtToEnbSysRxGainCfg) cmdObject.getO();
                buf = gainCfg.ToBytes();
                break;

            //发射功率配置
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG:
                wrFLLmtToEnbSysPwr1DegreeCfg degreeCfg = (wrFLLmtToEnbSysPwr1DegreeCfg) cmdObject.getO();
                buf = degreeCfg.ToBytes();
                break;

            //设置基站测量UE配置
            case Constants_C1.O_FL_LMT_TO_ENB_MEAS_UE_CFG:
                wrFLLmtToEnbMeasUecfg uecfg = (wrFLLmtToEnbMeasUecfg) cmdObject.getO();
                buf = uecfg.ToBytes();
                break;

            //定位模式黑名单配置
            case Constants_C1.O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_CFG:
                dwblackcfg bcfg = (dwblackcfg) cmdObject.getO();
                buf = bcfg.ToBytes();
                break;

            default:
                LogPrintManager.Print("error", Tag, String.format("SendABlockCmdToBytes 未定义code %x\n", cmdObject.getMsgType()));

                break;
        }



        return buf;


//        //回包。
//        CmdObject cmdObject1 = ReadABlock(ds);
//
//        //依据这个东西触发事件


    }


//    public CmdObject ReadABlock() throws IOException
//    {
//        return ReadABlock(this.ds);
//    }




    private void SendHBtoUp(int index){
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
        sr.setIndex(index);
        List<StatusReport> lists = new ArrayList<>();
        lists.add(sr);
        StatusReportManager.getInstance().fireEvent(lists);
    }

    private void SendTDDFDDChangeOvertoUP(boolean success){
        int statustype = StatusType_BoardC1_TDDFDDCHANGE_ACK;

        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;
        //wrFLEnbToLmtSysInitInformInd hb = (wrFLEnbToLmtSysInitInformInd) cmdObject.getO();

        boolean issuccess = true;
        int errcode = 0;
        String msg = "收到模式切换配置成功命令";


        Object obj = null;


        if(success){
            issuccess = true;
            errcode = 0;
            msg = "模式切换配置成功";
            obj = null;
        }
        else {
            issuccess = false;
            errcode = 0;
            msg = "模式切换配置失败";
            obj = null;
        }



        StatusReport sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);



        StatusReport sr = sta;
        sr.setIndex(0);
        List<StatusReport> lists = new ArrayList<>();
        lists.add(sr);
        StatusReportManager.getInstance().fireEvent(lists);
    }

    private void SendArfcnPCIToUP(int earfcn, int pci){
        int statustype = StatusType_BoardC1_ARFCNPCIRPT;

        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;
        //wrFLEnbToLmtSysInitInformInd hb = (wrFLEnbToLmtSysInitInformInd) cmdObject.getO();

        boolean issuccess = true;
        int errcode = 0;
        String msg = "";


        Object []obj1 = new Object[]{earfcn, pci};

        Object obj;

        if(issuccess){
            issuccess = true;
            errcode = 0;
            msg = "收到频点PCI信息成功";
            obj = obj1;
        }
        else {
            issuccess = false;
            errcode = 0;
            msg = "收到频点PCI信息失败";
            obj = obj1;
        }



        StatusReport sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);



        StatusReport sr = sta;
        sr.setIndex(0);
        List<StatusReport> lists = new ArrayList<>();
        lists.add(sr);
        StatusReportManager.getInstance().fireEvent(lists);
    }


    //返回  Object[0]是 通道号   Object[1]是CmdObject


    private Date StrToDate(String str){
        String dateString = str;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            return date;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }




    public void StopThread() throws IOException {

        byte[]buf = new byte[1];
        buf[0] = (byte)0x18;
        DatagramPacket dp=
                new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), this.localport);

        DatagramSocket client = new DatagramSocket();

        client.send(dp);

    }


    public Object[] ReadABlock(DatagramSocket s) throws IOException {


        //byte[] recvBuf = new byte[wrMsgHeader.GetLength()];
        byte[] recvBuf = new byte[1024];
        DatagramPacket recvPacket
                = new DatagramPacket(recvBuf , recvBuf.length);
        s.receive(recvPacket);

        if(recvPacket.getLength()==1)
        {
            if(recvBuf[0] == 0x18)
            {
                return null;
            }
        }

        if (recvPacket.getLength() == recvBuf.length) {
            LogPrintManager.Print("error", Tag, "UDP buf fill, we need change code");

        }

        //s.re

        /*

        wrMsgHeader a = new wrMsgHeader(recvPacket.getData());

        int msgtype = a.getU16MsgType().GetVal();
        int msglen = a.getU16MsgLength().GetVal();
        int frameheader = a.getU32FrameHeader().GetVal();
        if(frameheader != Constants_C1.FrameHeader){
            System.out.printf("FrameHeader ! = %x   =(%x)", Constants_C1.FrameHeader, frameheader);
            return null;
        }

        if(msgtype == 0xF016)
        {
            int aa = 12;
            System.out.println(aa);
        }
        byte[] recvBuf2;
        if(msgtype != Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND)
        {
            recvBuf2 = new byte[1024];
        }
        else{
            recvBuf2 = new byte[msglen-wrMsgHeader.GetLength()];
        }
        //byte[] recvBuf2 = new byte[msglen-wrMsgHeader.GetLength()];

        DatagramPacket recvPacket2
                = new DatagramPacket(recvBuf2 , recvBuf2.length);
        if(recvBuf2.length!=0)
        {
            s.receive(recvPacket2);
        }


*/

//        String hostAddress = recvPacket.getAddress().getHostAddress();
//        String canonicalHostName = recvPacket.getAddress().getCanonicalHostName();
//        String hostName = recvPacket.getAddress().getHostName();

        InetAddress address = recvPacket.getAddress();
        String pp = String.valueOf(recvPacket.getPort());
        String hostAddress = address.getHostAddress();
        byte[] address1 = address.getAddress();
        int aint = getInt(address1, 0);
        int bindex = 0;
        for(int i=0; i<this.remoteipaddrnum.size();i++)
        {
            if(aint == remoteipaddrnum.get(i)){
                bindex = i;
                break;
            }
        }


        int msgtype = ParseBytes.GetMsgType(recvBuf);
        byte []recbytes = new byte[recvPacket.getLength()];
        System.arraycopy(recvBuf, 0, recbytes,0, recvPacket.getLength());
        if(msgtype != Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND)
        {
            String printstr = "";
            String tmpstr = "";
            tmpstr = String.format("Recv Msg From "+ hostAddress +" "+pp+" "+ GetMsgStrType(recvBuf));
            printstr+=tmpstr;
            //System.out.print("Recv Msg From "+ hostAddress +" "+pp+" "+ GetMsgStrType(recvBuf));
            for (int i=0; i<recbytes.length;i++) {
                //System.out.printf(" %02X ", recbytes[i]);
                tmpstr = String.format(" %02X ", recbytes[i]);
                printstr += tmpstr;
            }
            printstr+="\n";
//            for (int i=0; i<recvBuf2.length;i++) {
//                System.out.printf(" %02X ", recvBuf2[i]);
//            }
            LogPrintManager.Print("debug", this.Tag, printstr);
            //System.out.println(printstr);
        }

        SocketAddress remoteSocketAddress = s.getRemoteSocketAddress();


        Object o = null;

        switch (msgtype)
        {

            // 收到查询频点PCI回复
            case Constants_C1.O_FL_ENB_TO_LMT_ARFCN_IND:

                wrFLLmtToEnbSysArfcnCfg huifu = new wrFLLmtToEnbSysArfcnCfg(recbytes);
                SendArfcnPCIToUP(huifu.sysDlARFCN.GetVal(), huifu.PCI.GetVal());

                Global.recvarfcn = true;

                break;


            //收到查询基站状态回包
            case Constants_C1.O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK:


                CellStateAck cellack = new CellStateAck(recbytes);
                o = cellack;
                Global.recvcellstate = true;
                break;

            //查询功率应答 ，主要是用来做心跳用的。
            case Constants_C1.O_FL_ENB_TO_LMT_RXGAIN_POWER_DEREASE_QUERY_ACK:

                LogPrintManager.Print("info", Tag, "收到查询返回，上传心跳");
                SendHBtoUp(bindex);

                return null;
            //break;

            //扫频
            case Constants_C1.O_FL_ENB_TO_LMT_REM_INFO_RPT:
                wrFLEnbToLmtScanCellInfoRpt rptscan = new wrFLEnbToLmtScanCellInfoRpt(recbytes);
                Global.scanrecvbytes = recbytes;
                o = rptscan;
                break;

            //频点动态更新应答
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_ARFCN_MOD_ACK:
                wrFLEnbToLmtSysArfcnModAck ack1 = new wrFLEnbToLmtSysArfcnModAck(recbytes);

                o = ack1;


                break;

            case Constants_C1.O_FL_ENB_TO_LMT_SYS_MODE_ACK:    //TDD  FDD 修改应答


                if(recbytes[12]==0x00 && recbytes[13]==0x00 && recbytes[14]==0x00 && recbytes[15]==0x00){
                    SendTDDFDDChangeOvertoUP(true);
                }
                else {
                    SendTDDFDDChangeOvertoUP(false);
                }




                break;
            //复位应答
            case Constants_C1.O_FL_ENB_TO_LMT_REBOOT_ACK:

                wrFLEnbToLmtRebootAck rebootack = new wrFLEnbToLmtRebootAck(recbytes);
                o = rebootack;
                break;


            //频点配置应答
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_ARFCN_ACK:

                wrFLEnbToLmtSysArfcnAck ack = new wrFLEnbToLmtSysArfcnAck(recbytes);

                o = ack;

                break;

            //采集用户信息上传
            case Constants_C1.O_FL_ENB_TO_LMT_UE_INFO_RPT:
                wrFLEnbToLmtUeInfoRpt rpt = new wrFLEnbToLmtUeInfoRpt(recbytes);
                o = rpt;
                break;

            //小区激活去激活配置应答
            case Constants_C1.O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK:
                wrFLEnbToLmtSetAdminStateAck adminack = new wrFLEnbToLmtSetAdminStateAck(recbytes);
                o = adminack;
                break;

            // 下位机来的心跳
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND:  //下位机来的心跳
                wrFLEnbToLmtSysInitInformInd ind = new wrFLEnbToLmtSysInitInformInd(recbytes);
                o = ind;

                int tddfddtype = ind.getWrmsgHeaderInfo().getU16frame().GetVal();

                if(tddfddtype == 0xFF00)
                {//FDD
                    Global.ModelType = "FDD";
                }
                else if(tddfddtype == 0x00FF){
                    //TDD
                    Global.ModelType = "TDD";
                }

//                short earfcn__ = ByteUtil.getShort(recbytes, 20);
//                int arfcn__ = earfcn__ & 0xFFFF;
//                short pci__ = ByteUtil.getShort(recbytes, 32);

                break;

            //接收增益配置应答
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK:
                wrFLEnbToLmtSysRxGainAck gainAck = new wrFLEnbToLmtSysRxGainAck(recbytes);
                o = gainAck;
                break;

            //发射功率配置应答
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK:
                wrFLEnbToLmtSysPwr1DegreeAck dack = new wrFLEnbToLmtSysPwr1DegreeAck(recbytes);
                o = dack;
                break;

            //基站状态实时上报
            case Constants_C1.O_FL_ENB_TO_LMT_ENB_STATE_IND:

                LogPrintManager.Print("info", Tag, "收到基站状态返回，上传心跳");
                SendHBtoUp(bindex);

                wrFLEnbToLmtEnbStateInd stateind = new wrFLEnbToLmtEnbStateInd(recbytes);
                o = stateind;
                break;

            // 定位UE测量应答
            case Constants_C1.O_FL_ENB_TO_LMT_MEAS_UE_ACK:
                wrFLEnbToLmtMeasUeAck ueack = new wrFLEnbToLmtMeasUeAck(recbytes);
                o = ueack;
                break;

            // 定位UE测量值上报
            case Constants_C1.O_FL_ENB_TO_LMT_MEAS_INFO_RPT:
                wrFLEnbToLmtMeasInfoRpt infoppp = new wrFLEnbToLmtMeasInfoRpt(recbytes);
                o = infoppp;
                break;

            default:

                LogPrintManager.Print("error", Tag, String.format("ReadABlock 未定义code = 0x%x\n", msgtype));
                break;
        }


        CmdObject cmdo = new CmdObject(msgtype, o );
        Object []r = new Object[2];
        r[0] = bindex;
        r[1] = cmdo;
        return r;
        //return new CmdObject(msgtype, o);
    }


    public void SetOver() {

        LogPrintManager.Print("info", Tag, "SetOver in");
        this.isover = true;
        this.interrupt();

        new Thread(){
            @Override
            public void run() {

                try {

                    StopThread();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

//        for (int i=0; i<4; i++)
//        {
//            if(Global.pinglists[i]!=null){
//                Global.pinglists[i].SetOver();
//                Global.pinglists[i] = null;
//            }
//
//        }

        LogPrintManager.Print("info", Tag, "SetOver out");
    }

    @Override
    public void run() {


        LogPrintManager.Print("info", Tag, "thread UDPMachine begin to run");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        while (isover == false){

            try {
                //CmdObject cmdObject = ReadABlock(this.server);
                Object []or = ReadABlock(this.server);

                if(or == null){
                    continue;
                }

                int bindex = (int)or[0];
                CmdObject cmdObject = (CmdObject)or[1];

                if (cmdObject == null) {
                    Adjust();
                }

                //这里触发事件

                //System.out.printf("收到一个消息 %x\n", cmdObject.getMsgType());


                switch (cmdObject.getMsgType())
                {
                    //采集用户信息上报 流水信息
                    case Constants_C1.O_FL_ENB_TO_LMT_UE_INFO_RPT:
                        StreamReport streamReport = ParseRecBlockStream(cmdObject);

                        streamReport.setIndex(bindex);
                        long imsi = streamReport.getImsi();
                        //TODO 这里要做一次与黑名单库的比较，填充相应字段
                        boolean compare = BlackListUnit.Compare(imsi);
                        streamReport.setIstarget(compare);

                        LogPrintManager.Print("debug", Tag, String.format("index = %d imsi = %d istarget = %B", bindex, imsi, compare));

                        List<StreamReport> list = new ArrayList<>();
                        list.add(streamReport);
                        StreamReportManager.getInstance().fireEvent(list);

                        break;

                    //下位机发来的心跳，要立马回包
                    case Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND:

                        //立马回包
                        wrFLEnbToLmtSysInitInformRsp rsp = wrFLEnbToLmtSysInitInformRsp.GetDefault();
                        SendBlcok(bindex, new CmdObject(wrFLEnbToLmtSysInitInformRsp.GetMsgType(), rsp));

                        StatusReport sr = ParseRecBlockStatus(cmdObject);
                        sr.setIndex(bindex);
                        List<StatusReport> lists = new ArrayList<>();
                        lists.add(sr);
                        StatusReportManager.getInstance().fireEvent(lists);

                        break;
                    //除了流水信息之外的所有状态信息
                    default:
                        StatusReport statusReport = ParseRecBlockStatus(cmdObject);
                        statusReport.setIndex(bindex);
                        List<StatusReport> list_status = new ArrayList<>();
                        list_status.add(statusReport);
                        StatusReportManager.getInstance().fireEvent(list_status);


//                        if(statusReport.getStatustype() == StatusType_BoardC1_STATION_RPT){
//                            int val1 = statusReport.getErrcode();
//                            if(val1 == 8)//小区去激活结束 开启线程
//                            {
//                                int mindex = bindex;
//                                String ipaddr = this.remoteipaddr.get(mindex);
//                                PingThread pingthread = new PingThread(mindex, ipaddr);
//                                pingthread.start();
//                                Global.pinglists[mindex] = pingthread;
//
//                            } else if (val1 == 6) {//小区建立 停止线程
//                                int mindex = bindex;
//                                PingThread pingthread = Global.pinglists[mindex];
//                                if (pingthread != null) {
//                                    pingthread.SetOver();
//                                    Global.pinglists[mindex] = null;
//                                }
//
//                            }
//                        }


                        break;
                }



            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


        this.server.close();

        LogPrintManager.Print("info", Tag, "thread UDPMachine Over");
    }

}
