package com.adapter.event.door;

import java.util.EventListener;

/**
 * Created by usr on 2017/7/13.
 */
public interface DoorListener extends EventListener {
    public void doorEvent(DoorEvent event);
}
