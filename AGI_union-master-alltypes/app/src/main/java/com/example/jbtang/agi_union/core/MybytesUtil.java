package com.example.jbtang.agi_union.core;

import com.example.jbtang.agi_union.core.type.U8;

/**
 * Created by 刘洋旭 on 2017/8/7.
 */
public class MybytesUtil {
    private static final char[] DIGITS={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    public static int toUnsigned(byte b){
        return b<0?b+256:b;
    }
    private static int toUnsigned(byte[] bytes) {
        return (bytes[0] & 0xFF) |
                ((bytes[1] & 0xFF) << 8);
    }
    public static String toBinaryString(byte b){
        int u = toUnsigned(b);
        return new String(new char[]{
                DIGITS[(u >>> 7) & 0x1],
                DIGITS[(u >>> 6) & 0x1],
                DIGITS[(u >>> 5) & 0x1],
                DIGITS[(u >>> 4) & 0x1],
                DIGITS[(u >>> 3) & 0x1],
                DIGITS[(u >>> 2) & 0x1],
                DIGITS[(u >>> 1) & 0x1],
                DIGITS[u & 0x1],
        });
    }
    public static String bytesToString(byte[] bytes){
        StringBuffer str=new StringBuffer();
        for (int i=0;i<bytes.length;i++){
            U8 x=new U8(bytes[i]);
            int bae=x.getBase();
            String mstr=Integer.toBinaryString(bae);
            int len=mstr.length();
            if (len<8){
                for (int j=0;j<8-len;j++){
                    mstr="0"+mstr;
                }
            }
            str.append(mstr);
        }
        return str.toString();
    }
    public static String byteToBit(byte b){
        return ""+(byte)((b>>7)&0x1)+
        (byte)((b>>6)&0x1)+
        (byte)((b>>5)&0x1)+
        (byte)((b>>4)&0x1)+
        (byte)((b>>3)&0x1)+
        (byte)((b>>2)&0x1)+
        (byte)((b>>1)&0x1)+
        (byte)((b>>0)&0x1);
    }
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for(int i=7;i>=0;i--){
            array[i]=(byte)(b&1);
            b=(byte)(b>>1);
        }
        return array;
    }


}

