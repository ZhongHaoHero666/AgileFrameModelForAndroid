package com.android.szh.agileframedemo.activity;

import android.view.View;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.eventbus.EventBusHelper;

import java.util.Random;

/**
 * Created by sunzhonghao on 2018/7/4.
 * desc:用于测试eventBus 发送消息的界面
 */

public class EventBusPostMessageActivity extends BaseActivity {
    @Override
    protected void initViews() {
        findViewById(R.id.btn_post_event_bus_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.post("已经接收到消息", String.valueOf((int) (new Random().nextFloat() * 100)));
                finish();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_use_event_bus;
    }
}
