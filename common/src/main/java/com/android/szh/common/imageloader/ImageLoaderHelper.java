package com.android.szh.common.imageloader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;


import com.android.szh.common.BaseApplication;
import com.android.szh.common.imageloader.glide.GlideImageLoaderStrategy;

import java.io.File;

/**
 * 图片加载辅助类
 *
 * Created by sunzhonghao
 * @date 2016/12/12 17:56
 */
public class ImageLoaderHelper implements BaseImageLoaderHelper {

    private ImageLoaderStrategy mDefaultStrategy;
    private ImageLoaderStrategy mCustomStrategy;

    private static class ImageLoaderHelperHolder {
        private static final ImageLoaderHelper INSTANCE = new ImageLoaderHelper();
    }

    private ImageLoaderHelper() {
        this.mDefaultStrategy = new GlideImageLoaderStrategy(BaseApplication.getInstance());
        this.setCustomStrategy(mDefaultStrategy);
    }

    public static ImageLoaderHelper getInstance() {
        return ImageLoaderHelperHolder.INSTANCE;
    }

    /**
     * 设置默认图片加载策略
     */
    @Override
    public void setDefaultStrategy() {
        this.mCustomStrategy = mDefaultStrategy;
    }

    /**
     * 返回当前图片加载策略
     */
    @Override
    public ImageLoaderStrategy getCustomStrategy() {
        return mCustomStrategy;
    }

    /**
     * 设置自定义图片加载策略
     *
     * @param strategy 图片加载策略
     */
    @Override
    public void setCustomStrategy(ImageLoaderStrategy strategy) {
        this.mCustomStrategy = strategy;
    }

    /**
     * 加载资源图片
     */
    @Override
    public void loadFromResource(ImageView target, int resId, ImageLoaderOptions options) {
        mCustomStrategy.loadFromResource(target, resId, options);
    }

    /**
     * 加载Assets文件
     */
    @Override
    public void loadFromAssets(ImageView target, String assetName, ImageLoaderOptions options) {
        mCustomStrategy.loadFromAssets(target, assetName, options);
    }

    /**
     * 加载本地文件
     */
    @Override
    public void loadFromFile(ImageView target, File file, ImageLoaderOptions options) {
        mCustomStrategy.loadFromFile(target, file, options);
    }

    /**
     * 加载指定URI的图片
     */
    @Override
    public void loadFromUri(ImageView imageView, Uri uri, ImageLoaderOptions options) {
        mCustomStrategy.loadFromUri(imageView, uri, options);
    }

    /**
     * 加载网络图片
     */
    @Override
    public void loadFromUrl(ImageView target, String url, ImageLoaderOptions options) {
        mCustomStrategy.loadFromUrl(target, url, options);
    }

    /**
     * 加载网络图片
     */
    @Override
    public void loadFromUrl(ImageView target, String url, ImageLoaderOptions options, boolean loadOnlyWifi) {
        mCustomStrategy.loadFromUrl(target, url, options, loadOnlyWifi);
    }

    /**
     * 下载图片
     */
    @Override
    public Bitmap downloadImage(String url, ImageLoaderOptions options) {
        return mCustomStrategy.downloadImage(url, options);
    }

    /**
     * 下载图片
     */
    @Override
    public Bitmap downloadImage(String url, ImageLoaderOptions options, boolean downloadOnlyWifi) {
        return mCustomStrategy.downloadImage(url, options, downloadOnlyWifi);
    }

    /**
     * 恢复请求
     */
    @Override
    public void resumeRequests() {
        mCustomStrategy.resumeRequests();
    }

    /**
     * 取消请求
     */
    @Override
    public void pauseRequests() {
        mCustomStrategy.pauseRequests();
    }

    /**
     * 取消请求
     */
    @Override
    public void cancleRequests(Object object) {
        mCustomStrategy.cancleRequests(object);
    }

    /**
     * 清除内存缓存
     */
    @Override
    public void clearMemoryCache() {
        mCustomStrategy.clearMemoryCache();
    }

    /**
     * 清除磁盘缓存
     */
    @Override
    public void clearDiskCache() {
        mCustomStrategy.clearDiskCache();
    }
}
