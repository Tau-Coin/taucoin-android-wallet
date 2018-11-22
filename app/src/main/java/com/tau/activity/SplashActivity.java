package com.mofei.tau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mofei.tau.R;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.res_post.UTXOList;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;

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


public class SplashActivity extends BaseActivity {

    private List<UTXORecord> utxoRecordList;
    public static List<UTXOList.UtxosBean> utxo_list;
    public static double balance;
    public static double utxo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置全屏幕
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //初始化数据库的数据
      //  getBalanceData(SharedPreferencesHelper.getInstance(this).getString("email",""));

        //new个Handler
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashJump();
            }
        }, 3000);//3秒后跳转

        //初始化keydatabase
        /*Key key=new Key();
        key.setPubkey(SharedPreferencesHelper.getInstance(this).getString("Pubkey","Pubkey"));
        key.setPrivkey(SharedPreferencesHelper.getInstance(this).getString("Privkey","Privkey"));
        key.setAddress(SharedPreferencesHelper.getInstance(this).getString("Address","Address"));
        key.setUtxo(new BigInteger(SharedPreferencesHelper.getInstance(this).getString("utxo","utxo")));
        key.setBalance(new BigInteger(SharedPreferencesHelper.getInstance(this).getString("balance","balance")));
        key.setReward(new BigInteger(SharedPreferencesHelper.getInstance(this).getString("reward","reward")));
        KeyDaoUtils.getInstance().insertKeyStoreData(key);*/

    }

    //跳转
    private void splashJump() {
        boolean isLogin= SharedPreferencesHelper.getInstance(this).getBoolean("isLogin",false);
        if (isLogin==true){
            Intent intent = new Intent(this, SendAndReceiveActivity.class);
            startActivity(intent);
            L.i("SendAndReceiveActivity");
        }else {
            Intent intent = new Intent(this, LoginActivity2.class);
            startActivity(intent);
            L.i("LoginActivity");
        }

       // finish();
    }

    /*public void getBalanceData(String email) {
        Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        ApiService apiService=NetWorkManager.getApiService();
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
                        SharedPreferencesHelper.getInstance(SplashActivity.this).putString("balance",""+balanceRetBalance.getRet().getCoins());
                        SharedPreferencesHelper.getInstance(SplashActivity.this).putString("reward",""+balanceRetBalance.getRet().getRewards());
                        SharedPreferencesHelper.getInstance(SplashActivity.this).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        //balance,reward,utxo插入KeyBD
                        KeyValue key=new KeyValue();
                        key.setPubkey(SharedPreferencesHelper.getInstance(SplashActivity.this).getString("Pubkey","Pubkey"));
                        key.setPrivkey(SharedPreferencesHelper.getInstance(SplashActivity.this).getString("Privkey","Privkey"));
                        key.setPubkey(SharedPreferencesHelper.getInstance(SplashActivity.this).getString("Address","Address"));
                        key.setUtxo((long) balanceRetBalance.getRet().getUtxo());
                        key.setBalance((long) balanceRetBalance.getRet().getCoins());
                        key.setReward((long) balanceRetBalance.getRet().getRewards());

                        KeyDaoUtils.getInstance().deleteAllData();
                        KeyDaoUtils.getInstance().insertKeyStoreData(key);

                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");

                    }

                    @Override
                    public void onError(Throwable e) {
                       // hideWaitDialog();
                        L.e("error");
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                        getUTXOList();

                        L.e("complete");
                    }
                });
    }*/

    /*private void getUTXOList() {
        Map<String,String> address=new HashMap<>();
        String addre=SharedPreferencesHelper.getInstance(this).getString("Address","Address");
        L.e("getUTXOList_Address: "+addre);
        String pub=SharedPreferencesHelper.getInstance(this).getString("Pubkey","Pubkey");
        L.e("getUTXOList_publ: "+pub);
        String Priv=SharedPreferencesHelper.getInstance(this).getString("Privkey","Privkey");
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
                            //scriptPubkey.setReqSigs(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getReqSigs());
                            // scriptPubkey.setType(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getType());
                            // scriptPubkey.setAddress(utxoListRetUTXOList.getUtxosX().get(i).getScriptPubKey().getAddresses().get(0));
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

                       // hideWaitDialog();
                        L.e("onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete");
                        UTXORecordDaoUtils.getInstance().deleteAllData();
                        UTXORecordDaoUtils.getInstance().insertOrReplaceMultiData(utxoRecordList);
                       // hideWaitDialog();
                    }
                });
    }*/

}
