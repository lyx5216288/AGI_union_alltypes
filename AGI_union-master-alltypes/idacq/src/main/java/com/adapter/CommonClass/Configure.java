package com.adapter.CommonClass;

/**
 * Created by john on 2017/7/18.
 */
public class Configure {


    //private int boardtype;
    //private int modetype;

    int index;
    Object obj;

    public Configure(int index, Object obj) {
        this.index = index;
        this.obj = obj;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    //    public Configure(int boardtype, int modetype, Object obj) {
//        this.boardtype = boardtype;
//        this.modetype = modetype;
//        this.obj = obj;
//    }
//
//    public int getBoardtype() {
//        return boardtype;
//    }
//
//    public void setBoardtype(int boardtype) {
//        this.boardtype = boardtype;
//    }
//
//    public int getModetype() {
//        return modetype;
//    }
//
//    public void setModetype(int modetype) {
//        this.modetype = modetype;
//    }
//
//    public Object getObj() {
//        return obj;
//    }
//
//    public void setObj(Object obj) {
//        this.obj = obj;
//    }
}
