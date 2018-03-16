package com.hardware.board.board_C1;

/**
 * Created by john on 2017/7/23.
 */

//这个类填充到
public class MeasUeInfoObject {

    private long imsi;
    private int measval;


    public MeasUeInfoObject(long imsi, int measval) {
        this.imsi = imsi;
        this.measval = measval;
    }

    public long getImsi() {
        return imsi;
    }

    public void setImsi(long imsi) {
        this.imsi = imsi;
    }

    public int getMeasval() {
        return measval;
    }

    public void setMeasval(int measval) {
        this.measval = measval;
    }
}
