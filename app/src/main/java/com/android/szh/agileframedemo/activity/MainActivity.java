package com.android.szh.agileframedemo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.config.RoutePathConfig;
import com.android.szh.common.constant.PageJumpsKeys;
import com.android.szh.common.entity.ARouteSerializableBean;
import com.android.szh.common.eventbus.BaseEvent;
import com.android.szh.common.eventbus.EventHandlerMain;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * EventHandlerMain 接口是用来接收eventBus发送的消息，使用时需要重写   false:useEventBus()
 * <p>
 * Created by sunzhonghao on 2018/7/10.
 * desc:主界面
 */
public class MainActivity extends BaseActivity implements EventHandlerMain<String> {
    @BindView(R.id.btn_use_event_bus)
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
    }

    @OnClick({R.id.btn_show_mvp, R.id.btn_check_permission, R.id.btn_use_event_bus, R.id.btn_to_greendao_test,
            R.id.btn_to_other_model, R.id.btn_to_view_demo, R.id.btn_to_multiple_base_url_demo})
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
            case R.id.btn_to_other_model:
                //组件通讯需要传递的对象
                ARouteSerializableBean aRouteSerializableBean = new ARouteSerializableBean("姓名", 18);

                ARouter.getInstance().build(RoutePathConfig.ROUTE_USER_AROUTEDEMO)
                        .withString(PageJumpsKeys.KEY_AROUTER_DEMO_PAGE_DATA_1, "data1")
                        .withSerializable(PageJumpsKeys.KEY_AROUTER_DEMO_PAGE_DATA_2, aRouteSerializableBean)
                        .navigation();
                break;
            case R.id.btn_to_view_demo:
                startActivity(new Intent(this, ViewHelperDemoActivity.class));
                break;
            case R.id.btn_to_multiple_base_url_demo:
                startActivity(new Intent(this, MultipleBaseUrlSwitchActivity.class));
                break;
            default:
                break;
        }
    }

    /* 重写此方法是 一定要带有  @Subscribe(threadMode = ThreadMode.MAIN)
     * 否则将会报错 its super classes have no public methods with the @Subscribe
     */
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
