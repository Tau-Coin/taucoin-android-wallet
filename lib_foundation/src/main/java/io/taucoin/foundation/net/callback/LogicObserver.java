package io.taucoin.foundation.net.callback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.taucoin.foundation.net.exception.CodeException;
import io.taucoin.foundation.util.StringUtil;

public abstract class LogicObserver<T> implements Observer<T> {
    @Override
    public void onError(Throwable e) {
        String msg = "unknown error";
        if(e != null && StringUtil.isNotEmpty(e.getMessage())){
            msg = e.getMessage();
        }
        handleError(100, msg);
    }

    public void onError() {
        onError(CodeException.getError());
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
    }
}
