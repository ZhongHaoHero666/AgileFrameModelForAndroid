package com.android.szh.common.http.cache;

import java.util.Collection;

import okhttp3.Cookie;

/**
 * 处理不稳定的Cookie会话存储的Cookie缓存接口
 */
public interface CookieCache extends Iterable<Cookie> {

    /**
     * 将所有的Cookie添加到Session中(已经存在的Cookie将被覆盖)
     *
     * @param cookies 需要添加的所有所有Cookie的集合
     */
    void addAll(Collection<Cookie> cookies);

    /**
     * 从Session中清除所有的Cookie
     */
    void clear();
}