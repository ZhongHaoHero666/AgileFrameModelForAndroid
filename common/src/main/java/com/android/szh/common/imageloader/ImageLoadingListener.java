package com.android.szh.common.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片加载监听接口
 *
 * Created by sunzhonghao
 * @date 2017/2/9 13:43
 */
public interface ImageLoadingListener<ModelType> {

    void onLoadingStarted(ImageView imageView, ModelType model);

    void onLoadingFailed(ImageView imageView, ModelType model, Exception e);

    void onLoadingComplete(ImageView imageView, ModelType model, Bitmap bitmap);

}
