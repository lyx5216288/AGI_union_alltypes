package com.adapter.event.StreamReport;

import com.adapter.CommonClass.StreamReport;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Created by usr on 2017/7/13.
 */
public class StreamReportEvent extends EventObject {


    List<StreamReport> lists = null;


    public StreamReportEvent(Object source) {
        super(source);
        lists = new ArrayList<>();
    }


    public StreamReportEvent(Object source, List<StreamReport> _lists) {
        super(source);
        lists = _lists;
    }


    public List<StreamReport> getLists() {
        return lists;
    }

    public void setLists(List<StreamReport> lists) {
        this.lists = lists;
    }
}
