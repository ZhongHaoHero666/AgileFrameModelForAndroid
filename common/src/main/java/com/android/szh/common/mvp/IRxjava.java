package com.android.szh.common.mvp;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by sunzhonghao on 2018/5/17.
 * desc:  Observer 管理接口
 */

public interface IRxjava {
    /**
     * 注册 Observer CompositeDisposable中统一管理
     */
    boolean add(@NonNull Disposable disposable);

    /**
     * 取消订阅指定的{@link Observer}
     */
    boolean remove(@NonNull Disposable disposable);

    /**
     * 取消订阅所有的{@link Observer}
     */
    void unsubscribe();
}
