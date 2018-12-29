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
package io.taucoin.android.wallet.module.model;

import android.graphics.Bitmap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.util.KeyValueDaoUtils;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import io.taucoin.android.wallet.util.FileUtil;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.platform.adress.Key;
import io.taucoin.platform.adress.KeyManager;

public class UserModel implements IUserModel{
    @Override
    public void saveKeyAndAddress(final KeyValue keyValue, LogicObserver<KeyValue> observer) {
        Observable.create((ObservableOnSubscribe<KeyValue>) emitter -> {

            KeyValue kv = keyValue;
            if(kv == null){
                Key key = KeyManager.generatorKey();
                if(key != null){
                    kv = new KeyValue();
                    kv.setPrivkey(key.getPrivkey());
                    kv.setPubkey(key.getPubkey());
                    kv.setAddress(key.getAddress());
                }else {
                    emitter.onError(null);
                    return;
                }
            }
            KeyValue result = KeyValueDaoUtils.getInstance().insertOrReplace(kv);
            emitter.onNext(result);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void saveName(String name, LogicObserver<KeyValue> observer) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null || StringUtil.isEmpty(keyValue.getAddress())){
            return;
        }
        Observable.create((ObservableOnSubscribe<KeyValue>) emitter -> {
            keyValue.setNickName(name);
            KeyValueDaoUtils.getInstance().update(keyValue);
            emitter.onNext(keyValue);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void saveAvatar(String avatar, Bitmap bitmap, LogicObserver<KeyValue> observer) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null || StringUtil.isEmpty(keyValue.getAddress())){
            return;
        }
        Observable.create((ObservableOnSubscribe<KeyValue>) emitter -> {
            keyValue.setHeaderImage(avatar);
            KeyValueDaoUtils.getInstance().update(keyValue);
            FileUtil.saveFilesDirBitmap(avatar, bitmap);
            FileUtil.deleteExternalBitmap();
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
            emitter.onNext(keyValue);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void getKeyAndAddress(String publicKey, LogicObserver<KeyValue> observer) {
        Observable.create((ObservableOnSubscribe<KeyValue>) emitter -> {
            KeyValue keyValue = KeyValueDaoUtils.getInstance().queryByPubicKey(publicKey);
            emitter.onNext(keyValue);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
    @Override
    public void updateOldTxHistory(String address){
        Observable.create(emitter ->
            TransactionHistoryDaoUtils.getInstance().updateOldTxHistory(address)
        ).subscribeOn(Schedulers.io())
        .subscribe();
    }
}