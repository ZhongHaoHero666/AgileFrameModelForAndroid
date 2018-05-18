package com.android.szh.common.permission;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 权限请求结果
 * 方法介绍：</strong>
 * {@link #getGrantedPermissions()}返回已授予所有权限的集合
 * {@link #getDeniedPermissions()}返回已拒绝的所有权限的集合
 * {@link #areAllPermissionsGranted()}返回用户是否授予了所有请求的权限
 * {@link #isAnyPermissionNeverAskAgain()}返回用户是否对请求的某一个或多个权限选择了不再询问
 * Created by sunzhonghao
 *
 * @date 2017/7/27 18:01
 * @see PermissionGranted
 * @see PermissionDenied
 */
final class PermissionsResult {

    private final List<PermissionGranted> grantedPermissions;
    private final List<PermissionDenied> deniedPermissions;

    PermissionsResult() {
        grantedPermissions = new LinkedList<>();
        deniedPermissions = new LinkedList<>();
    }

    /**
     * 返回已授予所有权限的集合
     */
    List<PermissionGranted> getGrantedPermissions() {
        return grantedPermissions;
    }

    /**
     * 返回已拒绝的所有权限的集合
     */
    List<PermissionDenied> getDeniedPermissions() {
        return deniedPermissions;
    }

    /**
     * 返回用户是否授予了所有请求的权限
     */
    boolean areAllPermissionsGranted() {
        return deniedPermissions.isEmpty();
    }

    /**
     * 返回用户是否对请求的某一个或多个权限选择了不再询问
     */
    boolean isAnyPermissionNeverAskAgain() {
        boolean hasNeverAskAgainAnyPermission = false;
        for (PermissionDenied denied : deniedPermissions) {
            if (denied.isNeverAskAgain()) {
                hasNeverAskAgainAnyPermission = true;
                break;
            }
        }
        return hasNeverAskAgainAnyPermission;
    }

    /**
     * 添加被授予的权限
     */
    boolean addGrantedPermission(PermissionGranted response) {
        return grantedPermissions.add(response);
    }

    /**
     * 添加被拒绝的权限
     */
    boolean addDeniedPermission(PermissionDenied response) {
        return deniedPermissions.add(response);
    }

    /**
     * 返回已授予的所有权限名称的集合
     */
    List<String> getGrantedPermissionNames() {
        List<String> permissions = new ArrayList<>(grantedPermissions.size());
        for (PermissionGranted permission : grantedPermissions) {
            permissions.add(permission.getPermissionName());
        }
        return permissions;
    }

    /**
     * 返回拒绝的所有权限名称的集合
     */
    List<String> getDeniedPermissionNames() {
        List<String> permissions = new ArrayList<>(deniedPermissions.size());
        for (PermissionDenied permission : deniedPermissions) {
            permissions.add(permission.getPermissionName());
        }
        return permissions;
    }

    /**
     * 清空被授予的权限和被拒绝的权限
     */
    void clear() {
        grantedPermissions.clear();
        deniedPermissions.clear();
    }

}
