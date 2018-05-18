package com.android.szh.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.szh.common.R;
import com.android.szh.common.utils.SafetyHandler;


/**
 * 状态对话框(成功、失败、提示)
 */
public class StateDialog extends Dialog implements SafetyHandler.Delegate {

    private Context context;
    private static final int MSG_DIALOG_DISMISS = 1;
    private static final int MSG_DISMISS_DELAYED = 800;
    private SafetyHandler mHandler = SafetyHandler.create(this);

    public StateDialog(Context context) {
        super(context);
        this.context = context;
    }

    public StateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    public void show() {
        if (!((Activity) context).isFinishing()) {
            super.show();
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, MSG_DISMISS_DELAYED);
            }
        }

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_DIALOG_DISMISS:
                dismiss();
                break;
        }
    }

    public static class Builder {

        private static final int DEFAULT_MESSAGE_COLOR = Color.parseColor("#5c5c5c");
        private static final int DEFAULT_STATE_RESID = R.drawable.icon_state_prompt;
        /**
         * 上下文对象
         */
        public Context context;
        /**
         * 对话框内容
         */
        private String message;
        /**
         * 对话框内容字体颜色
         */
        private int messageColor = DEFAULT_MESSAGE_COLOR;
        /**
         * 对话框状态图标
         */
        private int stateResId = DEFAULT_STATE_RESID;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置对话框的提示信息
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置对话框的提示信息
         */
        public Builder setMessage(@StringRes int messageResId) {
            if (messageResId > 0) {
                this.message = (String) context.getText(messageResId);
            } else {
                this.message = "";
            }
            return this;
        }

        /**
         * 设置对话框的提示信息字体颜色
         */
        public Builder setMessageColor(@ColorInt int messageColor) {
            this.messageColor = messageColor;
            return this;
        }

        /**
         * 设置对话框的状态图标
         */
        public Builder setStateResId(@DrawableRes int stateResId) {
            this.stateResId = stateResId;
            return this;
        }

        /**
         * 创建对话框
         */
        public StateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final StateDialog dialog = new StateDialog(context, R.style.CustomDialogStyle);// 根据自定义主题初始化对话框
            View rootView = inflater.inflate(R.layout.layout_dialog_state, null); // 加载对话框布局
            setStateResId(rootView);// 设置对话框状态图标
            setMessage(rootView);// 设置对话框内容
            dialog.setCancelable(true); // 设置对话框可以取消
            setAttributes(dialog, rootView); // 设置对话框参数
            return dialog;
        }

        /**
         * 设置对话框状态图标
         */
        private void setStateResId(View layout) {
            ImageView ivStateIcon = (ImageView) layout.findViewById(R.id.state_dialog_icon);
            if (stateResId > 0) {
                ivStateIcon.setImageResource(stateResId);
            } else {
                ivStateIcon.setVisibility(View.GONE);
            }
        }

        /**
         * 设置对话框内容
         */
        private void setMessage(View layout) {
            TextView tvMessage = (TextView) layout.findViewById(R.id.state_dialog_message);
            if (!TextUtils.isEmpty(message)) {
                tvMessage.setText(message);
                tvMessage.setTextColor(messageColor);
            } else {
                tvMessage.setVisibility(View.GONE);
            }
        }

        /**
         * 设置对话框参数
         */
        private void setAttributes(StateDialog dialog, View rootView) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            rootView.measure(w, h);
            int height = rootView.getMeasuredHeight();
            int width = rootView.getMeasuredWidth();
            int max = Math.max(height, width); // 得到宽和高的最大值
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(max, max);
            rootView.setLayoutParams(layoutParams); // 设置对话框布局的参数
            dialog.setContentView(rootView); // 设置对话框布局
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams params = window.getAttributes(); // 获取对话框当前的参数值
            params.height = max;
            params.width = max;
            window.setAttributes(params); // 设置对话框的参数
        }
    }

}
