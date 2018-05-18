package com.android.szh.common.permission;

import java.util.List;

/**
 * 权限请求准备监听
 *
 * Created by sunzhonghao
 * @date 2017/7/31 14:20
 */
public interface OnPrepareListener {

    /**
     * 当检查有请求的权限未授予时调用该方法(可以用于提示用户需要的权限信息等操作)
     *
     * @param permissions 未授予的权限集合
     * @param rationale   提供给用户选择权限请求如何进行的工具
     * @see Rationale
     */
    void onPermissionsPrepareRequest(int requestCode, List<String> permissions, Rationale rationale);
}
