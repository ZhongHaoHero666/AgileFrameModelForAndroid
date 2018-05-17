package com.android.szh.common.http.wrapper;

import android.support.annotation.NonNull;


import com.android.szh.common.http.exception.ConverterIOException;
import com.android.szh.common.http.exception.ExceptionCode;
import com.android.szh.common.http.exception.ExceptionEngine;
import com.android.szh.common.http.exception.HttpException;
import com.android.szh.common.utils.CharsetHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * {RxJava2CallAdapterFactory}的包装类(用于处理异常)
 */
public class RxJava2CallAdapterFactoryWrapper extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxJava2CallAdapterFactoryWrapper() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxJava2CallAdapterFactoryWrapper();
    }

    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new RxJava2CallAdapterWrapper<>(original.get(returnType, annotations, retrofit));
    }

    /**
     * {@link retrofit2.adapter.rxjava2.RxJava2CallAdapter}的包装类(用于RxJava2处理异常)
     */
    private static class RxJava2CallAdapterWrapper<R> implements CallAdapter<R, Observable<?>> {

        private final CallAdapter<?, ?> inner;

        RxJava2CallAdapterWrapper(CallAdapter<?, ?> inner) {
            this.inner = inner;
        }

        @Override
        public Type responseType() {
            return inner.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Observable<?> adapt(@NonNull final Call call) {
            return ((Observable) inner.adapt(call)).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                @Override
                public ObservableSource apply(Throwable throwable) throws Exception {
                    if (!(throwable instanceof retrofit2.HttpException)) {
                        int code = ExceptionEngine.getExceptionCode(throwable);
                        HttpException httpException = new HttpException(throwable, call.request());
                        httpException.setCode(code);
                        httpException.setDesc(ExceptionCode.get(code));
                        if (throwable instanceof ConverterIOException) {
                            ResponseBody responseBody = ((ConverterIOException) throwable).getResponseBody();
                            if (responseBody != null) {
                                BufferedSource source = responseBody.source();
                                Buffer buffer = source.buffer();
                                String responseContent = buffer.clone().readString(CharsetHelper.UTF_8);
                                httpException.setResponseContent(responseContent);
                            }
                        }
                        return Observable.error(httpException);
                    }
                    return Observable.error(throwable);
                }
            });
        }
    }

}
