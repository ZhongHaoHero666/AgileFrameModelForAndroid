package com.android.szh.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.szh.common.R;
import com.android.szh.common.utils.DensityHelper;
import com.android.szh.common.utils.DialogUtil;


/**
 * 自定义对话框
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private static final int DEFAULT_COLOR_TITLE = Color.parseColor("#191919");
        private static final int DEFAULT_COLOR_SUBTITLE = Color.parseColor("#191919");
        private static final int DEFAULT_COLOR_TOP_MESSAGE = Color.parseColor("#2a2a2a");
        private static final int DEFAULT_COLOR_BOTTOM_MESSAGE = Color.parseColor("#999999");
        private static final int DEFAULT_COLOR_SELECT_BUTTON = Color.parseColor("#0076FF");
        private static final float DEFAULT_BUTTON_TEXTSIZE = 18.0f;
        private Context context;
        private CharSequence title;
        private CharSequence subTitle;
        private CharSequence topMessage;
        private CharSequence bottomMessage;
        private CharSequence leftButtonText;
        private CharSequence rightButtonText;
        private int titleColor = DEFAULT_COLOR_TITLE;
        private int subTitleColor = DEFAULT_COLOR_SUBTITLE;
        private int topMessageColor = DEFAULT_COLOR_TOP_MESSAGE;
        private int bottomMessageColor = DEFAULT_COLOR_BOTTOM_MESSAGE;
        private int leftButtonTextColor = DEFAULT_COLOR_SELECT_BUTTON;
        private int rightButtonTextColor = DEFAULT_COLOR_SELECT_BUTTON;
        private float leftButtonTextSize = DEFAULT_BUTTON_TEXTSIZE;
        private float rightButtonTextSize = DEFAULT_BUTTON_TEXTSIZE;
        private int titleGravity = Gravity.CENTER;
        private int subTitleGravity = Gravity.CENTER;
        private int topMessageGravity = Gravity.CENTER;
        private int bottomMessageGravity = Gravity.LEFT;
        private int subTitleDisTop;
        private int imageResId;
        private View customView;
        private int position;
        private View.OnClickListener leftButtonListener;
        private View.OnClickListener rightButtonListener;

        /**
         * 构造方法
         */
        public Builder(Context context) {
            this.context = context;
            subTitleDisTop = DensityHelper.dp2px(context, 5);
        }

        /**
         * 设置对话框标题
         */
        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        /**
         * 设置对话框标题
         */
        public Builder setTitle(int titleResId) {
            if (titleResId != 0) {
                this.title = context.getText(titleResId);
            } else {
                this.title = "";
            }
            return this;
        }

        /**
         * 设置对话框标题字体颜色
         */
        public Builder setTitleColor(@ColorInt int titleColor) {
            if (titleColor != 0) {
                this.titleColor = titleColor;
            }
            return this;
        }

        /**
         * 设置对话框标题位置
         */
        public Builder setTitleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return this;
        }

        /**
         * 设置对话框副标题
         */
        public Builder setSubTitle(CharSequence subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        /**
         * 设置对话框副标题
         */
        public Builder setSubTitle(int subTitleResId) {
            if (subTitleResId != 0) {
                this.subTitle = context.getText(subTitleResId);
            } else {
                this.subTitle = "";
            }
            return this;
        }

        /**
         * 设置对话框副标题字体颜色
         */
        public Builder setSubTitleColor(@ColorInt int subTitleColor) {
            if (subTitleColor != 0) {
                this.subTitleColor = subTitleColor;
            }
            return this;
        }

        /**
         * 设置对话框副标题位置
         */
        public Builder setSubTitleGravity(int subTitleGravity) {
            this.subTitleGravity = subTitleGravity;
            return this;
        }

        /**
         * 设置对话框副标题位置距离图片高度
         */
        public Builder setSubTitleDisTop(int subTitleDisTop) {
            this.subTitleDisTop = DensityHelper.dp2px(context, subTitleDisTop);
            return this;
        }

        /**
         * 设置对话框顶部消息内容
         */
        public Builder setTopMessage(CharSequence topMessage) {
            this.topMessage = topMessage;
            return this;
        }

        /**
         * 设置对话框顶部消息内容
         */
        public Builder setTopMessage(int topMessageResId) {
            if (topMessageResId != 0) {
                this.topMessage = context.getText(topMessageResId);
            } else {
                this.topMessage = "";
            }
            return this;
        }

        /**
         * 设置对话框顶部消息内容字体颜色
         */
        public Builder setTopMessageColor(@ColorInt int topMessageColor) {
            if (topMessageColor != 0) {
                this.topMessageColor = topMessageColor;
            }
            return this;
        }

        /**
         * 设置对话框顶部消息内容位置
         */
        public Builder setTopMessageGravity(int topMessageGravity) {
            this.topMessageGravity = topMessageGravity;
            return this;
        }

        /**
         * 设置对话框底部消息内容
         */
        public Builder setBottomMessage(CharSequence bottomMessage) {
            this.bottomMessage = bottomMessage;
            return this;
        }

        /**
         * 设置对话框底部消息内容
         */
        public Builder setBottomMessage(int bottomMessageResId) {
            if (bottomMessageResId != 0) {
                this.bottomMessage = context.getText(bottomMessageResId);
            } else {
                this.bottomMessage = "";
            }
            return this;
        }

        /**
         * 设置对话框底部消息内容字体颜色
         */
        public Builder setBottomMessageColor(@ColorInt int bottomMessageColor) {
            if (bottomMessageColor != 0) {
                this.bottomMessageColor = bottomMessageColor;
            }
            return this;
        }

        /**
         * 设置对话框底部消息内容位置
         */
        public Builder setBottomMessageGravity(int bottomMessageGravity) {
            this.bottomMessageGravity = bottomMessageGravity;
            return this;
        }

        /**
         * 设置对话框的图片
         */
        public Builder setImageResId(@DrawableRes int imageResId) {
            if (imageResId != 0) {
                this.imageResId = imageResId;
            }
            return this;
        }

        /**
         * 设置对话框的自定义View
         */
        public Builder setCustomView(View customView) {
            this.customView = customView;
            this.position = 0;
            return this;
        }

        /**
         * 设置对话框的自定义View
         */
        public Builder setCustomView(View customView, int position) {
            this.customView = customView;
            this.position = position;
            return this;
        }

        /**
         * 设置对话框左侧按钮文本内容和点击事件
         */
        public Builder setLeftButton(CharSequence leftButtonText, View.OnClickListener listener) {
            this.leftButtonText = leftButtonText;
            this.leftButtonListener = listener;
            return this;
        }

        /**
         * 设置对话框左侧按钮文本内容和点击事件
         */
        public Builder setLeftButton(int leftButtonTextResId, View.OnClickListener listener) {
            if (leftButtonTextResId != 0) {
                this.leftButtonText = context.getText(leftButtonTextResId);
            } else {
                this.leftButtonText = "";
            }
            this.leftButtonListener = listener;
            return this;
        }

        /**
         * 设置对话框左侧按钮文本内容字体颜色
         */
        public Builder setLeftButtonTextColor(@ColorInt int leftButtonTextColor) {
            if (leftButtonTextColor != 0) {
                this.leftButtonTextColor = leftButtonTextColor;
            }
            return this;
        }

        /**
         * 设置对话框左侧按钮文本内容字体大小
         */
        public Builder setLeftButtonTextSize(float leftButtonTextSize) {
            if (leftButtonTextSize > 0) {
                this.leftButtonTextSize = leftButtonTextSize;
            }
            return this;
        }

        /**
         * 设置对话框右侧按钮文本内容和点击事件
         */
        public Builder setRightButton(CharSequence rightButtonText, View.OnClickListener listener) {
            this.rightButtonText = rightButtonText;
            this.rightButtonListener = listener;
            return this;
        }

        /**
         * 设置对话框右侧按钮文本内容和点击事件
         */
        public Builder setRightButton(int rightButtonTextResId, View.OnClickListener listener) {
            if (rightButtonTextResId != 0) {
                this.rightButtonText = context.getText(rightButtonTextResId);
            } else {
                this.rightButtonText = "";
            }
            this.rightButtonListener = listener;
            return this;
        }

        /**
         * 设置对话框右侧按钮文本内容字体颜色
         */
        public Builder setRightButtonTextColor(@ColorInt int rightButtonTextColor) {
            if (rightButtonTextColor != 0) {
                this.rightButtonTextColor = rightButtonTextColor;
            }
            return this;
        }

        /**
         * 设置对话框右侧按钮文本内容字体大小
         */
        public Builder setRightButtonTextSize(float rightButtonTextSize) {
            if (rightButtonTextSize > 0) {
                this.rightButtonTextSize = rightButtonTextSize;
            }
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialogStyle);
            View rootView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_custom, null, false);
            setTitle(rootView); // 设置对话框标题
            setSubTitle(rootView); // 设置对话框副标题
            setTopMessage(rootView); // 设置对话框顶部消息
            setBottomMessage(rootView); // 设置对话框底部消息
            setImageResId(rootView); // 设置对话框图片
            setCustomViewPosition(rootView); // 设置对话框的自定义View
            setLeftButton(rootView, dialog); // 设置对话框左侧按钮
            setRightButton(rootView, dialog); // 设置对话框右侧侧按钮
            dialog.setContentView(rootView, DialogUtil.getDialogLayoutParams(context)); // 设置对话框布局
            return dialog;
        }

        /**
         * 设置对话框标题
         */
        private void setTitle(View view) {
            TextView tvTitle = (TextView) view.findViewById(R.id.dialog_custom_title);
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                tvTitle.setTextColor(titleColor);
                tvTitle.setGravity(titleGravity);
            }
        }

        /**
         * 设置对话框副标题
         */
        private void setSubTitle(View view) {
            TextView tvSubTitle = (TextView) view.findViewById(R.id.dialog_custom_subtitle);
            if (TextUtils.isEmpty(subTitle)) {
                tvSubTitle.setVisibility(View.GONE);
            } else {
                tvSubTitle.setVisibility(View.VISIBLE);
                tvSubTitle.setText(subTitle);
                tvSubTitle.setTextColor(subTitleColor);
                tvSubTitle.setGravity(subTitleGravity);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvSubTitle.getLayoutParams();
                lp.setMargins(0, subTitleDisTop, 0, 0);
                tvSubTitle.setLayoutParams(lp);
            }
        }

        /**
         * 设置对话框顶部消息
         */
        private void setTopMessage(View view) {
            TextView tvTopMessage = (TextView) view.findViewById(R.id.dialog_custom_top_message);
            if (TextUtils.isEmpty(topMessage)) {
                tvTopMessage.setVisibility(View.GONE);
            } else {
                tvTopMessage.setVisibility(View.VISIBLE);
                tvTopMessage.setTextColor(topMessageColor);
                tvTopMessage.setGravity(topMessageGravity);
                tvTopMessage.setText(Html.fromHtml(topMessage.toString()));
            }
        }

        /**
         * 设置对话框底部消息
         */
        private void setBottomMessage(View view) {
            TextView tvBottomMessage = (TextView) view.findViewById(R.id.dialog_custom_bottom_message);
            if (TextUtils.isEmpty(bottomMessage)) {
                tvBottomMessage.setVisibility(View.GONE);
            } else {
                tvBottomMessage.setVisibility(View.VISIBLE);
                tvBottomMessage.setText(bottomMessage);
                tvBottomMessage.setTextColor(bottomMessageColor);
                tvBottomMessage.setGravity(bottomMessageGravity);
            }
        }

        /**
         * 设置对话框图片
         */
        private void setImageResId(View view) {
            ImageView ivStateIcon = (ImageView) view.findViewById(R.id.dialog_custom_img);
            if (imageResId <= 0) {
                ivStateIcon.setVisibility(View.GONE);
            } else {
                ivStateIcon.setVisibility(View.VISIBLE);
                ivStateIcon.setImageResource(imageResId);
            }
        }

        /**
         * 设置对话框的自定义View
         */
        private void setCustomViewPosition(View view) {
            if (customView != null) {
                LinearLayout llContainer = (LinearLayout) view.findViewById(R.id.dialog_custom_container);
                llContainer.addView(customView, position);
            }
        }

        /**
         * 设置对话框左侧按钮
         */
        private void setLeftButton(View view, final CustomDialog dialog) {
            TextView tvLeft = (TextView) view.findViewById(R.id.dialog_double_button_left);
            if (TextUtils.isEmpty(leftButtonText)) {
                tvLeft.setVisibility(View.GONE);
                view.findViewById(R.id.dialog_double_button_divider).setVisibility(View.GONE);
                view.findViewById(R.id.dialog_double_button_right).setBackgroundResource(R.drawable.bg_custom_dialog_single_button);
            } else {
                tvLeft.setVisibility(View.VISIBLE);
                tvLeft.setText(leftButtonText);
                tvLeft.setTextColor(leftButtonTextColor);
                tvLeft.setTextSize(leftButtonTextSize);
                tvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (leftButtonListener != null) {
                            leftButtonListener.onClick(v);
                        }
                    }
                });
            }
        }

        /**
         * 设置对话框右侧按钮
         */
        private void setRightButton(View view, final CustomDialog dialog) {
            TextView tvRight = (TextView) view.findViewById(R.id.dialog_double_button_right);
            if (TextUtils.isEmpty(rightButtonText)) {
                tvRight.setVisibility(View.GONE);
                view.findViewById(R.id.dialog_double_button_divider).setVisibility(View.GONE);
                view.findViewById(R.id.dialog_double_button_left).setBackgroundResource(R.drawable.bg_custom_dialog_single_button);
            } else {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(rightButtonText);
                tvRight.setTextColor(rightButtonTextColor);
                tvRight.setTextSize(rightButtonTextSize);
                tvRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (rightButtonListener != null) {
                            rightButtonListener.onClick(v);
                        }
                    }
                });
            }
        }

    }

}
