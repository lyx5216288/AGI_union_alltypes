package com.example.jbtang.agi_union.dao.FindSTMSIInfos;

/**
 * Created by ai on 16/6/14.
 */
public class FindeSTMSIInfoDAO {
    public String stmsi;
    public String count;
    public String pagingcount;
    public String time;
    public String pci;
    public String earfcn;
    public String ecgi;
    public String doubtful;
    private FindeSTMSIInfoDAO(){
        this.stmsi = "";
        this.count = "";
        this.pagingcount = "";
        this.time = "";
        this.pci = "";
        this.earfcn = "";
        this.ecgi = "";
        this.doubtful = "false";
    }
    public FindeSTMSIInfoDAO(String stmsi,String pagingcount, String count, String time, String pci, String earfcn, String ecgi, String doubtful){
        this.stmsi = stmsi;
        this.count = count;
        this.pagingcount = pagingcount;
        this.time = time;
        this.pci = pci;
        this.earfcn = earfcn;
        this.ecgi = ecgi;
        this.doubtful = doubtful;
    }
}
