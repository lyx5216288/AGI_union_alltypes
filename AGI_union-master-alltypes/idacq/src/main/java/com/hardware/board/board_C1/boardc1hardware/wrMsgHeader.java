package com.hardware.board.board_C1.boardc1hardware;

import com.hardware.board.board_C1.MacroDef.U16;
import com.hardware.board.board_C1.MacroDef.U32;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/7/16.
 */
public class wrMsgHeader {


    private U32 u32FrameHeader;/*0x5555AAAA*/
    private U16 u16MsgType;/*定义消息的类型名称*/
    private U16 u16MsgLength;/*定义消息的长度*/
    private U16 u16frame;/*系统工作模式，FDD: 0xFF00, TDD: 0x00FF*/
    private U16 u16SubSysCode;/*定义消息的产生的子系统编号*/




    public static int GetLength() {
        return 4+2+2+2+2;
    }

    public wrMsgHeader() {
    }

//    public wrMsgHeader(byte[] bytes, int len) {
//        byte[] n = new byte[len];
//        System.arraycopy(bytes, 0, n, 0, len);
//        this(n);
//    }


    public wrMsgHeader(byte[] bytes) {

        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(4);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(2);

        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        u32FrameHeader = new U32(list.get(0));
        u16MsgType = new U16(list.get(1));
        u16MsgLength = new U16(list.get(2));
        u16frame = new U16(list.get(3));
        u16SubSysCode = new U16(list.get(4));


    }


    public wrMsgHeader(U32 u32FrameHeader, U16 u16MsgType, U16 u16MsgLength, U16 u16frame, U16 u16SubSysCode) {
        this.u32FrameHeader = u32FrameHeader;
        this.u16MsgType = u16MsgType;
        this.u16MsgLength = u16MsgLength;
        this.u16frame = u16frame;
        this.u16SubSysCode = u16SubSysCode;
    }


    public U32 getU32FrameHeader() {
        return u32FrameHeader;
    }

    public void setU32FrameHeader(U32 u32FrameHeader) {
        this.u32FrameHeader = u32FrameHeader;
    }

    public U16 getU16MsgType() {
        return u16MsgType;
    }

    public void setU16MsgType(U16 u16MsgType) {
        this.u16MsgType = u16MsgType;
    }

    public U16 getU16MsgLength() {
        return u16MsgLength;
    }

    public void setU16MsgLength(U16 u16MsgLength) {
        this.u16MsgLength = u16MsgLength;
    }

    public U16 getU16frame() {
        return u16frame;
    }

    public void setU16frame(U16 u16frame) {
        this.u16frame = u16frame;
    }

    public U16 getU16SubSysCode() {
        return u16SubSysCode;
    }

    public void setU16SubSysCode(U16 u16SubSysCode) {
        this.u16SubSysCode = u16SubSysCode;
    }

    public byte[] ToBytes(){

        byte[] bytes0 = u32FrameHeader.ToBytes();
        byte[] bytes1 = u16MsgType.ToBytes();
        byte[] bytes2 = u16MsgLength.ToBytes();
        byte[] bytes3 = u16frame.ToBytes();
        byte[] bytes4 = u16SubSysCode.ToBytes();

        List<byte[]> a = new ArrayList<>(5);
        a.add(bytes0);
        a.add(bytes1);
        a.add(bytes2);
        a.add(bytes3);
        a.add(bytes4);

        //byte[] ret = new byte[bytes0.length + bytes1.length + bytes2.length + bytes3.length + bytes4.length];
        byte[] ret = ByteOper.ByteMergeList(a);



        return ret;

    }




}
