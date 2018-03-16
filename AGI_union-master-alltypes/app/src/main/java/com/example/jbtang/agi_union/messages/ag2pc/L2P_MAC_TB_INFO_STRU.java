package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U16;
import com.example.jbtang.agi_union.core.type.U8;

import java.util.Arrays;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_MAC_TB_INFO_STRU {
    public U16 mu16TBLen;              /* TB块的大小 */
    public U16 mu16MacCeNum;           /* TB块中MAC CE个数,如果在协议跟踪配置中，MAC CE的bit位无效，则此值为0 */
    public U16 mu16SubHeaderLen;       /* TB块的MAC子头原始码流的长度，单位4 Byte, 如果在协议跟踪配置中，MACSUB HEAD的bit位无效，则此值为0，后续的BodyHead1数据结构无效 */
    public U8[]  mau8Padding;
    public L2P_MAC_HARQ_STRU mstHarqInfo;  /* TB块的HARQ信息 */


    public static final int byteArrayLen =2* U8.byteArrayLen + 3* U16.byteArrayLen + L2P_MAC_HARQ_STRU.byteArrayLen;

    public L2P_MAC_TB_INFO_STRU(){
        mu16TBLen=new U16();
        mu16MacCeNum=new U16();
        mu16SubHeaderLen=new U16();
        mau8Padding=new U8[2];
        mstHarqInfo=new L2P_MAC_HARQ_STRU();
    }

    public L2P_MAC_TB_INFO_STRU(byte[] bytes){
        validate(bytes);
        int pos=0;
        mu16TBLen=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
        pos+= U16.byteArrayLen;
        mu16MacCeNum=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
        pos+= U16.byteArrayLen;
        mu16SubHeaderLen=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
        pos+= U16.byteArrayLen;
        mau8Padding=new U8[2];
        for (int i=0;i<mau8Padding.length;i++){
            mau8Padding[i]=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
            pos+= U8.byteArrayLen;
        }
        mstHarqInfo=new L2P_MAC_HARQ_STRU(MsgSendHelper.getSubByteArray(bytes,pos, L2P_MAC_HARQ_STRU.byteArrayLen));
    }




    private void validate(byte[] bytes) {
        if (bytes.length < L2P_MAC_TB_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_MAC_TB_INFO_STRU!");
        }
    }

    public U16 getMu16TBLen() {
        return mu16TBLen;
    }

    public void setMu16TBLen(U16 mu16TBLen) {
        this.mu16TBLen = mu16TBLen;
    }

    public U16 getMu16MacCeNum() {
        return mu16MacCeNum;
    }

    public void setMu16MacCeNum(U16 mu16MacCeNum) {
        this.mu16MacCeNum = mu16MacCeNum;
    }

    public U16 getMu16SubHeaderLen() {
        return mu16SubHeaderLen;
    }

    public void setMu16SubHeaderLen(U16 mu16SubHeaderLen) {
        this.mu16SubHeaderLen = mu16SubHeaderLen;
    }

    public U8[] getMau8Padding() {
        return mau8Padding;
    }

    public void setMau8Padding(U8[] mau8Padding) {
        this.mau8Padding = mau8Padding;
    }

    public L2P_MAC_HARQ_STRU getMstHarqInfo() {
        return mstHarqInfo;
    }

    public void setMstHarqInfo(L2P_MAC_HARQ_STRU mstHarqInfo) {
        this.mstHarqInfo = mstHarqInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_MAC_TB_INFO_STRU that = (L2P_MAC_TB_INFO_STRU) o;

        if (mu16TBLen != null ? !mu16TBLen.equals(that.mu16TBLen) : that.mu16TBLen != null)
            return false;
        if (mu16MacCeNum != null ? !mu16MacCeNum.equals(that.mu16MacCeNum) : that.mu16MacCeNum != null)
            return false;
        if (mu16SubHeaderLen != null ? !mu16SubHeaderLen.equals(that.mu16SubHeaderLen) : that.mu16SubHeaderLen != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(mau8Padding, that.mau8Padding)) return false;
        return mstHarqInfo != null ? mstHarqInfo.equals(that.mstHarqInfo) : that.mstHarqInfo == null;

    }

    @Override
    public int hashCode() {
        int result = mu16TBLen != null ? mu16TBLen.hashCode() : 0;
        result = 31 * result + (mu16MacCeNum != null ? mu16MacCeNum.hashCode() : 0);
        result = 31 * result + (mu16SubHeaderLen != null ? mu16SubHeaderLen.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(mau8Padding);
        result = 31 * result + (mstHarqInfo != null ? mstHarqInfo.hashCode() : 0);
        return result;
    }
}
