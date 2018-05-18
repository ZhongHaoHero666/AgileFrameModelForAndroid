package com.android.szh.common.image;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

/**
 * 图片URI处理辅助类
 *
 * Created by sunzhonghao
 * @date 2017/8/1 13:55
 */
public class ImageUriHelper {

    /**
     * 将uri地址转换成文件地址
     *
     * @param activity 当前Activity
     * @param uri      uri地址
     * @return 文件地址
     */
    public static String getFilePathFromUri(Activity activity, Uri uri) {
        Log.i("TAG", "uri =" + uri.toString());
        String filePath = null;
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            String uriStr = uri.toString();
            // fix bug .当uri以file:///开头时，无法正确查询到文件路径
            if (uriStr.startsWith("file:///")) {
                int permission = activity.getApplicationContext().checkCallingUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.i("TAG", "permission = " + permission);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    return uriStr;
                } else {
                    Log.i("TAG", "无法读取该文件夹图片，请选择其他图片");
                    return null;
                }
            }

            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

}
