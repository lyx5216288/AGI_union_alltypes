package com.hardware.board.board_C1.boardc1hardware.querycell;

import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.board.board_C1.boardc1hardware.Constants_C1;
import com.hardware.board.board_C1.boardc1hardware.wrFLLmtToEnbSysArfcnCfg;
import com.hardware.board.board_C1.boardc1hardware.wrMsgHeader;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/11/2.
 */

public class wrFLLmtToEnbGetEnbState {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/

    public wrFLLmtToEnbGetEnbState() {

        U32 u32FrameHeader = new U32(Constants_C1.FrameHeader);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short) wrFLLmtToEnbGetEnbState.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLLmtToEnbGetEnbState.GetLength());/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);/*定义消息的产生的子系统编号*/

        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType,
                u16MsgLength, u16frame, u16SubSysCode);

        WrmsgHeaderInfo = header;

    }

    public static int GetLength(){
        return wrMsgHeader.GetLength();
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_GET_ARFCN;
    }

    public byte[] ToBytes(){

        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());

        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;
    }

}
