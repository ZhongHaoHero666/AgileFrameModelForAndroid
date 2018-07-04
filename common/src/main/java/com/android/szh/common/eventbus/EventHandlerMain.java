package com.android.szh.common.eventbus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus消息处理器(在UI线程执行)
 */
public interface EventHandlerMain<T> {

    /**
     * 在UI线程执行
     * <br/>无论事件是在哪个线程中发布出来的，接收事件就会在UI线程中运行
     * 实现这个方法要把 @Subscribe(threadMode = ThreadMode.MAIN) 添加到类中
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    void onEventMainThread(BaseEvent<T> event);

}
