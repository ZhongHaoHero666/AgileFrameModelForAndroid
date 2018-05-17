package com.android.szh.common.logger.printer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.android.szh.common.logger.utils.LogConstants;
import com.android.szh.common.utils.CharsetHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件打印助手(输出日志信息到文件)
 *
 * Created by sunzhonghao
 * @date 2017/7/24 10:35
 */
public class FilePrinter extends Printer {

    private static final String DIRECTORY_NAME = LogConstants.TAG;
    private static final String DIRECTORY_PATH = new File(Environment.getExternalStorageDirectory(), DIRECTORY_NAME).getPath();
    private static final String FILE_NAME_VERBOSE = "_verbose";
    private static final String FILE_NAME_INFO = "_info";
    private static final String FILE_NAME_DEBUG = "_debug";
    private static final String FILE_NAME_WARN = "_warn";
    private static final String FILE_NAME_ERROR = "_error";
    private static final String FILE_NAME_ASSERT = "_assert";
    private static final String FILE_NAME_SUFFIX = ".log";
    private Context mContext;
    private String mDirectory;
    private ExecutorService mExecutorService;

    public FilePrinter(Context context) {
        this(context, null);
    }

    public FilePrinter(Context context, String directory) {
        this.mContext = context;
        this.mDirectory = directory;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        if (TextUtils.isEmpty(mDirectory)) {
            mDirectory = mContext.getPackageName();
        }
    }

    @Override
    protected void printLog(final int level, final String tag, final String message) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                saveMessageToSDCard(level, tag, message);
            }
        });
    }

    private synchronized void saveMessageToSDCard(int level, String tag, String message) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 如果SD卡不存在或无法使用，则无法把日志信息写入SD卡
            System.err.println("sdcard unmounted, skip dump exception");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 如果是系统版本号大于6.0，则需要检查读写SD卡权限
            String packageName = mContext.getPackageName();
            PackageManager packageManager = mContext.getPackageManager();
            int result = packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName);
            if (result != PackageManager.PERMISSION_GRANTED) { // 如果没有权限，则无法把日志信息写入SD卡
                System.err.println("It does not have the permission " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
        File dir = new File(DIRECTORY_PATH, mDirectory); // 日志文件目录
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) { // 如果目录创建失败，则无法把日志信息写入SD卡
                System.err.println("Failed to create directory " + dir.getAbsolutePath());
                return;
            }
        }
        String fileName;
        switch (level) {
            case Log.VERBOSE:
                fileName = FILE_NAME_VERBOSE;
                break;
            case Log.INFO:
                fileName = FILE_NAME_INFO;
                break;
            case Log.DEBUG:
                fileName = FILE_NAME_DEBUG;
                break;
            case Log.WARN:
                fileName = FILE_NAME_WARN;
                break;
            case Log.ERROR:
                fileName = FILE_NAME_ERROR;
                break;
            case Log.ASSERT:
                fileName = FILE_NAME_ASSERT;
                break;
            default:
                fileName = FILE_NAME_VERBOSE;
                break;
        }
        String fileNameTime = generateFormatDate("yyyy-MM-dd");
        fileName = fileNameTime + fileName; // 拼接打印时间(yyyy-MM-dd格式)
        fileName = fileName + FILE_NAME_SUFFIX; // 拼接文件后缀(.log格式)
        File logFile = new File(dir, fileName); // 日志文件
        try {
            boolean needPrintPhoneInfo;
            if (!logFile.exists()) {
                boolean newFile = logFile.createNewFile();
                if (!newFile) { // 如果文件创建失败，则无法把日志信息写入SD卡
                    System.err.println("Failed to create file " + logFile.getAbsolutePath());
                }
                needPrintPhoneInfo = true;
            } else {
                needPrintPhoneInfo = false;
            }
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), CharsetHelper.UTF_8)));
            if (!needPrintPhoneInfo) {
                // 换行
                printWriter.println();
                printWriter.println();
            }
            // 打印发生异常的时间
            String timeSecond = generateFormatDate("yyyy-MM-dd HH:mm:ss");
            printWriter.println(timeSecond);
            if (needPrintPhoneInfo) {
                // 打印设备信息
                printDeviceInfo(printWriter);
                //换行
                printWriter.println();
            }
            //打印日志信息
            printWriter.print(tag + "\t" + message);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印设备信息
     */
    private void printDeviceInfo(PrintWriter printWriter) {
        try {
            // 应用的版本名称和版本号
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            printWriter.print("App Version:");
            printWriter.print(packageInfo.versionName);
            printWriter.print('(');
            printWriter.print("versionCode:");
            printWriter.print(packageInfo.versionCode);
            printWriter.println(')');

            // Android版本号
            printWriter.print("OS Version:");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print('(');
            printWriter.print("SDKVersion:");
            printWriter.print(Build.VERSION.SDK_INT);
            printWriter.println(')');

            // 手机制造商
            printWriter.print("Vendor:");
            printWriter.println(Build.MANUFACTURER);

            // 手机型号
            printWriter.print("Model:");
            printWriter.println(Build.MODEL);

            // CPU架构
            printWriter.print("CPU ABI:");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printWriter.println(Build.SUPPORTED_ABIS[0]);
            } else {
                printWriter.println(Build.CPU_ABI);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成格式化的日期时间
     *
     * @param pattern 日期时间格式
     */
    private String generateFormatDate(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }

}
