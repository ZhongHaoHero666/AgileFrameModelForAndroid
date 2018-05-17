package com.android.szh.common.base;

import android.content.Context;

import com.android.szh.common.mvp.IPresenter;
import com.android.szh.common.mvp.IView;
import com.android.szh.common.rxjava.BaseObserver;
import com.android.szh.common.rxjava.RxJavaHelper;
import com.android.szh.common.rxjava.transformer.ObservableTransformerAsync;
import com.android.szh.common.rxjava.transformer.ObservableTransformerError;
import com.android.szh.common.rxjava.transformer.ObservableTransformerSync;
import com.android.szh.common.utils.ReflexHelper;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:presenter基类 责完成View与Model间的交互(作为View与Model交互的中间纽带，处理与用户的交互)
 */

public class BasePresenter<Model, View extends IView> implements IPresenter<Model, View> {

    private View mMVPView;
    private Model mMVPModel;
    private Context mContext;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public boolean add(Disposable disposable) {
        return getCompositeDisposable().add(disposable);
    }

    @Override
    public boolean remove(Disposable disposable) {
        return getCompositeDisposable().remove(disposable);
    }

    @Override
    public void unsubscribe() {
        getCompositeDisposable().dispose();
        mCompositeDisposable = null;
    }

    @Override
    public void attachView(View mvpView) {
        this.mMVPView = mvpView;
    }

    @Override
    public void detachView() {
        this.mMVPView = null;
        this.mMVPModel = null;
        unsubscribe();
    }

    protected Context getContext() {
        if (mContext == null) {
            mContext = getView().getContext();
        }
        return mContext;
    }

    protected Model getModel() {
        if (mMVPModel == null) {
            mMVPModel = ReflexHelper.getTypeInstance(this, 0);
        }
        return mMVPModel;
    }

    protected View getView() {
        if (!isViewAttached()) {
            throw new NullPointerException("View is not Attached!");
        }
        return mMVPView;
    }

    protected CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    /**
     * 订阅(异步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    protected <T> Disposable subscribe(Observable<T> observable, final BaseObserver<T> observer) {
        observable = observable
                .compose(new ObservableTransformerAsync<T>())
                .compose(new ObservableTransformerError<T>());
        Disposable disposable = RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
        add(disposable);
        return disposable;
    }

    /**
     * 订阅(同步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    protected <T> Disposable subscribeSync(Observable<T> observable, final BaseObserver<T> observer) {
        observable = observable
                .compose(new ObservableTransformerSync<T>())
                .compose(new ObservableTransformerError<T>());
        Disposable disposable = RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
        add(disposable);
        return disposable;
    }

    private boolean isViewAttached() {
        return mMVPView != null;
    }
}
