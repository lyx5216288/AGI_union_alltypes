package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U16;
import com.example.jbtang.agi_union.core.type.U32;
import com.example.jbtang.agi_union.core.type.U8;

import java.util.Arrays;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class l2P_PROTOCOL_DATA_HEADER_STRU {
    public U8[] mau8TimeStampH; //size=4
    public U32 mu32TimeStampL;
    public U16 mu16EARFCN;
    public U16 mu16PCI;
    public U16 mu16FrameNumber;
    public U8 mu8SubFrameNumber;
    public U8 mu8Direction;
    public U8 mu8PhyChType;
    public U8 mu8TrChType;
    public U8 mu8RntiType;
    public U8 mau8Padding;
    public U32 mu32RntiValue;

    public static final int byteArrayLen = 3 * U16.byteArrayLen + (4+6) * U8.byteArrayLen+2* U32.byteArrayLen;

    public l2P_PROTOCOL_DATA_HEADER_STRU(){
        mau8TimeStampH=new U8[4];
        mu32TimeStampL=new U32();
        mu16EARFCN=new U16();
        mu16PCI=new U16();
        mu16FrameNumber=new U16();
        mu8SubFrameNumber=new U8();
        mu8Direction=new U8();
        mu8PhyChType=new U8();
        mu8TrChType=new U8();
        mu8RntiType=new U8();
        mau8Padding=new U8();
        mu32RntiValue=new U32();

    }
    public l2P_PROTOCOL_DATA_HEADER_STRU(byte[] bytes ){
        validate(bytes);
        int pos = 0;
         // size = 21
        mau8TimeStampH = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        };
        mu32TimeStampL=new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos+= U32.byteArrayLen;

        mu16EARFCN = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16PCI = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16FrameNumber = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu8SubFrameNumber = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8Direction = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8PhyChType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8TrChType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8RntiType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mau8Padding = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu32RntiValue=new U32(MsgSendHelper.getSubByteArray(bytes,pos, U32.byteArrayLen));


    }
    private void validate(byte[] bytes) {
        if (bytes.length < l2P_PROTOCOL_DATA_HEADER_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message l2P_PROTOCOL_DATA_HEADER_STRU!");
        }
    }
    public U8[] getMau8TimeStampH() {
        return mau8TimeStampH;
    }

    public void setMau8TimeStampH(U8[] mau8TimeStampH) {
        this.mau8TimeStampH = mau8TimeStampH;
    }

    public U32 getMu32TimeStampL() {
        return mu32TimeStampL;
    }

    public void setMu32TimeStampL(U32 mu32TimeStampL) {
        this.mu32TimeStampL = mu32TimeStampL;
    }

    public U16 getMu16EARFCN() {
        return mu16EARFCN;
    }

    public void setMu16EARFCN(U16 mu16EARFCN) {
        this.mu16EARFCN = mu16EARFCN;
    }

    public U16 getMu16PCI() {
        return mu16PCI;
    }

    public void setMu16PCI(U16 mu16PCI) {
        this.mu16PCI = mu16PCI;
    }

    public U16 getMu16FrameNumber() {
        return mu16FrameNumber;
    }

    public void setMu16FrameNumber(U16 mu16FrameNumber) {
        this.mu16FrameNumber = mu16FrameNumber;
    }

    public U8 getMu8SubFrameNumber() {
        return mu8SubFrameNumber;
    }

    public void setMu8SubFrameNumber(U8 mu8SubFrameNumber) {
        this.mu8SubFrameNumber = mu8SubFrameNumber;
    }

    public U8 getMu8Direction() {
        return mu8Direction;
    }

    public void setMu8Direction(U8 mu8Direction) {
        this.mu8Direction = mu8Direction;
    }

    public U8 getMu8PhyChType() {
        return mu8PhyChType;
    }

    public void setMu8PhyChType(U8 mu8PhyChType) {
        this.mu8PhyChType = mu8PhyChType;
    }

    public U8 getMu8TrChType() {
        return mu8TrChType;
    }

    public void setMu8TrChType(U8 mu8TrChType) {
        this.mu8TrChType = mu8TrChType;
    }

    public U8 getMu8RntiType() {
        return mu8RntiType;
    }

    public void setMu8RntiType(U8 mu8RntiType) {
        this.mu8RntiType = mu8RntiType;
    }

    public U8 getMau8Padding() {
        return mau8Padding;
    }

    public void setMau8Padding(U8 mau8Padding) {
        this.mau8Padding = mau8Padding;
    }

    public U32 getMu32RntiValue() {
        return mu32RntiValue;
    }

    public void setMu32RntiValue(U32 mu32RntiValue) {
        this.mu32RntiValue = mu32RntiValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        l2P_PROTOCOL_DATA_HEADER_STRU that = (l2P_PROTOCOL_DATA_HEADER_STRU) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(mau8TimeStampH, that.mau8TimeStampH)) return false;
        if (mu32TimeStampL != null ? !mu32TimeStampL.equals(that.mu32TimeStampL) : that.mu32TimeStampL != null)
            return false;
        if (mu16EARFCN != null ? !mu16EARFCN.equals(that.mu16EARFCN) : that.mu16EARFCN != null)
            return false;
        if (mu16PCI != null ? !mu16PCI.equals(that.mu16PCI) : that.mu16PCI != null) return false;
        if (mu16FrameNumber != null ? !mu16FrameNumber.equals(that.mu16FrameNumber) : that.mu16FrameNumber != null)
            return false;
        if (mu8SubFrameNumber != null ? !mu8SubFrameNumber.equals(that.mu8SubFrameNumber) : that.mu8SubFrameNumber != null)
            return false;
        if (mu8Direction != null ? !mu8Direction.equals(that.mu8Direction) : that.mu8Direction != null)
            return false;
        if (mu8PhyChType != null ? !mu8PhyChType.equals(that.mu8PhyChType) : that.mu8PhyChType != null)
            return false;
        if (mu8TrChType != null ? !mu8TrChType.equals(that.mu8TrChType) : that.mu8TrChType != null)
            return false;
        if (mu8RntiType != null ? !mu8RntiType.equals(that.mu8RntiType) : that.mu8RntiType != null)
            return false;
        if (mau8Padding != null ? !mau8Padding.equals(that.mau8Padding) : that.mau8Padding != null)
            return false;
        return mu32RntiValue != null ? mu32RntiValue.equals(that.mu32RntiValue) : that.mu32RntiValue == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(mau8TimeStampH);
        result = 31 * result + (mu32TimeStampL != null ? mu32TimeStampL.hashCode() : 0);
        result = 31 * result + (mu16EARFCN != null ? mu16EARFCN.hashCode() : 0);
        result = 31 * result + (mu16PCI != null ? mu16PCI.hashCode() : 0);
        result = 31 * result + (mu16FrameNumber != null ? mu16FrameNumber.hashCode() : 0);
        result = 31 * result + (mu8SubFrameNumber != null ? mu8SubFrameNumber.hashCode() : 0);
        result = 31 * result + (mu8Direction != null ? mu8Direction.hashCode() : 0);
        result = 31 * result + (mu8PhyChType != null ? mu8PhyChType.hashCode() : 0);
        result = 31 * result + (mu8TrChType != null ? mu8TrChType.hashCode() : 0);
        result = 31 * result + (mu8RntiType != null ? mu8RntiType.hashCode() : 0);
        result = 31 * result + (mau8Padding != null ? mau8Padding.hashCode() : 0);
        result = 31 * result + (mu32RntiValue != null ? mu32RntiValue.hashCode() : 0);
        return result;
    }
}
