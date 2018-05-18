package com.android.szh.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.szh.common.mvp.IPresenter;
import com.android.szh.common.mvp.IView;
import com.android.szh.common.rxjava.BaseObserver;
import com.android.szh.common.rxjava.RxJavaHelper;
import com.android.szh.common.rxjava.transformer.ObservableTransformerAsync;
import com.android.szh.common.rxjava.transformer.ObservableTransformerError;
import com.android.szh.common.utils.ReflexHelper;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:Activity基类
 */
public abstract class BaseActivity<Presenter extends IPresenter> extends AppCompatActivity implements IView {
    Context mContext;
    protected final String TAG = this.getClass().getSimpleName();
    ImmersionBar mImmersionBar;
    protected Presenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        beforeSuper();                         // 初始化(super.onCreate(savedInstanceState)之前调用)
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);              // 处理Intent(主要用来获取其中携带的参数)
        }
        setContentView(getContentLayoutId());  // 加载页面布局
        ButterKnife.bind(this);         // butterKnife绑定
        initViews();                           // 初始化view
        if (isImmersionPage()) {
            initImmersion();
        }
        loadData();                            // 加载数据
    }

    /**
     * 初始化沉浸式状态栏
     */
    protected void initImmersion() {
        mImmersionBar = ImmersionBar.with(this).navigationBarEnable(false);
        mImmersionBar.init();
    }

    /**
     * 初始化完成后加载数据
     */
    protected void loadData() {
    }


    /**
     * 返回Presenter
     */
    protected Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new NullPointerException("Please check the generic Activity.");
        }
        return mPresenter;
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

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        unsubscribe();//反注册订阅的异步处理
        super.onDestroy();
    }

    /**
     * 若是在activity中进行轻量的数据访问
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
     * 在页面绘制之前执行的逻辑
     */
    protected void beforeSuper() {
    }


    /**
     * 处理页面之间传递的数据
     */
    protected void handleIntent(Intent intent) {
    }

    ;

    /**
     * 初始化数据
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract int getContentLayoutId();

    /**
     * 是否是沉浸式界面
     */
    protected boolean isImmersionPage() {
        return false;
    }

    /**
     * @return 获取context
     */
    @Override
    public Context getContext() {
        return mContext;
    }
}
