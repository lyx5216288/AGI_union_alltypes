package com.hardware.board.board_C1.MacroDef;

import com.hardware.util.ByteOper;

import java.util.List;

/**
 * Created by john on 2017/7/17.
 */
public class UArrayArray {



    List<byte[]> lists;


    public UArrayArray(List<byte[]> abclists) {
        lists = abclists;
    }


    public byte[] ToBytes() {

        byte[] bytes = ByteOper.ByteMergeList(this.lists);
        return bytes;



    }
}
