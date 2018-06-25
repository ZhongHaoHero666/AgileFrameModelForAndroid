package com.android.szh.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.android.szh.common.imageloader.image.ImagePathHelper;

import java.io.File;
import java.util.List;

/**
 * Android 7.0辅助类(适配FileProvider)
 */
public class Android7Helper {

    private Android7Helper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * 根据{@link File}对象生成对应的{@link Uri}对象(适配7.0)
     *
     * @param context 上下文
     * @param file    File对象
     * @see #getUriForFileProvider(Context, File)
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri;
        if (SDKVersionHelper.hasNougat()) {
            fileUri = getUriForFileProvider(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 根据{@link File}对象通过{@link FileProvider}生成对应的{@link Uri}对象
     *
     * @param context 上下文
     * @param file    File对象
     */
    public static Uri getUriForFileProvider(Context context, File file) {
        String authority = String.format("%s.fileprovider", context.getPackageName());
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * 为{@link Intent}对象设置{@link File}类型的数据
     *
     * @param context   上下文
     * @param intent    {@link Intent}权限
     * @param file      {@link File}对象
     * @param writeAble 是否可写
     */
    public static void setIntentData(Context context, Intent intent, File file, boolean writeAble) {
        setIntentDataAndType(context, intent, file, null, writeAble);
    }

    /**
     * 为{@link Intent}对象设置{@link File}类型的数据
     *
     * @param context   上下文
     * @param intent    {@link Intent}权限
     * @param file      {@link File}对象
     * @param type      {@link Intent}处理的数据的MIME类型
     * @param writeAble 是否可写
     * @see #setIntentDataAndType(Context, Intent, File, String, boolean)
     */
    public static void setIntentDataAndType(Context context, Intent intent, File file, String type, boolean writeAble) {
        setIntentDataAndType(intent, getUriForFile(context, file), type, writeAble);
    }

    /**
     * 为{@link Intent}对象设置{@link Uri}类型的数据
     *
     * @param intent    {@link Intent}权限
     * @param uri       {@link Uri}对象
     * @param type      {@link Intent}处理的数据的MIME类型
     * @param writeAble 是否可写
     * @see #setIntentDataAndType(Intent, Uri, String, boolean)
     */
    public static void setIntentDataAndType(Intent intent, Uri uri, String type, boolean writeAble) {
        if (SDKVersionHelper.hasNougat()) {
            if (TextUtils.isEmpty(type)) {
                intent.setData(uri);
            } else {
                intent.setDataAndType(uri, type);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(uri, type);
        }
    }

    /**
     * 为{@link Intent}对象授予临时权限
     *
     * @param context   上下文
     * @param intent    {@link Intent}权限
     * @param uri       {@link Uri}对象
     * @param writeAble 是否可写
     */
    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }

    /**
     * 返回用于安装APK的{@link Intent}对象
     *
     * @param context 上下文
     * @param file    {@link File}对象
     */
    public static Intent getIntentWithInstallApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (SDKVersionHelper.hasNougat()) {
            intent.setDataAndType(getUriForFileProvider(context, file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        return intent;
    }

    /**
     * 返回用于拍照的{@link Intent}对象
     *
     * @param context 上下文
     * @param file    {@link File}对象
     */
    public static Intent getIntentWithCapture(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        if (SDKVersionHelper.hasNougat()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFileProvider(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        return intent;
    }

    /**
     * 返回从相册选择图片的{@link Intent}对象
     */
    public static Intent getIntentWithGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * 返回从文件选择图片的{@link Intent}对象
     */
    public static Intent getIntentWithDocuments() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 返回用于裁剪的{@link Intent}对象
     *
     * @param context    上下文
     * @param inputUri   {@link Uri}对象
     * @param outputFile {@link File}对象
     */
    public static Intent getIntentWithCrop(Context context, Uri inputUri, File outputFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (SDKVersionHelper.hasNougat()) {
            intent.setDataAndType(inputUri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String path = ImagePathHelper.getPath(context, inputUri);
                intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }

        }
        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        // 设置裁剪
        intent.putExtra("crop", true);
        // aspectX/aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX/outputY是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

}
