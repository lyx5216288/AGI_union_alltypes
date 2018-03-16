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

public class wrFLCollectionCellInfo {


    // MAX_INTER_FREQ_LST  数值瞎写的 要改
    public wrFLCellInfo stCellInfo;
    public U32 NeighNum;
    public wrFLNeighCellInfo[] stNeighCellInfo;    //C_MAX_INTRA_NEIGH_NUM
    public U32 NumOfInterFreq;
    public stF1LteIntreFreqLst[] stInterFreqLstInfo;   // MAX_INTER_FREQ_LST

    public wrFLCollectionCellInfo() {
    }

    public wrFLCollectionCellInfo(byte[]bytes) {
        List<Integer> lenlists = new ArrayList<>();

        lenlists.add(wrFLCellInfo.GetLength());
        lenlists.add(4);

        for (int j=0;j<C_MAX_INTRA_NEIGH_NUM; j++){
            lenlists.add(wrFLNeighCellInfo.GetLength());
        }
        lenlists.add(4);

        for (int j=0; j<MAX_INTER_FREQ_LST; j++){
            lenlists.add(stF1LteIntreFreqLst.GetLength());
        }



        List<byte[]> list = ByteOper.BytesSpilt(bytes, lenlists);

        int i = 0;
        stCellInfo = new wrFLCellInfo(list.get(i));
        i++;

        this.NeighNum = new U32(list.get(i));
        i++;

        stNeighCellInfo = new wrFLNeighCellInfo[C_MAX_INTRA_NEIGH_NUM];
        for (int j=0;j<C_MAX_INTRA_NEIGH_NUM; j++){
            this.stNeighCellInfo[j] = new wrFLNeighCellInfo(list.get(i));
            i++;
        }
        this.NumOfInterFreq = new U32(list.get(i));
        i++;

        stInterFreqLstInfo = new stF1LteIntreFreqLst[MAX_INTER_FREQ_LST];
        for (int j=0; j<MAX_INTER_FREQ_LST; j++){
            stInterFreqLstInfo[j] = new stF1LteIntreFreqLst(list.get(i));
            i++;
        }

    }

    public wrFLCollectionCellInfo(wrFLCellInfo stCellInfo, U32 neighNum, wrFLNeighCellInfo[] stNeighCellInfo, U32 numOfInterFreq, stF1LteIntreFreqLst[] stInterFreqLstInfo) {
        this.stCellInfo = stCellInfo;
        NeighNum = neighNum;
        this.stNeighCellInfo = stNeighCellInfo;
        NumOfInterFreq = numOfInterFreq;
        this.stInterFreqLstInfo = stInterFreqLstInfo;
    }

    public static int GetLength() {
        return wrFLCellInfo.GetLength()+4+wrFLNeighCellInfo.GetLength()*C_MAX_INTRA_NEIGH_NUM+4+stF1LteIntreFreqLst.GetLength()*MAX_INTER_FREQ_LST;
    }






}
