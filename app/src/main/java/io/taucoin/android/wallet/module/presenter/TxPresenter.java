package io.taucoin.android.wallet.module.presenter;

import android.graphics.Bitmap;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.module.view.main.iview.IHomeView;
import io.taucoin.android.wallet.module.view.main.iview.ISendReceiveView;
import io.taucoin.android.wallet.module.view.main.iview.ISendView;
import io.taucoin.android.wallet.net.callBack.TAUObserver;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.ResResult;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.platform.adress.Key;
import io.taucoin.platform.adress.KeyManager;

public class TxPresenter {
    private IHomeView mHomeView;
    private ISendView mSendView;
    private ISendReceiveView mSendReceiveView;
    private TxModel mTxModel;

    public TxPresenter() {
        mTxModel = new TxModel();
    }

    public TxPresenter(IHomeView homeView) {
        mTxModel = new TxModel();
        mHomeView = homeView;
    }
    public TxPresenter(ISendView sendView) {
        mTxModel = new TxModel();
        mSendView = sendView;
    }
    public TxPresenter(ISendReceiveView sendView) {
        mTxModel = new TxModel();
        mSendReceiveView = sendView;
    }

    public void getBalanceLocal() {
        mTxModel.getBalanceLocal(new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                if(keyValue != null){
                    MyApplication.setKeyValue(keyValue);
                    EventBus.getDefault().postSticky(keyValue);
                }
            }
        });
    }

    public void isAnyTxPending() {
        mTxModel.isAnyTxPending(new LogicObserver<Boolean>() {
            @Override
            public void handleData(Boolean isAnyTxPending) {
                if(isAnyTxPending){
                    ToastUtils.showShortToast(R.string.send_has_pending);
                }else{
                    mSendView.checkForm();
                }
            }
        });
    }

    //First step: update Balance and UTXO
    public void getBalanceAndUTXO(TransactionHistory tx) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null) return;
        long utxo = keyValue.getUtxo();
        long balance = keyValue.getBalance();
        Logger.i("balance=" + balance + "\tutxo=" + utxo);
        if (utxo == balance){
            mTxModel.getUTXOListLocal(new LogicObserver<List<UTXORecord>>() {
                @Override
                public void handleError(int code, String msg) {
                    super.handleError(code, msg);
                    ProgressManager.closeProgressDialog();
                }

                @Override
                public void handleData(List<UTXORecord> utxoRecord) {
                    long sum = 0;
                    Logger.e("utxoRecord size=" + utxoRecord.size());
                    for (int i = 0; i < utxoRecord.size(); i++){
                        long v = utxoRecord.get(i).getValue().longValue();
                        sum += v;
                    }
                    if (utxo == sum){
                        createTransaction(tx);
                    }else {
                        mTxModel.getUTXOList();
                        ProgressManager.closeProgressDialog();
                    }
                }
            });
        }else {
            mTxModel.getUTXOList();
            ProgressManager.closeProgressDialog();
        }
    }
    //The second step: Building transactions
    private void createTransaction(TransactionHistory txHistory) {
        mTxModel.createTransaction(txHistory, new LogicObserver<String>() {
            @Override
            public void handleData(String tx_hex) {
                sendRawTransaction(tx_hex, txHistory);
            }

            @Override
            public void handleError(int code, String msg) {
                super.handleError(code, msg);
                ToastUtils.showShortToast(msg);
                ProgressManager.closeProgressDialog();
            }
        });

    }

    //The third step: Send the transaction to the trading pool
    private void sendRawTransaction(String tx_hex, TransactionHistory txHistory) {
        mTxModel.sendRawTransaction(tx_hex, new TAUObserver<ResResult<String>>(){
            @Override
            public void handleData(ResResult<String> stringResResult) {
                super.handleData(stringResResult);
                ProgressManager.closeProgressDialog();
                Logger.d("get_txid_after_sendTX=" + stringResResult.getRet());
                ToastUtils.showShortToast(R.string.send_tx_success);
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setTxId(txHistory.getTxId());
                transactionHistory.setResult("Confirming");
                mTxModel.updateTransactionHistory(transactionHistory);

                checkRawTransaction();
            }

            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                ProgressManager.closeProgressDialog();
                TransactionHistory transactionHistory = TransactionHistoryDaoUtils.getInstance().queryTransactionById(txHistory.getTxId());
                transactionHistory.setResult("Failed");
                transactionHistory.setMessage(msg);
                mTxModel.updateTransactionHistory(transactionHistory);
            }
        });
    }
    //The fourth step: Check to see if the transaction is on the chain
    private void checkRawTransaction() {
        TxService.startTxService(TransmitKey.ServiceType.GET_RAW_TX);
    }

    public void queryTransactionHistory() {
        mTxModel.queryTransactionHistory(new LogicObserver<List<TransactionHistory>>(){

            @Override
            public void handleData(List<TransactionHistory> transactionHistories) {
                mSendReceiveView.loadTransactionHistory(transactionHistories);
            }
        });
    }

    public void saveKeyAndAddress(KeyValue keyValue, LogicObserver observer) {
        if(keyValue == null){
            keyValue = new KeyValue();
            Key key = KeyManager.generatorKey();
            if(key != null){
                keyValue.setPrivkey(key.getPrivkey());
                keyValue.setPubkey(key.getPubkey());
                keyValue.setAddress(key.getAddress());
            }
        }
        mTxModel.saveKeyAndAddress(keyValue, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                MyApplication.setKeyValue(keyValue);
                SharedPreferencesHelper.getInstance().putString(TransmitKey.PUBLIC_KEY, keyValue.getPubkey());
                SharedPreferencesHelper.getInstance().putString(TransmitKey.ADDRESS, keyValue.getAddress());
                TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
                observer.onNext(keyValue);
            }
        });
    }

    public void saveName(String name) {
        mTxModel.saveName(name, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                EventBus.getDefault().postSticky(keyValue);
            }
        });
    }

    public void saveAvatar(String avatar, Bitmap bitmap) {
        mTxModel.saveAvatar(avatar, bitmap, new LogicObserver<KeyValue>() {
            @Override
            public void handleData(KeyValue keyValue) {
                EventBus.getDefault().postSticky(keyValue);
            }
        });
    }
}