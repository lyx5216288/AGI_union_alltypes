package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U8;
import com.hardware.board.board_C1.MacroDef.UArray;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */

// DW 定位UE测量值上报
public class wrFLEnbToLmtMeasInfoRpt {
    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U8 UeMeasValue;   // 0....97
    UArray IMSI;   // 17个字节
    //UArray u8Res; //2个字节
    U8 TimingAdv;
    U8 ProtocolVer;


    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_MEAS_INFO_RPT;
    }


    public wrFLEnbToLmtMeasInfoRpt(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(1);
        lenlists.add(17);
        lenlists.add(1);
        lenlists.add(1);



        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;

        this.UeMeasValue = new U8(list.get(i));
        i++;

        this.IMSI = new UArray(list.get(i));
        i++;

        this.TimingAdv = new U8(list.get(i));
        i++;

        this.ProtocolVer = new U8(list.get(i));
        i++;


    }


    public wrMsgHeader getWrmsgHeaderInfo() {
        return WrmsgHeaderInfo;
    }

    public void setWrmsgHeaderInfo(wrMsgHeader wrmsgHeaderInfo) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
    }

    public U8 getUeMeasValue() {
        return UeMeasValue;
    }

    public void setUeMeasValue(U8 ueMeasValue) {
        UeMeasValue = ueMeasValue;
    }

    public UArray getIMSI() {
        return IMSI;
    }

    public void setIMSI(UArray IMSI) {
        this.IMSI = IMSI;
    }


    public U8 getTimingAdv() {
        return TimingAdv;
    }

    public void setTimingAdv(U8 timingAdv) {
        TimingAdv = timingAdv;
    }

    public U8 getProtocolVer() {
        return ProtocolVer;
    }

    public void setProtocolVer(U8 protocolVer) {
        ProtocolVer = protocolVer;
    }
}
