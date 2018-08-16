package com.android.szh.agileframedemo.api;

import com.android.szh.common.config.UrlConfig;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

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

    /**
     * 切换域名的特使接口
     * Add the Domain-Name header
     */
    @Headers({UrlConfig.FLAG_MULTIPLE_BASE_URL_KEY + UrlConfig.FLAG_MULTIPLE_BASE_URL_A})
    @GET("/v2/book/{id}")
    Observable<ResponseBody> testSwitchBaseUrl(@Path("id") int id);
}