package com.android.szh.common.imageloader.glide;

import android.content.Context;
import android.util.Log;

import com.android.szh.common.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * Glide配置
 * <p>注意：
 * <br>GlideModel需要在AndroidManifest.xml文件中添加<meta-data android:name="com.android.szh.common.imageloader.glide.GlideModelConfig" android:value="GlideModule" />注册，
 * 以便Glide能够找到定义的GlideModel
 * <br>代码混淆时需要添加-keep class com.android.szh.common.glide.GlideModelConfig
 *
 * Created by sunzhonghao
 * @date 2017/4/1 17:25
 */
public class GlideModelConfig implements GlideModule {

    private static final String TAG = "GlideModelConfig";
    private static final int DISK_SIZE = 200 * 1024 * 1024; // 200MB
    private static final int MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory()) / 4;  // 取最大内存的1/4作为最大缓存
    public static final String DISK_CACHE_NAME = "image";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Log.i(TAG, "GlideModule初始化...");

        // 全局设置ViewTaget的tagId
        ViewTarget.setTagId(R.id.glide_tag_id);

        // 定义缓存大小和位置
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_CACHE_NAME, DISK_SIZE)); // 外部存储

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(MEMORY_SIZE)); // 设置缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(MEMORY_SIZE)); // 设置图片池大小

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); // 设置图片格式(默认为PREFER_RGB_565)
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
