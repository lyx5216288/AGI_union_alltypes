package com.adapter.event.StatusReport;

import com.adapter.CommonClass.StatusReport;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by john on 2017/7/18.
 */
public class StatusReportManager {


    private static StatusReportManager instance = null;


    synchronized public static StatusReportManager getInstance(){
        if (instance == null) {
            instance = new StatusReportManager();
        }
        return instance;
    }

    private StatusReportManager()
    {

    }

    private Collection listeners;


    synchronized public void addListener(StatusReportListener listener) {
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
    synchronized public void removeListener(StatusReportListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }


    synchronized public void fireEvent(List<StatusReport> lists ) {
        if (listeners == null)
            return;
        StatusReportEvent event = new StatusReportEvent(this, lists);
        notifyListeners(event);
    }


    synchronized private void notifyListeners(StatusReportEvent event) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            StatusReportListener listener = (StatusReportListener) iter.next();
            listener.StatusReportHandle(event);
        }
    }

}
