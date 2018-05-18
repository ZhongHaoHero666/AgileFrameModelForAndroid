package com.android.szh.common.permission;

/**
 * 权限请求回调
 */
public interface OnPermissionCallback {

    /**
     * 当请求的权限全部被允许时调用该方法
     */
    void onPermissionCallback(int requestCode);
}
