package com.android.szh.common.recycleview.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView的Item点击事件监听
 *
 * Created by sunzhonghao
 * @date 2018/1/25 9:49
 */
public class SimpleRecycleViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetectorCompat mGestureDetector;

    public SimpleRecycleViewItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return getGestureDetector(rv).onTouchEvent(e);// 把事件交给GestureDetector处理
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        getGestureDetector(rv).onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private GestureDetectorCompat getGestureDetector(RecyclerView rv) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetectorCompat(rv.getContext(), new ItemTouchHelperGestureListener(rv));
        }
        return mGestureDetector;
    }

    /**
     * Item触摸辅助手势监听
     */
    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private RecyclerView recyclerView;

        private ItemTouchHelperGestureListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        /**
         * 单击事件
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null) {
                mListener.onItemClick(childView, recyclerView.getChildLayoutPosition(childView));
                return true;
            }
            return super.onSingleTapUp(e);
        }

        /**
         * 长按事件
         */
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null) {
                mListener.onItemLongClick(childView, recyclerView.getChildLayoutPosition(childView));
            }
        }

        /**
         * 双击事件
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            int action = e.getAction();
            if (action == MotionEvent.ACTION_UP) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListener != null) {
                    mListener.onItemDoubleClick(childView, recyclerView.getChildLayoutPosition(childView));
                    return true;
                }
            }
            return super.onDoubleTapEvent(e);
        }
    }

    /**
     * RecyclerView的Item点击事件监听接口
     *
     * Created by sunzhonghao
     * @date 2018/1/25 9:51
     */
    public interface OnItemClickListener {

        /**
         * 当ItemView的单击事件触发时调用
         */
        void onItemClick(View view, int position);

        /**
         * 当ItemView的长按事件触发时调用
         */
        void onItemLongClick(View view, int position);

        /**
         * 当ItemView的双击事件触发时调用
         */
        void onItemDoubleClick(View view, int position);
    }


    /**
     * RecyclerView的Item点击事件监听实现
     *
     * Created by sunzhonghao
     * @date 2018/1/25 9:55
     */
    public static class SimpleOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onItemLongClick(View view, int position) {

        }

        @Override
        public void onItemDoubleClick(View view, int position) {

        }
    }

}
