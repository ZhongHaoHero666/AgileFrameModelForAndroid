package com.android.szh.common.rxjava.transformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 调度转换器(异步)
 */
public class ObservableTransformerAsync<T> implements ObservableTransformer<T, T> {


    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread()); // 指定下游接收事件的线程(每次指定都有效)
    }
}
