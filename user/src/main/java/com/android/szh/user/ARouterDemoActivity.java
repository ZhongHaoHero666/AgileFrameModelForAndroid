package com.android.szh.user;

import android.content.Intent;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.config.RoutePathConfig;
import com.android.szh.common.constant.PageJumpsKeys;
import com.android.szh.common.entity.ARouteSerializableBean;

import butterknife.BindView;

/**
 * Created by sunzhonghao on 2018/7/10.
 * desc:ARouter组件通讯的演示类
 */

@Route(path = RoutePathConfig.ROUTE_USER_AROUTEDEMO)
public class ARouterDemoActivity extends BaseActivity {
    @BindView(R2.id.tv_show_result)
    TextView tvShowResult;
    String data1;
    ARouteSerializableBean data2;

    @Override
    protected void initViews() {
        tvShowResult.setText(String.valueOf("接收到的String : = " + data1 + "\n" + "接收到的对象 : = " + data2.toString()));
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        //在此处处理传递过来的数据
        data1 = intent.getStringExtra(PageJumpsKeys.KEY_AROUTER_DEMO_PAGE_DATA_1);
        data2 = (ARouteSerializableBean) intent.getSerializableExtra(PageJumpsKeys.KEY_AROUTER_DEMO_PAGE_DATA_2);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_arouter_demo;
    }

}
