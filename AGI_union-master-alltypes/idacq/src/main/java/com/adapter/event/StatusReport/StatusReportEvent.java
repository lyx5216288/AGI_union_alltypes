package com.adapter.event.StatusReport;

import com.adapter.CommonClass.StatusReport;

import java.util.EventObject;
import java.util.List;

/**
 * Created by john on 2017/7/18.
 */
public class StatusReportEvent extends EventObject {


    List<StatusReport> lists = null;

    public StatusReportEvent(Object source) {
        super(source);
    }

    public StatusReportEvent(Object source, List<StatusReport> lists) {
        super(source);
        this.lists = lists;
    }


    public List<StatusReport> getLists() {
        return lists;
    }

    public void setLists(List<StatusReport> lists) {
        this.lists = lists;
    }
}
