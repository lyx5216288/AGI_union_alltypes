package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U8;

import java.util.Arrays;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_MAC_DATA_STRU {
    public U8 mu8TbNum;
    public U8[] mau8Padding; //size=3;
    public L2P_MAC_TB_INFO_STRU[] mastMacTbInfo; //size=2;

    public static final int byteArrayLen =4* U8.byteArrayLen + 2*L2P_MAC_TB_INFO_STRU.byteArrayLen;

    public L2P_MAC_DATA_STRU(){
        mu8TbNum=new U8();
        mau8Padding=new U8[3];
        mastMacTbInfo=new L2P_MAC_TB_INFO_STRU[2];
    }

    public L2P_MAC_DATA_STRU(byte[] bytes){
        validate(bytes);
        int pos=0;
        mu8TbNum=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        mau8Padding=new U8[3];
        for(int i=0;i<mau8Padding.length;i++){
            mau8Padding[i]=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
            pos+= U8.byteArrayLen;
        }

        mastMacTbInfo=new L2P_MAC_TB_INFO_STRU[2];
        for (int i=0;i<mastMacTbInfo.length;i++){
            mastMacTbInfo[i]=new L2P_MAC_TB_INFO_STRU(MsgSendHelper.getSubByteArray(bytes,pos,L2P_MAC_TB_INFO_STRU.byteArrayLen));
            pos+=L2P_MAC_TB_INFO_STRU.byteArrayLen;
        }
    }


    public L2P_MAC_TB_INFO_STRU[] getMastMacTbInfo() {
        return mastMacTbInfo;
    }

    public void setMastMacTbInfo(L2P_MAC_TB_INFO_STRU[] mastMacTbInfo) {
        this.mastMacTbInfo = mastMacTbInfo;
    }

    public U8[] getMau8Padding() {
        return mau8Padding;
    }

    public void setMau8Padding(U8[] mau8Padding) {
        this.mau8Padding = mau8Padding;
    }

    public U8 getMu8TbNum() {
        return mu8TbNum;
    }

    public void setMu8TbNum(U8 mu8TbNum) {
        this.mu8TbNum = mu8TbNum;
    }

    private void validate(byte[] bytes) {
        if (bytes.length < L2P_MAC_DATA_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_MAC_DATA_STRU!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_MAC_DATA_STRU that = (L2P_MAC_DATA_STRU) o;

        if (mu8TbNum != null ? !mu8TbNum.equals(that.mu8TbNum) : that.mu8TbNum != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(mau8Padding, that.mau8Padding)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(mastMacTbInfo, that.mastMacTbInfo);

    }

    @Override
    public int hashCode() {
        int result = mu8TbNum != null ? mu8TbNum.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(mau8Padding);
        result = 31 * result + Arrays.hashCode(mastMacTbInfo);
        return result;
    }
}
