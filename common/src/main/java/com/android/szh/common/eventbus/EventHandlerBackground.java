package com.android.szh.common.eventbus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus消息处理器(在后台线程执行)
 */
public interface EventHandlerBackground<T> {

    /**
     * 在后台线程执行
     * <br/>如果事件是在UI线程中发布出来的，那么接收事件就会在UI线程中运行
     * <br/>如果事件是子线程中发布出来的，那么接收事件就会在该子线程中执行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    void onEventBackgroundThread(BaseEvent<T> event);

}
