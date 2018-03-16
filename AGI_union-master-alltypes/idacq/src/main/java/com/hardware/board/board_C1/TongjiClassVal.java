package com.hardware.board.board_C1;

public class TongjiClassVal {

    int index;
    int []valinn = new int[10];



    int num = 0;


    private int average(){
        int total = 0;
        for (int i=0; i<10; i++) {
            total = total + valinn[i];
        }
        return total/10;
    }

    public TongjiClassVal(int index) {
        this.index = index;
    }


    public int SetVal(int val) {

        if(num>=0&&num<10){
            valinn[num] = val;
        }



        num++;
        if(num>9) num=0;


        if(num == 0){
            return average();
        }
        else {
            return -1;
        }

    }


}
