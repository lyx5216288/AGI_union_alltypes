package com.adapter.CommonClass;

/**
 * Created by john on 2017/7/18.
 */
public class StreamReport {

    private int boardtype;
    private int modetype;

    private int index;

    private long imei;   // 0代表无意义
    private long imsi;   // 0代表无意义
    private int rssi;
    private boolean istarget;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

//    public StreamReport(int boardtype, int modetype, long imei, long imsi, boolean istarget) {
//        this.boardtype = boardtype;
//        this.modetype = modetype;
//        this.imei = imei;
//        this.imsi = imsi;
//        this.istarget = istarget;
//    }

    public StreamReport(int boardtype, int modetype, long imei, long imsi, int rssi, boolean istarget) {
        this.boardtype = boardtype;
        this.modetype = modetype;

        this.imei = imei;
        this.imsi = imsi;
        this.rssi = rssi;
        this.istarget = istarget;
    }

    public int getBoardtype() {
        return boardtype;
    }

    public void setBoardtype(int boardtype) {
        this.boardtype = boardtype;
    }

    public int getModetype() {
        return modetype;
    }

    public void setModetype(int modetype) {
        this.modetype = modetype;
    }

    public long getImei() {
        return imei;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public long getImsi() {
        return imsi;
    }

    public void setImsi(long imsi) {
        this.imsi = imsi;
    }

    public boolean isIstarget() {
        return istarget;
    }

    public void setIstarget(boolean istarget) {
        this.istarget = istarget;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
