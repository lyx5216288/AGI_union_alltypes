package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
// DW 测试UE 应答
public class wrFLEnbToLmtMeasUeAck {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 CfgResult;


    public U32 getCfgResult() {
        return CfgResult;
    }

    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_MEAS_UE_ACK;
    }

    public wrFLEnbToLmtMeasUeAck(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(4);


        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;

        this.CfgResult = new U32(list.get(i));
        i++;


    }


}
