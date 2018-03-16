package com.example.abc.testui.ui;

import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLCellInfo;
import com.hardware.board.board_C1.boardc1hardware.freqscan.wrFLEnbToLmtScanCellInfoRpt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usr on 2017/10/16.
 */

public class ScanFreqDisplay {

    public int arfcn;
    public int priority;
    public int pci;


    public ScanFreqDisplay() {
    }

    public ScanFreqDisplay(int arfcn, int priority, int pci) {
        this.arfcn = arfcn;
        this.priority = priority;
        this.pci = pci;
    }





    public static List<ScanFreqDisplay> GetFromOrigin(wrFLEnbToLmtScanCellInfoRpt rpt){
        int cellnum = rpt.collectionCellNum.GetVal();

        List<ScanFreqDisplay> lists = new ArrayList<>();
        if(cellnum!=0){
            for (int i=0; i<cellnum; i++){

                wrFLCellInfo stCellInfo = rpt.stCollCellInfo[i].stCellInfo;
                int arfcn_ = stCellInfo.Arfcn.GetVal();
                int priority_ = stCellInfo.Priority.GetVal();
                int pci_ = stCellInfo.pci.GetVal();
                ScanFreqDisplay one_ = new ScanFreqDisplay(arfcn_, priority_, pci_);
                lists.add(one_);


                int neightnum = rpt.stCollCellInfo[i].NeighNum.GetVal();
                for(int qq = 0; qq<neightnum; qq++){
                    int arfcn = rpt.stCollCellInfo[i].stNeighCellInfo[qq].Arfcn.GetVal();
                    int priority = -1;
                    int pci = rpt.stCollCellInfo[i].stNeighCellInfo[qq].pci.GetVal();
                    ScanFreqDisplay one = new ScanFreqDisplay(arfcn, priority, pci);
                    lists.add(one);
                }


                int numofinterfreq = rpt.stCollCellInfo[i].NumOfInterFreq.GetVal();

                if(numofinterfreq!=0){
                    for (int j=0; j<numofinterfreq; j++){

                        int arfcn = rpt.stCollCellInfo[i].stInterFreqLstInfo[j].dlARFCN.GetVal();
                        int priority = rpt.stCollCellInfo[i].stInterFreqLstInfo[j].cellReselectPriotry.GetVal();

                        int interfreqneighnum = rpt.stCollCellInfo[i].stInterFreqLstInfo[j].interFreqNghNum.GetVal();

                        if(interfreqneighnum!=0)
                        {
                            for (int k = 0; k<interfreqneighnum; k++){

                                int pci = rpt.stCollCellInfo[i].stInterFreqLstInfo[j].stInterFreq[k].pci.GetVal();
                                ScanFreqDisplay one = new ScanFreqDisplay(arfcn, priority, pci);
                                lists.add(one);

                            }
                        }
                        else {

                            ScanFreqDisplay one = new ScanFreqDisplay(arfcn, priority, -1);
                            lists.add(one);

                        }


                    }

                }
                else {

                }
            }
        }
        else {

        }


        return lists;
    }



}
