package com.android.szh.common.http.interceptor;

import android.support.annotation.NonNull;

import com.android.szh.common.BaseApplication;
import com.android.szh.common.http.exception.NetErrorException;
import com.android.szh.common.utils.NetworkHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 *
 * @author liyunlong
 * @date 2018/1/15 10:04
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkHelper.isNetworkAvailable(BaseApplication.getInstance())) {
            throw new NetErrorException();
        }
        return chain.proceed(request);
    }

}
