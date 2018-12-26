package io.taucoin.foundation.net.callback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.taucoin.foundation.net.exception.ApiException;
import io.taucoin.foundation.net.exception.FactoryException;

public abstract class RequestObserver<T> implements Observer<T> {
    @Override
    public void onError(Throwable e) {
        try {
            ApiException error = FactoryException.analysisException(e);
            handleError(error.getDisplayMessage(), error.getCode());
        } catch (Throwable ex) {
            // never do anything!
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if(null != t){
            if(t instanceof RetResult){
                RetResult response = (RetResult) t;
                boolean isSuccess = response.getStatus() == NetResultCode.SUCCESS_CODE;
                if(isSuccess){
                   handleData(t);
                }else{
                    handleError(response.getMessage(), response.getStatus());
                }
            }else{
                handleData(t);
            }
        }else{
            onError(new Throwable());
        }
    }

    public abstract void handleError(String msg, int msgCode);

    public abstract void handleData(T t);
}
