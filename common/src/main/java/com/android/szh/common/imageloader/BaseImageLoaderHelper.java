package com.android.szh.common.imageloader;

/**
 * 图片加载辅助类接口
 *
 * Created by sunzhonghao
 * @date 2017/3/31 10:19
 */
public interface BaseImageLoaderHelper extends ImageLoaderStrategy {

    /**
     * 设置默认图片加载策略
     */
    void setDefaultStrategy();

    /**
     * 返回当前图片加载策略
     */
    ImageLoaderStrategy getCustomStrategy();

    /**
     * 设置自定义图片加载策略
     *
     * @param strategy 图片加载策略
     */
    void setCustomStrategy(ImageLoaderStrategy strategy);

}
