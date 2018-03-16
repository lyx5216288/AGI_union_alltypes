package com.hardware.board;

//import com.hardware.board.board_C1.PingThread;
import com.hardware.board.board_C1.TongjiClassVal;
import com.hardware.board.board_C1.UDPMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/7/18.
 */
public class Global {

    public static boolean recvarfcn = false;
    public static boolean recvcellstate = false;

    public static String ModelType = "FDD";


    public static List<BoardInfo> boardinfolist = new ArrayList<>();

    public static UDPMachine udpmachine = null;


    //public static PingThread[] pinglists = new PingThread[4];

    public static TongjiClassVal[] tongjilist = new TongjiClassVal[4];
    public static byte[] scanrecvbytes = null;
//    private static UDPMachine udpmachine = null;
//
//
//    public static void RegisterUDPMachine(UDPMachine _udpMachine) {
//        udpmachine = _udpMachine;
//    }
//
//
//    public static UDPMachine GetUDPMachine()
//    {
//        return udpmachine;
//    }

    static {
//        for(int i=0; i<4; i++){
//            pinglists[i] = null;
//        }
    }

}
