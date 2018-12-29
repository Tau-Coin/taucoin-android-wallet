package io.taucoin.foundation.net.callback;

import com.github.naturs.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.taucoin.foundation.net.exception.CodeException;

public abstract class LogicObserver<T> implements Observer<T> {
    @Override
    public void onError(Throwable e) {
        Logger.e(e,"LogicObserver onError");
        handleError(100, "unknown error");
    }

    public void onError() {
        onError(null);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        handleData(t);
    }

    public abstract void handleData(T t);

    public void handleError(int code, String msg){
        Logger.e("LogicObserver onError:" + msg);
    }
}
