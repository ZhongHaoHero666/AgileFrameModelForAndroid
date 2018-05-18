package com.android.szh.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.util.LinkifyCompat;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * {@link View}辅助类(常用方法)
 * Created by sunzhonghao
 * @date 2017/8/1 15:59
 */
public final class ViewHolder {

    private View mItemView;
    private Context mContext;
    private SparseArray<View> mViews;

    /**
     * 创建{@link ViewHolder}
     * @param itemView ItemView对象
     */
    public static ViewHolder creat(View itemView) {
        return new ViewHolder(itemView);
    }

    private ViewHolder(View itemView) {
        this.mItemView = itemView;
        this.mViews = new SparseArray<>();
        this.mContext = mItemView.getContext();
    }

    /**
     * 返回{@link Context}对象
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 返回{@link View}对象(ItemView)
     */
    public View getItemView() {
        return mItemView;
    }

    public SparseArray<View> getChildViews() {
        return mViews;
    }

    /**
     * 通过控件的资源ID获取控件
     * @param viewId 控件的资源ID
     */
    public final <ViewType extends View> ViewType getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (ViewType) view;
    }


    /****以下为辅助方法*****/

    /**
     * 为{@link TextView}设置文本
     */
    public ViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 为{@link TextView}设置文本
     */
    public ViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        TextView textView = getView(viewId);
        textView.setText(resId);
        return this;
    }

    /**
     * 为{@link TextView}设置字体大小 sp
     */
    public ViewHolder setTextSize(@IdRes int viewId, float size) {
        TextView tv = getView(viewId);
        tv.setTextSize(size);
        return this;
    }

    /**
     * 为{@link TextView}设置字体大小 sp
     */
    public ViewHolder setTextSize(@IdRes int viewId, @DimenRes int deminsId) {
        TextView tv = getView(viewId);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(deminsId));
        return this;
    }

    /**
     * 为{@link TextView}设置字体 粗体
     */
    public ViewHolder setFokeBoldText(@IdRes int viewId,boolean isBold) {
        TextView tv =  getView(viewId);
        tv.getPaint().setFakeBoldText(isBold);
        return this;
    }

    /**
     * 为{@link TextView}设置文本颜色
     */
    public ViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * 为{@link TextView}设置文本颜色
     */
    public ViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColor(mContext, textColorRes));
        return this;
    }

    /**
     * 为{@link TextView}设置超链接
     */
    public ViewHolder linkify(@IdRes int viewId, @LinkifyCompat.LinkifyMask int mask) {
        TextView textView = getView(viewId);
        Linkify.addLinks(textView, mask);
        return this;
    }

    /**
     * 为{@link TextView}设置文本字体
     */
    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                TextView textView = getView(viewId);
                textView.setTypeface(typeface);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }
        return this;
    }

    /**
     * 获取{@link TextView}文本文字
     */
    public CharSequence getText(@IdRes int viewId) {
        TextView textView = getView(viewId);
        return textView.getText();
    }

    /**
     * 为{@link ImageView}设置图片
     */
    public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 为{@link ImageView}设置图片
     */
    public ViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 为{@link ImageView}设置图片
     */
    public ViewHolder setImageDrawable(@IdRes int viewId, @Nullable Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    /**
     * 为{@link ImageView}设置背景颜色
     */
    public ViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 为{@link ImageView}设置背景图片
     */
    public ViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 为{@link View}设置Padding
     */
    public ViewHolder setPadding(@IdRes int viewId, int left, int top, int right, int bottom) {
        View view = getView(viewId);
        view.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 为{@link View}设置可视状态
     */
    public ViewHolder setVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * 获取{@link View}可视状态
     */
    public int getVisibility(@IdRes int viewId) {
        View view = getView(viewId);
        return view.getVisibility();
    }

    /**
     * 为{@link ProgressBar}设置进度
     */
    public ViewHolder setProgress(@IdRes int viewId, int progress) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setProgress(progress);
        return this;
    }

    /**
     * 为{@link ProgressBar}设置最大值
     */
    public ViewHolder setMax(@IdRes int viewId, int max) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setMax(max);
        return this;
    }

    /**
     * 为{@link ProgressBar}设置进度和最大值
     */
    public ViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setMax(max);
        progressBar.setProgress(progress);
        return this;
    }

    /**
     * 为{@link RatingBar}设置等级
     */
    public ViewHolder setRating(@IdRes int viewId, float rating) {
        RatingBar ratingBar = getView(viewId);
        ratingBar.setRating(rating);
        return this;
    }

    /**
     * 为{@link RatingBar}设置等级和星星的数目
     */
    public ViewHolder setRating(@IdRes int viewId, float rating, int numStars) {
        RatingBar ratingBar = getView(viewId);
        ratingBar.setRating(rating);
        ratingBar.setNumStars(numStars);
        return this;
    }

    /**
     * 为{@link Checkable}设置选中状态
     */
    public ViewHolder setChecked(@IdRes int viewId, boolean checked) {
        Checkable checkable = getView(viewId);
        checkable.setChecked(checked);
        return this;
    }

    /**
     * 为{@link View}设置透明度
     */
    public ViewHolder setAlpha(@IdRes int viewId, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        View view = getView(viewId);
        view.setAlpha(alpha);
        return this;
    }

    /**
     * 为{@link View}设置可视状态
     */
    public ViewHolder setVisibility(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 为{@link View}设置TAG
     */
    public ViewHolder setTag(@IdRes int viewId, final Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 为{@link View}设置TAG
     */
    public ViewHolder setTag(@IdRes int viewId, int key, final Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 为{@link View}触发点击事件
     */
    public ViewHolder performClick(@IdRes int viewId) {
        View view = getView(viewId);
        view.performClick();
        return this;
    }

    /**
     * 为{@link View}设置点击事件
     */
    public ViewHolder setOnClickListener(@IdRes int viewId, @Nullable View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为{@link View}设置长点击事件
     */
    public ViewHolder setOnLongClickListener(@IdRes int viewId, @Nullable View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 为{@link View}设置触摸事件
     */
    public ViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

}
