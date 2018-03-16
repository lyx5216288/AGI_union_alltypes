package com.example.abc.testui;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by abc on 2017/9/3.
 */

public class RealTimeDatas implements Parcelable {
    private int sequence;
    private long imsi;
    private int isTarget;
    private int count;
    private Date beginDate;
    private Date endDate;
    private int rssi;

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return sequence;
    }

    public void setImsi(long imsi) {
        this.imsi = imsi;
    }

    public long getImsi() {
        return imsi;
    }

    public void setIsTarget(int isTarget) {
        this.isTarget = isTarget;
    }

    public int getIsTarget() {
        return isTarget;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putInt("sequence", sequence);
        b.putLong("imsi", imsi);
        b.putInt("count", count);
        b.putSerializable("beginDate", beginDate);
        b.putSerializable("endDate", endDate);
        b.putInt("rssi", rssi);
        dest.writeBundle(b);
    }

    public static final Creator<RealTimeDatas> CREATOR = new Creator<RealTimeDatas>() {
        @Override
        public RealTimeDatas createFromParcel(Parcel source) {
            RealTimeDatas realTimeDatas = new RealTimeDatas();

            Bundle b = source.readBundle();
            realTimeDatas.setSequence(b.getInt("sequence"));
            realTimeDatas.setImsi(b.getLong("imsi"));
            realTimeDatas.setCount(b.getInt("count"));
            realTimeDatas.setBeginDate((Date)b.getSerializable("beginDate"));
            realTimeDatas.setEndDate((Date)b.getSerializable("endDate"));
            realTimeDatas.setRssi(b.getInt("rssi"));

            return realTimeDatas;
        }

        @Override
        public RealTimeDatas[] newArray(int size) {
            return new RealTimeDatas[size];
        }
    };
}
