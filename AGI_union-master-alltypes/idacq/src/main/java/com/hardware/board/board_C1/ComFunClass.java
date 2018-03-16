package com.hardware.board.board_C1;

/**
 * Created by john on 2017/7/22.
 */
public class ComFunClass {


    public static String LongToString(long val) {
        String format = String.format("%d", val);
        return format;
    }

    public static long StringToLong(String str) {
        long l = Long.parseLong(str);
        return l;
    }


    public static String ByteToString(byte[]b){
        return new String(b);
    }

    public static long ByteToLong(byte[] bytes, int offset, int length) {
        String a = new String(bytes, offset, length);
        return StringToLong(a);
    }




}
