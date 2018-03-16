package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.ConstructVal;
import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.board.board_C1.MacroDef.U8;
import com.hardware.board.board_C1.MacroDef.UArray;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/9/19.
 */

public class wrFLLmtToEnbSysArfcnMod {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 sysUlARFCN;/*上行频点*/
    U32 sysDlARFCN;/*下行频点*/
    UArray PLMN; /*plmn str,   eg: “46001”*/ // 7个字节

    U8  sysBand;/*频段:Band38/band39/band40*/


    U32 CellId;
    U32  UePMax;/*<=23dBm*/


    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SYS_ARFCN_MOD;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength()+4+4+7+1+4+4;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(sysUlARFCN.ToBytes());
        a.add(sysDlARFCN.ToBytes());
        a.add(PLMN.ToBytes());

        a.add(sysBand.ToBytes());


        a.add(CellId.ToBytes());
        a.add(UePMax.ToBytes());



        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }

    public wrFLLmtToEnbSysArfcnMod(wrMsgHeader wrmsgHeaderInfo, U32 sysUlARFCN, U32 sysDlARFCN, UArray PLMN, U8 sysBand, U32 cellId, U32 uePMax) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        this.sysUlARFCN = sysUlARFCN;
        this.sysDlARFCN = sysDlARFCN;
        this.PLMN = PLMN;

        this.sysBand = sysBand;

        CellId = cellId;
        UePMax = uePMax;

    }

    public wrFLLmtToEnbSysArfcnMod(byte[] bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(4);
        lenlists.add(4);
        lenlists.add(7);
        lenlists.add(1);
        lenlists.add(4);
        lenlists.add(4);


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



        this.sysBand = (U8) ConstructVal.GetVal(list.get(i), sysBand.GetLength());
        i++;



        CellId = (U32) ConstructVal.GetVal(list.get(i), CellId.GetLength());
        i++;

        UePMax = (U32) ConstructVal.GetVal(list.get(i), UePMax.GetLength());
        i++;




    }

}
