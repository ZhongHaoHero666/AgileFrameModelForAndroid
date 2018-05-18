package com.android.szh.common.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 权限请求助手
 *
 * Created by sunzhonghao
 * @date 2017/7/27 15:58
 */
public class PermissionHelper {


    /**
     * 静态方法(创建{@link PermissionRequest}实例)
     */
    public static PermissionRequest with(Activity activity) {
        return new PermissionRequest(activity);
    }

    /**
     * 静态方法(创建{@link PermissionRequest}实例)
     */
    public static PermissionRequest with(android.app.Fragment fragment) {
        return new PermissionRequest(fragment);
    }

    /**
     * 静态方法(创建{@link PermissionRequest}实例)
     */
    public static PermissionRequest with(android.support.v4.app.Fragment fragment) {
        return new PermissionRequest(fragment);
    }

    /**
     * 判断当前系统版本是否为Android 6.0及以上系统
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 检查应用是否拥有该权限(被授权返回{@link PackageManager#PERMISSION_GRANTED}，否则返回{@link PackageManager#PERMISSION_DENIED})
     * <br>{@link Build.VERSION_CODES#M}之前的版本调用该方法将直接回调{@link Activity#onRequestPermissionsResult(int, String[], int[])}
     *
     * @see ContextCompat#checkSelfPermission
     */
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * 弹出请求授权对话框进行权限请求
     * <br>{@link Build.VERSION_CODES#M}之前的版本永远返回false
     *
     * @see ActivityCompat#requestPermissions
     */
    public static void requestPermissions(@Nullable Activity activity, @NonNull String[] permissions, int requestCode) {
        if (activity == null) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 是否需要解释需要申请权限的原因
     * <br>Build.VERSION_CODES.M之前的版本永远返回false
     *
     * @see ActivityCompat#shouldShowRequestPermissionRationale
     */
    public static boolean shouldShowRequestPermissionRationale(@Nullable Activity activity, @NonNull String permission) {
        if (activity == null) {
            return false;
        }
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 启动应用设置页面
     */
    public static void startPackageSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 查找指定的权限集合中未授予的权限
     *
     * @param context     上下文对象
     * @param permissions 指定的权限集合
     * @return 未授予的权限集合
     */
    public static List<String> findUnGrantedPermissions(Context context, Collection<String> permissions) {
        List<String> denyPermissions = new ArrayList<>();
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                int permissionState = PermissionHelper.checkSelfPermission(context, permission);
                if (permissionState != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(permission);
                }
            }
        }
        return denyPermissions;
    }

    /**
     * 检查指定的权限集中的权限是否都被授予
     *
     * @param context     上下文对象
     * @param permissions 指定的权限集合
     * @return 指定的权限集中的权限是否都被授予
     */
    public static boolean checkAllPermissionsGranted(Context context, String permissions) {
        return checkAllPermissionsGranted(context, Arrays.asList(permissions));
    }

    /**
     * 检查指定的权限集中的权限是否都被授予
     *
     * @param context     上下文对象
     * @param permissions 指定的权限集合
     * @return 指定的权限集中的权限是否都被授予
     */
    public static boolean checkAllPermissionsGranted(Context context, Collection<String> permissions) {
        boolean allPermissionsGranted = true;
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                int permissionState = PermissionHelper.checkSelfPermission(context, permission);
                if (permissionState != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        return allPermissionsGranted;
    }

}
