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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.github.naturs.logger.Logger;

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
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.module.view.main.MainActivity;
import io.taucoin.android.wallet.module.view.tx.SendActivity;
import io.taucoin.android.wallet.net.callback.CommonObserver;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.StringUtil;

public class TxService extends Service {

    private ITxModel mTxModel;
    private IAppModel mAppModel;
    private boolean mIsChecked;

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
        mIsChecked = false;
        Logger.i("TxService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(intent != null && keyValue != null){
            String serviceType = intent.getStringExtra(TransmitKey.SERVICE_TYPE);
            switch (serviceType){
                case TransmitKey.ServiceType.GET_HOME_DATA:
                    getBalance(serviceType);
                    getUTXOList();
                    if(!mIsChecked){
                        checkRawTransaction();
                    }
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
                case TransmitKey.ServiceType.GET_INFO:
                    getInfo();
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
                            Logger.d("checkRawTransaction TxId=" + txHistories.get(i).getTxId());
                            mTxModel.checkRawTransaction(txHistories.get(i).getTxId(), new LogicObserver<Boolean>(){
                                @Override
                                public void handleData(Boolean isOnBlockChain) {
                                    getBalance(TransmitKey.ServiceType.GET_BALANCE);
                                    getUTXOList();
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

    private void getBalance(String serviceType) {
        mTxModel.getBalance( new TAUObserver<RetResult<BalanceBean>>() {
            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                ProgressManager.closeProgressDialog();
            }

            @Override
            public void handleData(RetResult<BalanceBean> balanceRetBalance) {
                super.handleData(balanceRetBalance);
                BalanceBean balance = balanceRetBalance.getRet();
                Logger.i("getBalance success");
                if(ActivityManager.isTopActivity(MainActivity.class) ||
                        ActivityManager.isTopActivity(SendActivity.class)){
                    ProgressManager.closeProgressDialog();
                }
                KeyValue entry = KeyValueDaoUtils.getInstance().insertOrReplace(balance);
                MyApplication.setKeyValue(entry);
                if(entry != null){
                    if(StringUtil.isSame(serviceType, TransmitKey.ServiceType.GET_HOME_DATA)){
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