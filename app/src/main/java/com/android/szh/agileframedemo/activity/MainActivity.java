package com.android.szh.agileframedemo.activity;

import android.content.Intent;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.btn_show_mvp)
    public void onViewClicked() {
        startActivity(new Intent(this, MVPTestActivity.class));
    }
}
