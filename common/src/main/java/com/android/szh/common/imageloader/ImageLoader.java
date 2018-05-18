package com.android.szh.common.imageloader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;


import com.android.szh.common.utils.UrlHelper;

import java.io.File;

/**
 * 图片加载辅助类(封装)
 *
 * Created by sunzhonghao
 * @date 2017/10/27 15:37
 */
public class ImageLoader {

    /**
     * 设置默认图片加载策略
     */
    public static void setDefaultStrategy() {
        ImageLoaderHelper.getInstance().setDefaultStrategy();
    }

    /**
     * 返回当前图片加载策略
     */
    public static ImageLoaderStrategy getCustomStrategy() {
        return ImageLoaderHelper.getInstance().getCustomStrategy();
    }

    /**
     * 设置自定义图片加载策略
     *
     * @param strategy 图片加载策略
     */
    public static void setCustomStrategy(ImageLoaderStrategy strategy) {
        ImageLoaderHelper.getInstance().setCustomStrategy(strategy);
    }

    /**
     * 加载资源图片
     */
    public static void loadFromResource(ImageView target, int resId, ImageLoaderOptions options) {
        ImageLoaderHelper.getInstance().loadFromResource(target, resId, options);
    }

    /**
     * 加载Assets文件
     */
    public static void loadFromAssets(ImageView target, String assetName, ImageLoaderOptions options) {
        ImageLoaderHelper.getInstance().loadFromAssets(target, assetName, options);
    }

    /**
     * 加载本地文件
     */
    public static void loadFromFile(ImageView target, File file, ImageLoaderOptions options) {
        ImageLoaderHelper.getInstance().loadFromFile(target, file, options);
    }

    /**
     * 加载指定URI的图片
     */
    public static void loadFromUri(ImageView imageView, Uri uri, ImageLoaderOptions options) {
        ImageLoaderHelper.getInstance().loadFromUri(imageView, uri, options);
    }

    /**
     * 加载网络图片
     */
    public static void loadFromUrl(ImageView target, String url, ImageLoaderOptions options) {
        url = UrlHelper.getFullUrlWithDomain(url); // 处理Url
        ImageLoaderHelper.getInstance().loadFromUrl(target, url, options);
    }

    /**
     * 加载网络图片
     */
    public static void loadFromUrl(ImageView target, String url, ImageLoaderOptions options, boolean loadOnlyWifi) {
        url = UrlHelper.getFullUrlWithDomain(url); // 处理Url
        ImageLoaderHelper.getInstance().loadFromUrl(target, url, options, loadOnlyWifi);
    }

    /**
     * 下载图片
     */
    public static Bitmap downloadImage(String url, ImageLoaderOptions options) {
        url = UrlHelper.getFullUrlWithDomain(url); // 处理Url
        return ImageLoaderHelper.getInstance().downloadImage(url, options);
    }

    /**
     * 下载图片
     */
    public static Bitmap downloadImage(String url, ImageLoaderOptions options, boolean downloadOnlyWifi) {
        url = UrlHelper.getFullUrlWithDomain(url); // 处理Url
        return ImageLoaderHelper.getInstance().downloadImage(url, options, downloadOnlyWifi);
    }

    /**
     * 恢复请求
     */
    public static void resumeRequests() {
        ImageLoaderHelper.getInstance().resumeRequests();
    }

    /**
     * 取消请求
     */
    public static void pauseRequests() {
        ImageLoaderHelper.getInstance().pauseRequests();
    }

    /**
     * 取消请求
     */
    public static void cancleRequests(Object object) {
        ImageLoaderHelper.getInstance().cancleRequests(object);
    }

    /**
     * 清除内存缓存(在主线程执行)
     */
    public static void clearMemoryCache() {
        ImageLoaderHelper.getInstance().clearMemoryCache();
    }

    /**
     * 清除磁盘缓存(在后台线程执行)
     */
    public static void clearDiskCache() {
        ImageLoaderHelper.getInstance().clearDiskCache();
    }

}
