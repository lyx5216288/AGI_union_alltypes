package com.example.abc.testui.ui;

import com.example.abc.testui.MainActivity;
import com.example.abc.testui.RealTimeService;
import com.example.abc.testui.SoundPlayer;

import java.util.Date;

/**
 * Created by usr on 2017/9/5.
 */

public class Global {

    public static final int UI_V1 = 1;
    public static final int UI_V2 = 2;
    public static final int UI_FORMAT = UI_V1;

    public static String username = null;

    public static RealTimeService service = null;

    public static MainActivity activity = null;


    public static SoundPlayer soundPlayer = null;


    public static boolean HBrecv = false;


    public static Date uenewtime = new Date();
    public static boolean UErecv = false;
    public static boolean UErecvoffline = false;

    public static UECycle uecycle = null;



    public static RegLib reglib = null;


    public static boolean isopenreg = false;


    public static regcycle recycle = null;


    public static boolean cellsetup = false;
    public static boolean cellstopoverthenchangemode = false;
    public static String cellstopwillchangemode = null;



}
