package com.android.szh.common.imageloader.image;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by sunzhonghao
 * @date 2017/9/11 10:45
 */
public class Compressor {

    private static final int DEFAULT_MAX_WIDTH = 720;
    private static final int DEFAULT_MAX_HEIGHT = 1280;
    private static final int DEFAULT_MAX_SIZE = 0;
    private static final int DEFAULT_MAX_QUALITY = 80;
    private static final String DEFAULT_DIR_NAME = "images";
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    // max width and height values of the compressed image is taken as 720x1280
    private Context context;
    private int maxWidth;
    private int maxHeight;
    private int maxSize;
    private int quality;
    private String targetDir;
    private Bitmap.CompressFormat format;

    public static Compressor with(Context context) {
        return new Compressor(context);
    }

    private Compressor(Context context) {
        this.context = context;
        this.maxWidth = DEFAULT_MAX_WIDTH;
        this.maxHeight = DEFAULT_MAX_HEIGHT;
        this.maxSize = DEFAULT_MAX_SIZE;
        this.quality = DEFAULT_MAX_QUALITY;
        this.format = DEFAULT_COMPRESS_FORMAT;
        File imagesDir = this.context.getExternalFilesDir(DEFAULT_DIR_NAME);
        if (imagesDir == null) {
            imagesDir = new File(this.context.getFilesDir(), DEFAULT_DIR_NAME);
        }
        this.targetDir = imagesDir.getAbsolutePath();
    }

    public Compressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Compressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Compressor setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public Compressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public Compressor setCompressFormat(Bitmap.CompressFormat format) {
        this.format = format;
        return this;
    }

    public Compressor setTargetDirectory(String targetDir) {
        this.targetDir = targetDir;
        return this;
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return BitmapHelper.decodeSampledBitmapFromFile(imageFile.getAbsolutePath(), maxWidth, maxHeight);
    }

    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    public File compressToFile(File imageFile, String targetFileName) throws IOException {
        String targetPath = new File(targetDir, targetFileName).getAbsolutePath();
        return BitmapHelper.compressImage(imageFile, maxWidth, maxHeight, maxSize, format, quality, targetPath);
    }

    public Observable<Bitmap> compressToBitmapAsObservable(final File imageFile) {
        return Observable.defer(new Callable<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                try {
                    return Observable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public Observable<File> compressToFileAsObservable(final File imageFile) {
        return compressToFileAsObservable(imageFile, imageFile.getName());
    }

    public Observable<File> compressToFileAsObservable(final File imageFile, final String targetFileName) {
        return Observable.defer(new Callable<Observable<File>>() {
            @Override
            public Observable<File> call() throws Exception {
                try {
                    return Observable.just(compressToFile(imageFile, targetFileName));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

}
