package com.adapter.event.logprint;

import com.adapter.CommonClass.StatusReport;
import com.adapter.event.StatusReport.StatusReportEvent;
import com.adapter.event.StatusReport.StatusReportListener;
import com.adapter.event.StatusReport.StatusReportManager;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by john on 2017/7/22.
 */
public class LogPrintManager {

    private static LogPrintManager instance = null;


    public static LogPrintManager getInstance(){
        if (instance == null) {
            instance = new LogPrintManager();
        }
        return instance;
    }

    private LogPrintManager()
    {

    }

    private Collection listeners;


    public void addListener(LogPrintListener listener) {
        if (listeners == null) {
            listeners = new HashSet();
        }
        listeners.add(listener);
    }

    /**
     * 移除事件
     *
     * @param listener
     *            DoorListener
     */
    public void removeListener(LogPrintListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }


    public void fireEvent(String level, String tag, String msg ) {
        if (listeners == null)
            return;
        LogPrintEvent event = new LogPrintEvent(this, level, tag, msg);
        notifyListeners(event);
    }


    synchronized public static void Print(String level, String tag, String msg ){
        LogPrintManager.getInstance().fireEvent(level, tag, msg);
    }

    private void notifyListeners(LogPrintEvent event) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            LogPrintListener listener = (LogPrintListener) iter.next();
            listener.LogPrintHandle(event);
        }
    }

}
