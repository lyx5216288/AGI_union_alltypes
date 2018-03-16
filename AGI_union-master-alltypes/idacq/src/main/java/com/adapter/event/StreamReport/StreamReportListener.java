package com.adapter.event.StreamReport;

import java.util.EventListener;

/**
 * Created by usr on 2017/7/13.
 */
public interface StreamReportListener extends EventListener {


    public void streamReportHandle(StreamReportEvent event);
}
