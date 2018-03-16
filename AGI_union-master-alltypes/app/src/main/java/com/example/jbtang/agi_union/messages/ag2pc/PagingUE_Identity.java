package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.type.U32;

/**
 * Created by 刘洋旭 on 2017/8/7.
 */
public class PagingUE_Identity {
    public S_TMSI s_tmsi;
    public int[] imsi;
    public PagingUE_Identity(){
        s_tmsi=new S_TMSI();
    }
}
