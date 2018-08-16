package com.android.szh.common.http;

import com.android.szh.common.BaseApplication;
import com.android.szh.common.config.UrlConfig;
import com.android.szh.common.http.api.BaseApiService;
import com.android.szh.common.http.interceptor.HttpUrlInterceptor;
import com.android.szh.common.http.interceptor.NetworkInterceptor;
import com.android.szh.common.http.model.HttpHeaders;
import com.android.szh.common.http.model.HttpParams;
import com.android.szh.common.http.wrapper.GsonConverterFactoryWrapper;
import com.android.szh.common.http.wrapper.RxJava2CallAdapterFactoryWrapper;
import com.android.szh.common.rxjava.HttpObserver;
import com.android.szh.common.rxjava.RxJavaHelper;
import com.android.szh.common.rxjava.transformer.ObservableTransformerAsync;
import com.android.szh.common.rxjava.transformer.ObservableTransformerError;
import com.android.szh.common.rxjava.transformer.ObservableTransformerSync;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.http.Url;

/**
 * HttpClient管理器(单例)
 */
public class HttpManager {

    private HttpClient httpClient;

    private static class SingletonHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new HttpClient.Builder(BaseApplication.getInstance())
                    .baseUrl(UrlConfig.getDominUrl())
                    .readTimeout(HttpConfig.READ_TIME_OUT)
                    .writeTimeout(HttpConfig.WRITE_TIME_OUT)
                    .connectTimeout(HttpConfig.CONNECT_TIME_OUT)
                    .validateEagerly(true)
                    .setCacheEnabled(false)
                    .setCookieEnabled(true)
                    .addHeader(HttpConfig.getBaseHeaders())
                    .setLogEnabled(BaseApplication.isDebugModel())
                    .addInterceptor(new HttpUrlInterceptor())
                    .addInterceptor(new NetworkInterceptor())
                    .addConverterFactory(GsonConverterFactoryWrapper.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactoryWrapper.create())
                    .build();
        }
        return httpClient;
    }

    /**
     * 重新初始化，登录登出时更新token
     */
    public void reinitialize() {
        httpClient = null;
        getInstance();
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * 返回{@link Retrofit}对象
     */
    public Retrofit getRetrofit() {
        return getHttpClient().getRetrofit();
    }

    /**
     * 返回{@link OkHttpClient}对象
     */
    public OkHttpClient getOkHttpClient() {
        return getHttpClient().getOkHttpClient();
    }

    /**
     * 返回{@link BaseApiService}对象
     */
    public BaseApiService getApiService() {
        return create(BaseApiService.class);
    }

    /**
     * 返回{@link HttpHeaders}对象
     */
    public HttpHeaders getHeaders() {
        return getHttpClient().getHeaders();
    }

    /**
     * 返回{@link HttpParams}对象
     */
    public HttpParams getBaseParams() {
        return getHttpClient().getParams();
    }

    /**
     * 更新baseUrl
     */
    public void updateBaseUrl(String baseUrl) {
        getHttpClient().updateBaseUrl(baseUrl);
    }


    /**
     * 创建ApiService
     */
    public <T> T create(final Class<T> service) {
        //修改baseUrl,不需要直接newBuild，和apiService中的@Hearder注解结合
        return getHttpClient().create(service);
    }

    /*
     *当增加一个baseUrl时，需要在if 语句中添加UrlConfig.Falg判断
     *
     * @param baseUrl baseUrl
     * @return 根据 baseUrl获取Flag, from RetrofitUrlManager
     */
    private String getBaseUrlFlag(String baseUrl) {
        //如果不是默认的baseUrl
        if (!UrlConfig.getDominUrl().equals(baseUrl)) {
            return UrlConfig.FLAG_MULTIPLE_BASE_URL_A;
        } else {
            return UrlConfig.FLAG_MULTIPLE_BASE_URL_A;
        }
    }
    /**
     * 设置是否开启日志打印
     */
    public void setLogEnabled(boolean logEnabled) {
        getHttpClient().setLogEnabled(logEnabled);
    }

    /**
     * 订阅(同步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    public <T> Disposable subscribeSync(Observable<T> observable, final HttpObserver<T> observer) {
        observable = observable.compose(new ObservableTransformerSync<T>())
                .compose(new ObservableTransformerError<T>());
        return RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
    }

    /**
     * 订阅(异步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    public <T> Disposable subscribeAsync(Observable<T> observable, final HttpObserver<T> observer) {
        observable = observable.compose(new ObservableTransformerAsync<T>())
                .compose(new ObservableTransformerError<T>());
        return RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
    }

}
