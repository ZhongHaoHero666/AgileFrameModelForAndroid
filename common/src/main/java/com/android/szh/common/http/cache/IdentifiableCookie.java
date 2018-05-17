package com.android.szh.common.http.cache;

import okhttp3.Cookie;

/**
 * 实现了{@link #equals(Object)}方法和{@link #hashCode()}方法的{@link Cookie}的装饰类
 * 可以根据{@link Cookie}的属性(name、domain、path、secure、hostOnly)识别Cookie
 * 这种新的行为将有助于确定会话中的一个已经存在的Cookie必须覆盖。
 */
class IdentifiableCookie {

    private Cookie cookie;

    IdentifiableCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    Cookie getCookie() {
        return cookie;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IdentifiableCookie)) return false;
        IdentifiableCookie that = (IdentifiableCookie) other;
        return that.cookie.name().equals(this.cookie.name())
                && that.cookie.domain().equals(this.cookie.domain())
                && that.cookie.path().equals(this.cookie.path())
                && that.cookie.secure() == this.cookie.secure()
                && that.cookie.hostOnly() == this.cookie.hostOnly();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + cookie.name().hashCode();
        hash = 31 * hash + cookie.domain().hashCode();
        hash = 31 * hash + cookie.path().hashCode();
        hash = 31 * hash + (cookie.secure() ? 0 : 1);
        hash = 31 * hash + (cookie.hostOnly() ? 0 : 1);
        return hash;
    }

}
