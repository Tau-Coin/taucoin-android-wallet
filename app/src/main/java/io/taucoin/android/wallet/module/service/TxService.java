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
package io.taucoin.android.wallet.module.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.util.KeyValueDaoUtils;
import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.model.AppModel;
import io.taucoin.android.wallet.module.model.IAppModel;
import io.taucoin.android.wallet.module.model.ITxModel;
import io.taucoin.android.wallet.module.model.IUserModel;
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.module.model.UserModel;
import io.taucoin.android.wallet.module.view.main.MainActivity;
import io.taucoin.android.wallet.net.callback.CommonObserver;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.ResourcesUtil;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.StringUtil;

public class TxService extends Service {

    private ITxModel mTxModel;
    private IAppModel mAppModel;
    private IUserModel mUserModel;
    private boolean mIsChecked;
    private boolean mIsUpdate;

    public TxService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTxModel = new TxModel();
        mAppModel = new AppModel();
        mUserModel = new UserModel();
        mIsChecked = false;
        mIsUpdate = false;
        Logger.i("TxService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(intent != null && keyValue != null){
            String serviceType = intent.getStringExtra(TransmitKey.SERVICE_TYPE);
            switch (serviceType){
                case TransmitKey.ServiceType.GET_IMPORT_DATA:
                case TransmitKey.ServiceType.GET_HOME_DATA:
                    getBalance(serviceType);
                    getUTXOList();
                    if(!mIsChecked){
                        checkRawTransaction();
                    }
                    getInfo();
                    mIsUpdate = false;
                    getReferralInfo();
                    break;
                case TransmitKey.ServiceType.GET_SEND_DATA:
                    getBalance(serviceType);
                    getUTXOList();
                    break;
                case TransmitKey.ServiceType.GET_BALANCE:
                    getBalance(serviceType);
                    break;
                case TransmitKey.ServiceType.GET_UTXO_LIST:
                    getUTXOList();
                    break;
                case TransmitKey.ServiceType.GET_RAW_TX:
                    if(!mIsChecked){
                        checkRawTransactionDelay();
                    }
                    break;
                case TransmitKey.ServiceType.GET_REFERRAL_INFO:
                    getReferralInfo();
                    break;
                default:
                    break;
            }
            Logger.i("TxService onStartCommand, ServiceType=" + serviceType);
        }else{
            ProgressManager.closeProgressDialog();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void getInfo() {
        mAppModel.getInfo();
    }

    private void getReferralCounts() {
        mUserModel.getReferralCounts(new LogicObserver<Boolean>(){
            @Override
            public void handleData(Boolean aBoolean) {
                if(aBoolean){
                    EventBusUtil.post(MessageEvent.EventCode.NICKNAME);
                }
            }
        });
    }

    private void getReferralInfo() {
        getReferralCounts();
        if(!mIsUpdate){
            mUserModel.getReferralUrl(new LogicObserver<Boolean>(){
                @Override
                public void handleData(Boolean aBoolean) {
                    mIsUpdate = aBoolean;
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setData(aBoolean);
                    messageEvent.setCode(MessageEvent.EventCode.REFERRAL);
                    EventBusUtil.post(messageEvent);
                }
            });
        }
    }

    private void checkRawTransactionDelay() {
        mIsChecked = true;
        Observable.timer(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new CommonObserver<Long>() {
                    @Override
                    public void onComplete() {
                        checkRawTransaction();
                    }
                });
    }

    private void checkRawTransaction() {
        mIsChecked = true;
        mTxModel.getTxPendingList(new LogicObserver<List<TransactionHistory>>(){

            @Override
            public void handleData(List<TransactionHistory> txHistories) {
                if(txHistories.size() > 0){
                    Logger.d("checkRawTransaction start size=" + txHistories.size());
                    for (int i = 0; i < txHistories.size(); i++) {
                        try {
                            String txId = txHistories.get(i).getTxId();
                            Logger.d("checkRawTransaction TxId=" + txId);
                            mTxModel.checkRawTransaction(txId, new LogicObserver<Boolean>(){

                                @Override
                                public void handleData(Boolean isOnBlockChain) {
                                    if(isOnBlockChain){
                                        EventBusUtil.post(MessageEvent.EventCode.TRANSACTION);
                                        getBalance(TransmitKey.ServiceType.GET_BALANCE);
                                        getUTXOList();
                                    }else{
                                        TransactionHistory transactionHistory = new TransactionHistory();
                                        transactionHistory.setTxId(txId);
                                        transactionHistory.setResult(TransmitKey.TxResult.FAILED);
                                        transactionHistory.setMessage(ResourcesUtil.getText(R.string.send_tx_fail_in_pool));
                                        mTxModel.updateTransactionHistory(transactionHistory, new LogicObserver<Boolean>(){

                                            @Override
                                            public void handleData(Boolean aBoolean) {
                                                EventBusUtil.post(MessageEvent.EventCode.TRANSACTION);
                                            }
                                        });
                                    }
                                }
                            });
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Logger.d("checkRawTransaction end");
                    checkRawTransactionDelay();
                }else{
                    mIsChecked = false;
                }
            }
        });
    }

    private void getBalanceDelay(String serviceType) {
        mIsChecked = true;
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(new CommonObserver<Long>() {
                @Override
                public void onComplete() {
                    getBalance(serviceType);
                }
            });
    }

    private void getBalance(String serviceType) {
        mTxModel.getBalance( new TAUObserver<RetResult<BalanceBean>>() {
            @Override
            public void handleError(String msg, int msgCode) {
                if(StringUtil.isSame(serviceType, TransmitKey.ServiceType.GET_HOME_DATA) ||
                        StringUtil.isSame(serviceType, TransmitKey.ServiceType.GET_IMPORT_DATA)){
                    getBalanceDelay(TransmitKey.ServiceType.GET_BALANCE);
                }else{
                    ProgressManager.closeProgressDialog();
                    EventBusUtil.post(MessageEvent.EventCode.BALANCE);
                }
            }

            @Override
            public void handleData(RetResult<BalanceBean> balanceRetBalance) {
                super.handleData(balanceRetBalance);
                BalanceBean balance = balanceRetBalance.getRet();
                Logger.i("getBalance success");
                if(ActivityManager.getInstance().isTopActivity(MainActivity.class)){
                    ProgressManager.closeProgressDialog();
                }
                KeyValue entry = KeyValueDaoUtils.getInstance().insertOrReplace(balance);
                MyApplication.setKeyValue(entry);
                if(entry != null){
                    if(StringUtil.isSame(serviceType, TransmitKey.ServiceType.GET_HOME_DATA) ||
                            StringUtil.isSame(serviceType, TransmitKey.ServiceType.GET_IMPORT_DATA)){
                        EventBusUtil.post(MessageEvent.EventCode.ALL);
                    }else{
                        EventBusUtil.post(MessageEvent.EventCode.BALANCE);
                    }

                }
            }
        });
    }

    private void getUTXOList() {
        mTxModel.getUTXOList();
    }

    @Override
    public void onDestroy() {
        Logger.i("TxService onDestroy");
        super.onDestroy();
    }

    public static void startTxService(String serviceType){
        Intent intent = new Intent();
        intent.putExtra(TransmitKey.SERVICE_TYPE, serviceType);
        startTxService(intent);
    }

    public static void startTxService(Intent intent){
        Context context = MyApplication.getInstance();
        intent.setClass(context, TxService.class);
        context.startService(intent);
    }


    public static void stopService() {
        Context context = MyApplication.getInstance();
        Intent intent = new Intent();
        intent.setClass(context, TxService.class);
        context.stopService(intent);
    }
}