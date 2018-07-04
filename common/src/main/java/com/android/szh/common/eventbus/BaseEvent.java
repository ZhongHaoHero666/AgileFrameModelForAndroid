package com.android.szh.common.eventbus;

/**
 * EventBus事件类型(用于EventBus的参数传递)
 */
public class BaseEvent<T> {

    private String action;
    private T eventObject;

    public BaseEvent() {
    }

    public BaseEvent(String action) {
        this.action = action;
    }

    public BaseEvent(T eventObject) {
        this.eventObject = eventObject;
    }

    public BaseEvent(String action, T eventObject) {
        this.action = action;
        this.eventObject = eventObject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getEventObject() {
        return eventObject;
    }

    public void setEventObject(T eventObject) {
        this.eventObject = eventObject;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "action='" + action + '\'' +
                ", eventObject=" + eventObject +
                '}';
    }

}
