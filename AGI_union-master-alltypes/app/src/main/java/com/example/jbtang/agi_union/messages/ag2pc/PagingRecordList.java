package com.example.jbtang.agi_union.messages.ag2pc;

import com.example.jbtang.agi_union.core.type.U32;

/**
 * Created by 刘洋旭 on 2017/8/7.
 */
public class PagingRecordList {
    public U32 n;
    private PagingRecord[] pagingRecord; //size=n;
}
