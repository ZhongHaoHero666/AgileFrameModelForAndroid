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
     * @return 当前环境baseUrl
     */
    public static String getDominUrl() {
        return BuildConfig.ONLINE ? DOMAIN_ONLINE : DOMAIN_TEST;
    }
}
