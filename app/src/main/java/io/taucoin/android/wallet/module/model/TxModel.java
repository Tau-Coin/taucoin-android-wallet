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

import com.github.naturs.logger.Logger;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;
import io.taucoin.android.wallet.core.Wallet;
import io.taucoin.android.wallet.core.keystore.KeyStore;
import io.taucoin.android.wallet.core.transactions.CreateTransactionResult;
import io.taucoin.android.wallet.core.transactions.Transaction;
import io.taucoin.android.wallet.core.transactions.TransactionFailReason;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.ScriptPubkey;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import io.taucoin.android.wallet.db.util.UTXORecordDaoUtils;
import io.taucoin.android.wallet.module.bean.AddInOutBean;
import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.module.bean.RawTxBean;
import io.taucoin.android.wallet.module.bean.TxBean;
import io.taucoin.android.wallet.module.bean.UTXOList;
import io.taucoin.android.wallet.module.bean.UtxosBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.net.service.TransactionService;
import io.taucoin.android.wallet.util.DateUtil;
import io.taucoin.android.wallet.util.FmtMicrometer;
import io.taucoin.android.wallet.util.MD5_BASE64Util;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.android.wallet.util.WalletEncrypt;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;
import io.taucoin.foundation.net.exception.CodeException;
import io.taucoin.foundation.util.StringUtil;

public class TxModel implements ITxModel {

    @Override
    public void getBalance(TAUObserver<RetResult<BalanceBean>> observer) {
        String publicKey = SharedPreferencesHelper.getInstance().getString(TransmitKey.PUBLIC_KEY, "");
        Map<String,String> map=new HashMap<>();
        map.put("pubkey",  publicKey);
        NetWorkManager.createApiService(TransactionService.class)
            .getBalance(map)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(observer);
    }

    @Override
    public void isAnyTxPending(LogicObserver<Boolean> observer) {
        String address = SharedPreferencesHelper.getInstance().getString(TransmitKey.ADDRESS, "");
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            boolean isAnyTxPending = TransactionHistoryDaoUtils.getInstance().isAnyTxPending(address);
            emitter.onNext(isAnyTxPending);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void getUTXOList() {
        KeyValue keyValue = MyApplication.getKeyValue();
        Map<String,String> map = new HashMap<>();
        map.put("address",  keyValue.getAddress());
        NetWorkManager.createApiService(TransactionService.class)
            .getUTXOList(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new TAUObserver<UTXOList>() {

                @Override
                public void handleError(String msg, int msgCode) {

                }

                @Override
                public void handleData(UTXOList resResult) {
                    super.handleData(resResult);

                    Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                        List<UTXORecord> utxoRecordList = new ArrayList<>();
                        List<UtxosBean> list = resResult.getUtxosX();
                        if(list != null){
                            for (int i = 0; i < list.size(); i++){
                                UTXORecord utxoRecord = new UTXORecord();
                                utxoRecord.setConfirmations(list.get(i).getConfirmations());
                                utxoRecord.setTxId(list.get(i).getTxid());
                                utxoRecord.setVout(list.get(i).getVout());
                                utxoRecord.setAddress(list.get(i).getScriptPubKey().getAddresses().get(0));

                                ScriptPubkey scriptPubkey =new ScriptPubkey();
                                scriptPubkey.setAsm(list.get(i).getScriptPubKey().getAsm());
                                scriptPubkey.setHex(list.get(i).getScriptPubKey().getHex());
                                utxoRecord.setScriptPubKey(scriptPubkey);

                                utxoRecord.setVersion(list.get(i).getVersion());
                                utxoRecord.setCoinbase(list.get(i).isCoinbase());
                                utxoRecord.setBestblockhash(list.get(i).getBestblockhash());
                                utxoRecord.setBestblockheight(list.get(i).getBlockheight());
                                utxoRecord.setBestblocktime(list.get(i).getBestblocktime());
                                utxoRecord.setBlockhash(list.get(i).getBlockhash());
                                utxoRecord.setBlockheight(list.get(i).getBlockheight());
                                utxoRecord.setBlocktime(list.get(i).getBlocktime());

                                BigInteger value = FmtMicrometer.fmtUTXOValue(list.get(i).getValue());
                                utxoRecord.setValue(value);
                                utxoRecordList.add(0, utxoRecord);
                            }
                            UTXORecordDaoUtils.getInstance().deleteByAddress(keyValue.getAddress());
                            UTXORecordDaoUtils.getInstance().insertOrReplace(utxoRecordList);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe();

                }
            });
    }

    @Override
    public void getUTXOListLocal(LogicObserver<List<UTXORecord>> observer) {
        Observable.create((ObservableOnSubscribe<List<UTXORecord>>) emitter -> {
            KeyValue keyValue = MyApplication.getKeyValue();
            if(keyValue == null){
                emitter.onError(CodeException.getError());
                return;
            }
            String address = keyValue.getAddress();
            List<UTXORecord> list = UTXORecordDaoUtils.getInstance().queryByAddress(address);
            emitter.onNext(list);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void getTxPendingList(LogicObserver<List<TransactionHistory>> observer){
        KeyValue keyValue = MyApplication.getKeyValue();
        String address = keyValue.getAddress();
        Observable.create((ObservableOnSubscribe<List<TransactionHistory>>) emitter -> {
            List<TransactionHistory> list = TransactionHistoryDaoUtils.getInstance().getTxPendingList(address);
            emitter.onNext(list);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void checkRawTransaction(String txId, LogicObserver<Boolean> observer) {
        Map<String,String> map = new HashMap<>();
        map.put("txid", txId);
        NetWorkManager.createApiService(TransactionService.class)
            .getRawTransation(map)
            .subscribeOn(Schedulers.io())
            .subscribe(new TAUObserver<RetResult<RawTxBean>>() {
                @Override
                public void handleData(RetResult<RawTxBean> rawTxBeanResResult) {
                    super.handleData(rawTxBeanResResult);
                    RawTxBean rawTx = rawTxBeanResResult.getRet();
                    rawTx.setBlocktime(rawTx.getBlocktime());
                    TransactionHistory transactionHistory = TransactionHistoryDaoUtils.getInstance().queryTransactionById(txId);
                    if(transactionHistory != null){
                        transactionHistory.setTxId(txId);
                        transactionHistory.setConfirmations(rawTx.getConfirmations());
                        transactionHistory.setBlocktime(rawTx.getBlocktime());
                        transactionHistory.setResult(TransmitKey.TxResult.SUCCESSFUL);
                        TransactionHistoryDaoUtils.getInstance().insertOrReplace(transactionHistory);
                        if(rawTx.getConfirmations() > TransmitKey.TX_CONFIRMATIONS){
                            observer.onNext(true);
                        }
                    }
                }

                @Override
                public void handleError(String msg, int msgCode) {
                    if(msgCode == 402){
                        observer.onNext(false);
                    }
                }
            });

    }

    @Override
    public void createTransaction(TransactionHistory txHistory, LogicObserver<String> observer) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            KeyValue keyValue = MyApplication.getKeyValue();
            if(keyValue == null || StringUtil.isEmpty(keyValue.getPrivkey())){
                emitter.onError(CodeException.getError());
                return;
            }
            String newPrivateKey;
            try {
                String encryptKey = WalletEncrypt.decrypt(keyValue.getPrivkey());
                newPrivateKey = Utils.convertWIFPrivkeyIntoPrivkey(encryptKey);
            } catch (AddressFormatException e) {
                System.out.println(e.toString());
                Logger.e(e, "AddressFormatException in createTransaction");
                emitter.onError(CodeException.getError());
                return;
            }
            ECKey key = new ECKey(new BigInteger(newPrivateKey, 16));
            Logger.i("Compressed key hash:" + Utils.bytesToHexString(key.getCompressedPubKeyHash()));
            KeyStore.getInstance().addKey(key);
            String amount = FmtMicrometer.fmtTxValue(txHistory.getValue());
            HashMap<String, BigInteger> receipts = new HashMap<>();
            receipts.put(txHistory.getToAddress(), new BigInteger(amount, 10));
            Wallet wallet = Wallet.getInstance();
            Transaction tx = new Transaction(NetworkParameters.mainNet());
            String fee = txHistory.getFee();
            CreateTransactionResult result = wallet.createTransaction(receipts, fee, tx);
            if (result.failReason == TransactionFailReason.NO_ERROR) {
                Logger.i("Create tx success");
                Logger.i(tx.toString());
                txHistory.setTxId(tx.getHashAsString());
                txHistory.setConfirmations(0);
                txHistory.setResult(TransmitKey.TxResult.CONFIRMING);
                txHistory.setFromAddress(keyValue.getAddress());
                txHistory.setTime(DateUtil.getCurrentTime());
                txHistory.setSentOrReceived(TransmitKey.TxType.SEND);

                insertTransactionHistory(txHistory);

                Logger.i("Transactions converted to hexadecimal strings:"+tx.dumpIntoHexStr());
                String hex_after_base64= MD5_BASE64Util.EncoderByMd5_BASE64(tx.dumpIntoHexStr());
                Logger.i("Transactions encrypted by BASE64: " + hex_after_base64);
                emitter.onNext(hex_after_base64);
            } else {
                emitter.onError(CodeException.getError(result.failReason.getMsg()));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void sendRawTransaction(String tx_hex, TAUObserver<RetResult<String>> observer) {
        Map<String,String> map = new HashMap<>();
        map.put("tx_hex", tx_hex);
        NetWorkManager.createApiService(TransactionService.class)
                .sendRawTransation(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void updateTransactionHistory(TransactionHistory txHistory, LogicObserver<Boolean> observer){
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            TransactionHistory transactionHistory = TransactionHistoryDaoUtils.getInstance().queryTransactionById(txHistory.getTxId());
            transactionHistory.setResult(txHistory.getResult());
            transactionHistory.setMessage(txHistory.getMessage());
            TransactionHistoryDaoUtils.getInstance().insertOrReplace(transactionHistory);
            emitter.onNext(true);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void insertTransactionHistory(TransactionHistory txHistory){
        Observable.create((ObservableOnSubscribe<TransactionHistory>) emitter -> {
            TransactionHistoryDaoUtils.getInstance().insertOrReplace(txHistory);
            emitter.onNext(txHistory);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void queryTransactionHistory(int pageNo, String time, LogicObserver<List<TransactionHistory>> logicObserver) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null || StringUtil.isEmpty(keyValue.getAddress())){
            return;
        }
        Observable.create((ObservableOnSubscribe<List<TransactionHistory>>) emitter -> {
            List<TransactionHistory> result = TransactionHistoryDaoUtils.getInstance().queryData(pageNo, time, keyValue.getAddress());
            emitter.onNext(result);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(logicObserver);
    }

    @Override
    public void getAddOuts(TAUObserver<DataResult<AddInOutBean>> observer) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            observer.onError(CodeException.getError());
            return;
        }
        String address = keyValue.getAddress();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String time = TransactionHistoryDaoUtils.getInstance().getNewestTxTime(address);
            if(StringUtil.isNotEmpty(time)){
                emitter.onNext(time);
            }else {
                observer.onError();
            }
        }).observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(new LogicObserver<String>() {
                @Override
                public void handleData(String time) {

                    Map<String,String> map = new HashMap<>();
                    map.put("address", address);
                    map.put("time", time);
                    NetWorkManager.createApiService(TransactionService.class)
                            .getAddOuts(map)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(observer);
                }
            });

    }

    @Override
    public void saveAddOuts(AddInOutBean addInOut, LogicObserver<Boolean> observer) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if(null != addInOut){
                List<TxBean> receives = addInOut.getReceived();
                if(null != receives && receives.size() > 0){
                    for (TxBean bean : receives) {
                        saveAddOutsToDB(bean, TransmitKey.TxType.RECEIVE);
                    }
                }
                List<TxBean> sends = addInOut.getSent();
                if(null != sends && sends.size() > 0){
                    for (TxBean bean : sends) {
                        saveAddOutsToDB(bean, TransmitKey.TxType.SEND);
                    }
                }
            }
            emitter.onNext(true);
        }).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    private void saveAddOutsToDB(TxBean bean, String txType) {
        TransactionHistory tx = new TransactionHistory();
        tx.setSentOrReceived(txType);
        tx.setFromAddress(bean.getAddIn());
        tx.setToAddress(bean.getAddOut());
        // time and blockTime need set value here!
        tx.setTime(bean.getTime());
        try{
            long time = Long.valueOf(bean.getTime());
            tx.setBlocktime(time);
        }catch (Exception ignore){ }

        tx.setTxId(bean.getTxid());
        tx.setValue(FmtMicrometer.fmtAmount(bean.getVout()));
        tx.setFee(FmtMicrometer.fmtAmount(bean.getFee()));
        tx.setBlockheight(bean.getBlockHeight());
        TransactionHistoryDaoUtils.getInstance().saveAddOut(tx);
    }
}