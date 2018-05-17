package com.android.szh.common.http.interceptor;

import android.content.Context;
import android.util.Log;


import com.android.szh.common.utils.NetworkHelper;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 支持离线缓存，使用OKhttp自带的缓存功能
 */
public class CacheInterceptorOffline extends CacheInterceptor {

    private static final String TAG = "CacheInterceptorOffline";

    public CacheInterceptorOffline(Context context) {
        super(context);
    }

    public CacheInterceptorOffline(Context context, String cacheControlValue) {
        super(context, cacheControlValue);
    }

    public CacheInterceptorOffline(Context context, String cacheControlValue, String cacheOnlineControlValue) {
        super(context, cacheControlValue, cacheOnlineControlValue);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkHelper.isNetworkAvailable(context)) {
            Log.e(TAG, " no network load cache:" + request.cacheControl().toString());

            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, " + cacheControlValueOffline)
                    .build();
        }
        return chain.proceed(request);
    }
}

