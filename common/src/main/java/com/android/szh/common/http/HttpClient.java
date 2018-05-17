package com.android.szh.common.http;

import android.content.Context;
import android.support.annotation.NonNull;


import com.android.szh.common.config.UrlConfig;
import com.android.szh.common.http.api.BaseApiService;
import com.android.szh.common.http.cache.OkHttpCookieCache;
import com.android.szh.common.http.cookie.OkHttpCookieManager;
import com.android.szh.common.http.cookie.OkHttpCookiePersistor;
import com.android.szh.common.http.https.HttpsUtils;
import com.android.szh.common.http.interceptor.CacheInterceptor;
import com.android.szh.common.http.interceptor.CacheInterceptorOffline;
import com.android.szh.common.http.interceptor.HeadersInterceptor;
import com.android.szh.common.http.interceptor.HttpLoggingInterceptor;
import com.android.szh.common.http.interceptor.ParamsInterceptor;
import com.android.szh.common.http.model.HttpHeaders;
import com.android.szh.common.http.model.HttpParams;
import com.android.szh.common.utils.CheckHelper;


import java.io.File;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liyunlong
 * @date 2017/8/31 17:19
 */
public class HttpClient {

    private static final int DEFAULT_TIMEOUT = 8;
    private static final int DEFAULT_MAXIDLE_CONNECTIONS = 5;
    private static final long DEFAULT_KEEP_ALIVEDURATION = 8;
    private static final long MAX_CAHE_SIZE = 10 * 1024 * 1024;

    private Retrofit retrofit;
    private BaseApiService apiService;

    private HttpHeaders headers;
    private HttpParams params;
    private List<Interceptor> interceptors;
    private List<Interceptor> networkInterceptors;
    private List<Converter.Factory> converterFactories;
    private List<CallAdapter.Factory> callAdapterFactories;
    private okhttp3.Call.Factory callFactory;
    private Context context;
    private String baseUrl;
    private Executor callbackExecutor;
    private Boolean validateEagerly;
    private OkHttpClient okHttpClient;
    private Boolean logEnabled;
    private Boolean cookieEnabled;
    private Boolean cacheEnabled;
    private HostnameVerifier hostnameVerifier;
    private CertificatePinner certificatePinner;
    private CookieJar cookieJar;
    private TimeoutModel connectTimeout;
    private TimeoutModel readTimeout;
    private TimeoutModel writeTimeout;
    private Cache cache;
    private Proxy proxy;
    private HttpsUtils.SSLParams sslParams;
    private ConnectionPool connectionPool;
    private Authenticator authenticator;
    private Dispatcher dispatcher;
    private boolean followRedirects;
    private boolean followSslRedirects;
    private boolean retryOnConnectionFailure;

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private HttpClient(Builder builder) {
        this.context = builder.context;
        this.baseUrl = builder.baseUrl;

        // 初始化Retrofit.Builder
        Retrofit.Builder retrofitBuilder = generateRetrofit(builder);
        // 初始化OkHttpClient.Builder
        OkHttpClient.Builder okhttpBuilder = generateOkHttpClient(builder);
        // 创建OkHttpClient
        okHttpClient = okhttpBuilder.build();
        // 设置Retrofit client
        retrofitBuilder.client(okHttpClient);
        // 创建Retrofit
        retrofit = retrofitBuilder.build();
        // 创建BaseApiService
        apiService = retrofit.create(BaseApiService.class);
    }

    /**
     * 初始化Retrofit.Builder
     */
    @NonNull
    private Retrofit.Builder generateRetrofit(Builder builder) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder(); // 创建Retrofit.Builder对象
        // 设置一个API基础URL
        retrofitBuilder.baseUrl(UrlConfig.getDominUrl());
        // 添加用于序列化和反序列化对象的转化库
        converterFactories = builder.converterFactories;
        if (!converterFactories.isEmpty()) {
            for (Converter.Factory factory : converterFactories) {
                retrofitBuilder.addConverterFactory(factory);
            }
        } else {
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        }
        // 添加回调库
        callAdapterFactories = builder.callAdapterFactories;
        if (!callAdapterFactories.isEmpty()) {
            for (CallAdapter.Factory factory : callAdapterFactories) {
                retrofitBuilder.addCallAdapterFactory(factory);
            }
        } else {
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        // 指定用于创建Call实例的自定义调用工厂
        callFactory = builder.callFactory;
        if (callFactory != null) {
            retrofitBuilder.callFactory(callFactory);
        }
        // 设置回调方法执行器
        callbackExecutor = builder.callbackExecutor;
        if (callbackExecutor != null) {
            retrofitBuilder.callbackExecutor(callbackExecutor);
        }
        // 设置是否在调用{@link Retrofit#create(Class)}方法时检测接口定义是否正确
        validateEagerly = builder.validateEagerly;
        retrofitBuilder.validateEagerly(validateEagerly);
        return retrofitBuilder;
    }

    /**
     * 初始化OkHttpClient.Builder
     */
    @NonNull
    private OkHttpClient.Builder generateOkHttpClient(Builder builder) {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder(); // 创建OkHttpClient.Builder对象
        // 更新OkHttpClient.Builder
        okHttpClient = builder.httpClient;
        if (okHttpClient != null) {
            okhttpBuilder = okHttpClient.newBuilder();
        }
        // 设置连接超时时间
        connectTimeout = builder.connectTimeout;
        if (connectTimeout == null) {
            connectTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        okhttpBuilder.connectTimeout(connectTimeout.timeout, connectTimeout.unit);
        // 设置读取超时时间
        readTimeout = builder.readTimeout;
        if (readTimeout == null) {
            readTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        okhttpBuilder.readTimeout(readTimeout.timeout, readTimeout.unit);
        // 设置写入超时时间
        writeTimeout = builder.writeTimeout;
        if (writeTimeout == null) {
            writeTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        okhttpBuilder.writeTimeout(writeTimeout.timeout, writeTimeout.unit);
        // 设置请求头
        headers = builder.headers;
        if (!headers.isEmpty()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(CheckHelper.checkNotNull(headers, "header == null"));
            okhttpBuilder.addInterceptor(new HeadersInterceptor(httpHeaders));
        }
        // 设置请求参数
        params = builder.params;
        if (!params.isUrlEmpty()) {
            okhttpBuilder.addInterceptor(new ParamsInterceptor(CheckHelper.checkNotNull(params, "params == null")));
        }
        // 添加拦截器
        interceptors = builder.interceptors;
        if (!interceptors.isEmpty()) {
            okhttpBuilder.interceptors().addAll(interceptors);
        }
        // 添加网络拦截器
        networkInterceptors = builder.networkInterceptors;
        if (!networkInterceptors.isEmpty()) {
            okhttpBuilder.networkInterceptors().addAll(networkInterceptors);
        }
        // 设置用于复用HTTP和HTTPS连接的连接池
        connectionPool = builder.connectionPool;
        if (connectionPool == null) {
            connectionPool = new ConnectionPool(DEFAULT_MAXIDLE_CONNECTIONS, DEFAULT_KEEP_ALIVEDURATION, TimeUnit.SECONDS);
        }
        okhttpBuilder.connectionPool(connectionPool);
        // 设置HTTPS安全套接字工厂
        sslParams = builder.sslParams;
        if (sslParams != null) {
            okhttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        // 设置主机名验证
        hostnameVerifier = builder.hostnameVerifier;
        if (hostnameVerifier != null) {
            okhttpBuilder.hostnameVerifier(hostnameVerifier);
        }
        // 添加网络请求日志拦截器
        logEnabled = builder.logEnabled;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (logEnabled) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        okhttpBuilder.addInterceptor(loggingInterceptor);
        // 设置网络请求缓存
        cacheEnabled = builder.cacheEnabled;
        cache = builder.cache;
        if (cacheEnabled && cache != null) {
            okhttpBuilder.cache(cache);
        }
        // 设置Cookie管理器
        cookieEnabled = builder.cookieEnabled;
        cookieJar = builder.cookieJar;
        if (cookieEnabled && cookieJar == null) {
            okhttpBuilder.cookieJar(new OkHttpCookieManager(new OkHttpCookieCache(), new OkHttpCookiePersistor(context)));
        }
        if (cookieJar != null) {
            okhttpBuilder.cookieJar(cookieJar);
        }
        // 设置当异步请求执行策略
        dispatcher = builder.dispatcher;
        if (dispatcher != null) {
            okhttpBuilder.dispatcher(dispatcher);
        }
        // 设置身份验证
        authenticator = builder.authenticator;
        if (authenticator != null) {
            okhttpBuilder.authenticator(authenticator);
        }
        // 设置限制被信任的认证中心
        certificatePinner = builder.certificatePinner;
        if (certificatePinner != null) {
            okhttpBuilder.certificatePinner(certificatePinner);
        }
        // 设置代理
        proxy = builder.proxy;
        if (proxy != null) {
            okhttpBuilder.proxy(proxy);
        }
        // 设置是否支持HTTP重定向
        followRedirects = builder.followRedirects;
        okhttpBuilder.followRedirects(followRedirects);
        // 设置是否支持HTTPS重定向
        followSslRedirects = builder.followSslRedirects;
        okhttpBuilder.followSslRedirects(followSslRedirects);
        // 设置是否允许连接失败时重试
        retryOnConnectionFailure = builder.retryOnConnectionFailure;
        okhttpBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
        return okhttpBuilder;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public BaseApiService getApiService() {
        return apiService;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpParams getParams() {
        return params;
    }

    /**
     * 创建ApiService
     */
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 更新baseUrl
     *
     * @param baseUrl
     */
    public void updateBaseUrl(String baseUrl) {
        retrofit = retrofit.newBuilder()
                .baseUrl(UrlConfig.getDominUrl())
                .build();
    }

    /**
     * 设置是否开启日志打印
     */
    public void setLogEnabled(boolean logEnabled) {
        List<Interceptor> interceptors = okHttpClient.interceptors();
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        for (Interceptor interceptor : interceptors) {
            if (interceptor instanceof HttpLoggingInterceptor) {
                HttpLoggingInterceptor loggingInterceptor = (HttpLoggingInterceptor) interceptor;
                if (logEnabled) {
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                } else {
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                }
                break;
            }
        }
    }

    /**
     * Retrofit和OkHttpClient构建器
     */
    public static final class Builder {

        private HttpHeaders headers = new HttpHeaders();
        private HttpParams params = new HttpParams();
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<Interceptor> networkInterceptors = new ArrayList<>();
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private okhttp3.Call.Factory callFactory;
        private Context context;
        private String baseUrl;
        private Executor callbackExecutor;
        private boolean validateEagerly;
        private OkHttpClient httpClient;
        private Boolean logEnabled = false;
        private Boolean cookieEnabled = false;
        private Boolean cacheEnabled = true;
        private HostnameVerifier hostnameVerifier;
        private CertificatePinner certificatePinner;
        private HttpsUtils.SSLParams sslParams;
        private CookieJar cookieJar;
        private TimeoutModel connectTimeout;
        private TimeoutModel readTimeout;
        private TimeoutModel writeTimeout;
        private Cache cache;
        private Proxy proxy;
        private File cacheDirectory;
        private ConnectionPool connectionPool;
        private Interceptor rewriteCacheControlInterceptor;
        private Interceptor rewriteCacheControlInterceptorOffline;
        private Authenticator authenticator;
        private Dispatcher dispatcher;
        private boolean followRedirects = true;
        private boolean followSslRedirects = true;
        private boolean retryOnConnectionFailure = true;

        public Builder(Context context) {
            this.context = context;
        }

        Builder(HttpClient client) {
            this.context = client.context;
            this.headers = client.headers;
            this.params = client.params;
            this.interceptors = client.interceptors;
            this.networkInterceptors = client.networkInterceptors;
            this.converterFactories = client.converterFactories;
            this.callAdapterFactories = client.callAdapterFactories;
            this.callFactory = client.callFactory;
            this.baseUrl = client.baseUrl;
            this.callbackExecutor = client.callbackExecutor;
            this.validateEagerly = client.validateEagerly;
            this.httpClient = client.okHttpClient;
            this.logEnabled = client.logEnabled;
            this.cookieEnabled = client.cookieEnabled;
            this.cacheEnabled = client.cacheEnabled;
            this.hostnameVerifier = client.hostnameVerifier;
            this.certificatePinner = client.certificatePinner;
            this.cookieJar = client.cookieJar;
            this.connectTimeout = client.connectTimeout;
            this.readTimeout = client.readTimeout;
            this.writeTimeout = client.writeTimeout;
            this.cache = client.cache;
            this.proxy = client.proxy;
            this.sslParams = client.sslParams;
            this.connectionPool = client.connectionPool;
            this.authenticator = client.authenticator;
            this.dispatcher = client.dispatcher;
            this.followRedirects = client.followRedirects;
            this.followSslRedirects = client.followSslRedirects;
            this.retryOnConnectionFailure = client.retryOnConnectionFailure;
        }

        /**
         * 设置一个API基础URL(baseUlr必须以"/"结束，不然会抛出IllegalArgumentException异常)
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = CheckHelper.checkNotNull(baseUrl, "baseUrl == null");
            return this;
        }

        /**
         * 设置转化库
         */
        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactories.add(factory);
            return this;
        }

        /**
         * 设置回调库
         */
        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.callAdapterFactories.add(factory);
            return this;
        }

        /**
         * 设置回调方法执行器
         */
        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = CheckHelper.checkNotNull(executor, "executor == null");
            return this;
        }

        /**
         * 设置是否在调用{@link Retrofit#create(Class)}方法时检测接口定义是否正确
         */
        public Builder validateEagerly(boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }

        /**
         * 设置OkHttp网络请求
         */
        public Builder client(OkHttpClient client) {
            this.httpClient = CheckHelper.checkNotNull(client, "client == null");
            return this;
        }

        /**
         * 设置用于创建Call实例的自定义调用工厂
         */
        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.callFactory = CheckHelper.checkNotNull(factory, "factory == null");
            return this;
        }

        /**
         * 设置连接超时时间
         *
         * @param timeout 连接超时时间(单位：秒)
         */
        public Builder connectTimeout(int timeout) {
            return connectTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * 设置连接超时时间
         *
         * @param timeout 连接超时时间
         * @param unit    单位
         */
        public Builder connectTimeout(int timeout, TimeUnit unit) {
            if (timeout != -1) {
                this.connectTimeout = new TimeoutModel(timeout, unit);
            } else {
                this.connectTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }
            return this;
        }

        /**
         * 设置读取超时时间
         *
         * @param timeout 连接超时时间(单位：秒)
         */
        public Builder readTimeout(int timeout) {
            return readTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * 设置读取超时时间
         *
         * @param timeout 连接超时时间
         * @param unit    单位
         */
        public Builder readTimeout(int timeout, TimeUnit unit) {
            if (timeout != -1) {
                this.readTimeout = new TimeoutModel(timeout, unit);
            } else {
                this.readTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }
            return this;
        }

        /**
         * 设置写入超时时间
         *
         * @param timeout 连接超时时间(单位：秒)
         */
        public Builder writeTimeout(int timeout) {
            return writeTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * 设置写入超时时间
         *
         * @param timeout 连接超时时间
         * @param unit    单位
         */
        public Builder writeTimeout(int timeout, TimeUnit unit) {
            if (timeout != -1) {
                this.writeTimeout = new TimeoutModel(timeout, unit);
            } else {
                this.writeTimeout = new TimeoutModel(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }
            return this;
        }

        /**
         * 设置用于复用HTTP和HTTPS连接的连接池
         */
        public Builder connectionPool(ConnectionPool connectionPool) {
            if (connectionPool == null) throw new NullPointerException("connectionPool == null");
            this.connectionPool = connectionPool;
            return this;
        }

        /**
         * 设置是否开启日志打印
         */
        public Builder setLogEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        /**
         * 设置是否启用Cookie
         */
        public Builder setCookieEnabled(boolean cookieEnabled) {
            this.cookieEnabled = cookieEnabled;
            return this;
        }

        /**
         * 设置是否启用缓存
         */
        public Builder setCacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        /**
         * 设置是否支持HTTP重定向(默认为true)
         */
        public Builder followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        /**
         * 设置是否支持HTTPS重定向(默认为true)
         */
        public Builder followSslRedirects(boolean followSslRedirects) {
            this.followSslRedirects = followSslRedirects;
            return this;
        }

        /**
         * 设置是否允许连接失败时重试(默认为true)
         */
        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        /**
         * 设置代理
         */
        public Builder proxy(Proxy proxy) {
            this.proxy = CheckHelper.checkNotNull(proxy, "proxy == null");
            return this;
        }

        /**
         * 添加请求头
         */
        public Builder addHeader(String key, String object) {
            this.headers.put(key, object);
            return this;
        }

        /**
         * 添加请求头
         */
        public Builder addHeader(Map<String, String> headers) {
            this.headers.put(CheckHelper.checkNotNull(headers, "header == null"));
            return this;
        }

        /**
         * 设置请求头
         */
        public Builder setHeader(Map<String, String> headers) {
            this.headers.clear();
            if (headers != null && !headers.isEmpty()) {
                this.headers.put(headers);
            }
            return this;
        }

        /**
         * 清除请求头
         */
        public Builder clearHeader() {
            this.headers.clear();
            return this;
        }

        /**
         * 添加请求参数
         */
        public Builder addParameters(String key, String param) {
            this.params.put(key, param);
            return this;
        }

        /**
         * 添加请求参数
         */
        public Builder addParameters(Map<String, String> params) {
            this.params.put(CheckHelper.checkNotNull(params, "params == null"));
            return this;
        }

        /**
         * 设置请求参数
         */
        public Builder setParameters(Map<String, String> params) {
            this.params.clear();
            if (params != null && !params.isEmpty()) {
                this.params.put(params);
            }
            return this;
        }

        /**
         * 清除请求参数
         */
        public Builder clearParameters() {
            this.params.clear();
            return this;
        }

        /**
         * 添加拦截器
         */
        public Builder addInterceptor(Interceptor interceptor) {
            this.interceptors.add(CheckHelper.checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 添加网络连接器
         */
        public Builder addNetworkInterceptor(Interceptor interceptor) {
            this.networkInterceptors.add(CheckHelper.checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 设置Cookie管理器
         */
        public Builder cookieJar(CookieJar cookieJar) {
            if (cookieJar == null) throw new NullPointerException("cookieJar == null");
            this.cookieJar = cookieJar;
            return this;
        }

        /**
         * https的全局自签名证书
         */
        public Builder setCertificates(InputStream... certificates) {
            this.sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates);
            return this;
        }

        /**
         * https双向认证证书
         */
        public Builder setCertificates(InputStream bksFile, String password, InputStream... certificates) {
            this.sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
            return this;
        }

        /**
         * 设置主机名验证(全局访问规则)
         */
        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * 设置限制被信任的认证中心
         */
        public Builder addCertificatePinner(CertificatePinner certificatePinner) {
            this.certificatePinner = certificatePinner;
            return this;
        }

        /**
         * 设置身份验证
         */
        public Builder setAuthenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        /**
         * 设置当异步请求执行策略
         */
        public Builder setDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        /**
         * 设置缓存目录
         */
        public Builder setCacheDirectory(File cacheDirectory) {
            this.cacheDirectory = cacheDirectory;
            return this;
        }

        /**
         * 设置缓存
         */
        public Builder addCache(Cache cache) {
            int maxStale = 60 * 60 * 24 * 3;
            return addCache(cache, maxStale);
        }

        /**
         * 设置缓存
         */
        public Builder addCache(Cache cache, final int cacheTime) {
            addCache(cache, String.format(Locale.getDefault(), "max-age=%1$d", cacheTime));
            return this;
        }

        /**
         * 设置缓存
         */
        private Builder addCache(Cache cache, final String cacheControlValue) {
            rewriteCacheControlInterceptor = new CacheInterceptor(context, cacheControlValue);
            rewriteCacheControlInterceptorOffline = new CacheInterceptorOffline(context, cacheControlValue);
            addNetworkInterceptor(rewriteCacheControlInterceptor);
            addNetworkInterceptor(rewriteCacheControlInterceptorOffline);
            addInterceptor(rewriteCacheControlInterceptorOffline);
            this.cache = cache;
            return this;
        }

        /**
         * 创建{@link OkHttpClient}实例和{@link Retrofit}实例
         */
        public HttpClient build() {
            if (baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            if (cacheEnabled) {
                if (cacheDirectory == null) {
                    cacheDirectory = new File(context.getCacheDir(), "httpcache");
                }
                if (cache == null) {
                    cache = new Cache(cacheDirectory, MAX_CAHE_SIZE);
                }
                addCache(cache);
            }

            return new HttpClient(this);
        }
    }

    /**
     * 超时时间信息(包含时间和单位)
     */
    private static class TimeoutModel {

        private long timeout;
        private TimeUnit unit;

        TimeoutModel(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
        }
    }

}
