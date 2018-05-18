package com.android.szh.common.permission;

import android.support.annotation.NonNull;

/**
 * 授予的权限信息
 * Created by sunzhonghao
 * @date 2017/7/27 17:45
 */
final class PermissionGranted {

    private final String permissionName;

    /**
     * 根据给定的权限名称创建一个{@link PermissionGranted}实例
     */
    static PermissionGranted from(@NonNull String permission) {
        return new PermissionGranted(permission);
    }

    private PermissionGranted(@NonNull String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * 返回授予的权限名称
     */
    public String getPermissionName() {
        return permissionName;
    }

    @Override
    public String toString() {
        return "PermissionGranted{" +
                "permissionName=" + permissionName +
                '}';
    }
}
