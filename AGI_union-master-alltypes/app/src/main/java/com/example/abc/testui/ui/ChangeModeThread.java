package com.example.abc.testui.ui;

import com.adapter.CommonClass.Configure;
import com.adapter.Machine;

/**
 * Created by usr on 2017/9/15.
 */

public class ChangeModeThread extends Thread {



    private String mode;


    public ChangeModeThread(String mode_){
        this.mode = mode_;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void run(){

        Machine.ChangeMode(mode);

    }

}
