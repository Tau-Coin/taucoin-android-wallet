package io.taucoin.android.wallet.module.presenter;

import android.graphics.Bitmap;

import org.greenrobot.eventbus.EventBus;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.model.IUserModel;
import io.taucoin.android.wallet.module.model.UserModel;
import io.taucoin.android.wallet.module.view.manage.iview.IImportKeyView;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.platform.adress.Key;
import io.taucoin.platform.adress.KeyManager;

public class UserPresenter {

    private IImportKeyView mIImportKeyView;
    private IUserModel mUserModel;

    public UserPresenter() {
        mUserModel = new UserModel();
    }
    public UserPresenter(IImportKeyView view) {
        mUserModel = new UserModel();
        mIImportKeyView = view;
    }


    public void saveKeyAndAddress(KeyValue keyValue) {
        if(keyValue == null){
            keyValue = new KeyValue();
            Key key = KeyManager.generatorKey();
            if(key != null){
                keyValue.setPrivkey(key.getPrivkey());
                keyValue.setPubkey(key.getPubkey());
                keyValue.setAddress(key.getAddress());
            }
        }
        mUserModel.saveKeyAndAddress(keyValue, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                MyApplication.setKeyValue(keyValue);
                SharedPreferencesHelper.getInstance().putString(TransmitKey.PUBLIC_KEY, keyValue.getPubkey());
                SharedPreferencesHelper.getInstance().putString(TransmitKey.ADDRESS, keyValue.getAddress());
                TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
                TxService.startTxService(TransmitKey.ServiceType.GET_INFO);
                mIImportKeyView.gotoKeysActivity();
            }
        });
    }

    public void saveName(String name) {
        mUserModel.saveName(name, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                EventBus.getDefault().postSticky(keyValue);
            }
        });
    }

    public void saveAvatar(String avatar, Bitmap bitmap) {
        mUserModel.saveAvatar(avatar, bitmap, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                EventBus.getDefault().postSticky(keyValue);
            }
        });
    }
}