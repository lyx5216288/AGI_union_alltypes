package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U16;
import com.example.jbtang.agi_union.messages.base.MsgHeader;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_PROTOCOL_DATA {
    public MsgHeader header;
    public l2P_PROTOCOL_DATA_HEADER_STRU mstProtocolDataHeader;
    public U16 mu16DataType;
    public U16 mu16DataLength;

    public static final int byteArrayLen = l2P_PROTOCOL_DATA_HEADER_STRU.byteArrayLen + 2* U16.byteArrayLen+ MsgHeader.byteArrayLen;


    public L2P_PROTOCOL_DATA(){
        header=new MsgHeader();
        mstProtocolDataHeader=new l2P_PROTOCOL_DATA_HEADER_STRU();
        mu16DataType=new U16();
        mu16DataLength=new U16();
    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public L2P_PROTOCOL_DATA(byte[] bytes){
        validate(bytes);
        int pos=0;
        header=new MsgHeader(MsgSendHelper.getSubByteArray(bytes,pos, MsgHeader.byteArrayLen));
        pos+= MsgHeader.byteArrayLen;

        mstProtocolDataHeader=new l2P_PROTOCOL_DATA_HEADER_STRU(MsgSendHelper.getSubByteArray(bytes,pos, l2P_PROTOCOL_DATA_HEADER_STRU.byteArrayLen));
        pos+= l2P_PROTOCOL_DATA_HEADER_STRU.byteArrayLen;
        mu16DataType=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
        pos+= U16.byteArrayLen;
        mu16DataLength=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
        pos+= U16.byteArrayLen;
    }
    private void validate(byte[] bytes) {
        if (bytes.length < L2P_PROTOCOL_DATA.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message l2P_PROTOCOL_DATA_HEADER_STRU!");
        }
    }


    public l2P_PROTOCOL_DATA_HEADER_STRU getMstProtocolDataHeader() {
        return mstProtocolDataHeader;
    }

    public void setMstProtocolDataHeader(l2P_PROTOCOL_DATA_HEADER_STRU mstProtocolDataHeader) {
        this.mstProtocolDataHeader = mstProtocolDataHeader;
    }

    public U16 getMu16DataType() {
        return mu16DataType;
    }

    public void setMu16DataType(U16 mu16DataType) {
        this.mu16DataType = mu16DataType;
    }

    public U16 getMu16DataLength() {
        return mu16DataLength;
    }

    public void setMu16DataLength(U16 mu16DataLength) {
        this.mu16DataLength = mu16DataLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_PROTOCOL_DATA that = (L2P_PROTOCOL_DATA) o;

        if (mstProtocolDataHeader != null ? !mstProtocolDataHeader.equals(that.mstProtocolDataHeader) : that.mstProtocolDataHeader != null)
            return false;
        if (mu16DataType != null ? !mu16DataType.equals(that.mu16DataType) : that.mu16DataType != null)
            return false;
        return mu16DataLength != null ? mu16DataLength.equals(that.mu16DataLength) : that.mu16DataLength == null;

    }

    @Override
    public int hashCode() {
        int result = mstProtocolDataHeader != null ? mstProtocolDataHeader.hashCode() : 0;
        result = 31 * result + (mu16DataType != null ? mu16DataType.hashCode() : 0);
        result = 31 * result + (mu16DataLength != null ? mu16DataLength.hashCode() : 0);
        return result;
    }
}
