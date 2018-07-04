package com.android.szh.common.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus辅助类
 */
public class EventBusHelper {

    /**
     * 注册EventBus
     */
    public static void register(Object subscriber) {
        if (!isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 反注册EventBus
     */
    public static void unregister(Object subscriber) {
        if (isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 是否已注册EventBus
     *
     * @param subscriber
     */
    public static boolean isRegistered(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

    /**
     * 发送事件
     *
     * @param action 事件类型
     */
    public static void post(String action) {
        post(new BaseEvent<>(action));
    }

    /**
     * 发送事件
     *
     * @param action      事件类型
     * @param eventObject 事件内容
     */
    public static <T> void post(String action, T eventObject) {
        post(new BaseEvent<>(action, eventObject));
    }

    /**
     * 发送事件
     *
     * @param event 事件
     */
    public static void post(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送事件
     *
     * @param event 事件
     */
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件
     *
     * @param action 事件类型
     */
    public static void postSticky(String action) {
        postSticky(new BaseEvent<>(action));
    }

    /**
     * 发送粘性事件
     *
     * @param eventObject 事件内容
     */
    public static <T> void postSticky(T eventObject) {
        postSticky(new BaseEvent<>(eventObject));
    }

    /**
     * 发送粘性事件
     *
     * @param action      事件类型
     * @param eventObject 事件内容
     */
    public static <T> void postSticky(String action, T eventObject) {
        postSticky(new BaseEvent<>(action, eventObject));
    }

    /**
     * 发送粘性事件
     *
     * @param event 事件
     */
    public static void postSticky(BaseEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 终止事件往下传递(优先级高的订阅者可以终止事件往下传递)
     *
     * @param action 事件类型
     */
    public static void cancelEventDelivery(String action) {
        cancelEventDelivery(new BaseEvent<>(action));
    }

    /**
     * 终止事件往下传递(优先级高的订阅者可以终止事件往下传递)
     *
     * @param eventObject 事件内容
     */
    public static <T> void cancelEventDelivery(T eventObject) {
        cancelEventDelivery(new BaseEvent<>(eventObject));
    }

    /**
     * 终止事件往下传递(优先级高的订阅者可以终止事件往下传递)
     *
     * @param action      事件类型
     * @param eventObject 事件内容
     */
    public static <T> void cancelEventDelivery(String action, T eventObject) {
        cancelEventDelivery(new BaseEvent<>(action, eventObject));
    }

    /**
     * 终止事件往下传递(优先级高的订阅者可以终止事件往下传递)
     *
     * @param event 事件
     */
    public static <T> void cancelEventDelivery(BaseEvent<T> event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * 移除粘性事件
     *
     * @param action 事件类型
     */
    public static void removeStickyEvent(String action) {
        removeStickyEvent(new BaseEvent<>(action));
    }

    /**
     * 移除粘性事件
     *
     * @param eventObject 事件内容
     */
    public static <T> void removeStickyEvent(T eventObject) {
        removeStickyEvent(new BaseEvent<>(eventObject));
    }

    /**
     * 移除粘性事件
     *
     * @param action      事件类型
     * @param eventObject 事件内容
     */
    public static <T> void removeStickyEvent(String action, T eventObject) {
        removeStickyEvent(new BaseEvent<>(action, eventObject));
    }

    /**
     * 移除粘性事件
     *
     * @param event 事件
     */
    public static void removeStickyEvent(BaseEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    /**
     * 移除所有粘性事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

}
