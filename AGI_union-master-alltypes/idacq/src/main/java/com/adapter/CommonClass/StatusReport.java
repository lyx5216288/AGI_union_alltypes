package com.adapter.CommonClass;

/**
 * Created by john on 2017/7/18.
 */
public class StatusReport {

    private int boardtype;
    private int modeltype;
    private int statustype;

    int index;

    private boolean issuccess;

    private int errcode;
    private String msg;

    private Object obj;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public StatusReport(int boardtype, int modeltype, int statustype, boolean issuccess, int errcode, String msg, Object obj) {
        this.boardtype = boardtype;
        this.modeltype = modeltype;
        this.statustype = statustype;
        this.issuccess = issuccess;
        this.errcode = errcode;
        this.msg = msg;
        this.obj = obj;
    }



    public int getBoardtype() {
        return boardtype;
    }

    public void setBoardtype(int boardtype) {
        this.boardtype = boardtype;
    }

    public int getModeltype() {
        return modeltype;
    }

    public void setModeltype(int modeltype) {
        this.modeltype = modeltype;
    }

    public boolean isIssuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public int getStatustype() {
        return statustype;
    }

    public void setStatustype(int statustype) {
        this.statustype = statustype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
