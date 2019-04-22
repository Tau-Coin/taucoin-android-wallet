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

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.ReferralInfo;
import io.taucoin.android.wallet.db.util.KeyValueDaoUtils;
import io.taucoin.android.wallet.db.util.ReferralInfoDaoUtils;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import io.taucoin.android.wallet.module.bean.ReferralBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.net.service.UserService;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.exception.CodeException;
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
                    emitter.onError(CodeException.getError());
                    return;
                }
            }
            // private key encrypt
//            String encryptKey = WalletEncrypt.encrypt(kv.getPrivkey());
//            kv.setPrivkey(encryptKey);

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
    public void getKeyAndAddress(String publicKey, LogicObserver<KeyValue> observer) {
        Observable.create((ObservableOnSubscribe<KeyValue>) emitter -> {
            KeyValue keyValue = KeyValueDaoUtils.getInstance().queryByPubicKey(publicKey);

            if(keyValue != null && StringUtil.isNotEmpty(keyValue.getPrivkey())){
                Key key = KeyManager.validateKey(keyValue.getPrivkey());
//                if(key == null){
//                    // private key decrypt
//                    String encryptKey = WalletEncrypt.decrypt(keyValue.getPrivkey());
//                    key = KeyManager.validateKey(encryptKey);
//
//                }else{
//                    // private key encrypt
//                    String encryptKey = WalletEncrypt.encrypt(keyValue.getPrivkey());
//                    keyValue.setPrivkey(encryptKey);
//                }
                if(key != null){
                    keyValue.setPubkey(key.getPubkey());
                    keyValue.setAddress(key.getAddress());
                    KeyValueDaoUtils.getInstance().update(keyValue);
                }
                emitter.onNext(keyValue);
            }else{
                SharedPreferencesHelper.getInstance().clear();
                // Default initialization of private key
                saveKeyAndAddress(null, observer);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
    @Override
    public void updateOldTxHistory(){
        Observable.create(emitter ->
            TransactionHistoryDaoUtils.getInstance().updateOldTxHistory()
        ).subscribeOn(Schedulers.io())
        .subscribe();
    }

    @Override
    public void getReferralUrl(LogicObserver<Boolean> observer) {
        String address = SharedPreferencesHelper.getInstance().getString(TransmitKey.ADDRESS, "");
        Map<String,String> map = new HashMap<>();
        map.put("address", address);
        NetWorkManager.createApiService(UserService.class)
                .getReferralUrl(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TAUObserver<DataResult<ReferralBean>>() {
                    @Override
                    public void handleData(DataResult<ReferralBean> dataResult) {
                        super.handleData(dataResult);
                        if(dataResult.getData() != null){
                            saveReferralInfo(dataResult.getData(), observer);
                        }else{
                            observer.onNext(false);
                        }
                    }

                    @Override
                    public void handleError(String msg, int msgCode) {
                        observer.onNext(false);
                    }
                });
    }

    private void saveReferralInfo(ReferralBean data, LogicObserver<Boolean> observer) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            String publicKey = SharedPreferencesHelper.getInstance().getString(TransmitKey.PUBLIC_KEY, "");
            KeyValue keyValue = KeyValueDaoUtils.getInstance().queryByPubicKey(publicKey);
            keyValue.setReferralLink(data.getReferralUrl());
            KeyValueDaoUtils.getInstance().update(keyValue);
            ReferralInfoDaoUtils.getInstance().setReferralInfo(data.getInviteeReword(), data.getInviterReword());
            emitter.onNext(true);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void getReferralCounts(LogicObserver<Boolean> observer) {
        String address = SharedPreferencesHelper.getInstance().getString(TransmitKey.ADDRESS, "");
        Map<String,String> map = new HashMap<>();
        map.put("address", address);
        NetWorkManager.createApiService(UserService.class)
            .getReferralCounts(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new TAUObserver<DataResult<Integer>>() {
                @Override
                public void handleData(DataResult<Integer> dataResult) {
                    super.handleData(dataResult);
                    saveReferralCounts(dataResult.getData(), observer);
                }

                @Override
                public void handleError(String msg, int msgCode) {
                    observer.onNext(false);
                }
            });
    }

    private void saveReferralCounts(int counts, LogicObserver<Boolean> observer) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            String publicKey = SharedPreferencesHelper.getInstance().getString(TransmitKey.PUBLIC_KEY, "");
            KeyValue keyValue = KeyValueDaoUtils.getInstance().queryByPubicKey(publicKey);
            keyValue.setInvitedFriends(counts);
            KeyValueDaoUtils.getInstance().update(keyValue);
            emitter.onNext(true);
        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(observer);
    }

    @Override
    public void getReferralInfo(LogicObserver<ReferralInfo> observer) {
        Observable.create((ObservableOnSubscribe<ReferralInfo>) emitter -> {
            ReferralInfo referralInfo = ReferralInfoDaoUtils.getInstance().query();
            if(referralInfo == null){
                referralInfo = new ReferralInfo();
            }
            emitter.onNext(referralInfo);
        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(observer);
    }
}