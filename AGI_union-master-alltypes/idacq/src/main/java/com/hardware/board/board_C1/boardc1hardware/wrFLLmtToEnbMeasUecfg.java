package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U8;
import com.hardware.board.board_C1.MacroDef.UArray;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
//DW 测量 UE 配置
public class wrFLLmtToEnbMeasUecfg {


    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U8 u8WorkMode;
    UArray u8Res3;  //3个字节


    UArray IMSI; // 17个字节
    U8 u8MeasReportPeriod;
    U8 SchdUeMaxPowerTxFlag;
    U8 SchdUeMaxPowerValue;
    U8 SchdUeUIFixedPrbSwitch;
    U8 CampOnAllowedFlag;
    UArray u8Res2; //2个字节


    public static int GetLength(){
        return wrMsgHeader.GetLength()+1+3+17+5+2;
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_MEAS_UE_CFG;
    }

    public wrFLLmtToEnbMeasUecfg(wrMsgHeader wrmsgHeaderInfo, U8 u8WorkMode, UArray u8Res3, UArray IMSI, U8 u8MeasReportPeriod, U8 schdUeMaxPowerTxFlag, U8 schdUeMaxPowerValue, U8 schdUeUIFixedPrbSwitch, U8 campOnAllowedFlag, UArray u8Res2) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        this.u8WorkMode = u8WorkMode;
        this.u8Res3 = u8Res3;
        this.IMSI = IMSI;
        this.u8MeasReportPeriod = u8MeasReportPeriod;
        SchdUeMaxPowerTxFlag = schdUeMaxPowerTxFlag;
        SchdUeMaxPowerValue = schdUeMaxPowerValue;
        SchdUeUIFixedPrbSwitch = schdUeUIFixedPrbSwitch;
        CampOnAllowedFlag = campOnAllowedFlag;
        this.u8Res2 = u8Res2;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(u8WorkMode.ToBytes());
        a.add(u8Res3.ToBytes());
        a.add(IMSI.ToBytes());
        a.add(u8MeasReportPeriod.ToBytes());
        a.add(SchdUeMaxPowerTxFlag.ToBytes());

        a.add(SchdUeMaxPowerValue.ToBytes());
        a.add(SchdUeUIFixedPrbSwitch.ToBytes());
        a.add(CampOnAllowedFlag.ToBytes());
        a.add(u8Res2.ToBytes());



        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }

}
