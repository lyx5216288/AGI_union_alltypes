package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
public class wrFLEnbToLmtSysInitInformInd {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/


    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND;
    }


    public wrFLEnbToLmtSysInitInformInd(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());



        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;




    }

    public wrMsgHeader getWrmsgHeaderInfo() {
        return WrmsgHeaderInfo;
    }
}
