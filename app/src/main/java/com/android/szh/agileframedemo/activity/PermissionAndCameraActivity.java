package com.android.szh.agileframedemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.constant.UserAvatorConfig;
import com.android.szh.common.imageloader.ImageLoader;
import com.android.szh.common.imageloader.ImageLoaderOptions;
import com.android.szh.common.imageloader.image.Compressor;
import com.android.szh.common.logger.Logger;
import com.android.szh.common.permission.OnPermissionCallback;
import com.android.szh.common.permission.Permission;
import com.android.szh.common.permission.PermissionCode;
import com.android.szh.common.permission.PermissionHelper;
import com.android.szh.common.permission.SimplePermissionsCallback;
import com.android.szh.common.utils.Android7Helper;
import com.android.szh.common.utils.FileHelper;
import com.android.szh.common.utils.SDKVersionHelper;
import com.android.szh.common.utils.ToastUtil;
import com.android.szh.common.widget.BottomPopupWindow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sunzhonghao on 2018/5/18.
 * desc:申请权限及选照片的展示类
 */

public class PermissionAndCameraActivity extends BaseActivity {

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @Override
    protected void initViews() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_permiss_and_camera;
    }

    @OnClick(R.id.btn_check_permission)
    public void onViewClicked() {
        showCameraAndPhoto();
    }


    BottomPopupWindow mChouseWindow;
    /**
     * 使用照相机获取照片时,定义的照片名称
     */
    private Uri photoTempUri;
    private String tempImageUrl = null;

    /**
     * 展示相机和相册选择框
     */
    private void showCameraAndPhoto() {
        if (mChouseWindow == null) {
            mChouseWindow = new BottomPopupWindow(this).setListAction(new BottomPopupWindow.OnActionItemClickListener() {
                @Override
                public void onActionItemClickListener(CharSequence action, int position) {
                    checkPermissions(position);
                }
            }, "拍照", "从相册里选取");
        }
        mChouseWindow.show();
    }

    /**
     * 根据选择检查权限
     *
     * @param position 相册、相机
     */
    private void checkPermissions(int position) {
        PermissionHelper.with(this)
                .permissions(position == 1 ? Permission.GROUP_STORAGE : Permission.GROUP_CAMERA)
                .requestCode(position == 1 ? PermissionCode.PICTURES : PermissionCode.TAKEPHOTO) //打开相册 还是 照相
                .callback(new SimplePermissionsCallback(this, new OnPermissionCallback() {
                    @Override
                    public void onPermissionCallback(int requestCode) {
                        if (requestCode == PermissionCode.PICTURES) {
                            openGallery();//打开相册
                        } else if (requestCode == PermissionCode.TAKEPHOTO) {
                            openCapture();//打开相机
                        }
                    }
                }))
                .request();
    }

    private void openGallery() {
        Intent intent = Android7Helper.getIntentWithGallery();
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, UserAvatorConfig.REQUEST_IMAGE_ALBUM);
        } else {
            ToastUtil.showToast("手机中未安装相册应用");
        }
    }

    private void openCapture() {
        SimpleDateFormat format = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        String fileName = "temp_" + format.format(new Date()) + ".jpg";
        File file = new File(FileHelper.getCacheDirectory(this, "image"), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        if (SDKVersionHelper.hasNougat()) {
            photoTempUri = Android7Helper.getUriForFileProvider(PermissionAndCameraActivity.this, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoTempUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            photoTempUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoTempUri);
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, UserAvatorConfig.REQUEST_IMAGE_CAMERA);
        } else {
            ToastUtil.showToast("手机中未安装拍照应用.");
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void startPhotoCrop(Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        String tempImageUrl = "temp_" + format.format(new Date()) + ".jpg";
        File outFile = new File(FileHelper.getCacheDirectory(this, "image"), tempImageUrl);
        this.tempImageUrl = outFile.getAbsolutePath();
        Intent withCrop = Android7Helper.getIntentWithCrop(this, uri, outFile);
        startActivityForResult(withCrop, UserAvatorConfig.REQUEST_BANK_CARD_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case UserAvatorConfig.REQUEST_IMAGE_ALBUM:
                Uri uri = data.getData();
                if (uri == null) {
                    ToastUtil.showToast("获取图片失败");
                    return;
                }
                startPhotoCrop(uri);
                break;
            case UserAvatorConfig.REQUEST_IMAGE_CAMERA:
                try {
                    startPhotoCrop(photoTempUri);
                } catch (Exception e) {
                    Logger.e(TAG, e.toString());
                    ToastUtil.showToast("获取照片失败");
                }
                break;
            case UserAvatorConfig.REQUEST_BANK_CARD_INFO:
                File file = new File(tempImageUrl);
                if (file != null && file.isFile() && file.exists()) {
                    compressImage(file);
                } else {
                    Logger.e(TAG, "获取图片失败");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 压缩图片
     */
    private void compressImage(File file) {
        Compressor.with(getContext())
                .setMaxSize(UserAvatorConfig.DEFAULT_HEADER_PHOTO_SIZE)
                .compressToFileAsObservable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        //TODO 压缩完毕后上传图片 可以使用Luban
                        //your business
                        ToastUtil.showToast("start your business after compress");
                        ImageLoader.loadFromFile(ivPhoto, file, ImageLoaderOptions.getDefaultOptions());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        ToastUtil.showToast("获取图片失败");
                    }
                });
    }
}
