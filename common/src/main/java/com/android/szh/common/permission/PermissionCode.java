package com.android.szh.common.permission;

import android.Manifest;

/**
 * 通用权限请求Code码
 */
public class PermissionCode {

    /**
     * 启动--设备信息、存储空间{@link Manifest.permission#READ_PHONE_STATE}和{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    public static final int STARTUP = 1000;
    /**
     * 拍照--相机、存储空间{@link Manifest.permission#CAMERA}和{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    public static final int TAKEPHOTO = 1002;
    /**
     * 相册--存储空间{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    public static final int PICTURES = 1004;
    /**
     * 定位--位置信息{@link Manifest.permission#ACCESS_FINE_LOCATION}
     */
    public static final int NEARBYSTATION = 1005;
    /**
     * 保存图片{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    public static final int SAVE_IMAGE = 1006;
    /**
     * 分享--存储空间{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     */
    public static final int SHARE_APP = 1007;
}
