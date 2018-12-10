package com.android.szh.agileframedemo.api;

import com.android.szh.common.config.UrlConfig;
import com.android.szh.common.http.BaseEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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


    /**
     * 自定解析对象实例
     *
     * Object为任意数据类型（要和json对象数据结构相对应）
     */
    @GET("/xxxx/xxxx.do")
    Observable<BaseEntity<Object>> getXXX(@QueryMap Map<String, String> map);
}