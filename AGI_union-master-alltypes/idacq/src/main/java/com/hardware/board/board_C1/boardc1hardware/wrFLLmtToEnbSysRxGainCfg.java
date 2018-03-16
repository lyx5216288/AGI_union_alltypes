package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */

//接收增益配置
public class wrFLLmtToEnbSysRxGainCfg {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 Rxgain;


    public static int GetLength() {
        return wrMsgHeader.GetLength()+4;
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SYS_RxGAIN_CFG;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(Rxgain.ToBytes());

        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;

    }

    public wrFLLmtToEnbSysRxGainCfg(wrMsgHeader wrmsgHeaderInfo, U32 rxgain) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        Rxgain = rxgain;
    }
}
