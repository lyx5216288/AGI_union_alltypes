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

public class stF1LteIntreFreqLst {

   // MAX_INTER_FREQ_NGH  宏定义 瞎写的 要改
    public U32 dlARFCN;
    public U8 cellReselectPriotry;
    U8 Q_offsetFreq;
    U16 measBandWidth;
    public U32 interFreqNghNum;
    public wrFLInterNeighCellInfo[] stInterFreq;  // MAX_INTER_FREQ_NGH

    public stF1LteIntreFreqLst() {
    }

    public stF1LteIntreFreqLst(U32 dlARFCN, U8 cellReselectPriotry, U8 q_offsetFreq, U16 measBandWidth, U32 interFreqNghNum, wrFLInterNeighCellInfo[] stInterFreq) {
        this.dlARFCN = dlARFCN;
        this.cellReselectPriotry = cellReselectPriotry;
        Q_offsetFreq = q_offsetFreq;
        this.measBandWidth = measBandWidth;
        this.interFreqNghNum = interFreqNghNum;
        this.stInterFreq = stInterFreq;
    }

    public static int GetLength() {
        return 4+1+1+2+4+wrFLInterNeighCellInfo.GetLength()*MAX_INTER_FREQ_NGH;
    }


    public stF1LteIntreFreqLst(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();
        lenlists.add(wrMsgHeader.GetLength());
        lenlists.add(1);
        lenlists.add(1);
        lenlists.add(2);
        lenlists.add(4);
        for (int j=0; j<MAX_INTER_FREQ_NGH; j++){
            lenlists.add(wrFLInterNeighCellInfo.GetLength());
        }



        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        dlARFCN = new U32(list.get(i));
        i++;

        this.cellReselectPriotry = new U8(list.get(i));
        i++;

        this.Q_offsetFreq = new U8(list.get(i));
        i++;

        this.measBandWidth = new U16(list.get(i));
        i++;

        this.interFreqNghNum = new U32(list.get(i));
        i++;

        stInterFreq = new wrFLInterNeighCellInfo[MAX_INTER_FREQ_NGH];
        for (int j=0; j<MAX_INTER_FREQ_NGH; j++){
            stInterFreq[j] = new wrFLInterNeighCellInfo(list.get(i));
            i++;
        }

    }
}
