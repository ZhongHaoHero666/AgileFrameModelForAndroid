package com.android.szh.agileframedemo.activity;

import android.content.Intent;
import android.view.View;

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

    @OnClick({R.id.btn_show_mvp, R.id.btn_check_permission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_show_mvp:
                startActivity(new Intent(this, MVPTestActivity.class));
                break;
            case R.id.btn_check_permission:
                startActivity(new Intent(this, PermissionAndCameraActivity.class));
                break;
        }
    }
}
