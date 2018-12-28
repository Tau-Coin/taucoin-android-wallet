/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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