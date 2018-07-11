package com.android.szh.agileframedemo.activity;

import android.view.View;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author sunzhonghao
 * @date 2018/7/11
 * desc:BaseActivity中viewHelper 功能的演示
 */
public class ViewHelperDemoActivity extends BaseActivity {
    @Override
    protected void initViews() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_viewhelper_demo;
    }


    @OnClick({R.id.btn_show_loading, R.id.btn_show_empty_page, R.id.btn_show_error_page, R.id.btn_show_net_error_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_show_loading:
                showLoadingDialog();
                break;

            case R.id.btn_show_empty_page:

                break;
            case R.id.btn_show_error_page:

                break;
            case R.id.btn_show_net_error_page:

                break;

        }
    }
}
