package com.hardware.board;

import com.hardware.board.board_C1.UDPMachine;

/**
 * Created by john on 2017/7/22.
 */
public class BoardInfo {

    private String ipaddr;
    private String remoteport;
    private String localport;

    //private UDPMachine udp;

    private int boardindex;

    private String BoardName;
    private String ModelName;
    private String DisplayName;


    public BoardInfo(String ipaddr, String remoteport, String localport, int boardindex, String boardName, String modelName, String displayName) {
        this.ipaddr = ipaddr;
        this.remoteport = remoteport;
        this.localport = localport;
        this.boardindex = boardindex;
        BoardName = boardName;
        ModelName = modelName;
        DisplayName = displayName;
    }

    public BoardInfo() {
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getRemoteport() {
        return remoteport;
    }

    public void setRemoteport(String remoteport) {
        this.remoteport = remoteport;
    }

    public String getLocalport() {
        return localport;
    }

    public void setLocalport(String localport) {
        this.localport = localport;
    }



    public int getBoardindex() {
        return boardindex;
    }

    public void setBoardindex(int boardindex) {
        this.boardindex = boardindex;
    }

    public String getBoardName() {
        return BoardName;
    }

    public void setBoardName(String boardName) {
        BoardName = boardName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
