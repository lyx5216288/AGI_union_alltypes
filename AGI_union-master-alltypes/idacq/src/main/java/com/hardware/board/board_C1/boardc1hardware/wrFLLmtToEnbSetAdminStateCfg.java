package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */

//小区激活去激活配置
public class wrFLLmtToEnbSetAdminStateCfg {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    U32 workAdminState;


    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SET_ADMIN_STATE_CFG;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength()+4;
    }

    public wrFLLmtToEnbSetAdminStateCfg(wrMsgHeader wrmsgHeaderInfo, U32 workAdminState) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        this.workAdminState = workAdminState;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());
        a.add(workAdminState.ToBytes());



        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }


}
