package com.android.szh.common.imageloader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.szh.common.imageloader.ImageLoaderOptions;
import com.android.szh.common.imageloader.ImageLoaderStrategy;
import com.android.szh.common.imageloader.ImageLoadingListener;
import com.android.szh.common.imageloader.model.CropType;
import com.android.szh.common.imageloader.model.ImageSize;
import com.android.szh.common.utils.NetworkHelper;
import com.android.szh.common.utils.ThreadHelper;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Glide图片加载策略
 *
 * Created by sunzhonghao
 * @date 2016/12/12 16:52
 */
public class GlideImageLoaderStrategy implements ImageLoaderStrategy {

    private Context context;
    private RequestManager requestManager;

    public GlideImageLoaderStrategy(Context context) {
        this.context = context.getApplicationContext();
        requestManager = Glide.with(this.context);
    }

    public GlideImageLoaderStrategy(Activity activity) {
        this.context = activity.getApplicationContext();
        requestManager = Glide.with(activity);
    }

    public GlideImageLoaderStrategy(FragmentActivity activity) {
        this.context = activity.getApplicationContext();
        requestManager = Glide.with(activity);
    }

    public GlideImageLoaderStrategy(android.app.Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("The fragment has not attached to a Activity.");
        }
        this.context = activity.getApplicationContext();
        requestManager = Glide.with(fragment);
    }

    public GlideImageLoaderStrategy(android.support.v4.app.Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("The fragment has not attached to a Activity.");
        }
        this.context = activity.getApplicationContext();
        requestManager = Glide.with(fragment);
    }

    @Override
    public void loadFromResource(ImageView imageView, int resourceId, ImageLoaderOptions options) {
        load(imageView, resourceId, options);
    }

    @Override
    public void loadFromAssets(ImageView imageView, String assetName, ImageLoaderOptions options) {
        load(imageView, "file:///android_asset/" + assetName, options);
    }

    @Override
    public void loadFromFile(ImageView imageView, File file, ImageLoaderOptions options) {
        load(imageView, file, options);
    }

    @Override
    public void loadFromUri(ImageView imageView, Uri uri, ImageLoaderOptions options) {
        load(imageView, uri, options);
    }

    @Override
    public void loadFromUrl(ImageView imageView, String url, ImageLoaderOptions options) {
        loadFromUrl(imageView, url, options, false);
    }

    @Override
    public void loadFromUrl(ImageView imageView, String url, ImageLoaderOptions options, boolean loadOnlyWifi) {
        boolean loadFromCache = false;
        if (loadOnlyWifi) { // 仅在WIFI情况下加载
            if (!NetworkHelper.isWiFiAvailable(context)) { // WIFI不可用
                loadFromCache = true;
            }
        } else { // 正常加载
            if (!NetworkHelper.isNetworkAvailable(context)) { // 网络不可用
                loadFromCache = true;
            }
        }
        if (loadFromCache) { // 直接加载缓存
            load(requestManager.using(new StreamCacheLoader()).load(url), imageView, url, options);
        } else {
            load(requestManager.load(url), imageView, url, options);
        }
    }

    @Override
    public Bitmap downloadImage(String url, ImageLoaderOptions options) {
        return downloadImage(url, options, false);
    }

    @Override
    public Bitmap downloadImage(String url, ImageLoaderOptions options, boolean downloadOnlyWifi) {
        boolean loadFromCache = false;
        if (downloadOnlyWifi) { // 仅在WIFI情况下加载
            if (!NetworkHelper.isWiFiAvailable(context)) { // WIFI不可用
                loadFromCache = true;
            }
        } else { // 正常加载
            if (!NetworkHelper.isNetworkAvailable(context)) { // 网络不可用
                loadFromCache = true;
            }
        }
        DrawableTypeRequest<String> request;
        if (loadFromCache) { // 直接加载缓存
            request = requestManager.using(new StreamCacheLoader()).load(url);
        } else {
            request = requestManager.load(url);
        }
        initImageLoaderOptions(request, null, url, options);
        ImageSize imageSize = options.getImageSize();
        if (imageSize == null) {
            throw new IllegalArgumentException("The imageSize can not be null.");
        }
        try {
            return request.asBitmap()
                    .into(imageSize.getWidth(), imageSize.getHeight())
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resumeRequests() {
        requestManager.resumeRequests();
    }

    @Override
    public void pauseRequests() {
        requestManager.pauseRequests();
    }

    @Override
    public void cancleRequests(Object object) {
        if (object instanceof View) {
            Glide.clear((View) object);
        } else if (object instanceof FutureTarget) {
            Glide.clear((FutureTarget<?>) object);
        } else if (object instanceof Target) {
            Glide.clear((Target<?>) object);
        }
    }

    @Override
    public void clearMemoryCache() {
        if (!ThreadHelper.isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread.");
        }
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        if (!ThreadHelper.isOnBackgroundThread()) {
            throw new IllegalArgumentException("You must call this method on a background thread.");
        }
        Glide.get(context).clearDiskCache();
    }

    private <ModelType> void load(ImageView imageView, ModelType model, ImageLoaderOptions options) {
        if (model == null) {
            throw new IllegalArgumentException("model is null.");
        }
        load(requestManager.load(model), imageView, model, options);
    }

    private <ModelType> void load(DrawableTypeRequest request, ImageView imageView, ModelType model, ImageLoaderOptions options) {
        if (options == null) {
            options = ImageLoaderOptions.getDefaultOptions();
        }
        initImageLoaderOptions(request, imageView, model, options);
        if (options.getTarget() != null) {
            request.into(options.getTarget());
        } else {
            request.into(imageView);
        }
    }

    /**
     * 初始化图片加载配置选项
     *
     * @param request     图片加载请求
     * @param imageView   图片展示控件
     * @param model       数据模型
     * @param options     图片加载配置选项
     * @param <ModelType> 数据模型泛型
     */
    @NonNull
    private <ModelType> ImageLoaderOptions initImageLoaderOptions(DrawableTypeRequest request, ImageView imageView, ModelType model, ImageLoaderOptions options) {
        if (options.getPlaceHolderResId() != null) {
            request.placeholder(options.getPlaceHolderResId()); // 设置加载中的占位图
        }
        if (options.getErrorResId() != null) {
            request.error(options.getErrorResId()); // 设置加载失败的占位图
        }
        if (options.isAsGif()) {
            request.asGif(); // 加载Gif
        } else {
            request.asBitmap(); // 加载Bitmap
        }
        if (options.getImageSize() != null) {
            ImageSize imageSize = options.getImageSize();
            if (imageSize.getWidth() != 0 && imageSize.getHeight() != 0) {
                request.override(imageSize.getWidth(), imageSize.getHeight()); // 设置加载的图片大小
            }
        }
        if (options.getTag() != null) {
            request.signature(new StringSignature(options.getTag())); // 设置图片标识
        } else if (model != null && !TextUtils.isEmpty(model.toString())) {
            request.signature(new StringSignature(model.toString())); // 设置图片标识
        }
        if (options.getPriority() != null) {
            request.priority(options.getPriority()); // 设置图片加载优先级(优先级高的先加载，优先级低的后加载)
        }
        if (options.isSkipMemoryCache()) {
            request.skipMemoryCache(options.isSkipMemoryCache()); // 设置是否跳过内存缓存
        }
        if (options.getDiskCacheStrategy() != null) {
            request.diskCacheStrategy(options.getDiskCacheStrategy()); // 设置磁盘缓存策略
        }
        if (options.getCropType() == CropType.CENTER_CROP) {
            request.centerCrop(); // 当图片比ImageView大的时候，会把超过ImageView的部分裁剪掉，尽可能的让ImageView完全填充，但图像可能不会全部显示
        } else if (options.getCropType() == CropType.FIT_CENTER) {
            request.fitCenter(); // 会自适应ImageView的大小，并且会完整的显示图片在ImageView中，但是ImageView可能不会完全填充
        }
        if (options.getTransformations() != null) {
            request.transform(options.getTransformations()); // 设置动态转换
        }
        if (options.isDontAnimate()) {
            request.dontAnimate();
        } else if (options.isCrossFade()) {
            request.crossFade(); // 设置渐入渐出的动画效果
            if (options.getCrossDuration() > 0) {
                request.crossFade(options.getCrossDuration()); // 设置动画执行时间
            }
        } else if (options.getAnimator() != null) {
            request.animate(options.getAnimator()); // 设置自定义动画
        } else if (options.getAnimResId() != null) {
            request.animate(options.getAnimResId()); // 设置自定义动画
        }
        if (options.getThumbnail() > 0.0f) {
            request.thumbnail(options.getThumbnail()); // 设置缩略图支持
        }
        if (options.getRequestListener() != null) {
            ImageLoadingListener requestListener = options.getRequestListener();
            setRequestListener(request, imageView, requestListener); // 设置图片加载监听
        }
        return options;
    }

    /**
     * 设置图片加载监听
     *
     * @param <ModelType> 数据模型泛型
     * @param request     图片加载请求
     * @param imageView   图片展示控件
     * @param listener    图片加载监听
     */
    private <ModelType> void setRequestListener(DrawableTypeRequest<ModelType> request, final ImageView imageView, final ImageLoadingListener<ModelType> listener) {
        request.listener(new RequestListener<ModelType, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, ModelType model, Target<GlideDrawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(imageView, model, e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, ModelType model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Bitmap bitmap;
                if (resource instanceof GlideBitmapDrawable) {
                    bitmap = ((GlideBitmapDrawable) resource).getBitmap();
                } else {
                    bitmap = ((BitmapDrawable) resource.getCurrent()).getBitmap();
                }
                listener.onLoadingComplete(imageView, model, bitmap);
                return false;
            }
        });
    }

    private static class StreamCacheLoader implements StreamModelLoader<String> {

        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int width, int height) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    }
}
