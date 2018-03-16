package com.adapter.Parameter;

import java.util.List;

/**
 * Created by john on 2017/7/18.
 */

// 这个是作为Configure最后那个Obj存在的
public class BoardC1Set {

    private int earfcn;  //下拉菜单列表+文本框，用户可选择38400、37900、38950、40936、38544、38098
    //该值取值有四个区间，37750~38249、38250~38649、38650~39649、39650~41589，如果用户输入数据不在上述区间，弹出错误


    private int pci;  //文本框，默认88，数值在0-503之间

    private int PLMN;  // 下拉菜单列表+文本框，中国移动(46000)、中国联通(46001)、中国电信(46003)

    private long targetnum; //定位目标 文本框，15位数字，例如460028518137521

    private int powerval; //下拉菜单+文本框，分别是5档(0)、4档(12)、3档(24)、2档(36)、1档(48)

    private int gain; // 下拉菜单+文本框，分别是高(90)、中(52)、低(20)

    private int tac;
    private int cellid;

    private int event;   // 参考 BoardC1SetEnum 枚举

    private long[] blacklist;  // 最多50个

    public BoardC1Set() {
    }

    public BoardC1Set(int earfcn, int pci, int PLMN, long targetnum, int powerval, int gain, int tac, int cellid, int event, long[] blacklist) {
        this.earfcn = earfcn;
        this.pci = pci;
        this.PLMN = PLMN;
        this.targetnum = targetnum;
        this.powerval = powerval;
        this.gain = gain;
        this.tac = tac;
        this.cellid = cellid;
        this.event = event;
        this.blacklist = blacklist;
    }

    public int getTac() {
        return tac;
    }

    public void setTac(int tac) {
        this.tac = tac;
    }

    public int getCellid() {
        return cellid;
    }

    public void setCellid(int cellid) {
        this.cellid = cellid;
    }

    public int getEarfcn() {
        return earfcn;
    }

    public void setEarfcn(int earfcn) {
        this.earfcn = earfcn;
    }

    public int getPci() {
        return pci;
    }

    public void setPci(int pci) {
        this.pci = pci;
    }

    public int getPLMN() {
        return PLMN;
    }

    public void setPLMN(int PLMN) {
        this.PLMN = PLMN;
    }

    public long getTargetnum() {
        return targetnum;
    }

    public void setTargetnum(long targetnum) {
        this.targetnum = targetnum;
    }

    public int getPowerval() {
        return powerval;
    }

    public void setPowerval(int powerval) {
        this.powerval = powerval;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public long[] getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(long[] blacklist) {
        this.blacklist = blacklist;
    }
}
