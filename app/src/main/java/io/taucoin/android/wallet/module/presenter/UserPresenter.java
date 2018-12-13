package io.taucoin.android.wallet.module.presenter;

import io.taucoin.android.wallet.module.model.IUserModel;
import io.taucoin.android.wallet.module.model.UserModel;

public class UserPresenter {
//    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter() {
        mUserModel = new UserModel();
    }
}
