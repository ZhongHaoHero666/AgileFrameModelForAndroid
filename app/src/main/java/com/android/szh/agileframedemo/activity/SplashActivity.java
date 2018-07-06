package com.android.szh.agileframedemo.activity;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by sunzhonghao on 2018/7/4.
 * desc:闪屏页
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void initViews() {

    }


    @Override
    protected boolean usePageAnimation() {
        return false;
    }

    @Override
    protected void beforeSuper() {
        //设置成全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void loadData() {
        //使用公用的订阅器开始倒计时
        getCompositeDisposable()
                .add(Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(3)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 2 - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        if (time == 0) {
                            //执行完毕后植入到 MainActivity
                            startActivity(new Intent(getContext(), MainActivity.class));
                            finish();
                        }
                    }
                }));
    }
}
