package com.android.szh.common.http.wrapper;

import android.support.annotation.NonNull;

import com.android.szh.common.http.converter.StringConverterFactory;
import com.android.szh.common.http.exception.ConverterIOException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * {@link StringConverterFactory}的包装类(用于处理异常)
 */
public class StringConverterFactoryWrapper extends Converter.Factory {

    private final StringConverterFactory original;

    private StringConverterFactoryWrapper() {
        original = StringConverterFactory.create();
    }

    public static StringConverterFactoryWrapper create() {
        return new StringConverterFactoryWrapper();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverterWrapper<>(original.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit));
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringResponseBodyConverterWrapper<>(original.responseBodyConverter(type, annotations, retrofit));
    }

    /**
     * StringRequestBodyConverter的包装类 (用于RxJava2处理异常)
     */
    private static class StringRequestBodyConverterWrapper<T> implements Converter<T, RequestBody> {

        private final Converter<T, RequestBody> inner;

        StringRequestBodyConverterWrapper(Converter<T, RequestBody> inner) {
            this.inner = inner;
        }

        @Override
        public RequestBody convert(@NonNull T value) throws IOException {
            try {
                return inner.convert(value);
            } catch (IOException e) {
                throw new ConverterIOException(e);
            }
        }
    }

    /**
     * StringResponseBodyConverter的包装类(用于RxJava2处理异常)
     */
    private static class StringResponseBodyConverterWrapper<T> implements Converter<ResponseBody, T> {

        private final Converter<ResponseBody, T> inner;

        StringResponseBodyConverterWrapper(Converter<ResponseBody, T> inner) {
            this.inner = inner;
        }

        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            try {
                return inner.convert(value);
            } catch (IOException e) {
                throw new ConverterIOException(e, value);
            }
        }
    }

}
