package com.example.abc.testui.ui;

import com.adapter.Machine;
import com.hardware.board.*;
import com.hardware.board.Global;

/**
 * Created by usr on 2017/10/18.
 */

public class QueryStateThread extends Thread {






    public QueryStateThread(){

    }



    @Override
    public void run(){





//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Machine.QueryCellState();
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Machine.QueryCellState();

        while (Global.recvcellstate==false){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Machine.QueryCellState();
        }

    }

}

