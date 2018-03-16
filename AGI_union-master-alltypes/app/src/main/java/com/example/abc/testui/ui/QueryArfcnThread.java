package com.example.abc.testui.ui;

import com.adapter.Machine;
import com.hardware.board.*;
import com.hardware.board.Global;

/**
 * Created by usr on 2017/11/2.
 */

public class QueryArfcnThread extends Thread {

    @Override
    public void run(){

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//         Machine.QueryCellEarfcnPCI();
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Machine.QueryCellEarfcnPCI();
       // Machine.QueryCellState();
//


        while (Global.recvarfcn==false){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Machine.QueryCellEarfcnPCI();
        }


    }

}
