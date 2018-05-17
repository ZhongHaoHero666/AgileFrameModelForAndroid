package com.android.szh.common.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;


import com.android.szh.common.logger.Logger;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.security.auth.x500.X500Principal;

/**
 * App信息工具类
 *
 */
public class AppHelper {

    private static final String TAG = "AppUtils";

    private AppHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * 获取PackageManager对象
     */
    public static PackageManager getPackageManager(Context context) {
        return context.getApplicationContext().getPackageManager();
    }

    /**
     * 获取应用程序包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取PackageInfo对象
     *
     * @see PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = getPackageManager(context);
            return packageManager.getPackageInfo(getPackageName(context), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取ApplicationInfo对象
     *
     * @see ApplicationInfo
     */
    public static ApplicationInfo getApplicationInfo(Context context) {
        try {
            PackageManager packageManager = getPackageManager(context);
            return packageManager.getApplicationInfo(getPackageName(context), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        return null;
    }

    /**
     * 获取应用程序图标
     */
    public static Drawable getAppIcon(Context context) {
        try {
            PackageManager packageManager = getPackageManager(context);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(context), 0);
            if (applicationInfo != null) {
                return applicationInfo.loadIcon(packageManager);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * 获取应用程序版本名称
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return packageInfo.versionName;
    }

    /**
     * 获取应用程序版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return packageInfo.versionCode;
    }

    /**
     * 获取当前系统的可用内存信息
     */
    public static int getAvailMemorySize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(outInfo);//获取的内存信息放入到outInfo中
        }
        long size = outInfo.availMem;//当前系统中剩余的可用内存信息 单位 byte
        return (int) size / 1024 / 1024;
    }

    /**
     * 根据资源名称获取资源ID
     */
    public static int getResourceID(Context context, String name, String defType) {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * 获取应用进程名称
     */
    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 判断应用程序是否是debug包
     */
    public static boolean isDebuggable(Context context) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(getPackageName(context), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (Signature signature : signatures) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
            return debuggable;
        } catch (PackageManager.NameNotFoundException | CertificateException e) {
            return false;
        }
    }

    /**
     * 判断应用程序是否处于前台
     */
    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用程序是否处于后台
     */
    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list) {// 获取运行服务再启动
            if (info.service.getClassName().equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;

    }

    /**
     * 判断进程是否运行
     */
    public static boolean isProessRunning(Context context, String proessName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 获取获取Manifest中“meta-data”元素的值(Application级别)
     */
    public static Object getMetaDataFromApplication(Context context, String key) {
        try {
            PackageManager packageManager = getPackageManager(context);
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
            return null;
        }
    }

    /**
     * 获取获取Manifest中“meta-data”元素的值(Activity级别)
     */
    public static Object getMetaDataFromActivity(Context context, Class<?> activityCls, String key) {
        try {
            PackageManager packageManager = getPackageManager(context);
            ActivityInfo appInfo = packageManager.getActivityInfo(new ComponentName(context, activityCls), PackageManager.GET_META_DATA);
            return appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
            return null;
        }
    }

    /**
     * 获取获取Manifest中“meta-data”元素的值(Service级别)
     */
    public static Object getMetaDataFromService(Context context, Class<?> serviceCls, String key) {
        try {
            PackageManager packageManager = getPackageManager(context);
            ServiceInfo appInfo = packageManager.getServiceInfo(new ComponentName(context, serviceCls), PackageManager.GET_META_DATA);
            return appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
            return null;
        }
    }

    /**
     * 获取获取Manifest中“meta-data”元素的值(Receiver级别)
     */
    public static Object getMetaDataFromReceiver(Context context, Class<?> receiverCls, String key) {
        try {
            PackageManager packageManager = getPackageManager(context);
            ActivityInfo appInfo = packageManager.getReceiverInfo(new ComponentName(context, receiverCls), PackageManager.GET_META_DATA);
            return appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
            return null;
        }
    }

    /**
     * 判断应用程序是否安装
     */
    public static boolean isInsatalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            PackageManager packageManager = getPackageManager(context);
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取APP的Pid
     */
    public static int getAppPid() {
        return android.os.Process.myPid();
    }

    /**
     * 结束程序
     */
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid()); // 杀死进程
        System.exit(0); // 退出程序，返回值为0代表正常退出
    }

}
