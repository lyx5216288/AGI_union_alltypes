package com.example.abc.testui.ui;

import com.adapter.CommonClass.StreamReport;
import com.adapter.event.StreamReport.StreamReportEvent;
import com.adapter.event.StreamReport.StreamReportListener;

import java.util.List;

/**
 * Created by john on 2017/7/18.
 */
public class StreamReportLis1 implements StreamReportListener {


    //希望这里面的实现是不耗时的，比如启动一个线程之类的。
    @Override
    public void streamReportHandle(StreamReportEvent event) {


        //在这里 你会 拿到  imsi和 istarget 这两个值 开一个线程 执行后续步骤

        List<StreamReport> lists = event.getLists();


        for(int i=0; i<lists.size();i++) {
            StreamReport streamReport = lists.get(i);
            String str = String.format("index = %d imsi = %d imei = %d  istarget = %s\n",
                    streamReport.getIndex(),
                     streamReport.getImsi(),
                    streamReport.getImei(),
                    ((Boolean)(streamReport.isIstarget())).toString());

            long imsi = streamReport.getImsi();
            int istarget = 0;
            if(streamReport.isIstarget()){
                istarget = 1;
            }
            else {
                istarget = 0;
            }

            int rssi = streamReport.getRssi();

           // (new UpdateImsiThread(imsi, istarget)).start();

            Global.service.addANewData(imsi, istarget, rssi);

            System.out.println(str);
        }
    }
}
