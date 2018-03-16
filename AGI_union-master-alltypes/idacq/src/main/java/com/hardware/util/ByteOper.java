package com.hardware.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/7/16.
 */
public class ByteOper {


    public static byte[] ByteMerge(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length+data2.length];
        System.arraycopy(data1,0,data3,0,data1.length);
        System.arraycopy(data2,0,data3,data1.length,data2.length);
        return data3;
    }


    public static byte[] ByteMergeList(List<byte[]> lists) {

        int num = 0;

        for(int i=0; i<lists.size();i++) {
            num += lists.get(i).length;
        }

        byte[] ret = new byte[num];


        int nowpos = 0;
        for(int i=0; i<lists.size();i++) {
            System.arraycopy(lists.get(i),0,
                    ret,nowpos,
                    lists.get(i).length);
            nowpos+=lists.get(i).length;
        }

        return ret;

    }


    public static List<byte[]> BytesSpilt(byte[] srcbytes, List<Integer> lenlists) {
        List<byte[]> lists = new ArrayList<>();

        int nowpos = 0;
        for(int i=0; i<lenlists.size();i++) {
            byte[] a = new byte[lenlists.get(i)];
            System.arraycopy(srcbytes,nowpos,
                    a,0,
                    a.length);
            nowpos+=a.length;
            lists.add(a);
        }
        return lists;
    }





}
