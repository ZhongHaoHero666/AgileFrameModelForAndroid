package com.android.szh.common.http.cookie;

import okhttp3.CookieJar;

/**
 * 此接口继承{@link CookieJar}并添加用来清除Cookies的方法
 *
 */
interface ClearableCookieJar extends CookieJar {

    /**
     * 清除所有会话Cookie并保持所有的持久化Cookie
     */
    void clearSession();

    /**
     * 从持久性存储和缓存中清除所有的Cookie
     */
    void clear();
}
