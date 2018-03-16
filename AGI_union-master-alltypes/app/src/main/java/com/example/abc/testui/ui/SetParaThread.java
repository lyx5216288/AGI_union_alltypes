package com.example.abc.testui.ui;

import com.adapter.CommonClass.Configure;
import com.adapter.Machine;

/**
 * Created by usr on 2017/9/6.
 */

public class SetParaThread extends Thread {


    private Configure cfg;


    public SetParaThread(Configure cfg_){
        this.cfg = cfg_;
    }

    public Configure getCfg() {
        return cfg;
    }

    public void setCfg(Configure cfg) {
        this.cfg = cfg;
    }

    @Override
    public void run(){

        Machine.SetPara(cfg);

    }
}
