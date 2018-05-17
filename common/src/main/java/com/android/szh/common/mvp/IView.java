package com.android.szh.common.mvp;

import android.content.Context;

/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:
 * 负责绘制UI元素、与用户进行交互(在Android中体现为Activity)
 * UI层，包含所有UI相关组件
 * 持有对应的Presenter的对象，可通过依赖注入解耦此部分
 * 由Presenter来负责更新UI
 */

public interface IView {
    /**
     * 返回{@link Context}对象
     */
    Context getContext();
}
