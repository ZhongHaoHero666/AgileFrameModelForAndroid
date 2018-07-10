package com.android.szh.agileframedemo.activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.eventbus.BaseEvent;
import com.android.szh.common.eventbus.EventHandlerMain;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

/**
 * EventHandlerMain 接口是用来接收eventBus发送的消息，使用时需要重写   false:useEventBus()
 */
public class MainActivity extends BaseActivity implements EventHandlerMain<String> {

    Button btnUseEventBus;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean usePageAnimation() {
        return false;
    }

    @Override
    protected void initViews() {
        btnUseEventBus = findViewById(R.id.btn_use_event_bus);
    }

    @OnClick({R.id.btn_show_mvp, R.id.btn_check_permission, R.id.btn_use_event_bus,R.id.btn_to_greendao_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_show_mvp:
                startActivity(new Intent(this, MVPTestActivity.class));
                break;
            case R.id.btn_check_permission:
                startActivity(new Intent(this, PermissionAndCameraActivity.class));
                break;
            case R.id.btn_use_event_bus:
                startActivity(new Intent(this, EventBusPostMessageActivity.class));
                break;
                case R.id.btn_to_greendao_test:
                startActivity(new Intent(this, GreenDaoTestActivity.class));
                break;
        }
    }

    //重写此方法是 一定要带有  @Subscribe(threadMode = ThreadMode.MAIN)
    //否则将会报错 its super classes have no public methods with the @Subscribe
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseEvent<String> event) {
        btnUseEventBus.setText("调用eventBus（" + event.getAction() + "___" + event.getEventObject() + "）。");
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
