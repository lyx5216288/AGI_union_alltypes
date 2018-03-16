package com.hardware.board.board_C1.MacroDef;

/**
 * Created by usr on 2017/7/16.
 */
public class UArray {

    private byte[] bytes;

    public UArray(int num) {

        bytes = new byte[num];
        for (int i=0; i<num;i++) {
            bytes[i] = 0;
        }

    }

    public UArray(byte[] abc) {
        bytes = new byte[abc.length];
        System.arraycopy(abc,0,
                bytes,0,
                abc.length);

    }

    public byte[] ToBytes() {
        return bytes;
    }

}
