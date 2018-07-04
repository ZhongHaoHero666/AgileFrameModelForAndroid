package com.android.szh.common.eventbus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus消息处理器(在发送线程执行)
 */
public interface EventHandlerDefault<T> {

    /**
     * 在发送线程执行(默认方式)
     * <br/>无论事件是在哪个线程中发布出来的，接收事件就会在当前线程中运行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    void onEvent(BaseEvent<T> event);

}
