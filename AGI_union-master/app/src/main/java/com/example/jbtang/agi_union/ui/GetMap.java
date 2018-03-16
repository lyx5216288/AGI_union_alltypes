package com.example.jbtang.agi_union.ui;

import com.example.jbtang.agi_union.dao.BaseStationinfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 刘洋旭 on 2016/11/29.
 */
public interface GetMap {

    @GET("/cell")
    Call<BaseStationinfo> getLbs(@Query("mcc") int mcc, @Query("mnc") int mnc, @Query("lac") int lac, @Query("ci") int cellid, @Query("output") String output);
}
