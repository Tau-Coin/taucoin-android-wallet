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

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import java.util.List;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.module.bean.AddInOutBean;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.model.ITxModel;
import io.taucoin.android.wallet.module.model.TxModel;
import io.taucoin.android.wallet.module.service.TxService;
import io.taucoin.android.wallet.module.view.main.iview.ISendReceiveView;
import io.taucoin.android.wallet.module.view.main.iview.ISendView;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ResourcesUtil;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;

public class TxPresenter {
    private ISendView mSendView;
    private ISendReceiveView mSendReceiveView;
    private ITxModel mTxModel;

    TxPresenter() {
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
                    TxService.startTxService(TransmitKey.ServiceType.GET_RAW_TX);
                    ToastUtils.showShortToast(R.string.send_has_pending);
                }else{
                    mSendView.checkForm();
                }
            }
        });
    }

    //First step: update Balance and UTXO
    public void getBalanceAndUTXO(TransactionHistory tx, LogicObserver<Boolean> logicObserver) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null) {
            logicObserver.onError();
            return;
        }
        long utxo = keyValue.getUtxo();
        long balance = keyValue.getBalance();
        Logger.i("balance=" + balance + "\tutxo=" + utxo);
        if (utxo == balance){
            mTxModel.getUTXOListLocal(new LogicObserver<List<UTXORecord>>() {
                @Override
                public void handleError(int code, String msg) {
                    super.handleError(code, msg);
                    logicObserver.onNext(false);
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
                        createTransaction(tx, logicObserver);
                    }else {
                        mTxModel.getUTXOList();
                        logicObserver.onNext(false);
                    }
                }
            });
        }else {
            mTxModel.getUTXOList();
            logicObserver.onNext(false);
        }
    }
    //The second step: Building transactions
    private void createTransaction(TransactionHistory txHistory, LogicObserver<Boolean> logicObserver) {
        mTxModel.createTransaction(txHistory, new LogicObserver<String>() {
            @Override
            public void handleData(String tx_hex) {
                sendRawTransaction(tx_hex, txHistory, logicObserver);
            }

            @Override
            public void handleError(int code, String msg) {
                super.handleError(code, msg);
                ToastUtils.showShortToast(msg);
                logicObserver.onNext(false);
            }
        });

    }

    //The third step: Send the transaction to the trading pool
    private void sendRawTransaction(String tx_hex, TransactionHistory txHistory, LogicObserver<Boolean> logicObserver) {
        mTxModel.sendRawTransaction(tx_hex, new TAUObserver<RetResult<String>>(){
            @Override
            public void handleData(RetResult<String> stringResResult) {
                super.handleData(stringResResult);
                Logger.d("get_txid_after_sendTX=" + stringResResult.getRet());
                ToastUtils.showShortToast(R.string.send_tx_success);
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setTxId(txHistory.getTxId());
                transactionHistory.setResult(TransmitKey.TxResult.CONFIRMING);
                mTxModel.updateTransactionHistory(transactionHistory, new LogicObserver<Boolean>(){

                    @Override
                    public void handleData(Boolean aBoolean) {
                        EventBusUtil.post(MessageEvent.EventCode.TRANSACTION);
                        checkRawTransaction();
                    }
                });
                logicObserver.onNext(true);

            }

            @Override
            public void handleError(String msg, int msgCode) {
                String result = "Error in network, send failed";
                if(msgCode == 401){
                    result = ResourcesUtil.getText(R.string.send_tx_fail);
                }else if(msgCode == 402){
                    result = msg;
                }
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setTxId(txHistory.getTxId());
                transactionHistory.setResult(TransmitKey.TxResult.FAILED);
                transactionHistory.setMessage(result);
                mTxModel.updateTransactionHistory(transactionHistory, new LogicObserver<Boolean>(){

                    @Override
                    public void handleData(Boolean aBoolean) {
                        EventBusUtil.post(MessageEvent.EventCode.TRANSACTION);
                    }
                });
                logicObserver.onNext(false);

                super.handleError(result, msgCode);
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

    public void getAddOuts(LogicObserver<Boolean> observer) {
        Logger.i("getAddOuts start");
        mTxModel.getAddOuts(new TAUObserver<DataResult<AddInOutBean>>(){

            @Override
            public void handleError(String msg, int msgCode) {
                super.handleError(msg, msgCode);
                observer.onNext(false);
            }

            @Override
            public void handleData(DataResult<AddInOutBean> listDataResult) {
                super.handleData(listDataResult);
                Logger.i("getAddOuts success");
                if(listDataResult != null && listDataResult.getData() != null){
                    Logger.i("getAddOuts success");
                    mTxModel.saveAddOuts(listDataResult.getData(), observer);
                }else{
                    Logger.i("getAddOuts success = 0");
                    observer.onNext(true);
                }
            }
        });
    }
}