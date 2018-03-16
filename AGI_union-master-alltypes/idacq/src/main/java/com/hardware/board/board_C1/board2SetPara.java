package com.hardware.board.board_C1;

import com.adapter.BlackListUnit;
import com.adapter.CommonClass.Configure;
import com.adapter.CommonClass.StatusReport;
import com.adapter.CommonClass.StreamReport;
import com.adapter.Constants_Board_Model;
import com.adapter.Parameter.BoardC1Set;
import com.adapter.Parameter.BoardC1SetEnum;
import com.adapter.event.logprint.LogPrintManager;
import com.hardware.board.Global;
import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.board.board_C1.MacroDef.U8;
import com.hardware.board.board_C1.MacroDef.UArray;
import com.hardware.board.board_C1.boardc1hardware.*;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLEnbToLmtScanCellInfoRpt;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLLmtToEnbScanCellInfoCfg;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.adapter.Parameter.BoardC1SetEnum.*;
import static com.hardware.board.board_C1.ComFunClass.ByteToLong;
import static com.hardware.board.board_C1.boardc1hardware.Constants_C1.*;


/**
 * Created by john on 2017/7/18.
 */

//这个类里面放置了板子相关的配置参数
public class board2SetPara {


    private static String Tag = "board2SetPara";

    public static CmdObject ParaToCmdObject(Configure cfg) {
        StatusReport s = null;



        BoardC1Set set = (BoardC1Set) cfg.getObj();

        int event = set.getEvent();

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        CmdObject cmdo = null;

        int index ;
        BoardC1Set seto ;
        long[] blacklist ;

        switch (event) {
            case BoardC1SetEnum.Event_Reset:
                wrFLLmtToEnbRebootcfg wrFLLmtToEnbRebootcfg = BuildRebootSetBlock(cfg);
                cmdo = new CmdObject(wrFLLmtToEnbRebootcfg.GetMsgType(), wrFLLmtToEnbRebootcfg);
                break;

            //下发黑名单
            case BoardC1SetEnum.Event_BlackList:

                int indexs = cfg.getIndex();
                BoardC1Set setos = (BoardC1Set) cfg.getObj();
                long[] blacklists = setos.getBlacklist();
                BlackListUnit.SetBlackList(blacklists);

                cmdo = null;
                break;

            case Event_ScanFreq:
                index = cfg.getIndex();
                seto = (BoardC1Set) cfg.getObj();

                wrFLLmtToEnbScanCellInfoCfg scanfreq = BuildScanFreqCfgBlock(cfg);
                cmdo = new CmdObject(wrFLLmtToEnbScanCellInfoCfg.GetMsgType(), scanfreq);
                break;
            case Event_CellMod:

                index = cfg.getIndex();
                seto = (BoardC1Set) cfg.getObj();
                blacklist = seto.getBlacklist();



                printWriter.printf("建立操作 index = %d earfcn = %d power = %d gain = %d tac = %d cellid = %d pci = %d  plmn = %d\n",
                        index, seto.getEarfcn(), seto.getPowerval(), seto.getGain(), seto.getTac(), seto.getCellid(),
                        seto.getPci(), seto.getPLMN());

                LogPrintManager.Print("info", Tag, writer.toString());

                if (blacklist == null) {
                    LogPrintManager.Print("info", Tag, "blacklist == null");
                }
                else {
                    BlackListUnit.SetBlackList(blacklist);
                }

                //


                wrFLLmtToEnbSysArfcnMod arfcnmod = BuildArfcnmodBlock(cfg);
                cmdo = new CmdObject(wrFLLmtToEnbSysArfcnMod.GetMsgType(), arfcnmod);


                break;


            //建立小区
            case BoardC1SetEnum.Event_CellBtnPress:
                //shi希望有一系列自动化操作



                index = cfg.getIndex();
                seto = (BoardC1Set) cfg.getObj();
                blacklist = seto.getBlacklist();



                printWriter.printf("建立操作 index = %d earfcn = %d power = %d gain = %d tac = %d cellid = %d pci = %d  plmn = %d\n",
                        index, seto.getEarfcn(), seto.getPowerval(), seto.getGain(), seto.getTac(), seto.getCellid(),
                        seto.getPci(), seto.getPLMN());

                LogPrintManager.Print("info", Tag, writer.toString());

                if (blacklist == null) {
                    LogPrintManager.Print("info", Tag, "blacklist == null");
                }
                else {
                    BlackListUnit.SetBlackList(blacklist);
                }

                //





                wrFLLmtToEnbSysArfcnCfg arfcncfg = BuildArfcncfgBlock(cfg);
                cmdo = new CmdObject(wrFLLmtToEnbSysArfcnCfg.GetMsgType(), arfcncfg);

                break;

            //小区去激活配置
            case BoardC1SetEnum.Event_CellStop:

                wrFLLmtToEnbSetAdminStateCfg admincfg = BuildAdminCfgBlock(cfg);
                cmdo = new CmdObject(wrFLLmtToEnbSetAdminStateCfg.GetMsgType(), admincfg);
                break;

            case BoardC1SetEnum.Event_GainChanged:

                int gainval = set.getGain();
                wrFLLmtToEnbSysRxGainCfg wrFLLmtToEnbSysRxGainCfg = BuildGainSetBlock(gainval);
                cmdo = new CmdObject(wrFLLmtToEnbSysRxGainCfg.GetMsgType(), wrFLLmtToEnbSysRxGainCfg);

                break;

            case BoardC1SetEnum.Event_PowerChanged:

                int powerval = set.getPowerval();

                wrFLLmtToEnbSysPwr1DegreeCfg wrFLLmtToEnbSysPwr1DegreeCfg = BuildPowerSetBlock(powerval);

                cmdo = new CmdObject(Constants_C1.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG, wrFLLmtToEnbSysPwr1DegreeCfg);

                break;

            case BoardC1SetEnum.Event_TargetChanged:

                long target = set.getTargetnum();
                wrFLLmtToEnbMeasUecfg measuecfg = BuildMeasUeCfgSetBlock(cfg, target);


                cmdo = new CmdObject(measuecfg.GetMsgType(), measuecfg);
                break;
        }





        return cmdo;
    }


    private static wrFLLmtToEnbScanCellInfoCfg BuildScanFreqCfgBlock(Configure cfg) {

        int wholeBandRem_ = 0;
        int sysArfcnNum_ = 1;
        int earfcn_ = 38950;

        BoardC1Set set = (BoardC1Set) cfg.getObj();
        earfcn_ = set.getEarfcn();
        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbScanCellInfoCfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbScanCellInfoCfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        U32 wholeBandRem = new U32(wholeBandRem_);
        U32 sysArfcnNum = new U32(sysArfcnNum_);
        U32 sysarfcn[] = new U32[C_MAX_REM_ARFCN_NUM];
        sysarfcn[0] = new U32(earfcn_);
        for(int i=1; i<C_MAX_REM_ARFCN_NUM; i++){
            sysarfcn[i] = new U32(0);
        }

        return new wrFLLmtToEnbScanCellInfoCfg(header, wholeBandRem, sysArfcnNum, sysarfcn);
    }


    public static wrFLLmtToEnbSysArfcnMod BuildArfcnmodBlock(Configure cfg) {
        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbSysArfcnMod.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbSysArfcnMod.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);


        int index = cfg.getIndex();

        BoardC1Set set = (BoardC1Set) cfg.getObj();

        U32 sysUlARFCN = new U32(255);
//        if(index>=0&&index<=2)
//        {
//            sysUlARFCN = new U32(255);/*上行频点*/
//        }
//        else if(index==3){
//            sysUlARFCN = new U32(set.getEarfcn()+18000);
//        }

        if(Global.ModelType.equals("TDD")){
            sysUlARFCN = new U32(255);/*上行频点*/
        }
        else if(Global.ModelType.equals("FDD")){
            sysUlARFCN = new U32(set.getEarfcn()+18000);
        }

        U32 sysDlARFCN = new U32(set.getEarfcn());/*下行频点*/

        String s = com.hardware.board.board_C1.ComFunClass.LongToString(set.getPLMN());
        byte[] bytes = s.getBytes();
        byte[] bytes2 = new byte[7];
        System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
        for (int i= bytes.length;i<7;i++)
        {
            bytes2[i] = 0;
        }

        UArray PLMN = new UArray(bytes2);  /*plmn str,   eg: “46001”*/ // 7个字节


        U8 sysBandwidth = new U8((byte)50); /*wrFLBandwidth*/

        if(index>=0&&index<=2)
        {
            sysBandwidth = new U8((byte)50);
        }
        else {
            //sysBandwidth = new U8((byte)25);  //FDD
            sysBandwidth = new U8((byte)50);  //FDD    modify by efg 20170819
        }
        // U8 sysBandwidth = new U8((byte)100); /*wrFLBandwidth*/

        int sb = 0;
        int arfcn = set.getEarfcn();
        //if(index>=0&&index<=2)
        if(Global.ModelType.equals("TDD"))
        {
            if(arfcn>=37750&&arfcn<=38249)
            {
                sb = 38;
            } else if (arfcn >= 38250 && arfcn <= 38649) {
                sb = 39;
            }
            else if (arfcn >= 38650 && arfcn <= 39649) {
                sb = 40;
            }
            else if (arfcn >= 39650 && arfcn <= 41589) {
                sb = 41;
            }
        }
        else {
            if(arfcn>=0&&arfcn<=599)
            {
                sb = 1;
            } else if (arfcn >= 1200 && arfcn <= 1949) {
                sb = 3;
            }

        }



        U8  sysBand = new U8((byte)sb);/*频段:Band38/band39/band40*/
        U16  PCI = new U16((short) set.getPci());/*0~503*/
        U16  TAC = new U16((short)set.getTac());
        U32 CellId= new U32(set.getCellid());
        U32  UePMax = new U32((short)23);/*<=23dBm*/
        U16 EnodeBPMax = new U16((short)20);

        LogPrintManager.Print("info", "board2SetPara",
                String.format("index = %d  sysUlARFCN = %d sysDlARFCN = %d PLMN = %d sysBandwidth = %d sysBand = %d PCI = %d TAC = %d CellId = %d UePMax = %d EnodeBPMax = %d",
                        index, sysUlARFCN.GetVal(), sysDlARFCN.GetVal(), set.getPLMN(), sysBandwidth.GetVal(),
                        sysBand.GetVal(), PCI.GetVal(), TAC.GetVal(), CellId.GetVal(), UePMax.GetVal(), EnodeBPMax.GetVal()));

        return new wrFLLmtToEnbSysArfcnMod(header, sysUlARFCN, sysDlARFCN, PLMN,
                sysBand,  CellId, UePMax);
//        return new wrFLLmtToEnbSysArfcnCfg(header, sysUlARFCN, sysDlARFCN, PLMN, sysBandwidth,
//                sysBand, PCI, TAC, CellId, UePMax, EnodeBPMax);
    }

    private static wrFLLmtToEnbSysArfcnCfg BuildArfcncfgBlock(Configure cfg) {
        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbSysArfcnCfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbSysArfcnCfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);


        int index = cfg.getIndex();

        BoardC1Set set = (BoardC1Set) cfg.getObj();

        U32 sysUlARFCN = new U32(255);
//        if(index>=0&&index<=2)
//        {
//            sysUlARFCN = new U32(255);/*上行频点*/
//        }
//        else if(index==3){
//            sysUlARFCN = new U32(set.getEarfcn()+18000);
//        }

        if(Global.ModelType.equals("TDD")){
            sysUlARFCN = new U32(255);/*上行频点*/
        }
        else if(Global.ModelType.equals("FDD")){
            sysUlARFCN = new U32(set.getEarfcn()+18000);
        }

        U32 sysDlARFCN = new U32(set.getEarfcn());/*下行频点*/

        String s = com.hardware.board.board_C1.ComFunClass.LongToString(set.getPLMN());
        byte[] bytes = s.getBytes();
        byte[] bytes2 = new byte[7];
        System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
        for (int i= bytes.length;i<7;i++)
        {
            bytes2[i] = 0;
        }

        UArray PLMN = new UArray(bytes2);  /*plmn str,   eg: “46001”*/ // 7个字节


        U8 sysBandwidth = new U8((byte)50); /*wrFLBandwidth*/

        if(index>=0&&index<=2)
        {
            sysBandwidth = new U8((byte)50);
        }
        else {
            //sysBandwidth = new U8((byte)25);  //FDD
            sysBandwidth = new U8((byte)50);  //FDD    modify by efg 20170819
        }
       // U8 sysBandwidth = new U8((byte)100); /*wrFLBandwidth*/

        int sb = 0;
        int arfcn = set.getEarfcn();
        //if(index>=0&&index<=2)
        if(Global.ModelType.equals("TDD"))
        {
            if(arfcn>=37750&&arfcn<=38249)
            {
                sb = 38;
            } else if (arfcn >= 38250 && arfcn <= 38649) {
                sb = 39;
            }
            else if (arfcn >= 38650 && arfcn <= 39649) {
                sb = 40;
            }
            else if (arfcn >= 39650 && arfcn <= 41589) {
                sb = 41;
            }
        }
        else {
            if(arfcn>=0&&arfcn<=599)
            {
                sb = 1;
            } else if (arfcn >= 1200 && arfcn <= 1949) {
                sb = 3;
            }

        }



        U32  sysBand = new U32(sb);/*频段:Band38/band39/band40*/
        U16  PCI = new U16((short) set.getPci());/*0~503*/
        U16  TAC = new U16((short)set.getTac());
        U32 CellId= new U32(set.getCellid());
        U16  UePMax = new U16((short)23);/*<=23dBm*/
        U16 EnodeBPMax = new U16((short)20);

        LogPrintManager.Print("info", "board2SetPara",
                String.format("index = %d  sysUlARFCN = %d sysDlARFCN = %d PLMN = %d sysBandwidth = %d sysBand = %d PCI = %d TAC = %d CellId = %d UePMax = %d EnodeBPMax = %d",
                        index, sysUlARFCN.GetVal(), sysDlARFCN.GetVal(), set.getPLMN(), sysBandwidth.GetVal(),
                        sysBand.GetVal(), PCI.GetVal(), TAC.GetVal(), CellId.GetVal(), UePMax.GetVal(), EnodeBPMax.GetVal()));

        return new wrFLLmtToEnbSysArfcnCfg(header, sysUlARFCN, sysDlARFCN, PLMN, sysBandwidth,
                sysBand, PCI, TAC, CellId, UePMax, EnodeBPMax);
    }

    private static wrFLLmtToEnbSetAdminStateCfg BuildAdminCfgBlock(Configure cfg) {

        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbSetAdminStateCfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbSetAdminStateCfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        U32 workAdminState = new U32(0);

        return new wrFLLmtToEnbSetAdminStateCfg(header, workAdminState);


    }


    public static wrFLLmtToEnbMeasUecfg BuildMeasUeCfgSetBlock(Configure cfg, long target) {


        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbMeasUecfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbMeasUecfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        U8 u8WorkMode = new U8((byte)2);
        UArray u8Res3 = new UArray(3);  //3个字节

        String s = com.hardware.board.board_C1.ComFunClass.LongToString(target);
        byte[] bytes = s.getBytes();
        byte[] bytes2 = new byte[17];
        System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
        bytes2[15] = 0;
        bytes2[16] = 0;
        UArray IMSI = new UArray(bytes2);
       // U8 u8MeasReportPeriod = new U8((byte)(0));  //0 代表120ms
        U8 u8MeasReportPeriod = new U8((byte)(4));   //4 代表1024ms
        U8 SchdUeMaxPowerTxFlag = new U8((byte)(0));
        U8 SchdUeMaxPowerValue = new U8((byte)(23));
        U8 SchdUeUIFixedPrbSwitch = new U8((byte)(1));
        U8 CampOnAllowedFlag = new U8((byte)(1));
        UArray u8Res2 = new UArray(2);; //2个字节




        return new wrFLLmtToEnbMeasUecfg(header, u8WorkMode, u8Res3, IMSI, u8MeasReportPeriod, SchdUeMaxPowerTxFlag,
                SchdUeMaxPowerValue, SchdUeUIFixedPrbSwitch, CampOnAllowedFlag, u8Res2);
    }

    public static wrFLLmtToEnbRebootcfg BuildRebootSetBlock(Configure cfg){
        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbRebootcfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbRebootcfg.GetLength());/*定义消息的长度*/

        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);



        return new wrFLLmtToEnbRebootcfg(header);
    }

    public static wrFLLmtToEnbSysPwr1DegreeCfg BuildPowerSetBlock(int powerval) {

        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short) Constants_C1.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG);/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbSysPwr1DegreeCfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        U32 Pwr = new U32(powerval);
        U8 issave = new U8((byte)0);
        UArray res = new UArray(3);

        return new wrFLLmtToEnbSysPwr1DegreeCfg(header, Pwr, issave, res);


    }

    public static wrFLLmtToEnbSysRxGainCfg BuildGainSetBlock(int gainval) {

        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLLmtToEnbSysRxGainCfg.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbSysRxGainCfg.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short) Constants_C1.WorkMode_TDD);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        U32 rxgain= new U32(gainval);

        return new wrFLLmtToEnbSysRxGainCfg(header, rxgain);
    }


    public static StreamReport ParseRecBlockStream(CmdObject cmdObject){
        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;
        long imei = 0;   // 0代表无意义
        long imsi = 0;   // 0代表无意义
        boolean istarget;
        int rssi = 0;

        StreamReport sreport = null;
        switch (cmdObject.getMsgType()) {
            case Constants_C1.O_FL_ENB_TO_LMT_UE_INFO_RPT:

                wrFLEnbToLmtUeInfoRpt rpt = (wrFLEnbToLmtUeInfoRpt) cmdObject.getO();

                UArray imsi1 = rpt.getIMSI();
                UArray imei1 = rpt.getIMEI();
                U8 Rssi = rpt.getRssi();

                imsi = ByteToLong(imsi1.ToBytes(), 0, 15);
                imei = 0;
                rssi = Rssi.GetVal();

                if(rpt.getWrmsgHeaderInfo().getU16frame().GetVal()== Constants_C1.WorkMode_TDD)
                {
                    modeltype = Constants_Board_Model.ModelType_Board2_TDD;
                }
                else if(rpt.getWrmsgHeaderInfo().getU16frame().GetVal()== Constants_C1.WorkMode_FDD)
                {
                    modeltype = Constants_Board_Model.ModelType_Board2_FDD;
                }

                //TODO 判断是否中标

                istarget = false;

                //sreport = new StreamReport(boardtype, modeltype, imei, imsi, istarget);
                sreport = new StreamReport(boardtype, modeltype, imei, imsi, rssi, istarget);

               break;
            default:
                LogPrintManager.Print("error", Tag, String.format("ParseRecBlockStream 未定义code = 0x%x\n", cmdObject.getMsgType()));

                break;
        }

        return sreport;
    }


    private static String StationRPTintToStr(int val1) {
        String msg = null;
        switch (val1) {
            case 0:
                msg = "空口同步成功";
                break;
            case 1:
                msg = "空口同步失败";
                break;
            case 2:
                msg = "GPS同步成功";
                break;
            case 3:
                msg = "GPS同步失败";
                break;
            case 4:
                msg = "扫频成功";
                break;
            case 5:
                msg = "扫频失败";
                break;
            case 6:
                msg = "小区建立成功";
                break;
            case 7:
                msg = "小区建立失败";
                break;
            case 8:
                msg = "小区去激活成功";
                break;
            case 9:
                msg = "空口同步中";
                break;
            case 10:
                msg = "GPS同步中";
                break;
            case 11:
                msg = "扫频中";
                break;
            case 12:
                msg = "小区建立中";
                break;
            case 13:
                msg = "小区去激活中";
                break;
            case 0xFFFF:
                msg = "无效状态";
                break;

        }
        return msg;
    }

    //将收到的数据包转换成上层需要的状态提示
    public static StatusReport ParseRecBlockStatus(CmdObject cmdObject) {

        int boardtype = Constants_Board_Model.BoardType_Board2;
        int modeltype = Constants_Board_Model.ModelType_Board2_TDD;
        int statustype = 0;
        boolean issuccess;

        int errcode;
        String msg;

        Object obj;

        StatusReport sta = null;
        switch (cmdObject.getMsgType()) {

            //基站状态查询回包
            case Constants_C1.O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK:

                statustype = StatusType_BoardC1_CELLSTATERPT;
                CellStateAck cellack = (CellStateAck) cmdObject.getO();

                issuccess = true;
                errcode = cellack.CellState.GetVal();
                msg = "查询基站状态成功";


                obj = null;
                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;

            //扫频状态上报
            case Constants_C1.O_FL_ENB_TO_LMT_REM_INFO_RPT:

                statustype = StatusType_BoardC1_SCANFREQ;
                wrFLEnbToLmtScanCellInfoRpt rpt = (wrFLEnbToLmtScanCellInfoRpt) cmdObject.getO();

                issuccess = true;
                errcode = 0;
                msg = "扫频成功";

                Object olist[] = new Object[2];
                olist[0] = rpt;
                olist[1] = Global.scanrecvbytes;
                obj = olist;
                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            //频点动态更新应答
            case O_FL_ENB_TO_LMT_SYS_ARFCN_MOD_ACK:


                statustype = StatusType_BoardC1_ARFCN_ACK;

                wrFLEnbToLmtSysArfcnModAck arfckack1 = (wrFLEnbToLmtSysArfcnModAck) cmdObject.getO();
                int cr1 = arfckack1.getCfgResult().GetVal();
                if(cr1==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "动态频点配置成功";
                }
                else {
                    issuccess = false;
                    errcode = cr1;
                    msg = "动态频点配置失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;


            // UE测量值上报
            case O_FL_ENB_TO_LMT_MEAS_INFO_RPT:
                statustype = StatusType_BoardC1_MEASUE_RPT;

                wrFLEnbToLmtMeasInfoRpt inforpt = (wrFLEnbToLmtMeasInfoRpt) cmdObject.getO();

                int measueval = inforpt.getUeMeasValue().GetVal();
                int ver = inforpt.getProtocolVer().GetVal();


                if (Global.ModelType.equals("TDD"))
                {
                    if(measueval>=100)
                    {
                        measueval = 99;
                    }
                }
                else {
                    if(ver == 1)
                    {
                        measueval = measueval - 70;
                        if(measueval<0) measueval = 0;
                    }
                }


                LogPrintManager.Print("info", Tag, String.format("mea val before = %d", measueval));
               // if(measueval>99) measueval=99;  //TODO



                long imsi = ByteToLong(inforpt.getIMSI().ToBytes(), 0, 15);

                MeasUeInfoObject meobj = new MeasUeInfoObject(imsi, measueval);

                issuccess = true;
                errcode = 0;
                msg = "测量值上报";
                obj = meobj;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            //频点配置应答
            case O_FL_ENB_TO_LMT_SYS_ARFCN_ACK:

                statustype = StatusType_BoardC1_ARFCN_ACK;

                wrFLEnbToLmtSysArfcnAck arfckack = (wrFLEnbToLmtSysArfcnAck) cmdObject.getO();
                int cr = arfckack.getCfgResult().GetVal();
                if(cr==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "频点配置成功";
                }
                else {
                    issuccess = false;
                    errcode = cr;
                    msg = "频点配置失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            // 基站状态上报
            case O_FL_ENB_TO_LMT_ENB_STATE_IND:

                statustype = StatusType_BoardC1_STATION_RPT;

                wrFLEnbToLmtEnbStateInd ind = (wrFLEnbToLmtEnbStateInd)cmdObject.getO();
                int val1 = ind.getU32FlEnbStateInd().GetVal();
                obj = null;
                errcode = val1;
                msg = StationRPTintToStr(val1);

                if(val1==1||val1==3||val1==5||val1==7||val1==0xFFFF)
                {
                    issuccess = false;
                }
                else {
                    issuccess = true;
                }


                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;


            //功率设置ack
            case O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK:

                statustype = StatusType_BoardC1_PWR_ACK;

                wrFLEnbToLmtSysPwr1DegreeAck ack = (wrFLEnbToLmtSysPwr1DegreeAck) cmdObject.getO();
                int cfgresult = ack.getCfgResult().GetVal();
                if(cfgresult==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "发射功率配置成功";
                }
                else {
                    issuccess = false;
                    errcode = cfgresult;
                    msg = "发射功率配置失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;

            //增益设置ack
            case O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK:
                statustype = StatusType_BoardC1_GAIN_ACK;

                wrFLEnbToLmtSysRxGainAck gainack = (wrFLEnbToLmtSysRxGainAck) cmdObject.getO();
                int re = gainack.getCfgResult().GetVal();
                if(re==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "接收增益配置成功";
                }
                else {
                    issuccess = false;
                    errcode = re;
                    msg = "接收增益配置失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            //心跳上发
            case O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND:

                statustype = StatusType_BoardC1_HB;

                wrFLEnbToLmtSysInitInformInd hb = (wrFLEnbToLmtSysInitInformInd) cmdObject.getO();

                issuccess = true;
                errcode = 0;
                msg = "收到下位机心跳";


                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;

            // 测量UE配置ack
            case O_FL_ENB_TO_LMT_MEAS_UE_ACK:

                statustype = StatusType_BoardC1_POS_ACK;

                wrFLEnbToLmtMeasUeAck ueack = (wrFLEnbToLmtMeasUeAck) cmdObject.getO();

                int rea = ueack.getCfgResult().GetVal();
                if(rea==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "定位目标配置成功";
                }
                else {
                    issuccess = false;
                    errcode = rea;
                    msg = "定位目标配置失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            //重启ack
            case O_FL_ENB_TO_LMT_REBOOT_ACK:
                statustype = StatusType_BoardC1_REBOOT_ACK;

                wrFLEnbToLmtRebootAck rebootack = (wrFLEnbToLmtRebootAck) cmdObject.getO();

                int r = rebootack.getCfgResult().GetVal();
                if(r==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "重启设备成功";
                }
                else {
                    issuccess = false;
                    errcode = r;
                    msg = "重启设备失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);
                break;

            //小区去激活ack
            case O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK:

                statustype = StatusType_BoardC1_INACTIVE_ACK;

                wrFLEnbToLmtSetAdminStateAck adminstateack = (wrFLEnbToLmtSetAdminStateAck) cmdObject.getO();

                int val = adminstateack.getCfgResult().GetVal();
                if(val==0)
                {
                    issuccess = true;
                    errcode = 0;
                    msg = "收到小区去激活命令";
                }
                else {
                    issuccess = false;
                    errcode = val;
                    msg = "小区去激活失败！";
                }
                obj = null;

                sta = new StatusReport(boardtype, modeltype, statustype, issuccess, errcode, msg, obj);

                break;
            default:

                LogPrintManager.Print("error", Tag, String.format("ParseRecBlockStatus 未定义code = 0x%x\n", cmdObject.getMsgType()));
                break;
        }

        return sta;



    }



}
