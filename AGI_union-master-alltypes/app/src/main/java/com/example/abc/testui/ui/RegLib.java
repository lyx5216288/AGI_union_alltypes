package com.example.abc.testui.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegLib {


    private String Rcode_now;   //读出来的注册码

    private String Mcode_now;   //读出来的机器码

    private Date T_now;        //读出来来的当前时间

    private Date T_lastrun;   //读出来的上次程序运行时间


    private Date T_r_start;    //注册码中解析出的的起始时间
    private Date T_r_end;      //注册码中解析出的结束时间
    private String Mcode_r;    //注册码中解析出的机器码


    public RegLib() {
        this.Rcode_now = null;
        this.Mcode_now = null;
        this.T_now = new Date();
        this.T_lastrun = null;
        this.T_r_start = null;
        this.T_r_end = null;
        this.Mcode_r = null;
    }

    public String getRcode_now() {
        return Rcode_now;
    }

    public void setRcode_now(String rcode_now) {
        Rcode_now = rcode_now;
    }

    public String getMcode_now() {
        return Mcode_now;
    }

    public void setMcode_now(String mcode_now) {
        Mcode_now = mcode_now;
    }

    public Date getT_now() {
        return T_now;
    }

    public void setT_now(Date t_now) {
        T_now = t_now;
    }

    public Date getT_lastrun() {
        return T_lastrun;
    }

    public void setT_lastrun(Date t_lastrun) {
        T_lastrun = t_lastrun;
    }



    public String imeiToMcode(long imei){


        long mcode = imei ^ 0x1234567812345678L;


        return mcode+"";

    }

    private Date StrToDate(String str){
        String dateString = str;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            return date;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public String GetRegCode(String mcode, String starttime, String endtime){
        Date stime = StrToDate(starttime);
        Date etime = StrToDate(endtime);

        return GetRegCode(mcode, stime, etime);
    }



    //返回注册码
    public String GetRegCode(String mcode, Date starttime, Date endtime) {


        Rcode_struct reg = new Rcode_struct();
        reg.setMcode(mcode);
        reg.setStarttime(starttime);
        reg.setEndtime(endtime);

        String s = reg.Struct2Str(reg);


        return s;
    }


    public int JudegRCodeValidate(){

        try {
            Rcode_struct struct =  new Rcode_struct();
            boolean ret = struct.JudgeRCodeValidate(this.Rcode_now);

            if(ret){
                return 0;
            }
            else {
                return -1;
            }
        }
        catch (Exception e){
            return -2;
        }


    }

    // 返回 0  通过    负数 失败
    public int Judge(){


        try
        {
            if(T_now.getTime()<T_lastrun.getTime()){
                return -1;    //当前时间小于上次运行时间 时间退出
            }

            Rcode_struct struct =  new Rcode_struct();
            struct = struct.Str2Struct(this.Rcode_now);

            if(struct.getEndtime().getTime()<this.T_now.getTime()){
                return -2;    //过期
            }

            if(struct.getStarttime().getTime()>this.T_now.getTime()){
                return -3;    //还没有到开始使用时间
            }


            if(struct.getMcode().equals(this.Mcode_now)){
                return 0;   //   机器码匹配  成功返回
            }
            else {
                return -4;   //机器码不匹配 返回-4；
            }
        }
        catch (Exception e){
            return -5;
        }




    }

    public String GetRegInfo(){
        if(this.Rcode_now == null){
            return "注册信息为空";
        }

        if(this.JudegRCodeValidate()!=0){
            return "注册信息不合法";
        }

        Rcode_struct struct =  new Rcode_struct();
        struct = struct.Str2Struct(this.Rcode_now);

        Date expiretime = struct.getEndtime();

        String sdate;
        Date ddate = expiretime;

        sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(ddate);

        return "将会在 "+ sdate + " 之后过期";
    }

}
