package com.hardware.board.board_C1.boardc1hardware.freqscan;

import com.hardware.board.board_C1.MacroDef.*;
import com.hardware.board.board_C1.boardc1hardware.*;
import com.hardware.util.ByteOper;
import com.hardware.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

import static com.hardware.board.board_C1.boardc1hardware.Constants_C1.*;

/**
 * Created by usr on 2017/10/9.
 */

public class wrFLEnbToLmtScanCellInfoRpt {
    wrMsgHeader WrmsgHeaderInfo;/*消息头定义*/
    public U16 collectionCellNum;
    U16 collectionTypeFlag;
    public wrFLCollectionCellInfo[] stCollCellInfo;   //C_MAX_COLLTECTION_INTRA_CELL_NUM;


    public wrFLEnbToLmtScanCellInfoRpt(byte[]bytes) {


        byte[] bheader = new byte[wrMsgHeader.GetLength()];
        System.arraycopy(bytes, 0, bheader, 0, wrMsgHeader.GetLength());
        WrmsgHeaderInfo = new wrMsgHeader(bheader);

        collectionCellNum = new U16(ByteUtil.getShort(bytes, wrMsgHeader.GetLength()));
        collectionTypeFlag = new U16(ByteUtil.getShort(bytes, wrMsgHeader.GetLength()+ 2));

        int offset = wrMsgHeader.GetLength()+ 4;

        stCollCellInfo = new wrFLCollectionCellInfo[collectionCellNum.GetVal()];


        for (int jj=0; jj<collectionCellNum.GetVal();jj++) {

            stCollCellInfo[jj] = new wrFLCollectionCellInfo();

            byte[] tmpbytes = new byte[wrFLCellInfo.GetLength()];
            System.arraycopy(bytes, offset, tmpbytes, 0, wrFLCellInfo.GetLength());
            wrFLCellInfo cellInfo = new wrFLCellInfo(tmpbytes);
            stCollCellInfo[jj].stCellInfo = cellInfo;

            offset += wrFLCellInfo.GetLength();
            tmpbytes = new byte[4];
            System.arraycopy(bytes, offset, tmpbytes, 0, 4);
            U32 num0 = new U32(tmpbytes);
            stCollCellInfo[jj].NeighNum = num0;
            offset += 4;
            if (num0.GetVal() != 0) {//TODO

                int neighnum = num0.GetVal();
                stCollCellInfo[jj].stNeighCellInfo = new wrFLNeighCellInfo[neighnum];

                for(int bb = 0; bb<neighnum; bb++){

                    tmpbytes = new byte[wrFLNeighCellInfo.GetLength()];
                    System.arraycopy(bytes, offset, tmpbytes, 0, wrFLNeighCellInfo.GetLength());
                    offset += wrFLNeighCellInfo.GetLength();

                    stCollCellInfo[jj].stNeighCellInfo[bb] = new wrFLNeighCellInfo(tmpbytes);

                }


            }
            tmpbytes = new byte[4];
            System.arraycopy(bytes, offset, tmpbytes, 0, 4);
            U32 num1 = new U32(tmpbytes);
            stCollCellInfo[jj].NumOfInterFreq = num1;
            offset += 4;
            if (num1.GetVal() != 0) {//TODO
                int numofinterfreq = num1.GetVal();

                stCollCellInfo[jj].stInterFreqLstInfo = new stF1LteIntreFreqLst[numofinterfreq];

                for (int k = 0; k < numofinterfreq; k++) {

                    stCollCellInfo[jj].stInterFreqLstInfo[k] = new stF1LteIntreFreqLst();

                    tmpbytes = new byte[4];
                    System.arraycopy(bytes, offset, tmpbytes, 0, 4);
                    offset += 4;
                    U32 dlARFCN = new U32(tmpbytes);
                    stCollCellInfo[jj].stInterFreqLstInfo[k].dlARFCN = dlARFCN;

                    tmpbytes = new byte[1];
                    System.arraycopy(bytes, offset, tmpbytes, 0, 1);
                    offset += 1;
                    U8 cellreselect = new U8(tmpbytes);
                    stCollCellInfo[jj].stInterFreqLstInfo[k].cellReselectPriotry = cellreselect;

                    tmpbytes = new byte[1];
                    System.arraycopy(bytes, offset, tmpbytes, 0, 1);
                    offset += 1;
                    U8 qoffsetfreq = new U8(tmpbytes);
                    stCollCellInfo[jj].stInterFreqLstInfo[k].Q_offsetFreq = qoffsetfreq;

                    tmpbytes = new byte[2];
                    System.arraycopy(bytes, offset, tmpbytes, 0, 2);
                    offset += 2;
                    U16 meansbandwidth = new U16(tmpbytes);
                    stCollCellInfo[jj].stInterFreqLstInfo[k].measBandWidth = meansbandwidth;


                    tmpbytes = new byte[4];
                    System.arraycopy(bytes, offset, tmpbytes, 0, 4);
                    offset += 4;
                    U32 interfreqneightnum = new U32(tmpbytes);
                    stCollCellInfo[jj].stInterFreqLstInfo[k].interFreqNghNum = interfreqneightnum;


                    if(interfreqneightnum.GetVal()!=0){
                        stCollCellInfo[jj].stInterFreqLstInfo[k].stInterFreq = new wrFLInterNeighCellInfo[interfreqneightnum.GetVal()];

                        for (int q = 0; q<interfreqneightnum.GetVal(); q++){

                            stCollCellInfo[jj].stInterFreqLstInfo[k].stInterFreq[q] = new wrFLInterNeighCellInfo();

                            tmpbytes = new byte[2];
                            System.arraycopy(bytes, offset, tmpbytes, 0, 2);
                            offset += 2;
                            U16 pci = new U16(tmpbytes);
                            stCollCellInfo[jj].stInterFreqLstInfo[k].stInterFreq[q].pci = pci;


                            tmpbytes = new byte[2];
                            System.arraycopy(bytes, offset, tmpbytes, 0, 2);
                            offset += 2;
                            U16 qoffsetcell = new U16(tmpbytes);
                            stCollCellInfo[jj].stInterFreqLstInfo[k].stInterFreq[q].QoffsetCell = qoffsetcell;



                        }
                    }


                }
            }


        }
    }

    public static int GetMsgType() {
        return Constants_C1.O_FL_ENB_TO_LMT_REM_INFO_RPT ;
    }

    public static int GetLength() {
        return wrMsgHeader.GetLength()+2+2+wrFLCollectionCellInfo.GetLength()*C_MAX_COLLTECTION_INTRA_CELL_NUM;
    }

}
