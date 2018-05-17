package com.android.szh.common.http.cookie;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * Cookie持久化存储的接口
 */
interface CookiePersistor {

    /**
     * 加载全部Cookie
     */
    List<Cookie> loadAll();

    /**
     * 持久化存储所有Cookie(已经存在的Cookie将被覆盖)
     *
     * @param cookies 需要持久化存储的所有Cookie的集合
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * 从持久化存储中移除指定的所有Cookie
     *
     * @param cookies 需要从持久化存储中移除的所有Cookie的集合
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * 从持久化存储中清除所有Cookie
     */
    void clear();

}
