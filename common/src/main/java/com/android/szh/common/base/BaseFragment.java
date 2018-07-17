package com.android.szh.common.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.szh.common.eventbus.EventBusHelper;
import com.android.szh.common.logger.Logger;
import com.android.szh.common.mvp.IPresenter;
import com.android.szh.common.rxjava.BaseObserver;
import com.android.szh.common.rxjava.RxJavaHelper;
import com.android.szh.common.rxjava.transformer.ObservableTransformerAsync;
import com.android.szh.common.rxjava.transformer.ObservableTransformerError;
import com.android.szh.common.utils.ReflexHelper;
import com.android.szh.common.utils.SafetyHandler;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Fragment基类
 */
public abstract class BaseFragment<Presenter extends IPresenter> extends Fragment implements SafetyHandler.Delegate {

    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity activity;
    protected View rootView;
    private boolean isViewCreated; // rootView是否初始化的标志，防止回调函数在rootView为空的时候触发
    protected boolean isFragmentVisible; // 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    protected Presenter mPresenter;
    private SafetyHandler mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus()) {
            EventBusHelper.register(this);
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            handleBundle(bundle); // 处理Bundle(主要用来获取其中携带的参数)
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (rootView == null) {
            activity = (BaseActivity) getActivity();
            int contentLayoutID = getContentLayoutID();
            if (contentLayoutID == 0) {
                return super.onCreateView(inflater, container, savedInstanceState);
            } else {
                rootView = inflater.inflate(contentLayoutID, null);
                ButterKnife.bind(this, rootView);
                initTitleBar(rootView);         // 初始化titleBar
                initViews(rootView);            // 初始化View
                initComponents();               // 初始化系统组件(广播接收器和服务)
            }
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
            // java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        loadData();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewCreated) { // 判断rootView是否已经初始化
            isViewCreated = true;
            if (getUserVisibleHint()) {
                isFragmentVisible = true;
                onFragmentVisibleChange(true);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        if (isVisibleToUser) {
            isFragmentVisible = true;
            onFragmentVisibleChange(true);
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("BaseFragment", this.getClass().getName());
    }

    @Override
    public Context getContext() {
        return super.getContext() != null ? super.getContext() : getActivity();
    }

    /**
     * 是否使用EventBus
     */
    protected boolean useEventBus() {
        return false;
    }


    /**
     * 返回Presenter对象
     */
    public Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new NullPointerException("Please check the generic Fragment.");
        }
        return mPresenter;
    }

    /**
     * 返回根布局View
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 判断当前Fragment的根布局是否初始化
     */
    public boolean isViewCreated() {
        return isViewCreated;
    }

    /**
     * 判断当前Fragment是否可见
     */
    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * 处理Bundle(主要用来获取其中携带的参数)
     */
    protected void handleBundle(@NonNull Bundle bundle) {
    }

    /**
     * 初始化titleBar(需要调用{@link Fragment#setHasOptionsMenu(boolean)})
     */
    protected void initTitleBar(View rootView) {
    }

    /**
     * 加载Fragment的布局
     */
    protected abstract int getContentLayoutID();

    /**
     * 初始化View
     */
    protected abstract void initViews(View rootView);


    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 创建Presenter实例
     */
    protected Presenter createPresenter() {
        Presenter presenter = ReflexHelper.getTypeInstance(this, 0);
        if (presenter != null && presenter instanceof BasePresenter) {
            presenter.attachView(this);
        }
        return presenter;
    }


    /**
     * 获取内容布局(用于创建StatusLayoutManager实例，内容布局必须有父布局，不能是Fragment的根布局)
     */
    protected View getContentView() {
        return null;
    }


    /**
     * 查找View
     */
    protected <ViewType extends View> ViewType findViewById(@IdRes int id) {
        return findViewById(rootView, id);
    }

    /**
     * 查找View
     */
    protected <ViewType extends View> ViewType findViewById(View view, @IdRes int id) {
        return view.findViewById(id);
    }

    /**
     * 初始化系统组件(广播接收器和服务)
     */
    protected void initComponents() {
        registerReceivers();
        startServices();
    }

    /**
     * 注册广播接收器
     */
    protected void registerReceivers() {
    }

    /**
     * 开启服务
     */
    protected void startServices() {
    }

    /**
     * 当前{@link #Fragment}可见状态发生变化时会回调该方法
     * <br/>如果当前{@link #Fragment}是第一次加载，等待{@link #onViewCreated(View, Bundle)}后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     *
     * @param isVisible true表示{@link #Fragment}有不可见变为可见；false表示{@link #Fragment}有可见变为不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: " + this.getClass().getName());
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if (mHandler != null) {
            mHandler.clear();
            mHandler = null;
        }

        unsubscribe();//反注册订阅的异步处理

        if (useEventBus()) {
            EventBusHelper.unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 若是在fragment中进行轻量的数据访问
     */

    private CompositeDisposable mCompositeDisposable; //订阅事件管理器

    protected CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    public void unsubscribe() {
        getCompositeDisposable().dispose();
        mCompositeDisposable = null;
    }


    /**
     * 订阅(异步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    public <T> Disposable subscribe(Observable<T> observable, final BaseObserver<T> observer) {
        observable = observable
                .compose(new ObservableTransformerAsync<T>())
                .compose(new ObservableTransformerError<T>());
        Disposable disposable = RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
        add(disposable);
        return disposable;
    }

    public boolean add(@io.reactivex.annotations.NonNull Disposable disposable) {
        return getCompositeDisposable().add(disposable);
    }

    /**
     * 返回Handler对象
     */
    public SafetyHandler getHandler() {
        if (mHandler == null) {
            mHandler = SafetyHandler.create(this);
        }
        return mHandler;
    }

    //子类可以重写此方法，处理handler发送的消息
    @Override
    public void handleMessage(Message msg) {

    }
}
