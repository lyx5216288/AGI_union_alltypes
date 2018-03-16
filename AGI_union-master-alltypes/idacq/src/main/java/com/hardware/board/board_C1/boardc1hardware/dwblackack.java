package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
//黑名单配置应答
public class dwblackack {
    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 CfgResult;


    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_CFG_ACK;

    }

    public dwblackack(byte[]bytes) {
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
