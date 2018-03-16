package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/7/16.
 */
public class wrFLLmtToEnbSysArfcnCfg {
    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 sysUlARFCN;/*上行频点*/
    public U32 sysDlARFCN;/*下行频点*/
    UArray PLMN; /*plmn str,   eg: “46001”*/ // 7个字节
    U8 sysBandwidth; /*wrFLBandwidth*/
    U32  sysBand;/*频段:Band38/band39/band40*/
    public U16  PCI;/*0~503*/
    U16  TAC;
    U32 CellId;
    U16  UePMax;/*<=23dBm*/
    U16 EnodeBPMax;

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SYS_ARFCN_CFG;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength()+4+4+7+1+4+2+2+4+2+2;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(sysUlARFCN.ToBytes());
        a.add(sysDlARFCN.ToBytes());
        a.add(PLMN.ToBytes());
        a.add(sysBandwidth.ToBytes());
        a.add(sysBand.ToBytes());

        a.add(PCI.ToBytes());
        a.add(TAC.ToBytes());
        a.add(CellId.ToBytes());
        a.add(UePMax.ToBytes());
        a.add(EnodeBPMax.ToBytes());


        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }

    public wrFLLmtToEnbSysArfcnCfg(wrMsgHeader wrmsgHeaderInfo, U32 sysUlARFCN, U32 sysDlARFCN, UArray PLMN, U8 sysBandwidth, U32 sysBand, U16 PCI, U16 TAC, U32 cellId, U16 uePMax, U16 enodeBPMax) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        this.sysUlARFCN = sysUlARFCN;
        this.sysDlARFCN = sysDlARFCN;
        this.PLMN = PLMN;
        this.sysBandwidth = sysBandwidth;
        this.sysBand = sysBand;
        this.PCI = PCI;
        this.TAC = TAC;
        CellId = cellId;
        UePMax = uePMax;
        EnodeBPMax = enodeBPMax;
    }

    public wrFLLmtToEnbSysArfcnCfg(byte[] bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(4);
        lenlists.add(4);
        lenlists.add(7);
        lenlists.add(1);
        lenlists.add(4);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(4);
        lenlists.add(2);
        lenlists.add(2);

        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;

        this.sysUlARFCN = new U32(list.get(i));
        i++;

        this.sysDlARFCN = (U32) ConstructVal.GetVal(list.get(i), 4);
        i++;

        this.PLMN = new UArray(list.get(i));
        i++;

        this.sysBandwidth = (U8) ConstructVal.GetVal(list.get(i), 1);
        i++;

        this.sysBand = (U32) ConstructVal.GetVal(list.get(i), 4);
        i++;

        this.PCI = (U16) ConstructVal.GetVal(list.get(i), 2);
        i++;

        this.TAC = (U16) ConstructVal.GetVal(list.get(i), 2);
        i++;

        CellId = (U32) ConstructVal.GetVal(list.get(i), 4);
        i++;

        UePMax = (U16) ConstructVal.GetVal(list.get(i), 2);
        i++;

        EnodeBPMax = (U16) ConstructVal.GetVal(list.get(i), 2);
        i++;


    }



}
