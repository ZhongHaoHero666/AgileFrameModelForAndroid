package com.android.szh.common.rxjava;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * 基于Flowable的观察者基类
 *
 * @author liyunlong
 * @date 2017/5/24 11:42
 */
public class BaseSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription, Disposable {


    @Override
    public void onSubscribe(@NonNull Subscription subscription) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void dispose() {
        this.cancel();
    }

    @Override
    public boolean isDisposed() {
        return this.get() == SubscriptionHelper.CANCELLED;
    }

    @Override
    public void request(long n) {
        this.get().request(n);
    }

    @Override
    public void cancel() {
        SubscriptionHelper.cancel(this);
    }
}
