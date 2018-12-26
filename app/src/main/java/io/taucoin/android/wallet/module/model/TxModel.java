package io.taucoin.android.wallet.module.model;

import com.github.naturs.logger.Logger;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;
import io.taucoin.android.wallet.core.FeeRate;
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
import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.module.bean.RawTxBean;
import io.taucoin.android.wallet.module.bean.UTXOList;
import io.taucoin.android.wallet.module.bean.UtxosBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.android.wallet.net.service.TxService;
import io.taucoin.android.wallet.util.DateUtil;
import io.taucoin.android.wallet.util.FmtMicrometer;
import io.taucoin.android.wallet.util.MD5_BASE64Util;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;
import io.taucoin.foundation.util.StringUtil;

public class TxModel implements ITxModel {

    @Override
    public void getBalance(TAUObserver<RetResult<BalanceBean>> observer) {
        String publicKey = SharedPreferencesHelper.getInstance().getString(TransmitKey.PUBLIC_KEY, "");
        Map<String,String> map=new HashMap<>();
        map.put("pubkey",  publicKey);
        NetWorkManager.createApiService(TxService.class)
            .getBalance(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        NetWorkManager.createApiService(TxService.class)
            .getUTXOList(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new TAUObserver<UTXOList>() {

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
        KeyValue keyValue = MyApplication.getKeyValue();
        String address = "";
        if(keyValue != null){
            address = keyValue.getAddress();
        }
        String finalAddress = address;
        Observable.create((ObservableOnSubscribe<List<UTXORecord>>) emitter -> {
            List<UTXORecord> list = UTXORecordDaoUtils.getInstance().queryByAddress(finalAddress);
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
        NetWorkManager.createApiService(TxService.class)
            .getRawTransation(map)
            .subscribeOn(Schedulers.io())
            .subscribe(new TAUObserver<RetResult<RawTxBean>>() {
                @Override
                public void handleData(RetResult<RawTxBean> rawTxBeanResResult) {
                    super.handleData(rawTxBeanResResult);
                    RawTxBean rawTx = rawTxBeanResResult.getRet();
                    rawTx.setBlocktime(rawTx.getBlocktime());
                    TransactionHistory transactionHistory = new TransactionHistory();
                    transactionHistory.setTxId(txId);
                    transactionHistory.setConfirmations(rawTx.getConfirmations());
                    transactionHistory.setBlocktime(rawTx.getBlocktime());
                    transactionHistory.setResult("Successful");
                    updateTransactionHistory(transactionHistory);
                    if(rawTx.getBlocktime() >= 1){
                        observer.onNext(true);
                    }
                }
            });

    }

    @Override
    public void createTransaction(TransactionHistory txHistory, LogicObserver<String> observer) {
        KeyValue keyValue = MyApplication.getKeyValue();

        if(keyValue == null || StringUtil.isEmpty(keyValue.getPrivkey())){
            observer.onError(null);
            return;
        }
        String newPrivateKey;
        try {
            newPrivateKey = Utils.convertWIFPrivkeyIntoPrivkey(keyValue.getPrivkey());
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
            Logger.e(e, "AddressFormatException in createTransaction");
            observer.onError(null);
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
        String fee = FmtMicrometer.fmtTxValue(txHistory.getFee());
        FeeRate feeRate = new FeeRate(new BigInteger(fee, 10));
        CreateTransactionResult result = wallet.createTransaction(receipts, false, feeRate, tx);
        if (result.failReason == TransactionFailReason.NO_ERROR) {
            Logger.i("Create tx success");
            Logger.i(tx.toString());
            txHistory.setTxId(tx.getHashAsString());
            txHistory.setConfirmations(0);
            txHistory.setResult("sending");
            txHistory.setFromAddress(keyValue.getAddress());
            txHistory.setTime(DateUtil.getCurrentTime(DateUtil.pattern6));

            insertTransactionHistory(txHistory);

            Logger.i("Transactions converted to hexadecimal strings:"+tx.dumpIntoHexStr());
            String hex_after_base64= MD5_BASE64Util.EncoderByMd5_BASE64(tx.dumpIntoHexStr());
            Logger.i("Transactions encrypted by BASE64: " + hex_after_base64);
            observer.onNext(hex_after_base64);
        } else {
            observer.handleError(result.failReason.getCode(), result.failReason.getMsg());
            ToastUtils.showShortToast(result.failReason.getMsg());
        }
    }

    @Override
    public void sendRawTransaction(String tx_hex, TAUObserver<RetResult<String>> observer) {
        Map<String,String> map = new HashMap<>();
        map.put("tx_hex", tx_hex);
        NetWorkManager.createApiService(TxService.class)
                .sendRawTransation(map)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void updateTransactionHistory(TransactionHistory txHistory){
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            TransactionHistory transactionHistory = TransactionHistoryDaoUtils.getInstance().queryTransactionById(txHistory.getTxId());
            transactionHistory.setResult(txHistory.getResult());
            transactionHistory.setConfirmations(txHistory.getConfirmations());
            transactionHistory.setBlocktime(txHistory.getBlocktime());
            long result = TransactionHistoryDaoUtils.getInstance().insertOrReplace(transactionHistory);
            emitter.onNext(result);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void insertTransactionHistory(TransactionHistory txHistory){
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            long result = TransactionHistoryDaoUtils.getInstance().insertOrReplace(txHistory);
            emitter.onNext(result);
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
}