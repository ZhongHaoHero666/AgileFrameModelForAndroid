package com.android.szh.common.config;

import com.android.szh.common.BuildConfig;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc: 静态url 管理类
 */

public class UrlConfig {
    /**
     * 正式域名
     */
    public static final String DOMAIN_ONLINE = "http://pv.sohu.com";

    /**
     * 测试域名
     */
    public static final String DOMAIN_TEST = "http://pv.sohu.com";


    /**
     * 正式域名_A(多个BaseUrl情况)
     */
    public static final String DOMAIN_ONLINE_A = "https://api.douban.com";

    /**
     * 测试域名_A(多个BaseUrl情况)
     */
    public static final String DOMAIN_TEST_A = "https://api.douban.com";




    /**
     * @return 当前环境baseUrl
     */
    public static String getDominUrl() {
        return BuildConfig.ONLINE ? DOMAIN_ONLINE : DOMAIN_TEST;
    }

    /**
     * @return 当前环境的另外一个baseUrl
     */
    public static String getDominUrlA() {
        return BuildConfig.ONLINE ? DOMAIN_ONLINE_A : DOMAIN_TEST_A;
    }


    //___________________________________BaseUrl 标识____________________________________________
    /**
     * BaseUrl_A 标识multiple
     */
    public static final String FLAG_MULTIPLE_BASE_URL_A = "FLAG_MULTIPLE_BASE_URL_A";
    /**
     * 切换baseUrl时的key
     */
    public static final String FLAG_MULTIPLE_BASE_URL_KEY = "Domain-Name:";
}
