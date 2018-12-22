package io.taucoin.android.wallet.module.presenter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.github.naturs.logger.Logger;

import org.greenrobot.eventbus.EventBus;

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
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.net.callBack.CommonObserver;
import io.taucoin.android.wallet.net.callBack.TAUObserver;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.ResResult;

public class TxService extends Service {

    private TxModel mTxModel;
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
                    getBalance();
                    getUTXOList();
                    if(!mIsChecked){
                        checkRawTransaction();
                    }
                    break;
                case TransmitKey.ServiceType.GET_SEND_DATA:
                    getBalance();
                    getUTXOList();
                    break;
                case TransmitKey.ServiceType.GET_BALANCE:
                    getBalance();
                    break;
                case TransmitKey.ServiceType.GET_UTXO_LIST:
                    getUTXOList();
                    break;
                case TransmitKey.ServiceType.GET_RAW_TX:
                    if(!mIsChecked){
                        checkRawTransactionDelay();
                    }
                    break;
                default:
                    break;
            }
            Logger.i("TxService onStartCommand, ServiceType=" + serviceType);
        }
        return super.onStartCommand(intent, flags, startId);
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
                                    getBalance();
                                    getUTXOList();
                                }
                            });
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
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

    private void getBalance() {
        mTxModel.getBalance( new TAUObserver<ResResult<BalanceBean>>() {
            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                ProgressManager.closeProgressDialog();
            }

            @Override
            public void handleData(ResResult<BalanceBean> balanceRetBalance) {
                super.handleData(balanceRetBalance);
                BalanceBean balance = balanceRetBalance.getRet();
                Logger.i("getBalance success");
                ProgressManager.closeProgressDialog();
                KeyValue entry = KeyValueDaoUtils.getInstance().insertOrReplace(balance);
                MyApplication.setKeyValue(entry);
                if(entry != null){
                    EventBus.getDefault().post(entry);
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