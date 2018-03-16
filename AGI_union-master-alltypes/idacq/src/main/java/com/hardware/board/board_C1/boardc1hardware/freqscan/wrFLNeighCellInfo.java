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

public class wrFLNeighCellInfo {
    public U32 Arfcn;
    public U16 pci;
    public U16 QoffsetCell;

    public wrFLNeighCellInfo() {
    }

    public wrFLNeighCellInfo(U32 arfcn, U16 pci, U16 qoffsetCell) {
        Arfcn = arfcn;
        this.pci = pci;
        QoffsetCell = qoffsetCell;
    }

    public static int GetLength() {
        return 4+2+2;
    }

    public wrFLNeighCellInfo(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(4);
        lenlists.add(2);
        lenlists.add(2);




        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        Arfcn = new U32(list.get(i));
        i++;



        pci = new U16(list.get(i));
        i++;

        this.QoffsetCell = new U16(list.get(i));
        i++;



    }
}
