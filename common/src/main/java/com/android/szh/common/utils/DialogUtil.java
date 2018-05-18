package com.android.szh.common.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.szh.common.R;
import com.android.szh.common.widget.CustomDialog;
import com.android.szh.common.widget.StateDialog;

import java.util.Locale;

/**
 * 对话框工具类(包含单按钮对话框、双按钮对话框、文本图片对话框)
 */
public class DialogUtil {

    private static final float DEFAULT_WIDTH_SCALE = 0.85f;

    @NonNull
    public static FrameLayout.LayoutParams getDialogLayoutParams(Context context) {
        int screenWidth = ScreenHelper.getScreenWidth(context);
        return new FrameLayout.LayoutParams((int) (screenWidth * DEFAULT_WIDTH_SCALE), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 弹出确认信息对话框(点击按钮不做任何操作)
     *
     * @param context    上下文
     * @param title      标题
     * @param message    消息
     * @param buttonText 确定按钮文字
     * @see #showSingleButtonDialog(Context, CharSequence, CharSequence, CharSequence, View.OnClickListener)
     */
    public static void showSingleButtonDialog(Context context, CharSequence title, CharSequence message, CharSequence buttonText) {
        showSingleButtonDialog(context, title, message, buttonText, null);
    }

    /**
     * 弹出确认信息对话框(按钮默认为“确定”，点击按钮可以处理一些操作)
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息
     * @param buttonText     确定按钮文字
     * @param buttonListener 回调
     */
    public static void showSingleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence buttonText, View.OnClickListener buttonListener) {
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "确定";
        }
        new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setLeftButton(buttonText, buttonListener)
                .create()
                .show();
    }

    /**
     * 弹出确认信息对话框(按钮默认为“确定”，点击按钮可以处理一些操作)
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息
     * @param buttonText     确定按钮文字
     * @param buttonListener 回调
     * @param istextLeft     提示文字是否靠左（默认居中）
     */
    public static void showSingleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence buttonText, View.OnClickListener buttonListener, boolean istextLeft) {
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "确定";
        }
        new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setTopMessageGravity(istextLeft ? Gravity.LEFT : Gravity.CENTER)
                .setLeftButton(buttonText, buttonListener)
                .create()
                .show();
    }

    /**
     * 弹出确认信息对话框(强制对话框，必须点击按钮，按钮默认为“确定”，点击按钮可以处理一些操作)
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息
     * @param buttonText     确定按钮文字
     * @param buttonListener 回调
     */
    public static void showSingleButtonForceDialog(Context context, CharSequence title, CharSequence message,
                                                   CharSequence buttonText, View.OnClickListener buttonListener) {
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "确定";
        }
        CustomDialog dialog = new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setLeftButton(buttonText, buttonListener)
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    /**
     * 两个按钮的提示对话框(左侧按钮不做任何操作)
     *
     * @param context       上下文
     * @param title         标题
     * @param message       消息
     * @param leftText      左侧按钮文字
     * @param rightText     右侧按钮文字
     * @param rightListener 右侧按钮回调
     * @see #showDoubleButtonDialog(Context, CharSequence, CharSequence, boolean, CharSequence, View.OnClickListener, CharSequence, View.OnClickListener)
     */
    public static void showDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence leftText, CharSequence rightText, View.OnClickListener rightListener) {
        showDoubleButtonDialog(context, title, message, true, leftText, null, rightText, rightListener);
    }

    /**
     * 两个按钮的提示对话框(右侧按钮不做任何操作)
     *
     * @param context      上下文
     * @param title        标题
     * @param message      消息
     * @param leftText     左侧按钮文字
     * @param leftListener 左侧按钮回调
     * @param rightText    右侧按钮文字
     * @see #showDoubleButtonDialog(Context, CharSequence, CharSequence, boolean, CharSequence, View.OnClickListener, CharSequence, View.OnClickListener)
     */
    public static void showDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence leftText, View.OnClickListener leftListener, CharSequence rightText) {
        showDoubleButtonDialog(context, title, message, true, leftText, leftListener, rightText, null);
    }

    /**
     * 两个按钮的提示对话框(左侧按钮默认为“取消”，右侧按钮默认为“确定”)
     *
     * @param context       上下文
     * @param title         标题
     * @param message       消息
     * @param leftText      左侧按钮文字
     * @param leftListener  左侧按钮回调
     * @param rightText     右侧按钮文字
     * @param rightListener 右侧按钮回调
     */
    public static void showDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence leftText, View.OnClickListener leftListener,
                                              CharSequence rightText, View.OnClickListener rightListener) {
        showDoubleButtonDialog(context, title, message, true, leftText, leftListener, rightText, rightListener);
    }

    /**
     * 两个按钮的提示对话框(左侧按钮默认为“取消”，右侧按钮默认为“确定”)
     *
     * @param context       上下文
     * @param title         标题
     * @param message       消息
     * @param leftText      左侧按钮文字
     * @param leftListener  左侧按钮回调
     * @param rightText     右侧按钮文字
     * @param rightListener 右侧按钮回调
     */
    public static void showDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              boolean isMessageCenter, CharSequence leftText, View.OnClickListener leftListener,
                                              CharSequence rightText, View.OnClickListener rightListener) {
        if (TextUtils.isEmpty(leftText)) {
            leftText = "确定";
        }
        if (TextUtils.isEmpty(rightText)) {
            rightText = "取消";
        }
        new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setTopMessageGravity(isMessageCenter ? Gravity.CENTER : Gravity.LEFT)
                .setLeftButton(leftText, leftListener)
                .setRightButton(rightText, rightListener)
                .create()
                .show();
    }


    /**
     * 只creat 不 show ，可以重用
     * 两个按钮的提示对话框(左侧按钮默认为“取消”，右侧按钮默认为“确定”)
     *
     * @param context       上下文
     * @param title         标题
     * @param message       消息
     * @param leftText      左侧按钮文字
     * @param leftListener  左侧按钮回调
     * @param rightText     右侧按钮文字
     * @param rightListener 右侧按钮回调
     */
    public static CustomDialog getDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                                     boolean isMessageCenter, CharSequence leftText, View.OnClickListener leftListener,
                                                     CharSequence rightText, View.OnClickListener rightListener) {
        if (TextUtils.isEmpty(leftText)) {
            leftText = "确定";
        }
        if (TextUtils.isEmpty(rightText)) {
            rightText = "取消";
        }
        return new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setTopMessageGravity(isMessageCenter ? Gravity.CENTER : Gravity.LEFT)
                .setLeftButton(leftText, leftListener)
                .setRightButton(rightText, rightListener)
                .create();
    }


    /**
     * 两个按钮的提示对话框(左侧按钮默认为“取消”，右侧按钮默认为“确定”)，可设置两边按钮的字体颜色
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息
     * @param leftText       左侧按钮文字
     * @param leftTextColor  左侧字体的颜色
     * @param leftListener   左侧按钮回调
     * @param rightText      右侧按钮文字
     * @param rightTextColor 右侧字体的颜色
     * @param rightListener  右侧按钮回调
     */
    public static void showDoubleButtonDialog(Context context, CharSequence title, CharSequence message,
                                              CharSequence leftText, int leftTextColor, View.OnClickListener leftListener,
                                              CharSequence rightText, int rightTextColor, View.OnClickListener rightListener) {
        if (TextUtils.isEmpty(leftText)) {
            leftText = "确定";
        }
        if (TextUtils.isEmpty(rightText)) {
            rightText = "取消";
        }
        float buttonTextSize = 18;
        if (leftText.length() > 5 || rightText.length() > 5) {
            buttonTextSize = 16;
        }
        new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(message)
                .setLeftButton(leftText, leftListener)
                .setLeftButtonTextColor(leftTextColor)
                .setLeftButtonTextSize(buttonTextSize)
                .setRightButton(rightText, rightListener)
                .setRightButtonTextColor(rightTextColor)
                .setRightButtonTextSize(buttonTextSize)
                .create()
                .show();
    }

    /**
     * 文本和图片对话框
     *
     * @param context    上下文
     * @param title      标题
     * @param topMessage 消息
     * @param buttonText 按钮文本
     * @param imgResId   图片资源id
     */
    public static void showTextAndImageDialog(Context context, CharSequence title, CharSequence topMessage, String bottomMessage, CharSequence buttonText, int imgResId) {
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "知道了";
        }
        CustomDialog dialog = new CustomDialog.Builder(context)
                .setTitle(title)
                .setTopMessage(topMessage)
                .setImageResId(imgResId)
                .setBottomMessage(bottomMessage)
                .setLeftButton(buttonText, null)
                .create();
        dialog.show();
    }

    /**
     * 弹出成功状态对话框
     *
     * @param context  上下文
     * @param message  内容
     * @param listener 对话框消失监听
     */
    public static void showSuccessDialog(Context context, String message, DialogInterface.OnDismissListener listener) {
        showStateDialog(context, R.drawable.icon_state_success, message, listener);
    }

    /**
     * 弹出提示状态对话框
     *
     * @param context 上下文
     * @param message 内容
     */
    public static void showPromptDialog(Context context, String message) {
        showStateDialog(context, R.drawable.icon_state_prompt, message);
    }

    /**
     * 弹出状态对话框
     *
     * @param context      上下文
     * @param stateResId   状态图标的资源ID
     * @param messageResId 内容的资源ID
     * @see #showStateDialog(Context, int, int, DialogInterface.OnDismissListener)
     */
    public static void showStateDialog(Context context, int stateResId, int messageResId) {
        showStateDialog(context, stateResId, messageResId, null);
    }

    /**
     * 弹出状态对话框
     *
     * @param context    上下文
     * @param stateResId 状态图标的资源ID
     * @param message    内容
     * @see #showStateDialog(Context, int, String, DialogInterface.OnDismissListener)
     */
    public static void showStateDialog(Context context, int stateResId, String message) {
        showStateDialog(context, stateResId, message, null);
    }

    /**
     * 弹出状态对话框
     *
     * @param context      上下文
     * @param stateResId   状态图标的资源ID
     * @param messageResId 内容的资源ID
     * @param listener     对话框消失监听
     * @see #showStateDialog(StateDialog, DialogInterface.OnDismissListener)
     */
    public static void showStateDialog(Context context, int stateResId, int messageResId, DialogInterface.OnDismissListener listener) {
        StateDialog stateDialog = getStateDialog(context, stateResId, messageResId);
        showStateDialog(stateDialog, listener);
    }

    /**
     * 弹出状态对话框
     *
     * @param context    上下文
     * @param stateResId 状态图标的资源ID
     * @param message    内容
     * @param listener   对话框消失监听
     * @see #showStateDialog(StateDialog, DialogInterface.OnDismissListener)
     */
    public static void showStateDialog(Context context, int stateResId, String message, DialogInterface.OnDismissListener listener) {
        StateDialog stateDialog = getStateDialog(context, stateResId, message);
        showStateDialog(stateDialog, listener);
    }

    /**
     * 弹出状态对话框
     *
     * @param stateDialog 状态对话框
     * @param listener    对话框消失监听
     */
    public static void showStateDialog(StateDialog stateDialog, DialogInterface.OnDismissListener listener) {
        if (listener != null) {
            stateDialog.setOnDismissListener(listener);
        }
        stateDialog.show();
    }

    /**
     * 返回状态对话框
     *
     * @param context      上下文
     * @param stateResId   状态图标的资源ID
     * @param messageResId 内容的资源ID
     * @see #getStateDialog(Context, int, String)
     */
    public static StateDialog getStateDialog(Context context, int stateResId, int messageResId) {
        String message = "";
        if (messageResId > 0) {
            message = context.getString(messageResId);
        }
        return getStateDialog(context, stateResId, message);
    }

    /**
     * 返回状态对话框
     *
     * @param context    上下文
     * @param stateResId 状态图标的资源ID
     * @param message    内容
     */
    public static StateDialog getStateDialog(Context context, int stateResId, String message) {
        return new StateDialog.Builder(context)
                .setStateResId(stateResId)
                .setMessage(message)
                .create();
    }

    /**
     * 弹出网络环境不佳对话框
     *
     * @param context 上下文
     */
    public static void showDialogForBadNetwork(Context context) {
        showStateDialog(context, R.drawable.icon_state_bad_network, "网络环境不佳\n请稍后重试", null);
    }

    /**
     * 弹出当前网络不可用对话框
     *
     * @param context 上下文
     */
    public static void showDialogForNoNetwork(Context context) {
        showStateDialog(context, R.drawable.icon_state_no_network, "当前网络不可用\n请检查网络连接", null);
    }

    /**
     * 弹出服务器错误对话框(提示内容为“服务器出现了一点小问题，攻城狮正在紧急修复中~”，按钮为“知道了”)
     *
     * @param context        上下文
     * @param buttonListener 确定按钮文字
     * @see #showDialogForServerError(Context, int, View.OnClickListener)
     */
    public static void showDialogForServerError(Context context, View.OnClickListener buttonListener) {
        showDialogForServerError(context, 0, buttonListener);
    }

    /**
     * 弹出服务器错误对话框(提示内容为“服务器出现了一点小问题，攻城狮正在紧急修复中~”，按钮为“知道了”)
     *
     * @param context 上下文
     * @param code    错误码
     * @see #showDialogForServerError(Context, int, View.OnClickListener)
     */
    public static void showDialogForServerError(Context context, int code) {
        showDialogForServerError(context, code, null);
    }

    /**
     * 弹出服务器错误对话框(提示内容为“服务器出现了一点小问题，攻城狮正在紧急修复中~”，按钮为“知道了”)
     *
     * @param context        上下文
     * @param code           错误码
     * @param buttonListener 确定按钮文字
     */
    public static void showDialogForServerError(Context context, int code, View.OnClickListener buttonListener) {
        String message = "网络连接出现了一点小问题，请检查您的网络环境后重试~";
        if (code != 0) {
            message = String.format(Locale.getDefault(), "%s[%d]", message, code);
        }
        showSingleButtonDialog(context, "温馨提示", message, "知道了", buttonListener);
    }

}
