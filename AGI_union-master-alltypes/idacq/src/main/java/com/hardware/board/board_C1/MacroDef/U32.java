package com.hardware.board.board_C1.MacroDef;

import com.hardware.util.ByteUtil;

/**
 * Created by usr on 2017/7/16.
 */
public class U32 {
    private int num;

    public U32(byte[] bytes) {
        num = ByteUtil.getInt(bytes, 0);
    }

    public U32(int s) {
        num = s;
    }

    public byte[] ToBytes()
    {
        byte[] b = new byte[4];
        ByteUtil.putInt(b, num, 0);
        return b;
    }


    public int GetVal() {
        return num;
    }

    public int GetLength() {
        return 4;
    }

}
