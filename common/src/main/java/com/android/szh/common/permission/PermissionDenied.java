package com.android.szh.common.permission;

import android.support.annotation.NonNull;

/**
 * 拒绝的权限信息
 * 方法介绍：</strong>
 * {@link #getPermissionName()}返回拒绝的权限名称
 * {@link #isNeverAskAgain()}返回用户是否选择不再询问
 *
 *
 * Created by sunzhonghao
 * @date 2017/7/27 17:43
 */
final class PermissionDenied {

    private final String permissionName;
    private final boolean neverAskAgain;

    /**
     * 根据给定的权限名称和是否被不再询问创建一个{@link PermissionDenied}实例
     */
    static PermissionDenied from(@NonNull String permission, boolean neverAskAgain) {
        return new PermissionDenied(permission, neverAskAgain);
    }

    private PermissionDenied(@NonNull String permissionName, boolean neverAskAgain) {
        this.permissionName = permissionName;
        this.neverAskAgain = neverAskAgain;
    }

    /**
     * 返回拒绝的权限名称
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * 返回用户是否选择不再询问
     */
    public boolean isNeverAskAgain() {
        return neverAskAgain;
    }

    @Override
    public String toString() {
        return "PermissionDenied{" +
                "permissionName=" + permissionName +
                ", neverAskAgain=" + neverAskAgain +
                '}';
    }
}
