package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
public class wrFLEnbToLmtUeInfoRpt {

    private wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    private U32 UeIdType;  /* 0:IMSI  1:IMEI  2:BOTH */
    private UArray IMSI;   // 17字节
    private UArray IMEI;   // 17字节
    private U8 Rssi;  // 1个字节
    private U8 u8Res;  // 1个字节

    public static int GetLength() {
        return wrMsgHeader.GetLength()+4+17+17+2;
    }



    public wrFLEnbToLmtUeInfoRpt(wrMsgHeader wrmsgHeaderInfo, U32 ueIdType, UArray IMSI, UArray IMEI, U8 rssi, U8 u8Res) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        UeIdType = ueIdType;
        this.IMSI = IMSI;
        this.IMEI = IMEI;
        Rssi = rssi;
        this.u8Res = u8Res;
    }

    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_UE_INFO_RPT;
    }

    public wrFLEnbToLmtUeInfoRpt(byte[] bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(4);
        lenlists.add(17);
        lenlists.add(17);
        lenlists.add(1);
        lenlists.add(1);


        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;

        this.UeIdType = new U32(list.get(i));
        i++;

        this.IMSI = new UArray(list.get(i));
        i++;

        this.IMEI = new UArray(list.get(i));
        i++;

        this.Rssi = new U8(list.get(i));
        i++;

        this.u8Res = new U8(list.get(i));
        i++;

    }


    public wrMsgHeader getWrmsgHeaderInfo() {
        return WrmsgHeaderInfo;
    }

    public void setWrmsgHeaderInfo(wrMsgHeader wrmsgHeaderInfo) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
    }

    public U32 getUeIdType() {
        return UeIdType;
    }

    public void setUeIdType(U32 ueIdType) {
        UeIdType = ueIdType;
    }

    public UArray getIMSI() {
        return IMSI;
    }

    public void setIMSI(UArray IMSI) {
        this.IMSI = IMSI;
    }

    public UArray getIMEI() {
        return IMEI;
    }

    public void setIMEI(UArray IMEI) {
        this.IMEI = IMEI;
    }

    public U8 getRssi() {
        return Rssi;
    }

    public void setRssi(U8 rssi) {
        Rssi = rssi;
    }

    public U8 getU8Res() {
        return u8Res;
    }

    public void setU8Res(U8 u8Res) {
        this.u8Res = u8Res;
    }
}
