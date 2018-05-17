package com.android.szh.agileframedemo;

import com.android.szh.common.BaseApplication;

/**
 * Created by sunzhonghao on 2018/5/16.
 * desc:供测试的application
 */

public class TestApplication extends BaseApplication {

    @Override
    public void onCreate() {
        setDebugModel(BuildConfig.DEBUG);
        super.onCreate();
    }

}
