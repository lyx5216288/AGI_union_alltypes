package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/10/17.
 */

public class CellStateQuery {
    public wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/

    public CellStateQuery() {
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_CELL_STATE_INFO_QUERY;
    }

    public static int GetLength(){
        return wrMsgHeader.GetLength();
    }


    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());



        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }
}
