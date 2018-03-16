package com.adapter;

import com.adapter.CommonClass.Configure;
import com.adapter.Parameter.BoardC1Set;
import com.adapter.Parameter.BoardC1SetEnum;
import com.adapter.event.StatusReport.StatusReportListener;
import com.adapter.event.StatusReport.StatusReportManager;
import com.adapter.event.StreamReport.StreamReportListener;
import com.adapter.event.StreamReport.StreamReportManager;
import com.adapter.event.logprint.LogPrintListener;
import com.adapter.event.logprint.LogPrintManager;
import com.hardware.board.BoardInfo;
import com.hardware.board.Global;
import com.hardware.board.board_C1.CmdObject;
import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.board.board_C1.UDPMachine;
import com.hardware.board.board_C1.board2SetPara;
import com.hardware.board.board_C1.boardc1hardware.CellStateQuery;
import com.hardware.board.board_C1.boardc1hardware.Constants_C1;
import com.hardware.board.board_C1.boardc1hardware.querycell.wrFLLmtToEnbGetEnbState;
import com.hardware.board.board_C1.boardc1hardware.wrFLLmtToEnbSysArfcnCfg;
import com.hardware.board.board_C1.boardc1hardware.wrFLLmtToEnbSysArfcnMod;
import com.hardware.board.board_C1.boardc1hardware.wrMsgHeader;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/18.
 */
public class Machine {


    private static String Tag = "Machine";
    private static Thread udpthread = null;

    public static String GetVersion(){
        return "2017-08-22";
    }


    private static List<String>GetIniIPList() throws FileNotFoundException {


        IniReader ini = new IniReader();
        List<String> ipaddr = new ArrayList<>();

        ipaddr.add(ini.getProperty("Model0_IP"));
        ipaddr.add(ini.getProperty("Model1_IP"));
        ipaddr.add(ini.getProperty("Model2_IP"));
        ipaddr.add(ini.getProperty("Model3_IP"));

        return ipaddr;
    }

    private static List<Integer> GetIniPortList() throws FileNotFoundException {

        IniReader ini = new IniReader();
        List<Integer> port = new ArrayList<>();
        port.add(Integer.parseInt(ini.getProperty("Model0_PORT")));
        port.add(Integer.parseInt(ini.getProperty("Model1_PORT")));
        port.add(Integer.parseInt(ini.getProperty("Model2_PORT")));
        port.add(Integer.parseInt(ini.getProperty("Model3_PORT")));

        return port;
    }


    private static List<String>GetRegularIPList(){
        List<String> ipaddr = new ArrayList<>();
      //  ipaddr.add("192.168.199.12");
//        ipaddr.add("192.168.1.7");   //这里填写电脑IP地址
//       ipaddr.add("192.168.2.53");
        ipaddr.add("192.168.7.8");
        ipaddr.add("192.168.2.53");
        ipaddr.add("192.168.2.54");
        ipaddr.add("192.168.2.62");


        return ipaddr;
    }


    private static List<Integer> GetRegularPortList(){

        List<Integer> port = new ArrayList<>();
        port.add(3345);
        port.add(3345);
        port.add(3345);
        port.add(3345);
        return port;
    }


    private static List<String>GetSimulateIPList(){
        List<String> ipaddr = new ArrayList<>();
        ipaddr.add("127.0.0.1");
        ipaddr.add("127.0.0.1");
        ipaddr.add("127.0.0.1");
        ipaddr.add("127.0.0.1");

        return ipaddr;
    }


    private static List<Integer> GetSimulatePortList(){
        List<Integer> port = new ArrayList<>();
        port.add(3346);
        port.add(3347);
        port.add(3348);
        port.add(3349);
        return port;
    }





    public static Ret Init()  {



        LogPrintManager.Print("level", Tag, "Machine Init...");

        int localport = 3345;


        //List<String> ipaddr = GetSimulateIPList();

        //List<Integer> port = GetSimulatePortList();

//        List<String> ipaddr = GetRegularIPList();
//
//        List<Integer> port = GetRegularPortList();


        List<String> ipaddr = null;
        List<Integer> port = null;

        ipaddr = GetRegularIPList();
        port = GetRegularPortList();



        for (int i=0; i<4; i++){
            LogPrintManager.Print("info", Tag, String.format("index = %d ip = %s port = %d", i, ipaddr.get(i), port.get(i)));
        }


        InitBoardInfo(localport, ipaddr, port);


        UDPMachine udp = null;
        try {
            udp = new UDPMachine(ipaddr, port, localport);
        }
        catch (java.net.BindException e){
            LogPrintManager.Print("error", Tag, "BindException "+e.getMessage());
            e.printStackTrace();
            return new Ret(false, -1, "BindException "+e.getMessage());
        }
        catch (java.net.SocketException e){
            LogPrintManager.Print("error", Tag, "SocketException "+e.getMessage());
            e.printStackTrace();
            return new Ret(false, -2, "SocketException "+e.getMessage());
        }
        catch (UnknownHostException e) {
            LogPrintManager.Print("error", Tag, "UnknownHostException "+e.getMessage());
            e.printStackTrace();
            return new Ret(false, -3, "UnknownHostException "+e.getMessage());
        }

        Global.udpmachine = udp;

        //udpthread = new Thread((Runnable) Global.udpmachine);
        udpthread = udp;

        udpthread.start();
        return new Ret(true, 0, "网络建立成功");
    }

    private static void InitBoardInfo(int localport, List<String> ipaddr, List<Integer> port) {
        for (int i=0; i<4;i++){
            BoardInfo b0 = new BoardInfo();
            b0.setBoardindex(i);
            b0.setBoardName("C1");

            switch (i) {
                case 0:
                    b0.setDisplayName("T1");
                    b0.setModelName("TDD");
                    break;
                case 1:
                    b0.setDisplayName("T2");
                    b0.setModelName("TDD");
                    break;
                case 2:
                    b0.setDisplayName("T3");
                    b0.setModelName("TDD");
                    break;
                case 3:
                    b0.setDisplayName("F1");
                    b0.setModelName("FDD");
                    break;

            }

            b0.setIpaddr(ipaddr.get(i));
            b0.setRemoteport(String.valueOf(port.get(i)));
            b0.setLocalport(String.valueOf(localport));
            Global.boardinfolist.add(b0);
        }
    }


    public static void RegisterEventListener(StatusReportListener lis1, StreamReportListener lis2,
                                             LogPrintListener lis3) {
        StatusReportManager.getInstance().addListener(lis1);


        StreamReportManager.getInstance().addListener(lis2);


        LogPrintManager.getInstance().addListener(lis3);
    }


    public static Ret UnInit(){

//        for (int i=0 ; i<Global.boardinfolist.size();i++) {
//            UDPMachine udp = Global.boardinfolist.get(i).getUdp();
//            udp.SetOver();
//        }

        LogPrintManager.Print("info", Tag, "UnInit in");
        Global.udpmachine.SetOver();

        //udpthread.interrupt();
        udpthread.interrupt();

        try {
            udpthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LogPrintManager.Print("info", Tag, "UnInit out");
        return new Ret(true, 0, "网络释放成功");
    }

    public static void SetPara(Configure cfg)
    {

        BoardC1Set set = (BoardC1Set) cfg.getObj();
        int event = set.getEvent();

        try
        {
            if(event!= BoardC1SetEnum.Event_CellBtnPress)
            {
                CmdObject cmdo = board2SetPara.ParaToCmdObject(cfg);
                if(cmdo==null) return;
                int index = cfg.getIndex();
                Global.udpmachine.SendBlcok(index, cmdo);
            }
            else {

                CmdObject cmdo = null;
                int index = 0;

//                set.setEvent(BoardC1SetEnum.Event_GainChanged);
//                cmdo = board2SetPara.ParaToCmdObject(cfg);
//                if(cmdo==null) return;
//                index = cfg.getIndex();
//                Global.udpmachine.SendBlcok(index, cmdo);
//                LogPrintManager.Print("info", Tag, "set gain");
//
//                Thread.sleep(2000);
//
//                set.setEvent(BoardC1SetEnum.Event_PowerChanged);
//                cmdo = board2SetPara.ParaToCmdObject(cfg);
//                if(cmdo==null) return;
//                index = cfg.getIndex();
//                Global.udpmachine.SendBlcok(index, cmdo);
//                LogPrintManager.Print("info", Tag, "set power");
//                Thread.sleep(2000);

                set.setEvent(BoardC1SetEnum.Event_CellBtnPress);
                cmdo = board2SetPara.ParaToCmdObject(cfg);
                if(cmdo==null) return;
                index = cfg.getIndex();
                Global.udpmachine.SendBlcok(index, cmdo);
                LogPrintManager.Print("info", Tag, "set cell");

            }
        }
         catch (IOException e) {



            String msg = "SetPara 失败 "+e.getMessage();

            LogPrintManager.Print("error", Tag, msg);

        }


//        int index = cfg.getIndex();
//
//        if(cmdo!=null){
//            try {
//
//                UDPMachine udp = Global.boardinfolist.get(index).getUdp();
//                udp.SendBlcok(cmdo);
//            }
//            catch (Exception e) {
//
//            }
//        }


    }

    public static int GetModelNum()
    {
        return Global.boardinfolist.size();
    }

    public static String GetModelName(int index)
    {
        return Global.boardinfolist.get(index).getBoardName();
    }


    public static void QueryCellEarfcnPCI(){
        wrFLLmtToEnbGetEnbState state = new wrFLLmtToEnbGetEnbState();

        byte[] bytes = state.ToBytes();

        try {
            Global.udpmachine.SendBytes(0, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void QueryCellState(){
        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short) CellStateQuery.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)CellStateQuery.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        CellStateQuery query = new CellStateQuery();
        query.WrmsgHeaderInfo = header;

        byte[] buf = query.ToBytes();

        try {
            Global.udpmachine.SendBytes(0, buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ChangeMode(String mode) {

        byte[] buf = new byte[16];


        for (int i=0; i<16; i++){
            buf[i] = 0;
        }

        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)0xF001);/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)16);/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);


        try {

            if(mode.equals("TDD")){
                //change to TDD

                // 设置TDD字节

                byte[] bytes = header.ToBytes();
                System.arraycopy(bytes,0,
                        buf,0,
                        bytes.length);



                buf[12] = 0x00;
                //TODO

                Global.udpmachine.SendBytes(0, buf);


            }
            else{
                //change to FDD
                // 设置FDD字节

                byte[] bytes = header.ToBytes();
                System.arraycopy(bytes,0,
                        buf,0,
                        bytes.length);



                buf[12] = 0x01;

                //TODO
                Global.udpmachine.SendBytes(0, buf);
            }

        }catch (Exception e){

        }


    }
}
