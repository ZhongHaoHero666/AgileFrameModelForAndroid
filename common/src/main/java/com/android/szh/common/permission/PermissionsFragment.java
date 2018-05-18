package com.android.szh.common.permission;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.LinkedList;

/**
 * 用于权限请求的{@link Fragment}
 *
 * Created by sunzhonghao
 * @date 2017/7/27 19:07
 */
public final class PermissionsFragment extends Fragment {

    private int requestCode;
    private OnRequestResultListener resultListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // //在配置变化的时候将这个fragment保存下来
    }

    public void setResultListener(OnRequestResultListener listener) {
        this.resultListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(int requestCode, @NonNull String[] permissions) {
        this.requestCode = requestCode;
        requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != this.requestCode) {
            return;
        }
        Collection<String> grantedPermissions = new LinkedList<>();
        Collection<String> deniedPermissions = new LinkedList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            } else {
                grantedPermissions.add(permission);
            }
        }
        if (resultListener != null) {
            resultListener.onRequestResultListener(grantedPermissions, deniedPermissions);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        resultListener = null;
    }

}
