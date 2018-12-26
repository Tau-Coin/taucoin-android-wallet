package io.taucoin.android.wallet.net.callback;

import com.github.naturs.logger.Logger;

import io.reactivex.observers.DisposableObserver;

public abstract class CommonObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e, "CommonObserver error");
    }

}
