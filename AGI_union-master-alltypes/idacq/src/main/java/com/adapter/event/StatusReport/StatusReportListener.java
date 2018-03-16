package com.adapter.event.StatusReport;

import java.util.EventListener;

/**
 * Created by john on 2017/7/18.
 */
public interface StatusReportListener extends EventListener {

    public void StatusReportHandle(StatusReportEvent event);

}
