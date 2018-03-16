package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.UArrayArray;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */

//黑名单配置
public class dwblackcfg {
    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U16 BlackUENum;
    UArrayArray BlackUEIdentity; //50*17字节


    public int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_CFG;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(BlackUENum.ToBytes());
        a.add(BlackUEIdentity.ToBytes());


        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;

    }

}
