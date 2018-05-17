package com.android.szh.common.rxjava.transformer;


import com.android.szh.common.http.function.ErrorHandlerFunction;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * 异常处理器
 */
public class ObservableTransformerError<T> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.onErrorResumeNext(new ErrorHandlerFunction<T>());
    }

}
