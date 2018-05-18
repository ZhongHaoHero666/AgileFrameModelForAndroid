package com.android.szh.common.imageloader;

import android.support.annotation.DrawableRes;

import com.android.szh.common.imageloader.model.CropType;
import com.android.szh.common.imageloader.model.ImageSize;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BaseTarget;

/**
 * 图片加载配置选项
 *
 * Created by sunzhonghao
 * @date 2016/12/12 16:46
 */
public class ImageLoaderOptions {

    private static final int RESOURCE_NONE = -1; // 没有占位图
    private String tag; // 唯一标识
    private BaseTarget target;
    private int placeHolderResId; // 默认占位资源
    private int errorResId; // 错误时显示的资源
    private boolean dontAnimate; // 是否禁止动画
    private boolean crossFade; // 是否淡入淡出动画
    private int crossDuration; // 淡入淡出动画持续的时间
    private ImageSize imageSize; // 图片最终显示在ImageView上的宽高度像素
    private CropType cropType; // 裁剪类型(默认为中部裁剪)
    private boolean asGif; // true表示强制显示的是gif动画,如果url不是gif的资源,那么会回调error()
    private Priority priority;
    private boolean skipMemoryCache;// 是否跳过内存缓存(默认false不跳过)
    private DiskCacheStrategy diskCacheStrategy; // 硬盘缓存(默认为all类型)
    private float thumbnail;// 设置缩略图的缩放比例0.0f-1.0f,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
    private Integer animResId;// 图片加载完后的动画效果,在异步加载资源完成时会执行该动画。
    private ViewPropertyAnimation.Animator animator; // 在异步加载资源完成时会执行该动画
    private BitmapTransformation[] transformations;
    private ImageLoadingListener requestListener;

    public static ImageLoaderOptions getDefaultOptions() {
        return getDefaultOptions(RESOURCE_NONE, RESOURCE_NONE);
    }

    public static ImageLoaderOptions getDefaultOptions(@DrawableRes int errorResId, @DrawableRes int placeHolderResId) {
        return getDefaultOptions(errorResId, placeHolderResId, CropType.FIT_CENTER);
    }

    public static ImageLoaderOptions getDefaultOptions(@DrawableRes int errorResId, @DrawableRes int placeHolderResId, CropType cropType) {
        return new Builder()
                .setErrorResId(errorResId)
                .setPlaceHolderResId(placeHolderResId)
                .setAsGif(false)
                .setImageSize(null)
                .setCrossFade(false)
                .setDontAnimate(true)
                .setSkipMemoryCache(false)
                .setDiskCacheStrategy(DiskCacheStrategy.SOURCE)
                .setCropType(cropType)
                .build();
    }

    public static Builder newBuilder(ImageLoaderOptions options) {
        Builder builder = new Builder();
        builder.tag = options.tag;
        builder.target = options.target;
        builder.placeHolderResId = options.placeHolderResId;
        builder.errorResId = options.errorResId;
        builder.dontAnimate = options.dontAnimate;
        builder.crossFade = options.crossFade;
        builder.crossDuration = options.crossDuration;
        builder.imageSize = options.imageSize;
        builder.cropType = options.cropType;
        builder.asGif = options.asGif;
        builder.priority = options.priority;
        builder.skipMemoryCache = options.skipMemoryCache;
        builder.diskCacheStrategy = options.diskCacheStrategy;
        builder.thumbnail = options.thumbnail;
        builder.animResId = options.animResId;
        builder.animator = options.animator;
        builder.transformations = options.transformations;
        builder.requestListener = options.requestListener;
        return builder;
    }

    private ImageLoaderOptions(Builder builder) {
        this.tag = builder.tag;
        this.target = builder.target;
        this.placeHolderResId = builder.placeHolderResId;
        this.errorResId = builder.errorResId;
        this.dontAnimate = builder.dontAnimate;
        this.crossFade = builder.crossFade;
        this.crossDuration = builder.crossDuration;
        this.imageSize = builder.imageSize;
        this.cropType = builder.cropType;
        this.asGif = builder.asGif;
        this.priority = builder.priority;
        this.skipMemoryCache = builder.skipMemoryCache;
        this.diskCacheStrategy = builder.diskCacheStrategy;
        this.thumbnail = builder.thumbnail;
        this.animResId = builder.animResId;
        this.animator = builder.animator;
        this.transformations = builder.transformations;
        this.requestListener = builder.requestListener;
    }

    public String getTag() {
        return tag;
    }

    public BaseTarget getTarget() {
        return target;
    }

    public Integer getPlaceHolderResId() {
        return placeHolderResId;
    }

    public Integer getErrorResId() {
        return errorResId;
    }

    public boolean isDontAnimate() {
        return dontAnimate;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public int getCrossDuration() {
        return crossDuration;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public CropType getCropType() {
        return cropType;
    }

    public boolean isAsGif() {
        return asGif;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public DiskCacheStrategy getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public float getThumbnail() {
        return thumbnail;
    }

    public Integer getAnimResId() {
        return animResId;
    }

    public ViewPropertyAnimation.Animator getAnimator() {
        return animator;
    }

    public BitmapTransformation[] getTransformations() {
        return transformations;
    }

    public ImageLoadingListener getRequestListener() {
        return requestListener;
    }

    /**
     * Builder类
     */
    public static class Builder {

        private String tag; //唯一标识
        private BaseTarget target;
        private int placeHolderResId; //默认占位资源
        private int errorResId; //错误时显示的资源
        private boolean dontAnimate; //是否禁止动画
        private boolean crossFade; //是否淡入淡出动画
        private int crossDuration; //淡入淡出动画持续的时间
        private ImageSize imageSize; //图片最终显示在ImageView上的宽高度像素
        private CropType cropType; //裁剪类型,默认为中部裁剪
        private boolean asGif; //true,强制显示的是gif动画,如果url不是gif的资源,那么会回调error()
        private Priority priority;
        private boolean skipMemoryCache;//是否跳过内存缓存,默认false不跳过
        private DiskCacheStrategy diskCacheStrategy; //硬盘缓存,默认为all类型
        private float thumbnail;//设置缩略图的缩放比例0.0f-1.0f,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
        private Integer animResId;//图片加载完后的动画效果,在异步加载资源完成时会执行该动画。
        private ViewPropertyAnimation.Animator animator; //在异步加载资源完成时会执行该动画。可以接受一个Animator对象
        private BitmapTransformation[] transformations;
        private ImageLoadingListener requestListener;

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setTarget(BaseTarget target) {
            this.target = target;
            return this;
        }

        public Builder setPlaceHolderResId(@DrawableRes int placeHolderResId) {
            this.placeHolderResId = placeHolderResId;
            return this;
        }

        public Builder setErrorResId(@DrawableRes int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder setDontAnimate(boolean dontAnimate) {
            this.dontAnimate = dontAnimate;
            return this;
        }

        public Builder setCrossFade(boolean crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public Builder setCrossDuration(int crossDuration) {
            this.crossDuration = crossDuration;
            return this;
        }

        public Builder setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Builder setCropType(CropType cropType) {
            this.cropType = cropType;
            return this;
        }

        public Builder setAsGif(boolean asGif) {
            this.asGif = asGif;
            return this;
        }

        public Builder setPriority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder setSkipMemoryCache(boolean skipMemoryCache) {
            this.skipMemoryCache = skipMemoryCache;
            return this;
        }

        public Builder setDiskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }

        public Builder setThumbnail(float thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder setAnimResId(Integer animResId) {
            this.animResId = animResId;
            return this;
        }

        public Builder setAnimator(ViewPropertyAnimation.Animator animator) {
            this.animator = animator;
            return this;
        }

        public Builder setTransformations(BitmapTransformation[] transformations) {
            this.transformations = transformations;
            return this;
        }

        public <ModelType> Builder setImageLoadingListener(ImageLoadingListener<ModelType> requestListener) {
            this.requestListener = requestListener;
            return this;
        }

        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this);
        }
    }

}
