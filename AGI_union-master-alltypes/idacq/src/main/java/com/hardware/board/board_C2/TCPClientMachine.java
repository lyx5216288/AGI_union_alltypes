package com.hardware.board.board_C2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.io.InputStream;
import java.net.Socket;
/**
 * Created by john on 2017/7/11.
 */
public class TCPClientMachine implements Runnable
{

    private Socket sk = new Socket();

    private SocketAddress saddr = null;

    private boolean isover = false;

    private boolean connectstatus = false;


    public TCPClientMachine(String addr, int port) {
        saddr = new InetSocketAddress(addr, port);
    }


    private void Connect() throws IOException {

        sk = new Socket();
        this.sk.connect(saddr);
    }


    public void SendBytes(byte[] bytes){
        if (sk != null && sk.isConnected() && this.connectstatus) {

            try {
                OutputStream outputStream = this.sk.getOutputStream();
                outputStream.write(bytes);
            } catch (IOException e) {
                connectstatus =false;
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {

        System.out.println("thread begin to run");
        int flag = 0;
        while (isover == false) {

            try {
                if(sk==null || sk.isConnected() ==false || this.connectstatus == false)
                {
                    this.Connect();
                    connectstatus = true;
                    System.out.println(" Connect ok "+flag++);
                }


                if(sk.isConnected()==false )
                {
                    Thread.sleep(1000);
                    continue;
                }



                InputStream inputStream = sk.getInputStream();

                byte[] b = new byte[10];
                int len = inputStream.read(b, 0, 3);

                if(len == -1)
                {
                    System.out.println("Connect fail");
                    connectstatus = false;
                    continue;
                }

                System.out.println("len = "+len);
                System.out.println(b[0]);
                System.out.println(b[1]);
                System.out.println(b[2]);
                Thread.sleep(1000);
            }
            catch (SocketException e){
                System.out.println("Connect fail");
                connectstatus = false;
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }

            }

        }


    }
}
