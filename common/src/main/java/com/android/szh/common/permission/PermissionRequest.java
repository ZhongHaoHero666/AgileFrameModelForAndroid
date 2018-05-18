package com.android.szh.common.permission;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 权限请求
 *
 * Created by sunzhonghao
 * @date 2017/7/27 17:55
 */
public final class PermissionRequest implements Request<PermissionRequest>, Rationale, OnRequestResultListener {

    private static final String TAG = "PermissionHelper";
    private static final String ITEM_INDENT = "    ";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final int FLAG_NO_NEED = 1;
    public static final int FLAG_ALL_GRANTED = 2;

    @IntDef({FLAG_NO_NEED, FLAG_ALL_GRANTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionFlag {

    }

    private WeakReference<Activity> activity;
    private int mRequestCode;
    private boolean isDebugMode;
    private Collection<String> mPermissions;
    private OnPrepareListener mPrepareListener;
    private OnPermissionsCallback mCallback;
    private boolean isContinueRequest;
    private final PermissionsResult mPermissionsResult;
    private final Object permissionLock = new Object();

    PermissionRequest(Object object) {
        if (object instanceof Activity) {
            this.activity = new WeakReference<>((Activity) object);
        } else if (object instanceof android.app.Fragment) {
            Activity activity = ((android.app.Fragment) object).getActivity();
            this.activity = new WeakReference<>(activity);
        } else if (object instanceof Fragment) {
            Activity activity = ((Fragment) object).getActivity();
            this.activity = new WeakReference<>(activity);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported.");
        }
        this.mPermissions = new ArrayList<>();
        this.mPermissionsResult = new PermissionsResult();
    }

    /**
     * 设置是否为调试模式
     */
    @Override
    public PermissionRequest debugMode(boolean debug) {
        this.isDebugMode = debug;
        return this;
    }

    /**
     * 设置需要请求的权限
     */
    @Override
    public PermissionRequest permission(String permission) {
        this.mPermissions.add(permission);
        return this;
    }

    /**
     * 设置需要请求的权限
     */
    @Override
    public PermissionRequest permissions(String... permissions) {
        this.mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    /**
     * 设置需要请求的权限
     */
    @Override
    public PermissionRequest permissions(String[]... permissions) {
        for (String[] permissionArray : permissions) {
            mPermissions.addAll(Arrays.asList(permissionArray));
        }
        return this;
    }

    /**
     * 设置需要请求的权限
     */
    @Override
    public PermissionRequest permissions(Collection<String> permissions) {
        this.mPermissions.addAll(permissions);
        return this;
    }

    /**
     * 设置权限请求准备监听
     */
    @Override
    public PermissionRequest prepare(OnPrepareListener listener) {
        this.mPrepareListener = listener;
        return this;
    }

    /**
     * 设置权限请求回调
     */
    @Override
    public PermissionRequest callback(OnPermissionsCallback callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * 设置权限请求码
     */
    @Override
    public PermissionRequest requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    /**
     * 请求权限
     */
    @Override
    public void request() {
        if (activity.get() == null) {
            return;
        }
        if (mPermissions == null || mPermissions.isEmpty()) {
            printLog("需要请求的权限为空");
            return;
        }
        if (!PermissionHelper.isOverMarshmallow()) { // 判断系统版本号是否小于6.0
            updatePermissionsAsGranted(mPermissions);
            callbackPermissionsResult();
            printLog("当前系统版本小于Android 6.0系统，不需要请求权限");
        } else {
            List<String> permissions = PermissionHelper.findUnGrantedPermissions(activity.get(), mPermissions);// 查找未授予的权限集合
            if (permissions.isEmpty()) { // 判断所有权限是否被授予
                updatePermissionsAsGranted(permissions);
                callbackPermissionsResult();
                printLog("需要请求的所有权限都已被授予，不需要请求权限");
            } else {
                this.mPermissions = permissions;
                if (mPrepareListener != null) {
                    mPrepareListener.onPermissionsPrepareRequest(mRequestCode, permissions, this);
                } else {
                    resume();
                }
                printLog("有请求的权限未授予，需要进行权限请求，请指定权限请求操作");
                printLog(mPermissions);
            }
        }
    }

    @Override
    public void resume() {
        if (isContinueRequest) {
            if (mPermissionsResult != null) {
                mPermissions.addAll(mPermissionsResult.getDeniedPermissionNames());
                mPermissionsResult.getDeniedPermissions().clear();
            }
            isContinueRequest = false;
        }
        PermissionsFragment permissionsFragment = getPermissionsFragment(activity.get());
        permissionsFragment.setResultListener(this);
        String[] permissions = mPermissions.toArray(new String[mPermissions.size()]);
        permissionsFragment.requestPermissions(mRequestCode, permissions);
        printLog("开始进行权限请求...");
    }

    @Override
    public void cancel() {
        if (isContinueRequest) {
            if (mPermissions != null) {
                mPermissions.clear();
                mPermissions = null;
            }
            if (mPermissionsResult != null) {
                mPermissionsResult.clear();
            }
            mPrepareListener = null;
            mCallback = null;
            activity.clear();
        } else {
            updatePermissionsAsDenied(mPermissions);
            callbackPermissionsResult();
        }
    }

    @Override
    public void onRequestResultListener(Collection<String> grantedPermissions, Collection<String> deniedPermissions) {
        updatePermissionsAsGranted(grantedPermissions);
        updatePermissionsAsDenied(deniedPermissions);
        callbackPermissionsResult();
    }

    /**
     * 回调权限请求结果
     */
    private void callbackPermissionsResult() {
        synchronized (permissionLock) {
            if (mCallback != null) {
                if (mPermissionsResult.areAllPermissionsGranted()) {
                    List<String> deniedPermissions = mPermissionsResult.getDeniedPermissionNames();
                    @PermissionFlag int flag;
                    if (!PermissionHelper.isOverMarshmallow()) {
                        flag = FLAG_NO_NEED;
                    } else {
                        flag = FLAG_ALL_GRANTED;
                    }
                    mCallback.onPermissionsGranted(mRequestCode, deniedPermissions, flag);
                } else {
                    List<String> deniedPermissions = mPermissionsResult.getDeniedPermissionNames();
                    if (mPermissionsResult.isAnyPermissionNeverAskAgain()) {
                        mCallback.onPermissionsDenied(mRequestCode, deniedPermissions);
                    } else {
                        isContinueRequest = true;
                        mCallback.onShowRequestPermissionRationale(mRequestCode, deniedPermissions, this);
                    }
                }
            }
            printLog(mPermissionsResult);
            printLog("权限请求结果返回，请根据需求处理权限请求结果或指定权限请求操作");
        }
    }

    /**
     * 将指定的权限集合作为授予的权限存入{@link PermissionsResult}对象中
     *
     * @param permissions 指定的权限集合
     */
    private void updatePermissionsAsGranted(Collection<String> permissions) {
        for (String permission : permissions) {
            PermissionGranted response = PermissionGranted.from(permission);
            mPermissionsResult.addGrantedPermission(response);
        }
        removeCheckedPermissions(permissions);
    }

    /**
     * 将指定的权限集合作为拒绝的权限存入{@link PermissionsResult}对象中
     *
     * @param permissions 指定的权限集合
     */
    private void updatePermissionsAsDenied(Collection<String> permissions) {
        for (String permission : permissions) {
            boolean showRequestPermissionRationale = PermissionHelper.shouldShowRequestPermissionRationale(activity.get(), permission);
            PermissionDenied response = PermissionDenied.from(permission, !showRequestPermissionRationale);
            mPermissionsResult.addDeniedPermission(response);
        }
        removeCheckedPermissions(permissions);
    }

    /**
     * 将指定的权限集合从未处理的权限集合中移除
     *
     * @param permissions 指定的权限集合
     */
    private void removeCheckedPermissions(Collection<String> permissions) {
        synchronized (permissionLock) {
            if (permissions == null || permissions.isEmpty()) {
                return;
            }
            if (mPermissions == null || mPermissions.isEmpty()) {
                return;
            }
            mPermissions.removeAll(permissions);
        }
    }

    /**
     * 返回用于权限请求的{@link Fragment}对象
     *
     * @param activity 当前所在{@link Activity}对象
     */
    private PermissionsFragment getPermissionsFragment(Activity activity) {
        PermissionsFragment permissionsFragment = findPermissionsFragment(activity);
        boolean isNewInstance = permissionsFragment == null;
        if (isNewInstance) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permissionsFragment;
    }

    /**
     * 查找用于权限请求的{@link Fragment}对象
     *
     * @param activity 当前所在{@link Activity}对象
     */
    private PermissionsFragment findPermissionsFragment(Activity activity) {
        return (PermissionsFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    /**
     * 打印日志
     *
     * @param object 日志对象
     */
    private void printLog(Object object) {
        if (isDebugMode && object != null) {
            if (object instanceof String) {
                Log.i(TAG, (String) object);
            } else if (object instanceof Collection) {
                Collection collection = (Collection) object;
                StringBuilder builder = new StringBuilder();
                builder.append("[").append(LINE_SEPARATOR);
                if (!collection.isEmpty()) {
                    for (Object permission : collection) {
                        builder.append(ITEM_INDENT + permission.toString()).append(LINE_SEPARATOR);
                    }
                }
                builder.append("]");
                Log.i(TAG, "需要请求的权限 size = " + collection.size() + " " + builder.toString());
            } else if (object instanceof PermissionsResult) {
                PermissionsResult result = (PermissionsResult) object;
                List<PermissionGranted> grantedPermissions = result.getGrantedPermissions();
                StringBuilder grantedBuilder = new StringBuilder();
                grantedBuilder.append("[").append(LINE_SEPARATOR);
                if (!grantedPermissions.isEmpty()) {
                    for (PermissionGranted permission : grantedPermissions) {
                        grantedBuilder.append(ITEM_INDENT + permission.getPermissionName())
                                .append(LINE_SEPARATOR);
                    }
                }
                grantedBuilder.append("]");
                List<PermissionDenied> deniedPermissions = result.getDeniedPermissions();
                StringBuilder deniedBuilder = new StringBuilder();
                deniedBuilder.append("[").append(LINE_SEPARATOR);
                if (!deniedPermissions.isEmpty()) {
                    for (PermissionDenied permission : deniedPermissions) {
                        deniedBuilder.append(ITEM_INDENT + permission.getPermissionName())
                                .append("(是否不再询问：" + permission.isNeverAskAgain() + ")")
                                .append(LINE_SEPARATOR);
                    }
                }
                deniedBuilder.append("]");
                Log.i(TAG, "授予的权限 size = " + grantedPermissions.size() + " " + grantedBuilder.toString());
                Log.i(TAG, "拒绝的权限 size = " + deniedPermissions.size() + " " + deniedBuilder.toString());
                Log.i(TAG, "用户是否授予了所有请求的权限：" + result.areAllPermissionsGranted());
                Log.i(TAG, "用户是否永久拒绝请求的某一个或多个权限：" + result.isAnyPermissionNeverAskAgain());
            } else {
                Log.i(TAG, object.toString());
            }
        }
    }
}
