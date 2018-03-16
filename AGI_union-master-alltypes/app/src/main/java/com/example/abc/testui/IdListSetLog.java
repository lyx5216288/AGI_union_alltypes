package com.example.abc.testui;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.example.abc.testui.src.Com;
import com.example.abc.testui.src.WriteBLogDeal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abc on 2017/10/16.
 */

public class IdListSetLog {

    public static final int TYPE_ADD = 0;
    public static final int TYPE_DELETE = 1;

    public static final String FILE_DIR = Environment.getExternalStorageDirectory().toString() + File.separator + ".SignalLog";
    public static final String FILE_NAME = FILE_DIR + File.separator + ".listlog.txt";

    public static final String FILE_DIR_2 = Environment.getExternalStorageDirectory().toString() + File.separator + "AndroidData"+File.separator+"Phonestoreddata"+ File.separator+"monitor";
    public static final String FILE_NAME_2 = FILE_DIR_2 + File.separator + "task.log";

    public IdListSetLog(){
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        File dir2 = new File(FILE_DIR_2);
        if (!dir2.exists()) {
            dir2.mkdirs();
        }
    }

    public void writeLog(int type, IdListDatas idListDatas){
        if (type==TYPE_ADD){
            writefile("增加", idListDatas);
        }
        else if (type==TYPE_DELETE){
            writefile("删除", idListDatas);
        }
        else{

        }
    }

    private void writefile(String type, IdListDatas idListDatas){
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            bufferedWriter.write(dateStr+"  "+type+"  "+idListDatas.getName()+"  "+idListDatas.getImsi());
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readfile(final TextView tv){
        try {
            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String oneLine = "";
            while ((oneLine=bufferedReader.readLine())!=null){
                tv.append(oneLine+"\n");
            }

            bufferedReader.close();
            fileReader.close();

            tv.post(new Runnable() {
                @Override
                public void run() {
                    int offset = tv.getLineCount() * tv.getLineHeight();
                    if (offset>tv.getHeight()){
                        //TextView 滚动到最后一行
                        tv.scrollTo(0, offset-tv.getHeight());
                    }
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeBLog(byte[] msg){
        try {


            Com.WriteToFile(FILE_NAME, msg, true);
            Com.WriteToFile(FILE_NAME_2, msg, true);

//            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//
//            String dateStr = new String(msg, "UTF-8");
//            bufferedWriter.write(dateStr);
////            bufferedWriter.newLine();
//
//            bufferedWriter.close();
//            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readBLog(final TextView tv){
        try {
            //FileInputStream fis = new FileInputStream(FILE_NAME);
            //BufferedInputStream bis = new BufferedInputStream(fis);

            String oneLine = null;
            byte[] msg = new byte[1];
            WriteBLogDeal writeBLogDeal = new WriteBLogDeal();


            byte[] bytes = Com.ReadFromFile(FILE_NAME);
            String s = writeBLogDeal.FanYi(bytes);


            tv.append(s);

//            while (bis.read(msg)!=-1){
//                oneLine = new String(msg, "UTF-8");
//                tv.append(writeBLogDeal.FanYi(oneLine));
//            }

            //bis.close();
            //fis.close();

            tv.post(new Runnable() {
                @Override
                public void run() {
                    int offset = tv.getLineCount() * tv.getLineHeight();
                    if (offset>tv.getHeight()){
                        //TextView 滚动到最后一行
                        tv.scrollTo(0, offset-tv.getHeight());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
