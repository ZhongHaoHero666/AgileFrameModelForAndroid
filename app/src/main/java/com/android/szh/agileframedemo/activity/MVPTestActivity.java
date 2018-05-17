package com.android.szh.agileframedemo.activity;

import android.widget.TextView;

import com.android.szh.agileframedemo.R;
import com.android.szh.agileframedemo.contract.MVPTestContract;
import com.android.szh.agileframedemo.entry.CityInfo;
import com.android.szh.agileframedemo.presenter.MVPTestPresenter;
import com.android.szh.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by sunzhonghao on 2018/5/16.
 * desc: MVP 演示测试类
 */
public class MVPTestActivity extends BaseActivity<MVPTestPresenter> implements MVPTestContract.View {

    @BindView(R.id.mvp_result)
    TextView mvpResult;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvptest;
    }


    @Override
    protected void initViews() {

    }

    @Override
    public void handleCityInfoResult(CityInfo cityInfo) {
        mvpResult.setText(cityInfo.toString());
    }


    @OnClick(R.id.mvp_test)
    public void onViewClicked() {
        getPresenter().loadCityInfo();
    }

}
