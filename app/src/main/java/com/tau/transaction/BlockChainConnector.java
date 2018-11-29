package com.mofei.tau.transaction;

import android.os.Message;

import com.mofei.tau.activity.BaseActivity;
import com.mofei.tau.activity.SendAndReceiveActivity;
import com.mofei.tau.db.greendao.KeyDaoUtils;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.res_post.HexRet;
import com.mofei.tau.entity.res_post.RawTX;
import com.mofei.tau.entity.res_post.UTXOList;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.util.L;
import com.mofei.tau.util.UserInfoUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ly on 18-11-26
 *
 * @version 1.0
 * @description:
 */
public class BlockChainConnector extends BaseActivity{

    public static double balance;
    public static double utxo;
    private List<UTXORecord> utxoRecordList;
    public static List<UTXOList.UtxosBean> utxo_list;

    private SendRawTxResult sendRawTxResult;
    public static class SendRawTxResult {
        public String hexRetStatus;
        public String hexRetMessage;
        public String txId;

        public SendRawTxResult(String hexRetStatus,  String hexRetMessage, String txId) {
            this.hexRetStatus = hexRetStatus;
            this.hexRetMessage = hexRetMessage;
            this.txId = txId;
        }
    }

    private GetRawTxResult getRawTxResult;
    public static class GetRawTxResult {
        public String rawTXStatus;
        public String rawTXMessage;
        public int rawTXconfirmations;
        public long blockTime;
        public String txid;

        public GetRawTxResult(String rawTXStatus,  String rawTXMessage, int rawTXconfirmations,
                              long blockTime, String txid) {
            this.rawTXStatus = rawTXStatus;
            this.rawTXMessage = rawTXMessage;
            this.rawTXconfirmations = rawTXconfirmations;
            this.blockTime = blockTime;
            this.txid = txid;
        }
    }

    public static final int GET_RAW_TX_COMPLETED = 0x41;
    public static final int GET_BALANCE_COMPLETED = 0x42;
    public static final int SEND_RAW_TX_COMPLETED = 0x43;
    public static final int GET_UTXOLIST_COMPLETED = 0x44;

    // 单例模式
    private static BlockChainConnector sInstance;

    protected BlockChainConnector() {

    }

    public static BlockChainConnector getInstance() {
            if (sInstance==null){
                synchronized (BlockChainConnector.class) {
                    sInstance = new BlockChainConnector();
                }
            }
            return sInstance;
    }

    private Message getBalanceMessage;

    public void getBalanceData(String email, Message message) {
        //getBalanceMessage = message;
        //message.sendToTarget();
        Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<Balance<BalanceRet>> observable=apiService.getBalance2(emailMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Balance<BalanceRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Balance<BalanceRet> balanceRetBalance) {

                        L.e(balanceRetBalance.getStatus()+"");
                        L.e(balanceRetBalance.getMessage());

                        balance=balanceRetBalance.getRet().getCoins();
                        utxo=balanceRetBalance.getRet().getUtxo();
                        SharedPreferencesHelper.getInstance(BlockChainConnector.this).putString("balance",""+balanceRetBalance.getRet().getCoins());
                        SharedPreferencesHelper.getInstance(BlockChainConnector.this).putString("reward",""+balanceRetBalance.getRet().getRewards());
                        SharedPreferencesHelper.getInstance(BlockChainConnector.this).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        //balance,reward,utxo插入KeyBD
                        KeyValue key=new KeyValue();
                        key.setPubkey(SharedPreferencesHelper.getInstance(BlockChainConnector.this).getString("Pubkey","Pubkey"));
                        key.setPrivkey(SharedPreferencesHelper.getInstance(BlockChainConnector.this).getString("Privkey","Privkey"));
                        key.setAddress(SharedPreferencesHelper.getInstance(BlockChainConnector.this).getString("Address","Address"));
                        key.setUtxo((long) balanceRetBalance.getRet().getUtxo());
                        key.setBalance((long) balanceRetBalance.getRet().getCoins());
                        key.setReward((long) balanceRetBalance.getRet().getRewards());

                       // KeyDaoUtils.getInstance().deleteAllData();
                        List<KeyValue> listKeyValue = KeyDaoUtils.getInstance().queryAllData();
                        if (listKeyValue == null || listKeyValue.size() == 0) {
                            KeyDaoUtils.getInstance().insertKeyStoreData(key);
                        } else {
                            //KeyDaoUtils.getInstance().updateKeyValueData(key);
                        }

                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.e("getBalance error");
                        e.printStackTrace();
                        message.arg1 = -1;
                        message.sendToTarget();
                    }

                    @Override
                    public void onComplete() {
                        message.arg1 = 0;
                        message.sendToTarget();
                        L.e("getBalance onComplete");
                    }
                });
    }


    public void getUTXOList(Message message) {
        Map<String,String> address=new HashMap<>();
        String addre= UserInfoUtils.getAddress(getApplication());
        L.e("getUTXOList_Address: "+addre);
        String pub= UserInfoUtils.getPublicKey(getApplication());
        L.e("getUTXOList_publ: "+pub);
        String Priv=UserInfoUtils.getPrivateKey(getApplication());
        L.e("getUTXOList_Privkey: "+Priv);

        address.put("address",addre);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<UTXOList> observable=apiService.getUTXOList(address);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UTXOList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UTXOList utxoListRetUTXOList) {

                        L.e("Message : "+utxoListRetUTXOList.getMessage());
                        L.e("Status : "+utxoListRetUTXOList.getStatus());
                        L.e("utxos-size :"+utxoListRetUTXOList.getUtxosX().size());

                        utxoRecordList=new ArrayList<>();
                        utxo_list=utxoListRetUTXOList.getUtxosX();
                        L.e("getUtxosX的个数",utxo_list.size()+"");
                        for (int i=0;i<utxo_list.size();i++){
                            L.e("进ｆｏｒ_utxo "+i);

                            UTXORecord utxoRecord=new UTXORecord();
                            utxoRecord.setConfirmations(utxoListRetUTXOList.getUtxosX().get(i).getConfirmations());
                            utxoRecord.setTxId(utxoListRetUTXOList.getUtxosX().get(i).getTxid());
                            utxoRecord.setVout(utxoListRetUTXOList.getUtxosX().get(i).getVout());
                            utxoRecord.setAddress(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getAddresses().get(0));

                            ScriptPubkey scriptPubkey =new ScriptPubkey();
                            scriptPubkey.setAsm(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getAsm());
                            scriptPubkey.setHex(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getHex());
                            utxoRecord.setScriptPubKey(scriptPubkey);
                            utxoRecord.setVersion(utxoListRetUTXOList.getUtxosX().get(i).getVersion());
                            utxoRecord.setCoinbase(utxoListRetUTXOList.getUtxosX().get(i).isCoinbase());
                            utxoRecord.setBestblockhash(utxoListRetUTXOList.getUtxosX().get(i).getBestblockhash());
                            utxoRecord.setBestblockheight(utxoListRetUTXOList.getUtxosX().get(i).getBlockheight());
                            utxoRecord.setBestblocktime(utxoListRetUTXOList.getUtxosX().get(i).getBestblocktime());
                            utxoRecord.setBlockhash(utxoListRetUTXOList.getUtxosX().get(i).getBlockhash());
                            utxoRecord.setBlockheight(utxoListRetUTXOList.getUtxosX().get(i).getBlockheight());
                            utxoRecord.setBlocktime(utxoListRetUTXOList.getUtxosX().get(i).getBlocktime());

                            L.e("getValue="+utxoListRetUTXOList.getUtxosX().get(i).getValue());
                            //把5.00000000转化成50000000
                            Double d = new Double(utxoListRetUTXOList.getUtxosX().get(i).getValue())*Math.pow(10,8);
                            L.e("double"+d);
                            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                            nf.setGroupingUsed(false);
                            L.e("转化后　"+nf.format(d));

                            utxoRecord.setValue( new BigInteger(nf.format(d)));

                            utxoRecordList.add(utxoRecord);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        hideWaitDialog();
                        L.e("onError");
                        e.printStackTrace();
                        message.arg1 = -1;
                        message.sendToTarget();
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete");
                        UTXORecordDaoUtils.getInstance().deleteAllData();
                        UTXORecordDaoUtils.getInstance().insertOrReplaceMultiData(utxoRecordList);
                        hideWaitDialog();

                        //new SendFragment().syncTXDB();

                       // h.sendEmptyMessage(0x77);

                        message.arg1 = 0;
                        message.sendToTarget();

                    }
                });
    }




    public void sendRawTransation(String tx_hex, Message message) {
        Map<String,String> tx_hex_map=new HashMap<>();
        tx_hex_map.put("tx_hex", tx_hex);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<HexRet> observable=apiService.sendRawTransation(tx_hex_map);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HexRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HexRet hexRet) {
                        String hexRetStatus = hexRet.getStatus();
                        L.e("hexRetStatus="+hexRet.getStatus());
                        String hexRetMessage = hexRet.getMessage();
                        L.e("getMessage="+hexRetMessage);
                        String get_txid_after_sendTX=hexRet.getRet();
                        L.e("get_txid_after_sendTX="+get_txid_after_sendTX);

                        sendRawTxResult = new SendRawTxResult(hexRetStatus, hexRetMessage, get_txid_after_sendTX);

                       /* TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
                        transactionHistory.setResult("Confirming");
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        transactionHistory.setTime(date);
                        //transactionHistory.setMessage(errorMessg);
                        TransactionHistoryDaoUtils.getInstance().insertOrReplaceData(transactionHistory);
                        L.e("sendRawTransation");*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                        e.printStackTrace();
                        message.arg1=-1;
                        message.sendToTarget();
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete");
                        message.arg1=0;
                        message.obj = sendRawTxResult;
                        message.sendToTarget();
                    }
                });
    }


    public void getRawTransation(Map<String, String> txid, Message message) {
        ApiService apiService=NetWorkManager.getApiService();
        Observable<RawTX> observable=apiService.getRawTransation(txid);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RawTX>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RawTX rawTX) {

                        String rawTXStatus=rawTX.getStatus();
                        L.e("Status: "+rawTXStatus);
                        String rawTXMessage=rawTX.getMessage();
                        L.e("Message: "+rawTXMessage);
                        int rawTXconfirmations=rawTX.getRet().getConfirmations();

                        if (rawTXconfirmations > 0){
                            L.e("getConfirmations: "+rawTXconfirmations);
                        }

                        long blockTime=rawTX.getRet().getBlocktime();
                        L.e("getBlocktime:"+blockTime);

                        getRawTxResult = new GetRawTxResult(rawTXStatus, rawTXMessage,
                                rawTXconfirmations, blockTime ,txid.get("txid"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        L.e("onerror");
                       // handler_.sendEmptyMessage(0x67);
                        message.arg1=-1;
                        message.sendToTarget();
                    }

                    @Override
                    public void onComplete() {
                        L.e("getRawTX_onComplete");
                        message.arg1 = 0;
                        message.obj = getRawTxResult;
                        message.sendToTarget();
                    }
                });
    }
}
