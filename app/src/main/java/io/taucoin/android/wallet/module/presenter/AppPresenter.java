package io.taucoin.android.wallet.module.presenter;

import java.util.List;

import io.taucoin.android.wallet.module.bean.HelpBean;
import io.taucoin.android.wallet.module.model.AppModel;
import io.taucoin.android.wallet.module.model.IAppModel;
import io.taucoin.android.wallet.module.view.manage.iview.IHelpView;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.foundation.net.callback.DataResult;

public class AppPresenter {
    private IHelpView mHelpView;
    private IAppModel mAppModel;

    public AppPresenter(IHelpView view) {
        mAppModel = new AppModel();
        mHelpView = view;
    }

    public void getHelpData() {
        mAppModel.getHelpData(new TAUObserver<DataResult<List<HelpBean>>>() {
            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                ProgressManager.closeProgressDialog();
            }

            @Override
            public void handleData(DataResult<List<HelpBean>> listDataResult) {
                super.handleData(listDataResult);
                ProgressManager.closeProgressDialog();
                if(listDataResult != null && listDataResult.getData() != null){
                    mHelpView.loadHelpData(listDataResult.getData());
                }
            }
        });
    }
}