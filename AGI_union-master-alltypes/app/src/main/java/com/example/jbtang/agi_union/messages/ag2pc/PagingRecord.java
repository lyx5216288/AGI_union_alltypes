package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.type.U32;
import com.example.jbtang.agi_union.core.type.U8;

/**
 * Created by 刘洋旭 on 2017/8/7.
 */
public class PagingRecord {
    public PagingUE_Identity pagingUE_identity;
    public int cn_Domain;

    public PagingRecord(){
        pagingUE_identity=new PagingUE_Identity();
    }
}
