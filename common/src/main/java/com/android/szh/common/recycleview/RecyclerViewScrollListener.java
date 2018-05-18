package com.android.szh.common.recycleview;

import android.support.v7.widget.RecyclerView;

import com.android.szh.common.imageloader.ImageLoaderHelper;


/**
 * RecyclerView滚动监听
 *
 * Created by sunzhonghao
 * @date 2017/11/24 12:04
 */
public final class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;

    public RecyclerViewScrollListener() {
        this(false, true);
    }

    public RecyclerViewScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE: // 当前屏幕停止滚动
                ImageLoaderHelper.getInstance().resumeRequests();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING: // 屏幕在滚动且用户仍在触碰或手指还在屏幕上
                if (pauseOnScroll) {
                    ImageLoaderHelper.getInstance().pauseRequests();
                } else {
                    ImageLoaderHelper.getInstance().resumeRequests();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING: // 随用户的操作屏幕上产生的惯性滑动
                if (pauseOnFling) {
                    ImageLoaderHelper.getInstance().pauseRequests();
                } else {
                    ImageLoaderHelper.getInstance().resumeRequests();
                }
                break;
        }
    }
}
