package com.android.szh.common.http;

import java.util.HashMap;

/**
 * 网络请求配置信息
 */
public class HttpConfig {
    public static final String HTTP_TAG = "HttpClient";


    public static final int READ_TIME_OUT = 10;
    public static final int WRITE_TIME_OUT = 10;
    public static final int CONNECT_TIME_OUT = 10;

    /**
     * 获取网络请求公用请求头
     */
    public static HashMap<String, String> getBaseHeaders() {
        HashMap<String, String> params = new HashMap<>();
        //此处封装公用的请求头
        return params;
    }

    /**
     * 获取网络请求基础参数
     */
    public static HashMap<String, Object> getCommonRequestParams() {
        HashMap<String, Object> params = new HashMap<>();
        //此处封装公用的请求体参数集合
        return params;
    }
}
