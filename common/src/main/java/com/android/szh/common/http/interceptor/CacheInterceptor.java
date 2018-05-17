package com.android.szh.common.http.interceptor;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 设置缓存功能
 *
 * @author liyunlong
 * @date 2017/2/4 11:19
 */
public class CacheInterceptor implements Interceptor {

    private static final String TAG = "CacheInterceptor";
    protected Context context;
    protected String cacheControlValueOnline;
    protected String cacheControlValueOffline;
    //set cahe times is 3 days
    protected static final int MAX_STALE = 3 * 24 * 60 * 60;
    // read from cache for 60 s
    protected static final int MAX_STALE_ONLINE = 60;

    public CacheInterceptor(Context context) {
        this(context, String.format(Locale.getDefault(), "max-age=%d", MAX_STALE_ONLINE));
    }

    public CacheInterceptor(Context context, String cacheControlValue) {
        this(context, cacheControlValue, String.format(Locale.getDefault(), "max-age=%d", MAX_STALE));
    }

    public CacheInterceptor(Context context, String cacheControlValueOffline, String cacheControlValueOnline) {
        this.context = context;
        this.cacheControlValueOnline = cacheControlValueOnline;
        this.cacheControlValueOffline = cacheControlValueOffline;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");
        Log.e(TAG, MAX_STALE_ONLINE + "s load cache:" + cacheControl);
        if (TextUtils.isEmpty(cacheControl) || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age") || cacheControl.contains("max-stale")) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + MAX_STALE)
                    .build();

        } else {
            return originalResponse;
        }
    }
           /* Response response = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, " + cacheOnlineControlValue)
                    .build();
        } */

       /*else {
            *//*((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.load_cache, Toast.LENGTH_SHORT).show();
                }
            });*//*
            Log.e(TAG, " no network load cache");
          *//*  request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();*//*
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, " + cacheControlValue)
                    .build();
        }
    }*/
}
