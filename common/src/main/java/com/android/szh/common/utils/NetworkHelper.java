package com.android.szh.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断网络状态的工具类
 *
 * @author liyunlong
 * @date 2017/2/4 11:26
 */
public class NetworkHelper {

    public static final String DNS1 = "dns1";
    public static final String DNS2 = "dns2";

    private NetworkHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    public enum NetworkType {

        NETWORK_NONE("NONE"),
        NETWORK_UNKNOWN("UNKNOWN"),
        NETWORK_WIFI("WIFI"),
        NETWORK_2G("2G"),
        NETWORK_3G("3G"),
        NETWORK_4G("4G");

        private String name;

        NetworkType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 获取ConnectivityManager对象
     */
    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断网络是否是4G
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean is4G(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 获取网络运营商名称
     */
    public static String getNetworkProvider(Context context) {
        String provider = "未知";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return provider;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return provider;
            }
            @SuppressLint("HardwareIds")
            String IMSI = telephonyManager.getSubscriberId();
            if (TextUtils.isEmpty(IMSI)) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            provider = "未知";
        }
        return provider;
    }

    /**
     * 获取当前网络类型
     */
    public static String getNetworkTypeName(Context context) {
        return getNetworkType(context).getName();
    }

    /**
     * 获取当前网络类型
     */
    public static NetworkType getNetworkType(Context context) {
        NetworkType networkType = NetworkType.NETWORK_UNKNOWN;
        NetworkInfo networkInfo = getConnectivityManager(context).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = NetworkType.NETWORK_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String subTypeName = networkInfo.getSubtypeName();
                int subType = networkInfo.getSubtype();
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2G
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G
                    case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
                    case TelephonyManager.NETWORK_TYPE_1xRTT:// 电信2G
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                        networkType = NetworkType.NETWORK_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信3G
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:// 电信3G
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // 电信3G
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 联通3G
                    case TelephonyManager.NETWORK_TYPE_HSDPA: // 联通3G
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                        networkType = NetworkType.NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:  //3G到4G的一个过渡，称为准4G
                        networkType = NetworkType.NETWORK_4G;
                        break;
                    default:
                        //中国移动 联通 电信 三种3G制式
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            networkType = NetworkType.NETWORK_3G;
                        } else {
                            networkType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            }
        }
        return networkType;
    }

    /**
     * 获取当前的网络状态 （-1：没有网络；1：WIFI网络；2：移动网络；3：net网络；4：wap网络）
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = getConnectivityManager(context);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = 2;
//            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
//                netType = 3;
//            } else {
//                netType = 4;
//            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 判断网络是否可以使用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();// 后去可用的网络信息
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) { // 判断网络是否可用
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网络是否已连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断WiFi是否可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWiFiAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 得到与WiFi相关的网络信息
            if (networkInfo != null) {
                return networkInfo.isAvailable();// 判断网络是否可用
            }
        }
        return false;
    }

    /**
     * 判断WiFi是否已连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWiFiConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 得到与WiFi相关的网络信息
            if (networkInfo != null) {
                return networkInfo.isConnected();// 判断网络是否已连接
            }
        }
        return false;
    }

    /**
     * 获得WiFi的连接状态
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static NetworkInfo.State getWiFiNetState(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.getState();// 得到当前网络的连接状态
            }
        }
        return null;
    }

    /**
     * 判断Mobile是否可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isMobileAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断Mobile是否已连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 获得Mobile的连接状态
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static NetworkInfo.State getMobileNetState(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.getState();
            }
        }
        return null;
    }

    /**
     * 判断当前的网络类型(WiFi还是Mobile)
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static String getNetTypeName(Context context) {
        if (context != null) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return activeNetworkInfo.getTypeName();
            }
        }
        return null;
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 移动网络开关
     */
    public static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass; // ConnectivityManager类
        Field iConMgrField; // ConnectivityManager类中的字段
        Object iConMgr; // IConnectivityManager类的引用
        Class<?> iConMgrClass; // IConnectivityManager类
        Method setMobileDataEnabledMethod; // setMobileDataEnabled方法
        try {
            conMgrClass = Class.forName(conMgr.getClass().getName());// 取得ConnectivityManager类
            iConMgrField = conMgrClass.getDeclaredField("mService");// 取得ConnectivityManager类中的对象mService
            iConMgrField.setAccessible(true);// 设置mService可访问
            iConMgr = iConMgrField.get(conMgr);// 取得mService的实例化类IConnectivityManager
            iConMgrClass = Class.forName(iConMgr.getClass().getName());// 取得IConnectivityManager类
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);// 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);// 调用setMobileDataEnabled方法
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException |
                NoSuchMethodException | IllegalArgumentException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设备IP地址(WIFI)
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {// 判断wifi是否开启
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);
        }
        return null;
    }

    /**
     * 获取设备IP地址(2G/3G/4G)
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 域名解析
     */
    public static Map<String, String> getDomainResolution(String dormain) {
        Map<String, String> map = new HashMap<>();
        long timeMillis = 0;
        long time = 0;
        InetAddress[] inetAddresses = null;
        try {
            timeMillis = System.currentTimeMillis();
            inetAddresses = InetAddress.getAllByName(dormain);
            if (inetAddresses != null) {
                time = System.currentTimeMillis() - timeMillis;
            }
        } catch (UnknownHostException e) {
            time = System.currentTimeMillis() - timeMillis;
            inetAddresses = null;
        } finally {
            String useTime;
            String ipAddresses = null;
            if (time > 5000) {// 如果大于1000ms，则换用s来显示
                useTime = time / 1000 + "s";
            } else {
                useTime = time + "ms";
            }
            if (inetAddresses != null && inetAddresses.length > 0) {
                StringBuilder builder = new StringBuilder();
                int length = inetAddresses.length;
                for (int i = 0; i < length; i++) {
                    builder.append(inetAddresses[i].getHostAddress());
                    if (i < length - 1) {
                        builder.append(",");
                    }
                }
                ipAddresses = builder.toString();
            } else {
                ipAddresses = "DNS解析失败";
            }
            map.put("ipAddresses", ipAddresses);
            map.put("useTime", useTime);
        }
        return map;
    }

    /**
     * 获取本地DNS
     */
    public static String getLocalDns(String dns) {
        StringBuilder builder = new StringBuilder();
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("getprop net." + dns);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
            }
        }
        return builder.toString().trim();
    }

    /**
     * 返回Ping结果
     */
    public static String getPingResult(String ping) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(ping);
            int status = process.waitFor();
            if (status == 0) {
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                // 主宿机的IP冲突，或者网关有重复的路由设置，也或者路由器堵塞比较厉害，也可能还有其他原因会导致ping包的时候收到多个重复值回应
                if (!TextUtils.isEmpty(line) && !line.contains("(DUP!)")) { // 过滤重复回应
                    builder.append(line);
                    if (!TextUtils.isEmpty(line)) {
                        builder.append("\r\n");
                    }
                }
            }
            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
            }
        }
        return builder.toString().trim();
    }

    /**
     * 返回Ping结果
     */
    public static int getPingStatus(String ping) {
        Process process = null;
        int status = -1;
        try {
            process = Runtime.getRuntime().exec(ping);
            status = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
            }
        }
        return status;
    }

    /**
     * 返回Ping命令
     *
     * @param ip IP地址
     * @see #getPingCommand(int, int, int, String)
     */
    public static String getPingCommand(String ip) {
        return getPingCommand(4, 64, 1, ip);
    }

    /**
     * 返回Ping命令
     *
     * @param count 发送数据包的数量
     * @param size  发送数据包的大小(单位：byte)
     * @param time  发送数据包的时间间隔(单位：秒)
     * @param ip    IP地址
     */
    public static String getPingCommand(int count, int size, int time, String ip) {
        return "ping" + " -c " + count + " -s " + size + " -i " + time + " " + ip;
    }

}
