package com.android.szh.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.szh.common.mvp.IPresenter;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:Activity基类
 */
public abstract class BaseActivity<Presenter extends IPresenter> extends AppCompatActivity {
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
    protected abstract void loadData();

    /**
     * 在页面绘制之前执行的逻辑
     */
    protected abstract void beforeSuper();

    /**
     * 处理页面之间传递的数据
     */
    protected abstract void handleIntent(Intent intent);

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
}
