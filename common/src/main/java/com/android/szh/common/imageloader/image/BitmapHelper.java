package com.android.szh.common.imageloader.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;


import com.android.szh.common.BaseApplication;
import com.android.szh.common.utils.CloseHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap辅助类
 *
 * Created by sunzhonghao
 * @date 2017/9/11 9:55
 */
public class BitmapHelper {

    static File compressImage(Uri fileUri, int outWidth, int outHeight, int maxSize, Bitmap.CompressFormat format, int quality, String targetPath) throws IOException {
        String filePath = ImagePathHelper.getPath(BaseApplication.getInstance(), fileUri);
        return compressImage(filePath, outWidth, outHeight, maxSize, format, quality, targetPath);
    }

    static File compressImage(File file, int outWidth, int outHeight, int maxSize, Bitmap.CompressFormat format, int quality, String targetPath) throws IOException {
        return compressImage(file.getAbsolutePath(), outWidth, outHeight, maxSize, format, quality, targetPath);
    }

    static File compressImage(String filePath, int outWidth, int outHeight, int maxSize, Bitmap.CompressFormat format, int quality, String targetPath) throws IOException {
        Bitmap actualOutBitmap = decodeSampledBitmapFromFile(filePath, outWidth, outHeight);
        if (actualOutBitmap == null) {
            return null;
        }
        if (quality <= 0) {
            quality = 100;
        }
        //进行有损压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        actualOutBitmap.compress(format, quality, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
        if (maxSize > 0) { // 判断指定的图片大小是否大于0
            int baosLength = baos.toByteArray().length;
            while (baosLength / 1024 > maxSize) {// 循环判断如果压缩后图片是否大于maxSize,大于继续压缩
                baos.reset();// 重置baos即让下一次的写入覆盖之前的内容
                quality = Math.max(0, quality - 10);// 图片质量每次减少10
                actualOutBitmap.compress(format, quality, baos);// 将压缩后的图片保存到baos中
                baosLength = baos.toByteArray().length;
                if (quality == 0)//如果图片的质量已降到最低则，不再进行压缩
                    break;
            }
        }
        actualOutBitmap.recycle();

        File targetFile = new File(targetPath);
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (targetFile.exists() && targetFile.isFile()) {
            targetFile.delete();
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(targetFile);
            bos = new BufferedOutputStream(fos);// 包装缓冲流,提高写入速度
            bos.write(baos.toByteArray());
            bos.flush();
        } finally {
            CloseHelper.closeIOQuietly(baos, fos, bos);
        }

        return new File(targetPath);
    }


    static Bitmap decodeSampledBitmapFromFile(String filePath, int outWidth, int outHeight) throws IOException {
        // 进行大小缩放来达到压缩的目的
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // 根据原始图片的宽高比和期望的输出图片的宽高比计算最终输出的图片的宽和高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        float srcRatio = srcWidth / srcHeight;
        float outRatio = outWidth / outHeight;
        float actualOutWidth = srcWidth;
        float actualOutHeight = srcHeight;
        if (srcWidth > outWidth || srcHeight > outHeight) {
            // 如果输入比率小于输出比率,则最终输出的宽度以outHeight为准()
            // 比如输入比为10:20 输出比是300:10 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的高为10
            // 宽度为10/20 * 10 = 5  最终输出图片的比率为5:10 和原始输入的比率相同
            // 同理如果输入比率大于输出比率,则最终输出的高度以outHeight为准()
            // 比如输入比为20:10 输出比是5:100 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的宽为5
            // 高度需要根据输入图片的比率计算获得 为5 / 20/10= 2.5  最终输出图片的比率为5:2.5 和原始输入的比率相同
            if (srcRatio < outRatio) {
                actualOutHeight = outHeight;
                actualOutWidth = actualOutHeight * srcRatio;
            } else if (srcRatio > outRatio) {
                actualOutWidth = outWidth;
                actualOutHeight = actualOutWidth / srcRatio;
            } else {
                actualOutWidth = outWidth;
                actualOutHeight = outHeight;
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualOutWidth, actualOutHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (scaledBitmap == null) {
            return null; // 压缩失败
        }
        // 生成最终输出的Bitmap
        Bitmap actualOutBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int) actualOutWidth, (int) actualOutHeight, true);
        if (actualOutBitmap != scaledBitmap) {
            scaledBitmap.recycle();
        }
        // 处理图片旋转问题
        int degree = getRotateDegree(filePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        actualOutBitmap = Bitmap.createBitmap(actualOutBitmap, 0, 0, actualOutBitmap.getWidth(), actualOutBitmap.getHeight(), matrix, true);
        return actualOutBitmap;
    }

    /**
     * 计算Bitmap的缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return Bitmap的缩放比例
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
        // 图像的原始高度和宽度
        final int srcHeight = options.outHeight;
        final int srcWidth = options.outWidth;
        int inSampleSize = 1;

        if (srcHeight > reqHeight || srcWidth > reqWidth) {
            final int heightRatio = Math.round(srcHeight / reqHeight);
            final int widthRatio = Math.round(srcWidth / reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }

        final float totalPixels = srcWidth * srcHeight;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    /**
     * 获得图像旋转角(采用ExitInterface获取图片旋转角度)
     *
     * @param path 图片路径
     */
    private static int getRotateDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
