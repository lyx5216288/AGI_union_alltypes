package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.MsgSendHelper;
import com.example.jbtang.agi_union.core.type.U32;

import java.util.Arrays;

/**
 * Created by 刘洋旭 on 2017/2/27.
 */
public class L2P_MAC_PDU_SUBHEADER_STRU {
    public U32 u32SubHeaderNum;
    public U32 u32HeadSize;
    public  L2P_MAC_PDU_SUBHEADER_INFO_STRU[] pduSubHeaderInfo;

    public static final int byteArrayLen = 32* L2P_MAC_PDU_SUBHEADER_INFO_STRU.byteArrayLen+2* U32.byteArrayLen;

    public L2P_MAC_PDU_SUBHEADER_STRU(){
        u32SubHeaderNum=new U32();
        u32HeadSize=new U32();
        pduSubHeaderInfo=new L2P_MAC_PDU_SUBHEADER_INFO_STRU[32];
    }

    public L2P_MAC_PDU_SUBHEADER_STRU(byte[] bytes){
        validate(bytes);
        int pos=0;
        u32SubHeaderNum=new U32(MsgSendHelper.getSubByteArray(bytes,pos, U32.byteArrayLen));
        pos+= U32.byteArrayLen;
        u32HeadSize=new U32(MsgSendHelper.getSubByteArray(bytes,pos, U32.byteArrayLen));
        pos+= U32.byteArrayLen;
        pduSubHeaderInfo=new L2P_MAC_PDU_SUBHEADER_INFO_STRU[32];
        for (int i=0;i<pduSubHeaderInfo.length;i++){
                pduSubHeaderInfo[i] = new L2P_MAC_PDU_SUBHEADER_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, L2P_MAC_PDU_SUBHEADER_INFO_STRU.byteArrayLen));
                pos += L2P_MAC_PDU_SUBHEADER_INFO_STRU.byteArrayLen;
        }



    }

    public U32 getU32SubHeaderNum() {
        return u32SubHeaderNum;
    }

    public void setU32SubHeaderNum(U32 u32SubHeaderNum) {
        this.u32SubHeaderNum = u32SubHeaderNum;
    }

    public U32 getU32HeadSize() {
        return u32HeadSize;
    }

    public void setU32HeadSize(U32 u32HeadSize) {
        this.u32HeadSize = u32HeadSize;
    }

    public L2P_MAC_PDU_SUBHEADER_INFO_STRU[] getPduSubHeaderInfo() {
        return pduSubHeaderInfo;
    }

    public void setPduSubHeaderInfo(L2P_MAC_PDU_SUBHEADER_INFO_STRU[] pduSubHeaderInfo) {
        this.pduSubHeaderInfo = pduSubHeaderInfo;
    }

    private void validate(byte[] bytes) {
        if (bytes.length < L2P_MAC_PDU_SUBHEADER_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_MAC_PDU_SUBHEADER_STRU!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L2P_MAC_PDU_SUBHEADER_STRU that = (L2P_MAC_PDU_SUBHEADER_STRU) o;

        if (u32SubHeaderNum != null ? !u32SubHeaderNum.equals(that.u32SubHeaderNum) : that.u32SubHeaderNum != null)
            return false;
        if (u32HeadSize != null ? !u32HeadSize.equals(that.u32HeadSize) : that.u32HeadSize != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(pduSubHeaderInfo, that.pduSubHeaderInfo);

    }

    @Override
    public int hashCode() {
        int result = u32SubHeaderNum != null ? u32SubHeaderNum.hashCode() : 0;
        result = 31 * result + (u32HeadSize != null ? u32HeadSize.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(pduSubHeaderInfo);
        return result;
    }
}
