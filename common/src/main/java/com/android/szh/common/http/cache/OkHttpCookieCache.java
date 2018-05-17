package com.android.szh.common.http.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Cookie;

/**
 * OkHttp的Cookie缓存
 */
public class OkHttpCookieCache implements CookieCache {

    private Set<IdentifiableCookie> cookieSet;

    public OkHttpCookieCache() {
        cookieSet = new HashSet<>();
    }

    @Override
    public void addAll(Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            IdentifiableCookie identifiableCookie = new IdentifiableCookie(cookie);
            if (cookieSet.contains(identifiableCookie)) {
                cookieSet.remove(identifiableCookie);
            }
            cookieSet.add(identifiableCookie);
        }
    }

    @Override
    public void clear() {
        cookieSet.clear();
    }

    @Override
    public Iterator<Cookie> iterator() {
        return new SetCookieCacheIterator();
    }

    private class SetCookieCacheIterator implements Iterator<Cookie> {

        private Iterator<IdentifiableCookie> iterator;

        SetCookieCacheIterator() {
            iterator = cookieSet.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Cookie next() {
            return iterator.next().getCookie();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
