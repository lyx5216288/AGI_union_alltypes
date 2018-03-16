package com.example.abc.testui.ui;

import java.util.Date;

public class Rcode_struct {

    private String mcode;
    private Date starttime;
    private Date endtime;




    public Rcode_struct() {
        Inityshe();
    }


    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }


    //long类型转成byte数组
    public static byte[] longToByte(long number){
        long temp = number;
        byte[] b = new byte[8];
        for(int i =0; i < b.length; i++){
            b[i]=new Long(temp &0xff).byteValue();//
            //将最低位保存在最低位
                    temp = temp >>8;// 向右移8位
        }
        return b;
    }

    //byte数组转成long
    public static long byteToLong(byte[] b){
        long s =0;
        long s0 = b[0]&0xff;// 最低位
        long s1 = b[1]&0xff;
        long s2 = b[2]&0xff;
        long s3 = b[3]&0xff;
        long s4 = b[4]&0xff;// 最低位
        long s5 = b[5]&0xff;
        long s6 = b[6]&0xff;
        long s7 = b[7]&0xff;

        // s0不变
        s1 <<=8;
        s2 <<=16;
        s3 <<=24;
        s4 <<=8*4;
        s5 <<=8*5;
        s6 <<=8*6;
        s7 <<=8*7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }


 //    * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
 //           * @param src byte[] data
// * @return hex string
// */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public long HashCode(long mcode, long s, long e){

        return mcode + s + e + 0x1234567812345678L;

    }


    public boolean JudgeRCodeValidate(String str1){

        str1 = T2(str1);

        String str_mcode = str1.substring(0, 16);
        String str_s = str1.substring(16, 32);
        String str_e = str1.substring(32, 48);
        String str_h = str1.substring(48, 64);


        byte[] bytes_mcode = hexStringToBytes(str_mcode);
        byte[] bytes_s = hexStringToBytes(str_s);
        byte[] bytes_e = hexStringToBytes(str_e);
        byte[] bytes_h = hexStringToBytes(str_h);

        long mcode = byteToLong(bytes_mcode);
        long s = byteToLong(bytes_s);
        long e = byteToLong(bytes_e);
        long h = byteToLong(bytes_h);

        mcode = mcode ^ 0x0102030405060708L;
        s = s ^ 0x0102030405060708L;
        e = e ^ 0x0102030405060708L;
        h = h ^ 0x0102030405060708L;

        if(HashCode(mcode, s, e) == h){
            return true;
        }
        else {
            return false;
        }


    }

    int []yshe = new int[64];

    void InitChange(int i , int j){
        yshe[i] = j;         yshe[j] = i;
    }

    void Inityshe(){

        for (int i=0; i<64; i++)
        {
            yshe[i] = i;
        }

        InitChange(0, 62);
        InitChange(1, 59);
        InitChange(2, 57);
        InitChange(3, 55);
        InitChange(4, 53);
        InitChange(5, 51);
        InitChange(6, 63);
        InitChange(7, 49);
        InitChange(8, 47);
        InitChange(9, 46);
        InitChange(10, 45);
        InitChange(11, 43);
        InitChange(12, 41);
        InitChange(13, 40);
        InitChange(14, 39);
        InitChange(15, 38);


    }

    int To(int i){
        return yshe[i];
    }

    int From(int val){
        for (int i = 0; i<64; i++){
            if(val == yshe[i]){
                return i;
            }
        }
        return -1;
    }


    String T1(String str){
        byte[] bytes = str.getBytes();

        byte[] retbytes = new byte[bytes.length];

        for (int i=0; i<retbytes.length; i++){
            retbytes[i] = bytes[To(i)];
        }

        return new String(retbytes);
    }

    String T2(String str){
        byte[] bytes = str.getBytes();

        byte[] retbytes = new byte[bytes.length];

        for (int i=0; i<retbytes.length; i++){
            retbytes[i] = bytes[From(i)];
        }

        return new String(retbytes);
    }

    public Rcode_struct Str2Struct(String str1){


        str1 = T2(str1);

        String str_mcode = str1.substring(0, 16);
        String str_s = str1.substring(16, 32);
        String str_e = str1.substring(32, 48);
        String str_h = str1.substring(48, 64);


        byte[] bytes_mcode = hexStringToBytes(str_mcode);
        byte[] bytes_s = hexStringToBytes(str_s);
        byte[] bytes_e = hexStringToBytes(str_e);
        byte[] bytes_h = hexStringToBytes(str_h);

        long mcode = byteToLong(bytes_mcode);
        long s = byteToLong(bytes_s);
        long e = byteToLong(bytes_e);
        long h = byteToLong(bytes_h);

        mcode = mcode ^ 0x0102030405060708L;
        s = s ^ 0x0102030405060708L;
        e = e ^ 0x0102030405060708L;
        h = h ^ 0x0102030405060708L;

        if(HashCode(mcode, s, e) == h){
            ;
        }
        else {
            return null;
        }


        String str = mcode+"";
        Date sdate = new Date(s);
        Date edate = new Date(e);

        Rcode_struct ret = new Rcode_struct();
        ret.setMcode(str);
        ret.setStarttime(sdate);
        ret.setEndtime(edate);

        return ret;

    }


    public String Struct2Str(Rcode_struct struct){

        String str = struct.getMcode();
        long mcode = Long.valueOf(str);
        long s = struct.getStarttime().getTime();
        long e = struct.getEndtime().getTime();
        long h = HashCode(mcode, s, e);

        mcode = mcode ^ 0x0102030405060708L;
        s = s ^ 0x0102030405060708L;
        e = e ^ 0x0102030405060708L;
        h = h ^ 0x0102030405060708L;

        byte[] bytes_mcode = longToByte(mcode);
        byte[] bytes_s = longToByte(s);
        byte[] bytes_e = longToByte(e);
        byte[] bytes_h = longToByte(h);

        String str_mcode = bytesToHexString(bytes_mcode);
        String str_s = bytesToHexString(bytes_s);
        String str_e = bytesToHexString(bytes_e);
        String str_h = bytesToHexString(bytes_h);

        String ret = str_mcode+str_s+str_e+str_h;

        return T1(ret);
    }




}
