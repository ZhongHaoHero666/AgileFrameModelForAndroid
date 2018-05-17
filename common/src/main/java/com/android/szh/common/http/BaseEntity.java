package com.android.szh.common.http;

import com.google.gson.annotations.SerializedName;

/**
 * 服务端响应结果
 * 该实体类字段需要和服务端约定
 */
public class BaseEntity<T> {

    @SerializedName(value = "code")
    private int code;
    @SerializedName(value = "msg")
    private String desc;
    @SerializedName(value = "data")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccessful() {
        return code == 0;
    }

    /**
     * 判断是否失败
     */
    public boolean isFailure() {
        return code == -1;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }

}
