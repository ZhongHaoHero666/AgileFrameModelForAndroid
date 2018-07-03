package com.android.szh.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.szh.common.R;
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

        if (usePageAnimation()) {
            initSmoothPageAnimation(true);                  // 初始化进退场动画
        }

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

    @Override
    public void finish() {
        super.finish();

        if (usePageAnimation()) {
            initSmoothPageAnimation(false);    //关闭时的转场动画
        }

    }

    /**
     * 初始化页面进退场动画
     */
    protected void initSmoothPageAnimation(boolean isStartPage) {
        if (isDefaultSmoothPageAnimation()) {//是否是默认转场动画
            if (isStartPage) {
                overridePendingTransition(R.anim.slide_in_common_start, R.anim.slide_out_common_start);
            } else {
                overridePendingTransition(R.anim.slide_in_common_finish, R.anim.slide_out_common_finish);
            }
        } else {
            int[] animRes = getSlideInOrOutAnim();
            if (animRes == null) return;

            if (animRes.length != 4) {
                throw new NullPointerException("自定义activity转场动画的res数组长度必须为4");
            }

            //如果四个值都等于0 说明没有需要执行的自定义页面转场动画
            if (animRes[0] == 0 && animRes[1] == 0 && animRes[2] == 0 && animRes[3] == 0) return;

            if (isStartPage) {
                overridePendingTransition(animRes[0], animRes[1]);
            } else {
                overridePendingTransition(animRes[2], animRes[3]);
            }
        }
    }

    /**
     * @return anim资源数组 length = 4
     * 过去自定义进退场动画的资源，当isSmoothPageAnimation()返回false时调用。
     * 特殊页面如果想实现其他效果，重写该方法返回anim资源数组即可。
     */
    protected int[] getSlideInOrOutAnim() {
        return null;
    }


    /**
     * @return 是否是默认的页面平滑切换动画  开启页面 ：右 → 左
     */
    protected boolean isDefaultSmoothPageAnimation() {
        return true;
    }

    /**
     * @return 是否是用转场动画
     */
    protected boolean usePageAnimation() {
        return true;
    }
}
