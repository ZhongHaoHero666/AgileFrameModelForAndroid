package com.android.szh.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.szh.common.config.UrlConfig;
import com.android.szh.common.logger.Logger;
import com.android.szh.common.logger.printer.FilePrinter;
import com.android.szh.common.logger.printer.LogcatPrinter;
import com.android.szh.common.utils.AppHelper;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.http.Url;

/**
 * Application基类
 * <ul>
 * <strong>注意：</strong>
 * <li>重写{@link Application}的生命周期方法时必须调用父类的事件处理程序
 * <li>如果需要使用MultiDex解决Dex超出方法数的限制问题，可以让应用程序的{@link Application}继承{@link MultiDexApplication}或者重写{@link Application}的{@link #attachBaseContext(Context)}方法并添加{@link MultiDex#install(Context)}
 * <li>如果需要使用MultiDex解决Dex超出方法数的限制问题，必须在build.gradle文件的defaultConfig模块中将multiDexEnabled设置为true开启分包模式，并在dependencies模块中添加'com.android.support:multidex:1.0.1'依赖
 * </ul>
 * <ul>
 * <strong>生命周期方法介绍：</strong>
 * <li>{@link #onCreate()}：在创建应用程序时调用这个方法。可以重写这个方法来实例化应用程序单态，也可以创建和实例化任何应用程序状态变量或共享资源。
 * <li>{@link #onLowMemory()}：一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。可以重写这个处理程序来清空缓存或者释放不必要的资源。
 * <li>{@link #onTerminate()}：当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，将不调用此方法而直接终止进程。
 * <li>{@link #onTrimMemory(int)}：当运行时决定当前应用程序应该尝试减少其内存开销时(通常在它进入后台时)调用。它包含一个level参数，用于提供请求的上下文。
 * <li>{@link #onConfigurationChanged(Configuration)}：如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这个值，或者在应用程序级别处理配置改变，应用程序对象不会被终止和重启。
 * </ul>
 * <p>
 * Created by sunzhonghao
 *
 * @date 2018/5/6 14:43
 */
public abstract class BaseApplication extends Application {

    static {
        // 设置启用矢量图片资源向后兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // 设置全局异常处理(处理无法传递的异常)
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.e(TAG, throwable);
            }
        });

        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                MaterialHeader header = new MaterialHeader(context);
                header.setColorSchemeColors(0xfffc5638, 0xfffc5638, 0xfffc5638, 0xfffc5638, 0xfffc5638);
                return header;//指定为MaterialHeader
            }
        });
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setEnableLoadmoreWhenContentNotFull(true);//内容不满一页时候启用加载更多
                BallPulseFooter footer = new BallPulseFooter(context);
                footer.setSpinnerStyle(SpinnerStyle.Scale);//设置为拉伸模式
                return footer;//指定为BallPulseFooter
            }
        });
    }

    /**
     * TAG
     */
    private static final String TAG = "com.android.szh.common.BaseApplication";
    /**
     * {@link Application}实例
     */
    private static BaseApplication sInstance;
    /**
     * 是否是debug 环境
     */
    private static boolean DEBUG;
    /**
     * 网络是否已连接
     */
    private static boolean isNetworkAvailable;
    /**
     * 是否处于前台
     */
    private boolean isForeground;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public static Resources getAppResources() {
        return sInstance.getResources();
    }

    public static boolean isNetworkConnected() {
        return isNetworkAvailable;
    }

    public static void setNetworkConnected(boolean isNetworkConnected) {
        BaseApplication.isNetworkAvailable = isNetworkConnected;
    }

    public static boolean isDebugModel() {
        return DEBUG;
    }

    public static void setDebugModel(boolean DEBUG) {
        BaseApplication.DEBUG = DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        DEBUG = !BuildConfig.ONLINE;

        //声明一个默认BaseUrl之外的BaseUrl
        RetrofitUrlManager.getInstance().putDomain(UrlConfig.FLAG_MULTIPLE_BASE_URL_A, UrlConfig.getDominUrlA());

        String processName = AppHelper.getProcessName(this, Process.myPid());
        Logger.i(TAG, "当前进程为：" + processName);
        if (!TextUtils.isEmpty(processName) && processName.equals(getPackageName())) { // 判断是否为当前进程，避免浪费资源
            Logger.i(TAG, "初始化进程为：" + getPackageName());
            initStetho();                   //初始化Stetho
            initLogger();                   //初始化Log
            initARouter();                  //初始化路由中间件
            initStrictMode();               //初始化StrictMode(严苛模式)，检查和报告应用中存在的问题
            //TODO                          //可以在此初始化 推送，统计等自定义业务
            LeakCanary.install(this);       //内存泄漏检测工具
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 判断APP是否在前台
     */
    public boolean isForeground() {
        return isForeground;
    }

    /**
     * 设置APP是否在前台
     */
    public void setForeground(boolean foreground) {
        isForeground = foreground;
    }

    /**
     * 初始化阿里路由
     */
    protected void initARouter() {
        if (DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 初始化
    }


    /**
     * 初始化日志输出
     */
    protected void initLogger() {
        Logger.getLogConfig()
                .setLogEnabled(DEBUG)
                .setShowMethodInfo(true)
                .setShowThreadInfo(false);
        Logger.addPrinter(new LogcatPrinter(false));
        Logger.addPrinter(new FilePrinter(this));
    }

    /**
     * 初始化Stetho
     */
    private void initStetho() {
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    /**
     * 初始化StrictMode(严苛模式)，检查和报告应用中存在的问题
     */
    private void initStrictMode() {
        // 设置检测当前线程上的操作策略
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()    // 侦测一切潜在违规(自定义的耗时操作/磁盘读操作/磁盘写操作/网络操作)
                .penaltyLog()   // 将违规信息写入系统日志
                .build();
        StrictMode.setThreadPolicy(threadPolicy);
        // 设置用于检测VM进程(在任何线程上)的操作的策略
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()    // 侦测一切潜在违规(侦测Activity泄露/Closeable对象或其他对象在明确结束调用后未关闭/数据库未关闭/BroadcastReceiver或ServiceConnection是否被释放/网络访问是否有加密/应用是否通过file://的方式将文件共享给其他应用)
                .penaltyLog()   // 将违规信息写入系统日志
                .build();
        StrictMode.setVmPolicy(vmPolicy);
    }
}