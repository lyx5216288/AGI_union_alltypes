package com.adapter;

import com.adapter.event.logprint.LogPrintManager;

import java.io.*;
import java.util.Properties;

/**
 * Created by john on 2017/8/2.
 */
public class IniReader {



        String configpath = "idacq.ini";
        private static Properties properties =new Properties();
        FileInputStream fis = null; // 读
        OutputStream fos ;


        /**
         *
         */
        public IniReader() throws FileNotFoundException {
            try {
                fis = new FileInputStream(configpath);
                properties.load(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogPrintManager.Print("error", "inireader", "FileNotFoundException "+e.getMessage());
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public String getProperty(String key)
        {
            Object object = properties.get(key);
            return object.toString();
        }

        public void setProperty(String key, String value)
        {
            try {
                fos = new FileOutputStream(configpath);// 加载读取文件流
                properties.setProperty(key, value);
                properties.store(fos, null);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
