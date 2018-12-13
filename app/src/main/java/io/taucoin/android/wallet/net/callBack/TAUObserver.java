package io.taucoin.android.wallet.net.callBack;

import io.taucoin.foundation.net.callback.RequestObserver;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.android.wallet.util.ToastUtils;

public abstract class TAUObserver<T> extends RequestObserver<T> {

    @Override
    public void handleError(String msg, int msgCode) {

        if(StringUtil.isNotEmpty(msg)){
            ToastUtils.showLongToast(msg);
        }
    }

    @Override
    public void handleData(T t) {

    }
}
