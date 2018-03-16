package com.hardware.board.board_C2;

/**
 * Created by usr on 2017/7/13.
 */
public class ComFunClass {


    public static void test1(){
        String str = "460028518137521";
        byte[] bytes = StringToByte8imsi(str);

        for(int i = 0; i<bytes.length; i++){
            System.out.println(Integer.toHexString(bytes[i]));
        }

    }


    public static void test2(){
        byte []imsi={0x49, 0x06, 0x20, 0x58, (byte)0x81,0x31,0x57,0x12};

        String s = ComFunClass.Byte8ToString(imsi);

        System.out.println(s);
    }


    public static long Byte8ToLong(byte[] bytes) {
        long ret = 0;

        for(int i=0; i<15; i++)
        {
            ret =  ret * 10;
            ret = ret + (bytes[i]-'0');

        }
        return ret;
    }


    public static String Byte8ToString(byte[] bytes) {

        byte[] abc = new byte[15];

        abc[0] = (byte)(bytes[0] >> 4);
        for(int i=1; i<8;i++){
            abc[i*2-1] = (byte)(bytes[i] & 0x0F);
            abc[i*2] = (byte)((bytes[i] >> 4)& 0x0F);
        }

        char[]abcstr = new char[15];
        for(int i=0; i<15; i++){
            abcstr[i] = (char)(abc[i] + '0');
        }
        return new String(abcstr);

    }


    public static byte[] StringToByte8imsi(String str){
        byte[] abc= new byte[15];
        for(int i=0; i<15; i++){
            abc[i] = (byte)(str.charAt(i) - (byte)'0');
        }

        byte[] ret = new byte[8];

        ret[0] = (byte)(abc[0] << 4 | 0x09);
        for(int i=1; i<8; i++){
            ret[i] = (byte)(abc[2*i] << 4 | abc[2*i-1]);
        }

        return ret;


    }


    public static byte[] StringToByte8imei(String str){
        byte[] abc= new byte[15];
        for(int i=0; i<15; i++){
            abc[i] = (byte)(str.charAt(i) - (byte)'0');
        }

        byte[] ret = new byte[8];

        ret[0] = (byte)(abc[0] << 4 | 0x0a);
        for(int i=1; i<8; i++){
            ret[i] = (byte)(abc[2*i] << 4 | abc[2*i-1]);
        }

        return ret;


    }
}
