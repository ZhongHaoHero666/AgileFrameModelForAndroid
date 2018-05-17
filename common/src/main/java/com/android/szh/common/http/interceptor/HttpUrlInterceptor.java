package com.android.szh.common.http.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求Url拦截器(处理Url)
 */
public class HttpUrlInterceptor implements Interceptor {

    private static final List<String> FILE_SUFFIX = new ArrayList<>();

    static {
        FILE_SUFFIX.add(".txt");
        FILE_SUFFIX.add(".xml");
        FILE_SUFFIX.add(".json");
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        HttpUrl httpUrl = original.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        String path = httpUrl.url().getPath();
        if (!TextUtils.isEmpty(path) && path.contains(".")) { // 静态文件后面添加随机数
            String suffix = path.substring(path.indexOf("."));
            if (!TextUtils.isEmpty(suffix) && FILE_SUFFIX.contains(suffix)) {
                urlBuilder.addQueryParameter("rn", String.valueOf(Math.random()));
            }
        }
        builder.url(urlBuilder.build());
        return chain.proceed(builder.build());
    }

}
