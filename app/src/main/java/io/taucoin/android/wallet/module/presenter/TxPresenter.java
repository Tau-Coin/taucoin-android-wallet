package io.taucoin.android.wallet.module.presenter;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.util.List;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import io.taucoin.android.wallet.module.model.ITxModel;
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.module.view.main.iview.ISendReceiveView;
import io.taucoin.android.wallet.module.view.main.iview.ISendView;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;

public class TxPresenter {
    private ISendView mSendView;
    private ISendReceiveView mSendReceiveView;
    private ITxModel mTxModel;

    public TxPresenter() {
        mTxModel = new TxModel();
    }

    public TxPresenter(ISendView sendView) {
        mTxModel = new TxModel();
        mSendView = sendView;
    }
    public TxPresenter(ISendReceiveView sendView) {
        mTxModel = new TxModel();
        mSendReceiveView = sendView;
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
        mTxModel.sendRawTransaction(tx_hex, new TAUObserver<RetResult<String>>(){
            @Override
            public void handleData(RetResult<String> stringResResult) {
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

    public void queryTransactionHistory(int pageNo, String time) {
        mTxModel.queryTransactionHistory(pageNo, time, new LogicObserver<List<TransactionHistory>>(){

            @Override
            public void handleData(List<TransactionHistory> transactionHistories) {
                mSendReceiveView.loadTransactionHistory(transactionHistories);
                mSendReceiveView.finishRefresh();
                mSendReceiveView.finishLoadMore();
            }
        });
    }
}