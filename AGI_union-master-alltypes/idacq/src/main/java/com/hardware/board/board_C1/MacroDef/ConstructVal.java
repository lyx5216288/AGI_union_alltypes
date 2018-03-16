package com.hardware.board.board_C1.MacroDef;

/**
 * Created by usr on 2017/7/16.
 */
public class ConstructVal {

    public static Object GetVal(byte[] bytes, int length) {
        Object o = null;
        switch (length) {
            case 4:
                o= new U32(bytes);
            break;
            case 2:
                o= new U16(bytes);
            break;
            case 1:
                o= new U8(bytes);
                break;
        }
        return o;
    }

}
