package com.example.abc.testui.ui;

import com.adapter.event.logprint.LogPrintEvent;
import com.adapter.event.logprint.LogPrintListener;


/**
 * Created by john on 2017/7/20.
 */
public class UIPrint implements LogPrintListener{




    @Override
    public void LogPrintHandle(LogPrintEvent event) {


        //在这里你应该 另启动线程 去 记录后台运行日志，以备后期翻查 存在某一个后台文件中

//        System.out.println(event.getMsg());
    }
}
