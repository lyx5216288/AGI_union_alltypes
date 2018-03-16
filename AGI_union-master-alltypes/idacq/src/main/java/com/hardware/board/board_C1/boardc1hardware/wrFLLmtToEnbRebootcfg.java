package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/19.
 */
public class wrFLLmtToEnbRebootcfg {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_REBOOT_CFG;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength();
    }

    public wrFLLmtToEnbRebootcfg(wrMsgHeader wrmsgHeaderInfo) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());




        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }


}
