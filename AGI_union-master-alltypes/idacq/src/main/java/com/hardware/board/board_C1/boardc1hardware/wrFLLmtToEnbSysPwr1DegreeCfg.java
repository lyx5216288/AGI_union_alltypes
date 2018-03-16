package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.board.board_C1.MacroDef.U8;
import com.hardware.board.board_C1.MacroDef.UArray;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
// 发射功率配置
public class wrFLLmtToEnbSysPwr1DegreeCfg {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 Pwr1Derease;
    U8 IsSave;
    UArray Res;   //3个字节


    public static int GetLength() {

        return wrMsgHeader.GetLength()+4+1+3;
    }

    public int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG;
    }

    public wrFLLmtToEnbSysPwr1DegreeCfg(wrMsgHeader wrmsgHeaderInfo, U32 pwr1Derease, U8 isSave, UArray res) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        Pwr1Derease = pwr1Derease;
        IsSave = isSave;
        Res = res;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(Pwr1Derease.ToBytes());
        a.add(IsSave.ToBytes());
        a.add(Res.ToBytes());

        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;

    }


}
