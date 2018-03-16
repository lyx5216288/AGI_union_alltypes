package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U8;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_MAC_HARQ_STRU {
    public U8 mu8HarqID;                /* Harq process ID */
    public U8 mu8TransCnt;              /* RAR结构的个数 */
    public U8 mu8eResult;               /* 0-SUCCESS   1-FAILURE */
    public U8 mu8Pading;

    public static final int byteArrayLen =4* U8.byteArrayLen;

    public L2P_MAC_HARQ_STRU(){
        mu8HarqID=new U8();
        mu8TransCnt=new U8();
        mu8eResult=new U8();
        mu8Pading=new U8();
    }

    public L2P_MAC_HARQ_STRU(byte[] bytes){
        validate(bytes);
        int pos=0;
        mu8HarqID=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        mu8TransCnt=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        mu8eResult=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        mu8Pading=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));

    }

    private void validate(byte[] bytes) {
        if (bytes.length < L2P_MAC_HARQ_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_MAC_HARQ_STRU!");
        }
    }

    public U8 getMu8HarqID() {
        return mu8HarqID;
    }

    public void setMu8HarqID(U8 mu8HarqID) {
        this.mu8HarqID = mu8HarqID;
    }

    public U8 getMu8TransCnt() {
        return mu8TransCnt;
    }

    public void setMu8TransCnt(U8 mu8TransCnt) {
        this.mu8TransCnt = mu8TransCnt;
    }

    public U8 getMu8eResult() {
        return mu8eResult;
    }

    public void setMu8eResult(U8 mu8eResult) {
        this.mu8eResult = mu8eResult;
    }

    public U8 getMu8Pading() {
        return mu8Pading;
    }

    public void setMu8Pading(U8 mu8Pading) {
        this.mu8Pading = mu8Pading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_MAC_HARQ_STRU that = (L2P_MAC_HARQ_STRU) o;

        if (mu8HarqID != null ? !mu8HarqID.equals(that.mu8HarqID) : that.mu8HarqID != null)
            return false;
        if (mu8TransCnt != null ? !mu8TransCnt.equals(that.mu8TransCnt) : that.mu8TransCnt != null)
            return false;
        if (mu8eResult != null ? !mu8eResult.equals(that.mu8eResult) : that.mu8eResult != null)
            return false;
        return mu8Pading != null ? mu8Pading.equals(that.mu8Pading) : that.mu8Pading == null;

    }

    @Override
    public int hashCode() {
        int result = mu8HarqID != null ? mu8HarqID.hashCode() : 0;
        result = 31 * result + (mu8TransCnt != null ? mu8TransCnt.hashCode() : 0);
        result = 31 * result + (mu8eResult != null ? mu8eResult.hashCode() : 0);
        result = 31 * result + (mu8Pading != null ? mu8Pading.hashCode() : 0);
        return result;
    }
}
