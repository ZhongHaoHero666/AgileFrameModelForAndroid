package com.android.szh.common.eventbus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus消息处理器(强制在后台执行)
 */
public interface EventHandlerAsync<T> {

    /**
     * 强制在后台执行
     * <br/>无论事件是在哪个线程中发布出来的，接收事件都会在新创建的子线程中执行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    void onEventAsync(BaseEvent<T> event);
}
