package com.hardware.board.board_C1.boardc1hardware.freqscan;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.board.board_C1.boardc1hardware.*;
import com.hardware.util.ByteOper;

import java.util.ArrayList;
import java.util.List;

import static com.hardware.board.board_C1.boardc1hardware.Constants_C1.*;
/**
 * Created by usr on 2017/10/9.
 */

public class wrFLCellInfo {

    public U32 Arfcn;
    public U16 pci;
    U16 Tac;
    U16 Rssi;
    U16 SFassign;
    U32 cellid;
    public U32 Priority;
    U8 RSRP;
    U8 RSRQ;
    U8 Bandwidth;
    U8 SpecSFassign;


    public static int GetLength() {
        return 4+2+2+2+2+4+4+1+1+1+1;
    }

    public wrFLCellInfo(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(4);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(2);
        lenlists.add(4);
        lenlists.add(4);
        lenlists.add(1);
        lenlists.add(1);
        lenlists.add(1);
        lenlists.add(1);




        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        Arfcn = new U32(list.get(i));
        i++;

        this.pci = new U16(list.get(i));
        i++;

        this.Tac = new U16(list.get(i));
        i++;

        this.Rssi = new U16(list.get(i));
        i++;

        this.SFassign = new U16(list.get(i));
        i++;


        this.cellid = new U32(list.get(i));
        i++;

        this.Priority = new U32(list.get(i));
        i++;


        this.RSRP = new U8(list.get(i));
        i++;

        this.RSRQ = new U8(list.get(i));
        i++;


        this.Bandwidth = new U8(list.get(i));
        i++;

        this.SpecSFassign = new U8(list.get(i));
        i++;


    }

}
