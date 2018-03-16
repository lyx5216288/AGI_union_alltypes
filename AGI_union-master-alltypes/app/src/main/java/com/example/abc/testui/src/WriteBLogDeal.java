package com.example.abc.testui.src;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteBLogDeal {


    public String FanYi(byte[] a_){
        //byte[] a = new byte[0];
        byte[]a;
        a = a_;
        byte[] b = new byte[a.length];
        for(int i=0; i<a.length; i++){

            b[i] = (byte)(a[i] ^ '$');

        }
        try {
            return new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String FanYi(String msg){
        byte[] a = new byte[0];
        try {
            a = msg.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[a.length];
        for(int i=0; i<a.length; i++){

            b[i] = (byte)(a[i] ^ '$');

        }
        try {
            return new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }



    public byte[] GetBytes(String name, long no){


        try{
            SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String dstr = sdf.format(new Date());



            //String format = String.format("ACCOUNT:%s TIME:%s SPECIFIC TASK:MONI TARGET ISDN:%s\r\n", name, dstr, no);

            String format = String.format("用户名:%s|时间:%s|任务类型:定位|目标IMSI:%s|案件描述:测试\r\n", name, dstr, no);

            byte[] a = format.getBytes("UTF-8");
            byte[] b = new byte[a.length];
            for(int i=0; i<a.length; i++){

                b[i] = (byte)(a[i] ^ '$');

            }


            return b;
        }
        catch (Exception e){
            return null;
        }





    }


}
