package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/10/17.
 */

public class CellStateAck {


    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    public U32 CellState;   // 3 小区已激活

    public int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK;
    }

    public static int GetLength(){
        return wrMsgHeader.GetLength()+4;
    }

    public CellStateAck(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();

        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(4);


        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;

        this.CellState = new U32(list.get(i));
        i++;


    }

}
