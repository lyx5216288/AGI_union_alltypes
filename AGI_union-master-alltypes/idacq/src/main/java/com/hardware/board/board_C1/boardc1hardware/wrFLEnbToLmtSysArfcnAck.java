package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
public class wrFLEnbToLmtSysArfcnAck {

    private wrMsgHeader WrmsgHeaderInfo;
    private U32 CfgResult;


    public wrFLEnbToLmtSysArfcnAck(wrMsgHeader wrmsgHeaderInfo, U32 cfgResult) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
        CfgResult = cfgResult;
    }

    public wrFLEnbToLmtSysArfcnAck(byte[]bytes) {
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

    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_SYS_ARFCN_ACK;
    }

    public byte[] ToBytes(){

        byte[] bytes0 = WrmsgHeaderInfo.ToBytes();
        byte[] bytes1 = CfgResult.ToBytes();


        List<byte[]> a = new ArrayList<>(5);
        a.add(bytes0);
        a.add(bytes1);



        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }


    public U32 getCfgResult() {
        return CfgResult;
    }
}
