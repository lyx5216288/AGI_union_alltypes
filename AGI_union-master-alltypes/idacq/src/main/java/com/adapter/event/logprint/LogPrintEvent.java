package com.adapter.event.logprint;

import com.adapter.CommonClass.StatusReport;

import java.util.EventObject;
import java.util.List;

/**
 * Created by john on 2017/7/22.
 */
public class LogPrintEvent extends EventObject{

    private String level;
    private String tag;
    private String msg;

    public LogPrintEvent(Object source) {
        super(source);
    }

    public LogPrintEvent(Object source, String level, String tag, String msg) {
        super(source);
        this.level = level;
        this.tag = tag;
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
