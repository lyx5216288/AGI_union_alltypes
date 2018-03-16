package com.example.abc.testui.ui;

import java.util.Date;

/**
 * Created by usr on 2017/9/15.
 */

public class UECycle extends Thread {



    private boolean isover = false;


    public void SetOver(){
        this.isover = true;
    }

    @Override
    public void run() {


        System.out.println("uecycle start");
        while (isover==false){


            try {

                if(Global.UErecv == false){


                    Thread.sleep(1000);
                    continue;
                }
                else {

                    Date nowtime = new Date();
                    Date lasttime = Global.uenewtime;

                    if((nowtime.getTime() - lasttime.getTime())>=15*1000){

                        //已经连续30秒没有收到UE值了

                        if(Global.activity.voiceenable){
                            Global.soundPlayer.playSoundOffLine();
                            Global.service.addANewValue(0);
                        }
                        Global.service.addANewValue(0);
                        Global.UErecv = false;
                        Global.UErecvoffline = true;

                    }





                }








                Thread.sleep(1000);
            }

            catch (Exception e){


                //Thread.sleep(1000);
                continue;
            }

        }



        System.out.println("uecycle over");


    }




}
