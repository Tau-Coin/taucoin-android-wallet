package io.taucoin.android.wallet.module.model;

import android.graphics.Bitmap;

import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.foundation.net.callback.LogicObserver;

public interface IUserModel {

    void saveKeyAndAddress(KeyValue keyValue, LogicObserver<KeyValue> observer);

    void saveName(String name, LogicObserver<KeyValue> observer);

    void saveAvatar(String avatar, Bitmap bitmap, LogicObserver<KeyValue> observer);

    void getKeyAndAddress(String publicKey, LogicObserver<KeyValue> observer);

    void updateOldTxHistory(String address);
}