package com.android.szh.common.http.interceptor;

import android.support.annotation.NonNull;

import com.android.szh.common.http.HttpConfig;
import com.android.szh.common.http.model.HttpHeaders;
import com.android.szh.common.logger.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头拦截器(配置公共头部)
 *
 * @author liyunlong
 * @date 2017/8/31 17:21
 */
public class HeadersInterceptor implements Interceptor {

    private HttpHeaders headers;

    public HeadersInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers.headersMap.isEmpty()) {
            return chain.proceed(builder.build());
        }
        try {
            Set<Map.Entry<String, String>> entrySet = headers.headersMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                builder.addHeader(entry.getKey(), entry.getValue()).build();
            }
        } catch (Exception e) {
            Logger.e(HttpConfig.HTTP_TAG, e);
        }
        return chain.proceed(builder.build());
    }

}
