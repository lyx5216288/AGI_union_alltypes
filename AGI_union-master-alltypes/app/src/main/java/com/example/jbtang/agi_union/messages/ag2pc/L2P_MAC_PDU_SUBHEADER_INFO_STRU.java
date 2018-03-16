package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U16;
import com.example.jbtang.agi_union.core.type.U8;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_MAC_PDU_SUBHEADER_INFO_STRU {
    public U8 u8Lcid;
    public U8 u8F;
    public U16 u16L;

    public static final int byteArrayLen = U16.byteArrayLen + 2 * U8.byteArrayLen;


    public L2P_MAC_PDU_SUBHEADER_INFO_STRU(){
        u8Lcid=new U8();
        u8F=new U8();
        u16L=new U16();
    }
    public L2P_MAC_PDU_SUBHEADER_INFO_STRU(byte[] bytes){
        validate(bytes);
        int pos=0;
        u8Lcid=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        u8F=new U8(MsgSendHelper.getSubByteArray(bytes,pos, U8.byteArrayLen));
        pos+= U8.byteArrayLen;
        u16L=new U16(MsgSendHelper.getSubByteArray(bytes,pos, U16.byteArrayLen));
    }


    private void validate(byte[] bytes) {
        if (bytes.length < L2P_MAC_PDU_SUBHEADER_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_MAC_PDU_SUBHEADER_INFO_STRU!");
        }
    }
    public U8 getU8Lcid() {
        return u8Lcid;
    }

    public void setU8Lcid(U8 u8Lcid) {
        this.u8Lcid = u8Lcid;
    }

    public U8 getU8F() {
        return u8F;
    }

    public void setU8F(U8 u8F) {
        this.u8F = u8F;
    }

    public U16 getU16L() {
        return u16L;
    }

    public void setU16L(U16 u16L) {
        this.u16L = u16L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_MAC_PDU_SUBHEADER_INFO_STRU that = (L2P_MAC_PDU_SUBHEADER_INFO_STRU) o;

        if (u8Lcid != null ? !u8Lcid.equals(that.u8Lcid) : that.u8Lcid != null) return false;
        if (u8F != null ? !u8F.equals(that.u8F) : that.u8F != null) return false;
        return u16L != null ? u16L.equals(that.u16L) : that.u16L == null;

    }

    @Override
    public int hashCode() {
        int result = u8Lcid != null ? u8Lcid.hashCode() : 0;
        result = 31 * result + (u8F != null ? u8F.hashCode() : 0);
        result = 31 * result + (u16L != null ? u16L.hashCode() : 0);
        return result;
    }
}
