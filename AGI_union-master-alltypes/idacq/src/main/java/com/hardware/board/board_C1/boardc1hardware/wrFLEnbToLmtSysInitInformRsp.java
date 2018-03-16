package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/17.
 */

//
public class wrFLEnbToLmtSysInitInformRsp {

    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/

    public static int GetMsgType() {
        return Constants_C1.O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP;
    }

    public static int GetLength() {

        return wrMsgHeader.GetLength();
    }

    public wrFLEnbToLmtSysInitInformRsp(wrMsgHeader wrmsgHeaderInfo) {
        WrmsgHeaderInfo = wrmsgHeaderInfo;
    }

    public byte[] ToBytes(){


        List<byte[]> a = new ArrayList<>();

        a.add(WrmsgHeaderInfo.ToBytes());

        byte[] ret = ByteOper.ByteMergeList(a);

        return ret;

    }

    public wrFLEnbToLmtSysInitInformRsp(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());


        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        WrmsgHeaderInfo = new wrMsgHeader(list.get(i));
        i++;



    }


    public static wrFLEnbToLmtSysInitInformRsp GetDefault(){

        U32 u32FrameHeader = new U32(0x5555AAAA);/*0x5555AAAA*/
        U16 u16MsgType = new U16((short)wrFLEnbToLmtSysInitInformRsp.GetMsgType());/*定义消息的类型名称*/
        U16 u16MsgLength = new U16((short)wrFLEnbToLmtSysInitInformRsp.GetLength());;/*定义消息的长度*/
        U16 u16frame = new U16((short)0x00);;/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
        U16 u16SubSysCode= new U16((short)0xFF00);;/*定义消息的产生的子系统编号*/


        wrMsgHeader header = new wrMsgHeader(u32FrameHeader, u16MsgType, u16MsgLength, u16frame, u16SubSysCode);

        return new wrFLEnbToLmtSysInitInformRsp(header);

    }

}
