package com.example.jbtang.agi_union.trigger;

import android.app.Activity;

import com.example.jbtang.agi_union.core.Status;

import java.io.IOException;

/**
 * Created by jbtang on 12/6/2015.
 */
public interface Trigger {

    void start(Activity activity, Status.Service service) throws IOException;

    void stop();

    void restar(Activity activity, Status.Service service);
}
