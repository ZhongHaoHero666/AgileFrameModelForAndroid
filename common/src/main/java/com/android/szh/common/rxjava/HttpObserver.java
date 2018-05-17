package com.android.szh.common.rxjava;

import android.content.Context;

import com.android.szh.common.http.exception.HttpException;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 基于Observable的网络请求观察者
 */
public abstract class HttpObserver<T> extends BaseObserver<T> {

    private Context mContext;
    private boolean isCheckNetWork;         // 是否检查网络
    private boolean isShowLoading;          // 是否显示加载对话框
    private boolean isNetworkAvailable;     // 网络是否可用
    private long subscribeTime;
    private static final String TAG = "HttpObserver";

    public HttpObserver(Context context) {
        this(context, true);
    }

    public HttpObserver(Context context, boolean showLoading) {
        this(context, showLoading, true);
    }

    public HttpObserver(Context context, boolean showLoading, boolean checkNetWork) {
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public void onSubscribe(Disposable s) {
        subscribeTime = System.currentTimeMillis();
        if (isCheckNetWork && !isNetworkAvailable) {
            onStart();
        } else {
            super.onSubscribe(s);
        }
    }

    /**
     * 处理一些公共操作(如：显示加载对话框、检查网络是否可用)
     * <br>注意：通知下游不再继续接收事件需要调用{@link #onComplete()}方法
     */
    protected void onStart() {

    }

    /**
     * 处理一些公共操作(如：取消加载对话框)
     */
    protected void onStop() {

    }

    @Override
    public final void onNext(T t) {
        //先处理一些公共逻辑
        _onNext(t);
    }

    public abstract void _onNext(T t);

    @Override
    public void onComplete() {
        onStop();
    }

    /**
     * 处理异常(不允许重写改方法)
     */
    @Override
    public final void onError(@NonNull Throwable throwable) {
        onStop();
        HttpException httpException = null;
        if (throwable instanceof HttpException) {
            httpException = (HttpException) throwable;
        }
        _onError(httpException);
    }

    public abstract void _onError(@NonNull HttpException exception);


    private String getResponTime() {
        long interval = System.currentTimeMillis() - subscribeTime;
        String responTime;
        if (interval > 1000) {// 如果大于1000ms，则换用s来显示
            responTime = interval / 1000 + "s";
        } else {
            responTime = interval + "ms";
        }
        return responTime;
    }
}
