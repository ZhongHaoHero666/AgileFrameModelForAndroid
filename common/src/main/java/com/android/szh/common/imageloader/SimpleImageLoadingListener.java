package com.android.szh.common.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片加载监听接口的简单实现
 *
 * Created by sunzhonghao
 * @date 2017/4/7 16:19
 */
public class SimpleImageLoadingListener<ModelType> implements ImageLoadingListener<ModelType> {

    @Override
    public void onLoadingStarted(ImageView imageView, ModelType model) {

    }

    @Override
    public void onLoadingFailed(ImageView imageView, ModelType model, Exception e) {

    }

    @Override
    public void onLoadingComplete(ImageView imageView, ModelType model, Bitmap bitmap) {

    }
}
