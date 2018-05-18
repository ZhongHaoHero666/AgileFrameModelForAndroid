package com.android.szh.common.permission;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.android.szh.common.utils.AppHelper;
import com.android.szh.common.widget.CustomDialog;

import java.util.List;

/**
 * 权限请求回调实现类
 * <p>
 * Created by sunzhonghao
 *
 * @date 2017/10/26 19:41
 */
public class SimplePermissionsCallback implements OnPermissionsCallback {

    private Context mContext;
    private OnPermissionCallback mCallback;
    private final String appName;

    public SimplePermissionsCallback(Context context, OnPermissionCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        appName = AppHelper.getAppName(mContext);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> permissions, @PermissionRequest.PermissionFlag int flag) {
        if (mCallback != null) {
            mCallback.onPermissionCallback(requestCode);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> permissions) {
        String message = null;
        switch (requestCode) {
            case PermissionCode.STARTUP:
                message = "由于未获取设备信息和存储空间权限，无法正常使用" + appName;
                break;
            case PermissionCode.NEARBYSTATION:
                message = "由于未获取地理位置权限，无法定位你的位置";
                break;
            case PermissionCode.TAKEPHOTO:
                message = "由于未获取拍照权限，无法打开你的相机";
                break;
            case PermissionCode.PICTURES:
                message = "由于未获取存储空间权限，无法访问您的相册";
                break;
            case PermissionCode.SAVE_IMAGE:
                message = "由于未获取存储空间权限，无法保存文件";
                break;
            case PermissionCode.SHARE_APP:
                message = "需要获取存储空间权限，获取后可分享至第三方";
            default:
                break;
        }
        if (!TextUtils.isEmpty(message)) {
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext)
                    .setTitle("权限申请")
                    .setTopMessage(message)
                    .setTopMessageGravity(Gravity.LEFT)
                    .setRightButton("去授权", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionHelper.startPackageSettings(mContext);
                        }
                    });
            if (requestCode != PermissionCode.STARTUP) { // 启动时不允许取消
                builder.setLeftButton("取消", null);
            }
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    public void onShowRequestPermissionRationale(int requestCode, List<String> permissions, final Rationale rationale) {
        String message = null;
        switch (requestCode) {
            case PermissionCode.STARTUP:
                message = appName + "需要获取设备信息和存储空间权限，以保证账号安全";
                break;
            case PermissionCode.NEARBYSTATION:
                message = appName + "需要获取地理位置权限，以便帮你找到附近的地点";
                break;
            case PermissionCode.TAKEPHOTO:
                message = appName + "需要获取拍照权限，获取后可打开您的相机";
                break;
            case PermissionCode.PICTURES:
                message = appName + "需要获取存储空间权限，获取后可访问你的相册";
                break;
            case PermissionCode.SAVE_IMAGE:
                message = appName + "需要获取存储空间权限，获取后可保存文件";
                break;
            case PermissionCode.SHARE_APP:
                message = appName + "需要获取存储空间权限，获取后可分享至第三方";
            default:
                break;
        }
        if (!TextUtils.isEmpty(message)) {
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext)
                    .setTitle("权限申请")
                    .setTopMessage(message)
                    .setTopMessageGravity(Gravity.LEFT)
                    .setRightButton("去授权", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rationale.resume();
                        }
                    });
            if (requestCode != PermissionCode.STARTUP) { // 启动时不允许取消
                builder.setLeftButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rationale.cancel();
                    }
                });
            }
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }
}
