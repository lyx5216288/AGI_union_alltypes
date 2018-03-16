package com.example.abc.testui;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abc on 2017/9/3.
 */

public class ParamSetValue implements Parcelable {
    private int plmn;
    private int earfcn;
    private int pci;
    private int tac;
    private int cellId;
    private int power;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putInt("plmn", plmn);
        b.putInt("earfcn", earfcn);
        b.putInt("pci", pci);
        b.putInt("tac", tac);
        b.putInt("cellId", cellId);
        b.putInt("power", power);
        dest.writeBundle(b);
    }

    public static final Creator<ParamSetValue> CREATOR = new Creator<ParamSetValue>() {
        @Override
        public ParamSetValue createFromParcel(Parcel source) {
            Bundle b = source.readBundle();

            ParamSetValue p = new ParamSetValue();
            p.setPlmn(b.getInt("plmn"));
            p.setEarfcn(b.getInt("earfcn"));
            p.setPci(b.getInt("pci"));
            p.setTac(b.getInt("tac"));
            p.setCellId(b.getInt("cellId"));
            p.setPower(b.getInt("power"));
            return p;
        }

        @Override
        public ParamSetValue[] newArray(int size) {
            return new ParamSetValue[size];
        }
    };

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public void setEarfcn(int earfcn) {
        this.earfcn = earfcn;
    }

    public void setPci(int pci) {
        this.pci = pci;
    }

    public void setPlmn(int plmn) {
        this.plmn = plmn;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setTac(int tac) {
        this.tac = tac;
    }

    public int getCellId() {
        return cellId;
    }

    public int getEarfcn() {
        return earfcn;
    }

    public int getPci() {
        return pci;
    }

    public int getPlmn() {
        return plmn;
    }

    public int getPower() {
        return power;
    }

    public int getTac() {
        return tac;
    }
}
