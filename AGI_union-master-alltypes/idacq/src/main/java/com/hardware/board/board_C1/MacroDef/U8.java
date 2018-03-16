package com.hardware.board.board_C1.MacroDef;

/**
 * Created by usr on 2017/7/16.
 */
public class U8 {

    //private byte num;
    private int num;

    public U8(byte[] bytes) {
        num = (0x000000FF & bytes[0]);
    }

    public U8(byte s) {
        num = s;
    }


    public byte[] ToBytes()
    {
        byte[] b = new byte[1];
        b[0] = (byte)(num&0x000000FF);
        return b;
    }


    public int GetVal() {
        return num;
    }

    public int GetLength() {
        return 1;
    }




}
