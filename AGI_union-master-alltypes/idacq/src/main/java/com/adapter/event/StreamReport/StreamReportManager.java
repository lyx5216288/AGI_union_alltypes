package com.adapter.event.StreamReport;

import com.adapter.CommonClass.StreamReport;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by usr on 2017/7/13.
 */
public class StreamReportManager {

    private static StreamReportManager instance = null;


    synchronized public static StreamReportManager getInstance(){
        if (instance == null) {
            instance = new StreamReportManager();
        }
        return instance;
    }

    private StreamReportManager()
    {

    }


    private Collection listeners;


    synchronized public void addListener(StreamReportListener listener) {
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
    synchronized public void removeListener(StreamReportListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }


    synchronized public void fireEvent(List<StreamReport> lists ) {
        if (listeners == null)
            return;
        StreamReportEvent event = new StreamReportEvent(this, lists);
        notifyListeners(event);
    }


    synchronized private void notifyListeners(StreamReportEvent event) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            StreamReportListener listener = (StreamReportListener) iter.next();
            listener.streamReportHandle(event);
        }
    }

}
