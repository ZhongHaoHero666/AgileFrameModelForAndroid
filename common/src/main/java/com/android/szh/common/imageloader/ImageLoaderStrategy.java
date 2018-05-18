package com.android.szh.common.imageloader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载策略接口
 *
 * Created by sunzhonghao
 * @date 2016/12/12 16:45
 */
public interface ImageLoaderStrategy {

    /**
     * 加载资源图片
     *
     * @param imageView  ImageView对象
     * @param resourceId 图片资源ID
     * @param options    图片加载配置选项
     */
    void loadFromResource(ImageView imageView, int resourceId, ImageLoaderOptions options);

    /**
     * 加载Assets图片
     *
     * @param imageView ImageView对象
     * @param assetName Assets图片名称
     * @param options   图片加载配置选项
     */
    void loadFromAssets(ImageView imageView, String assetName, ImageLoaderOptions options);

    /**
     * 加载本地图片
     *
     * @param imageView ImageView对象
     * @param file      本地图片文件
     * @param options   图片加载配置选项
     */
    void loadFromFile(ImageView imageView, File file, ImageLoaderOptions options);

    /**
     * 加载指定URI的图片
     */
    void loadFromUri(ImageView imageView, Uri uri, ImageLoaderOptions options);

    /**
     * 加载网络图片
     *
     * @param imageView ImageView对象
     * @param url       图片地址Url
     * @param options   图片加载配置选项
     */
    void loadFromUrl(ImageView imageView, String url, ImageLoaderOptions options);

    /**
     * 加载网络图片
     *
     * @param imageView    ImageView对象
     * @param url          图片地址Url
     * @param options      图片加载配置选项
     * @param loadOnlyWifi 是否仅在WIFI情况下加载
     */
    void loadFromUrl(ImageView imageView, String url, ImageLoaderOptions options, boolean loadOnlyWifi);

    /**
     * 下载图片
     *
     * @param url              图片地址Url
     * @param options          图片加载配置选项
     */
    Bitmap downloadImage(String url, ImageLoaderOptions options);

    /**
     * 下载图片
     *
     * @param url              图片地址Url
     * @param options          图片加载配置选项
     * @param downloadOnlyWifi 是否仅在Wifi情况下下载
     */
    Bitmap downloadImage(String url, ImageLoaderOptions options, boolean downloadOnlyWifi);

    /**
     * 恢复请求
     */
    void resumeRequests();

    /**
     * 暂停请求
     */
    void pauseRequests();

    /**
     * 取消请求
     */
    void cancleRequests(Object object);

    /**
     * 清除内存缓存
     */
    void clearMemoryCache();

    /**
     * 清除磁盘缓存
     */
    void clearDiskCache();

}
