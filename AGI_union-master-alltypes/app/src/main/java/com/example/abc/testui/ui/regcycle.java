package com.example.abc.testui.ui;

/**
 * Created by usr on 2017/9/18.
 */

public class regcycle extends  Thread {

    private boolean isover = false;




    public void SetOver(){
        this.isover = true;
    }

    @Override
    public void run(){



        while (isover == false){

            try {
                Thread.sleep(5000);


                int ret = RegDeal.Judge();

                if(ret!=0){


                    if(Global.isopenreg == false){


                        //弹出注册界面

                        Global.service.startRegisterActivity();



                        Global.isopenreg = true;    // 再 注册界面的关闭Ondestroy那里设置为false
                    }


                }




            }
            catch (Exception e){
                ;
            }


        }






    }


}
