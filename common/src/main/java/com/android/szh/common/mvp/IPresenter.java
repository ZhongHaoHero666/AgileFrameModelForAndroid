package com.android.szh.common.mvp;


import android.support.annotation.UiThread;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:
 * 负责完成View与Model间的交互(作为View与Model交互的中间纽带，处理与用户的交互)
 * 持有View对象，对View进行操作
 * 持有Model层提供的数据接口对象，可通过依赖注入解耦此部分
 * 从数据接口对象中获取数据并处理，更新View
 */
public interface IPresenter<Model, View> extends IRxjava {

    /**
     * 关联MVPView到Presenter
     *
     * @param view MVPView实现类对象
     */
    @UiThread
    void attachView(View view);

    /**
     * 解除关联到Presenter的MVPView(视图被销毁时调用该方法)
     * <ul>
     * <strong>一般在以下方法中调用：<strong/>
     * <li>{@link android.app.Activity#onDestroy()}
     * <li>{@link android.app.Fragment#onDestroy()}
     * <li>{@link android.support.v4.app.Fragment#onDestroy()}
     * </ul>
     */
    @UiThread
    void detachView();
}