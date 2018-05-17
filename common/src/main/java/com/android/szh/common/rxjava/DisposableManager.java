package com.android.szh.common.rxjava;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Disposable辅助类
 */
public class DisposableManager {

    CompositeDisposable compositeDisposable;

    public static DisposableManager creat() {
        return new DisposableManager();
    }

    private DisposableManager() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void dispose() {
        compositeDisposable.dispose();
    }

    public boolean isDisposed() {
        return compositeDisposable.isDisposed();
    }

    public boolean addDisposable(Disposable disposable) {
        return compositeDisposable.add(disposable);
    }

    public boolean addAllDisposable(Disposable... disposables) {
        return compositeDisposable.addAll(disposables);
    }

    public boolean remove(Disposable disposable) {
        return compositeDisposable.remove(disposable);
    }

    public boolean delete(Disposable disposable) {
        return compositeDisposable.delete(disposable);
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public static boolean isDisposed(Disposable disposable) {
        if (disposable != null) {
            return disposable.isDisposed();
        }
        return false;
    }

    public static void dispose(Disposable disposable) {
        if (isDisposed(disposable)) {
            disposable.dispose();
        }
    }

}
