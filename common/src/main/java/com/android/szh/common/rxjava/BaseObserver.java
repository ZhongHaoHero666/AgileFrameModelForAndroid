package com.android.szh.common.rxjava;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

/**
 * 基于Observable的观察者基类
 * {@link #onComplete()}: 事件队列完结。RxJava不仅把每个事件单独处理，还会把它们看做一个队列。RxJava规定，当不会再有新的{@link #onNext(Object)}发出时，需要触发{@link #onComplete()}方法作为标志。
 * {@link #onError(Throwable)} : 事件队列异常。在事件处理过程中出异常时，{@link #onError(Throwable)}会被触发，同时队列自动终止，不允许再有事件发出。
 * 在一个正确运行的事件序列中,{@link #onComplete()}和{@link #onError(Throwable)}有且只有一个，并且是事件序列中的最后一个。需要注意的是，{@link #onComplete()}和{@link #onError(Throwable)}二者也是互斥的，即在队列中调用了其中一个，就不应该再调用另一个。
 */
public abstract class BaseObserver<T> implements Observer<T>, Disposable {

    protected final AtomicReference<Disposable> s = new AtomicReference<>();

    @Override
    public void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.s, s, getClass())) {
            onStart();
        }
    }

    /**
     * Called once the single upstream Disposable is set via onSubscribe.
     */
    protected void onStart() {
    }

    protected void onStop() {
    }

    @Override
    public void onComplete() {
        onStop();
    }

    @Override
    public void onError(Throwable e) {
        onStop();
    }

    @Override
    public final boolean isDisposed() {
        return s.get() == DisposableHelper.DISPOSED;
    }

    @Override
    public final void dispose() {
        DisposableHelper.dispose(s);
    }

}
