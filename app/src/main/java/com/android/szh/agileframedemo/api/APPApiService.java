package com.android.szh.agileframedemo.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by sunzhonghao on 2018/5/16.
 * desc: 接口service
 */
public interface APPApiService {
    /**
     * 获取城市信息
     */
    @GET("/cityjson?ie=utf-8&qq-pf-to=pcqq.c2c")
    Observable<ResponseBody> getCityInfo();

}