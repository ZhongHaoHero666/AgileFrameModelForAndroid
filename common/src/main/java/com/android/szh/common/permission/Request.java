package com.android.szh.common.permission;

import java.util.Collection;

/**
 * 权限请求接口
 *
 * Created by sunzhonghao
 * @date 2017/7/31 11:44
 */
interface Request<T extends Request> {

    /**
     * 设置是否为调试模式
     */
    T debugMode(boolean debug);

    /**
     * 设置需要请求的权限
     */
    T permission(String permission);

    /**
     * 设置需要请求的权限
     */
    T permissions(String... permissions);

    /**
     * 设置需要请求的权限
     */
    T permissions(String[]... permissions);

    /**
     * 设置需要请求的权限
     */
    T permissions(Collection<String> permissions);

    /**
     * 设置权限请求准备监听
     */
    T prepare(OnPrepareListener listener);

    /**
     * 设置权限请求回调
     */
    T callback(OnPermissionsCallback callback);

    /**
     * 设置权限请求码
     */
    T requestCode(int requestCode);

    /**
     * 请求权限
     */
    void request();
}
