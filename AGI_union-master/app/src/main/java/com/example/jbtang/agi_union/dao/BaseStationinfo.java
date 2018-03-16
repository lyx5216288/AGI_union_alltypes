package com.example.jbtang.agi_union.dao;

/**
 * Created by 刘洋旭 on 2016/11/29.
 */
public class BaseStationinfo {
    public int errcode;
    public String lat;
    public String lon;
    public String address;
    public String pci;
    public String tai;
    public String earfcn;
    public String ecgi;

    public String getPci() {
        return pci;
    }

    public void setPci(String pci) {
        this.pci = pci;
    }

    public String getTai() {
        return tai;
    }

    public void setTai(String tai) {
        this.tai = tai;
    }

    public String getEarfcn() {
        return earfcn;
    }

    public void setEarfcn(String earfcn) {
        this.earfcn = earfcn;
    }

    public String getEcgi() {
        return ecgi;
    }

    public void setEcgi(String ecgi) {
        this.ecgi = ecgi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
