package com.hardware.board.board_C1.MacroDef;

import com.hardware.util.ByteUtil;

/**
 * Created by usr on 2017/7/16.
 */
public class U16 {

    private short num;


    public U16(byte[] bytes) {
        num = ByteUtil.getShort(bytes, 0);
    }

    public U16(short s) {
        num = s;
    }


    public byte[] ToBytes()
    {
        byte[] b = new byte[2];
        ByteUtil.putShort(b, num, 0);
        return b;
    }


    public int GetVal() {
        return num&0x0000FFFF;
    }


    public int GetLength() {
        return 2;
    }

}
