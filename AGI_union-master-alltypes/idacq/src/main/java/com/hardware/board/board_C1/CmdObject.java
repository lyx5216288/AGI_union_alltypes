package com.hardware.board.board_C1;

/**
 * Created by john on 2017/7/17.
 */

//这里类里面放置了命令相关的类
public class CmdObject {



    private int MsgType;
    private Object o;


    public CmdObject(int msgType, Object o) {
        MsgType = msgType;
        this.o = o;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
