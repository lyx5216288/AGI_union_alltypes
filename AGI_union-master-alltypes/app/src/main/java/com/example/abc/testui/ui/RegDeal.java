package com.example.abc.testui.ui;

import com.example.abc.testui.RegisterDB;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by usr on 2017/9/18.
 */

public class RegDeal {


    private static String regcodename = "regcode";
    private static String lastruntimename = "lastruntime";

    //public static long lastruntime = 0;

    //public static String regcode = null;


    private static RegisterDB db = new RegisterDB(Global.activity);

    public static int Judge(){

        Global.reglib = new RegLib();

        String mcode = (new RegLib()).imeiToMcode(Global.service.getImei(Global.activity.imeistr));

        Global.reglib.setMcode_now(mcode);

        String regcode = null;

        //RegDeal.SetRegCode("2157da521022168e08f79d505d02020108ef6e31530b05dcc9272f280fba695c");

        regcode = RegDeal.GetRegCode();

//        if(mcode.equals("1312590399402621365")){
//            String s = Global.reglib.GetRegCode(mcode, new Date(), new Date(((new Date()).getTime() + 100 * 24 * 3600 * 1000)));
//
//            RegDeal.SetRegCode(s);
//        }


        if(regcode == null || regcode.trim().equals("")){
            //注册信息为空，自动授权10天吧。

            String zuiouriq = "2030-01-01";


            if((new Date().getTime()<StrToDate(zuiouriq).getTime())){
                String s = Global.reglib.GetRegCode(mcode, new Date(), new Date(((new Date()).getTime() + 10 * 24 * 3600 * 1000)));

                RegDeal.SetRegCode(s);
            }



        }

        Global.reglib.setRcode_now(RegDeal.GetRegCode());

        Global.service.setRegisterMsgandMachineCode(Global.reglib.GetRegInfo(), Global.reglib.getMcode_now());


        Global.reglib.setT_lastrun(new Date(RegDeal.GetLastRunTime()));
        Global.reglib.setT_now(new Date());
        int judge = Global.reglib.Judge();




        //RegDeal.SetLastRunTime((new Date()).getTime());

        return judge;

    }

    public static String GetRegCode(){
        return db.getValue(regcodename);
    }


    public static void SetRegCode(String value){
        db.setValue(regcodename, value);
    }

    public static long GetLastRunTime(){

        String val = db.getValue(lastruntimename);
        if(val == null){
            return 0;
        }

        return  Long.valueOf(val);


    }

    public static void SetLastRunTime(long val){

        db.setValue(lastruntimename, val+"");

    }

    public static void Init(){
        Global.reglib = new RegLib();

        String mcode = (new RegLib()).imeiToMcode(Global.service.getImei(Global.activity.imeistr));

        Global.reglib.setMcode_now(mcode);

        String regcode = null;

        //RegDeal.SetRegCode("2157da521022168e08f79d505d02020108ef6e31530b05dcc9272f280fba695c");

        regcode = RegDeal.GetRegCode();

//        if(mcode.equals("1312590399402621365")){
//            String s = Global.reglib.GetRegCode(mcode, new Date(), new Date(((new Date()).getTime() + 100 * 24 * 3600 * 1000)));
//
//            RegDeal.SetRegCode(s);
//        }


        if(regcode == null || regcode.trim().equals("")){
            //注册信息为空，自动授权10天吧。

            String zuiouriq = "2030-01-01";


            if((new Date().getTime()<StrToDate(zuiouriq).getTime())){
                String s = Global.reglib.GetRegCode(mcode, new Date(), new Date(((new Date()).getTime() + 10 * 24 * 3600 * 1000)));

                RegDeal.SetRegCode(s);
            }



        }

        Global.reglib.setRcode_now(RegDeal.GetRegCode());

        Global.service.setRegisterMsgandMachineCode(Global.reglib.GetRegInfo(), Global.reglib.getMcode_now());
    }

    private static Date StrToDate(String str){
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




    // 返回0 通过    返回1  请弹出注册提示框




}
