package com.hardware.board.board_C1.boardc1hardware.freqscan;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.board.board_C1.boardc1hardware.*;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

import static com.hardware.board.board_C1.boardc1hardware.Constants_C1.*;

/**
 * Created by usr on 2017/10/9.
 */

public class wrFLLmtToEnbScanCellInfoCfg {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 wholeBandRem;
    U32 sysArfcnNum;
    U32[] sysArfcn;

    public wrFLLmtToEnbScanCellInfoCfg() {
        sysArfcn = new U32[C_MAX_REM_ARFCN_NUM];
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_REM_CFG ;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength()+4+4+4*C_MAX_REM_ARFCN_NUM;
    }

    public wrFLLmtToEnbScanCellInfoCfg(wrMsgHeader wrmsgHeaderInfo, U32 wholeBandRem, U32 sysArfcnNum, U32[] sysArfcn) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        this.wholeBandRem = wholeBandRem;
        this.sysArfcnNum = sysArfcnNum;
        this.sysArfcn = sysArfcn;
    }



    public byte[] ToBytes(){

        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(wholeBandRem.ToBytes());
        a.add(sysArfcnNum.ToBytes());
        for(int i = 0; i<C_MAX_REM_ARFCN_NUM; i++){
            a.add(sysArfcn[i].ToBytes());
        }

        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;

    }


}
