package com.example.abc.testui.src;

import java.io.*;

public class Com {

    //isappend = true 追加写   isappend = false 覆盖写
    public static void WriteToFile(String filename ,byte[] bytes, boolean isappend){
        try {
            File file = new File(filename);
            FileOutputStream out1 =new FileOutputStream(file, isappend);
            DataOutputStream data_out = new DataOutputStream(out1);
            //int available = data_in.available();
            //byte[] itemBuf = new byte[available];
            //int read = data_in.read(itemBuf, 0, available);

            data_out.write(bytes);


            data_out.close();
            out1.close();

          //  return itemBuf;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static byte[] ReadFromFile(String filename){

        try {
            File file = new File(filename);
            FileInputStream in1 =new FileInputStream(file);
            DataInputStream data_in = new DataInputStream(in1);
            int available = data_in.available();
            byte[] itemBuf = new byte[available];
            int read = data_in.read(itemBuf, 0, available);



            data_in.close();
            in1.close();

            return itemBuf;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
