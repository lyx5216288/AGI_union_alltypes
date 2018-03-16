package com.adapter;

import com.adapter.event.logprint.LogPrintManager;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BlackListUnit {


    private static String Tag = "BlackListUnit";

    private static long[] blacklist = null;

    private static final Object o = new Object();


    public static void SetBlackList(long[] lists) {
        synchronized (o) {
            blacklist = lists;


            StringWriter writer = new StringWriter();
            PrintWriter bufwrite = new PrintWriter(writer);


            bufwrite.println("设置黑名单");
            if (blacklist != null) {
                for (Long ii : blacklist) {
                    bufwrite.println(ii);
                }

                LogPrintManager.Print("info", Tag, writer.toString());
            }

        }

    }

    public static boolean Compare(long one) {

        synchronized (o) {


            if (blacklist != null) {
                for (long a : blacklist) {
                    if (a == one) {
                        return true;
                    }
                }
            }

            return false;
        }


    }

}
