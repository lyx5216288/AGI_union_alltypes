package com.adapter.event.logprint;

import com.adapter.event.StatusReport.StatusReportEvent;

import java.util.EventListener;

/**
 * Created by john on 2017/7/22.
 */
public interface LogPrintListener extends EventListener {

    public void LogPrintHandle(LogPrintEvent event);
}
