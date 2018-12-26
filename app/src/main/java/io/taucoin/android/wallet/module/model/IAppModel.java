package io.taucoin.android.wallet.module.model;

import java.util.List;

import io.taucoin.android.wallet.module.bean.HelpBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.foundation.net.callback.DataResult;

public interface IAppModel {
    void getInfo();
    void getHelpData(TAUObserver<DataResult<List<HelpBean>>> observer);
}
