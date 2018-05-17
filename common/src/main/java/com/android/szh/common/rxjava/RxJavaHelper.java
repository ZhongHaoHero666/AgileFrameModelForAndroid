package com.android.szh.common.rxjava;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava辅助类
 */
public class RxJavaHelper {

    /**
     * 调度转换器
     */
    public static <T> ObservableTransformer<T, T> applySchedulersForObservable() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.unsubscribeOn(Schedulers.io()) // 取消订阅
                        .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                        .observeOn(AndroidSchedulers.mainThread()); // 指定下游接收事件的线程(每次指定都有效)
            }
        };
    }

    /**
     * 调度转换器
     */
    public static <T> FlowableTransformer<T, T> applySchedulersForFlowable() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> flowable) {
                return flowable.unsubscribeOn(Schedulers.io()) // 取消订阅
                        .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                        .observeOn(AndroidSchedulers.mainThread()); // 指定下游接收事件的线程(每次指定都有效)
            }
        };
    }

    /**
     * 遍历数据源序列
     *
     * @param <T>      数据源序列泛型
     * @param source   源头
     * @param observer 观察者
     */
    public static <T> Disposable create(ObservableOnSubscribe<T> source, BaseObserver<T> observer) {
        Observable<T> observable = Observable.create(source)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 失败重试
     *
     * @param <T>      数据源序列泛型
     * @param source   源头
     * @param observer 观察者
     */
    public static <T> Disposable retryWhen(ObservableOnSubscribe<T> source, Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler, BaseObserver<T> observer) {
        Observable<T> observable = Observable.create(source)
                .retryWhen(handler)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 遍历数据源序列
     *
     * @param <T>      数据源序列泛型
     * @param observer 观察者
     * @param items    数据源序列
     */
    public static <T> Disposable fromArray(BaseObserver<T> observer, T... items) {
        Observable<T> observable = Observable.fromArray(items)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 遍历数据源序列
     *
     * @param <T>      数据源序列泛型
     * @param iterable 数据源序列
     * @param observer 观察者
     */
    public static <T> Disposable fromIterable(Iterable<T> iterable, BaseObserver<T> observer) {
        Observable<T> observable = Observable.fromIterable(iterable)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 转换数据源序列
     *
     * @param iterable 数据源序列
     * @param mapper   数据转换器
     * @param observer 观察者
     * @param <T>      数据源序列泛型
     * @param <R>      转换后的数据序列泛型
     * @return
     */
    public static <T, R> Disposable mapAndFilter(Iterable<T> iterable, Function<T, R> mapper, BaseObserver<R> observer) {
        Observable<R> observable = Observable.fromIterable(iterable)
                .map(mapper)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 转换数据源序列并进行过滤
     *
     * @param iterable  数据源序列
     * @param mapper    数据转换器
     * @param predicate 过滤器
     * @param observer  观察者
     * @param <T>       数据源序列泛型
     * @param <R>       转换后的数据序列泛型
     * @return
     */
    public static <T, R> Disposable mapAndFilter(Iterable<T> iterable, Function<T, R> mapper, Predicate<R> predicate, BaseObserver<R> observer) {
        Observable<R> observable = Observable.fromIterable(iterable)
                .map(mapper)
                .filter(predicate)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 当延迟指定时间后执行操作
     *
     * @param delay    延迟时间
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static Disposable timer(long delay, TimeUnit unit, BaseObserver<Long> observer) {
        Observable<Long> observable = Observable.timer(delay, unit)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 当延迟指定间隔时间后每隔指定间隔时间后执行操作
     *
     * @param interval 间隔时间
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static Disposable interval(long interval, TimeUnit unit, BaseObserver<Long> observer) {
        return interval(interval, interval, unit, observer);
    }

    /**
     * 当延迟指定延迟时间后每隔指定间隔时间后执行操作
     *
     * @param delay    初始延迟时间
     * @param interval 间隔时间
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static Disposable interval(long delay, long interval, TimeUnit unit, BaseObserver<Long> observer) {
        Observable<Long> observable = Observable.interval(delay, interval, unit)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 将多个ObservableSource发射的数据组合并成一个
     *
     * @param subscriber 观察者
     * @param sources    观察源
     * @param <T>
     */
    public static <T> Disposable merge(BaseObserver<T> subscriber, Iterable<? extends ObservableSource<? extends T>> sources) {
        Observable<T> observable = Observable.merge(sources)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, subscriber);
    }

    /**
     * 将多个ObservableSource发射的数据组合并成一个
     *
     * @param subscriber 观察者
     * @param sources    观察源
     * @param <T>
     */
    public static <T> Disposable mergeArray(BaseObserver<T> subscriber, ObservableSource<? extends T>... sources) {
        Observable<T> observable = Observable.mergeArray(sources)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, subscriber);
    }

    /**
     * 遍历数据源并进行过滤
     *
     * @param source    数据源
     * @param predicate 过滤器
     * @param observer  观察者
     */
    public static <T> Disposable filter(Iterable<T> source, Predicate<T> predicate, BaseObserver<T> observer) {
        Observable<T> observable = Observable.fromIterable(source)
                .filter(predicate)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 遍历数据源并进行转换
     *
     * @param source   数据源
     * @param mapper   转换器
     * @param observer 观察者
     * @param <T>
     * @param <R>
     */
    public static <T, R> Disposable flatMap(Iterable<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper, BaseObserver<R> observer) {
        Observable<R> observable = Observable.fromIterable(source)
                .flatMap(mapper)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, observer);
    }

    /**
     * 将多个Publisher发射的数据连接起来处理
     *
     * @param httpObserver 观察者
     * @param sources      观察源
     * @param <T>
     * @return
     */
    public static <T> Disposable concat(BaseObserver<T> httpObserver, ObservableSource<? extends ObservableSource<? extends T>> sources) {
        Observable<T> observable = Observable.concat(sources)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, httpObserver);

    }

    /**
     * 将多个Publisher发射的数据连接起来处理
     *
     * @param httpObserver 观察者
     * @param sources      观察源
     * @param <T>
     * @return
     */
    public static <T> Disposable concatArray(BaseObserver<T> httpObserver, ObservableSource<? extends T>... sources) {
        Observable<T> observable = Observable.concatArray(sources)
                .unsubscribeOn(Schedulers.io()) // 取消订阅
                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
        return subscribe(observable, httpObserver);

    }

    /**
     * 基于{@link Flowable}的事件订阅
     *
     * @param flowable
     * @param subscriber
     * @param <T>
     * @return
     */
    public static <T> Disposable subscribe(Flowable<T> flowable, final BaseSubscriber<T> subscriber) {
        return flowable.subscribe(new Consumer<T>() {
            @Override
            public void accept(@NonNull T t) throws Exception {
                if (subscriber != null) {
                    subscriber.onNext(t);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (subscriber != null) {
                    subscriber.onError(throwable);
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (subscriber != null) {
                    subscriber.onComplete();
                }
            }
        }, new Consumer<Subscription>() {
            @Override
            public void accept(@NonNull Subscription subscription) throws Exception {
                subscription.request(Long.MAX_VALUE);
                if (subscriber != null) {
                    subscriber.onSubscribe(subscription);
                }
            }
        });
    }

    /**
     * 基于{@link Observable}的事件订阅
     *
     * @param observable
     * @param observer
     * @param <T>
     * @return
     */
    public static <T> Disposable subscribe(Observable<T> observable, final BaseObserver<T> observer) {
        return observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(@NonNull T t) throws Exception {
                if (observer != null) {
                    observer.onNext(t);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (observer != null) {
                    observer.onError(throwable);
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (observer != null) {
                    observer.onComplete();
                }
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                if (observer != null) {
                    observer.onSubscribe(disposable);
                }
            }
        });
    }

}
