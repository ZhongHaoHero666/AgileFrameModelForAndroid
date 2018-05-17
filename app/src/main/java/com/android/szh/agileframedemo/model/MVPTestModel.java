package com.android.szh.agileframedemo.model;


import com.android.szh.agileframedemo.api.APPApiService;
import com.android.szh.agileframedemo.contract.MVPTestContract;
import com.android.szh.common.http.HttpManager;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by sunzhonghao on 2018/5/16.
 * desc: Model实现类
 */
public class MVPTestModel implements MVPTestContract.Model {

    @Override
    public Observable<ResponseBody> getCityInfo() {
        return HttpManager.getInstance()
                .create(APPApiService.class)
                .getCityInfo();
    }
}
