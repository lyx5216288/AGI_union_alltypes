package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U16;
import com.example.jbtang.agi_union.core.type.U32;
import com.example.jbtang.agi_union.core.type.U8;

import java.util.Arrays;

/**
 * Created by 刘洋旭 on 2017/8/3.
 */
public class L2P_RRCNAS_MSG_STRU {
    public U8 mu8LchType;
    public U8 mu8LchId;
    public U8 mu8RbId;
    public U8 mu8TBIndex;
    public U16 mu16MsgType;
    public U16 mu16RrcOrNas;
    public U16 mu16PduLength;
    public U16 mau16Padding;
    public U8[] mu8MessageName; //size=64
//    public U8[] mau8DATA; //size=(mu16PduLength+3)/4)*4

    public static final int byteArrayLen=68*U8.byteArrayLen+4*U16.byteArrayLen;

    public void setMu8LchType(U8 mu8LchType){
        this.mu8LchType=mu8LchType;
    }
    public U8 getMu8LchType(){
        return this.mu8LchType;
    }
    public void setMu8LchId(U8 mu8LchId){
        this.mu8LchId=mu8LchId;
    }
    public U8 getMu8LchId(){
        return this.mu8LchId;
    }
    public void setMu8RbId(U8 mu8RbId){
        this.mu8RbId=mu8RbId;
    }
    public U8 getMu8RbId(){
        return this.mu8RbId;
    }
    public void setMu8TBIndex(U8 mu8TBIndex){
        this.mu8TBIndex=mu8TBIndex;
    }
    public U8 getMu8TBIndex(){
        return this.mu8TBIndex;
    }
    public void setMu16MsgType(U16 mu16MsgType){
        this.mu16MsgType=mu16MsgType;
    }
    public U16 getMu16MsgType(){
        return this.mu16MsgType;
    }
    public void setMu16RrcOrNas(U16 mu16RrcOrNas){
        this.mu16RrcOrNas=mu16RrcOrNas;
    }
    public U16 getmu16RrcOrNas(){
        return this.mu16RrcOrNas;
    }
    public void setMu16PduLength(U16 mu16PduLength){
        this.mu16PduLength=mu16PduLength;
    }
    public U16 getMu16PduLength(){
        return this.mu16PduLength;
    }
    public void setMau16Padding(U16 mau16Padding){
        this.mau16Padding=mau16Padding;
    }
    public U16 getMau16Padding(){
        return this.mau16Padding;
    }

    public void setMu8MessageName(U8[] mu8MessageName){
        this.mu8MessageName=mu8MessageName;
    }
    public U8[] getMu8MessageName(){
        return this.mu8MessageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_RRCNAS_MSG_STRU that = (L2P_RRCNAS_MSG_STRU) o;

        if (mu8LchType != null ? !mu8LchType.equals(that.mu8LchType) : that.mu8LchType != null)
            return false;
        if (mu8LchId != null ? !mu8LchId.equals(that.mu8LchId) : that.mu8LchId != null)
            return false;
        if (mu8RbId != null ? !mu8RbId.equals(that.mu8RbId) : that.mu8RbId != null) return false;
        if (mu8TBIndex != null ? !mu8TBIndex.equals(that.mu8TBIndex) : that.mu8TBIndex != null)
            return false;
        if (mu16MsgType != null ? !mu16MsgType.equals(that.mu16MsgType) : that.mu16MsgType != null)
            return false;
        if (mu16RrcOrNas != null ? !mu16RrcOrNas.equals(that.mu16RrcOrNas) : that.mu16RrcOrNas != null)
            return false;
        if (mu16PduLength != null ? !mu16PduLength.equals(that.mu16PduLength) : that.mu16PduLength != null)
            return false;
        if (mau16Padding != null ? !mau16Padding.equals(that.mau16Padding) : that.mau16Padding != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(mu8MessageName, that.mu8MessageName);

    }

    @Override
    public int hashCode() {
        int result = mu8LchType != null ? mu8LchType.hashCode() : 0;
        result = 31 * result + (mu8LchId != null ? mu8LchId.hashCode() : 0);
        result = 31 * result + (mu8RbId != null ? mu8RbId.hashCode() : 0);
        result = 31 * result + (mu8TBIndex != null ? mu8TBIndex.hashCode() : 0);
        result = 31 * result + (mu16MsgType != null ? mu16MsgType.hashCode() : 0);
        result = 31 * result + (mu16RrcOrNas != null ? mu16RrcOrNas.hashCode() : 0);
        result = 31 * result + (mu16PduLength != null ? mu16PduLength.hashCode() : 0);
        result = 31 * result + (mau16Padding != null ? mau16Padding.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(mu8MessageName);
        return result;
    }

    public L2P_RRCNAS_MSG_STRU(){
        mu8LchType=new U8();
        mu8LchId=new U8();
        mu8RbId=new U8();
        mu8TBIndex=new U8();
        mu16MsgType=new U16();
        mu16RrcOrNas=new U16();
        mu16PduLength=new U16();
        mau16Padding=new U16();
        mu8MessageName=new U8[64];

    }

    public L2P_RRCNAS_MSG_STRU(byte[] bytes){
        validate(bytes);
        int pos = 0;
        mu8LchType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8LchId = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8RbId = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8TBIndex = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu16MsgType = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16RrcOrNas = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16PduLength = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mau16Padding = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu8MessageName = new U8[64];
        for (int i=0;i<64;i++){

            mu8MessageName[i]=new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        }


    }
    private void validate(byte[] bytes) {
        if (bytes.length < L2P_RRCNAS_MSG_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_RRCNAS_MSG_STRU!");
        }
    }


}
