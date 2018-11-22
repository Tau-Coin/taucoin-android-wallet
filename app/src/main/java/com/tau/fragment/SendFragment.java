package com.mofei.tau.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.mofei.tau.R;
import com.mofei.tau.activity.BalanceActivity;
import com.mofei.tau.activity.HistoryTransationActivity;
import com.mofei.tau.activity.KeysAddressesActivity;
import com.mofei.tau.activity.MainActivity;
import com.mofei.tau.activity.SendAndReceiveActivity;
import com.mofei.tau.activity.TextActivity;

import com.mofei.tau.db.greendao.TransactionHistoryDao;
import com.mofei.tau.db.greendao.TransactionHistoryDaoUtils;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.entity.FirstEvent;
import com.mofei.tau.entity.MessageEvent;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.res_post.HexRet;
import com.mofei.tau.entity.res_post.RawTX;
import com.mofei.tau.entity.res_post.UTXOList;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.src.com.google.bitcoin.core.AddressFormatException;
import com.mofei.tau.src.com.google.bitcoin.core.ECKey;
import com.mofei.tau.src.com.google.bitcoin.core.NetworkParameters;
import com.mofei.tau.src.com.google.bitcoin.core.Utils;
import com.mofei.tau.src.io.taucoin.android.wallet.Wallet;
import com.mofei.tau.src.io.taucoin.android.wallet.keystore.KeyStore;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.CreateTransactionResult;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.Transaction;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.TransactionFailReason;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;
import com.mofei.tau.util.MD5_BASE64Util;
import com.mofei.tau.view.DialogWaitting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * create an instance of this fragment.
 */
public class SendFragment extends Fragment{
    private EditText amountET,addressET;
    //private CardView sendCV;
    private Button sendBT;
    private RelativeLayout historyRl;
    //private double balance;
    private DialogWaitting mWaitDialog = null;
    private Toast mToast = null;
    private String to_address;
    private TextView mBalanceTauTV;
    private List<UTXORecord> utxoRecordList;
    private List<UTXOList.UtxosBean> list;
    private String txid_from_tx;
    private String errorMessg;
    private String get_txid_after_sendTX;
    private String amount;
    private double double_coins;
    private double reward;
    private int status;
    AlertDialog dialog;
    private TransactionHistory transactionHistory;
    private LinearLayout balanceHoneLL;


    Handler handler_ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x33:
                    if(get_txid_after_sendTX.equals(SharedPreferencesHelper.getInstance(getActivity()).getString("txid","txid"))){
                        //showToast("the transaction has reached the trading pool, waiting for the result.");
                        showToast("success");
                    }
                    L.e("进入定时器　链端正在确认　片刻后得到结果");
                    dialog.show();
                    TimerTask task = new TimerTask(){
                        public void run(){
                            //execute the task
                            //查询comformation<2的txid数据发送
                            List<TransactionHistory> unComformTX=TransactionHistoryDaoUtils.getInstance().queryTransactionHistoryByParams("where CONFIRMATIONS<?", String.valueOf(2));
                           // showToast("The number of unconfirmed transactions is:"+unComformTX.size());
                            for (int i=0;i<unComformTX.size();i++){
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                getRawTransation();
                               /*new Timer().schedule(new TimerTask() {
                                   @Override
                                   public void run() {
                                       getRawTX();
                                   }
                               },2000);
                               */
                               L.e("comformation<2的txid个数"+i);
                               L.e("unComformTX="+unComformTX.get(i).getConfirmations());
                            }
                            dialog.dismiss();
                        }
                    };
                    Timer timer = new Timer();
                    //60s以后getRawTransation();
                    timer.schedule(task, 120000);

                    break;
                case 0x34:
                    showToast("The transaction has been confirmed.");
                    break;
                case 0x35:
                    if(errorMessg.equals("258: txn-mempool-conflict")){
                        showToast("Failure!transactions are too frequent,Try again.");
                        L.e("进入258: txn-mempool-conflict");
                    }
                    if(errorMessg.equals("18: bad-txns-inputs-spent")){
                      //  new SendAndReceiveActivity().getBalanceData(SharedPreferencesHelper.getInstance(getActivity()).getString("email",""));
                        showToast("Failure!utxo is inconsistent with the chain,Please update the utxo.");
                        L.e("进入bad-txns-inputs-spent");
                    }
                   // showToast("Send transaction failure");
                    //更新交易历史数据库的这条记录
                    TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
                    transactionHistory.setResult(false);
                    transactionHistory.setMessage(errorMessg);
                    TransactionHistoryDaoUtils.getInstance().insertOrReplaceData(transactionHistory);
                    break;

                case 0x20:
                    if(status==401){
                        showToast("Email does not exist");
                    }else if(status==402){
                        showToast("Fail to get balance");
                    }
                    break;
/*
                case 0x21:
                    Double double_8=new Double("100000000");
                    Double coin_double=new Double(double_coins);
                    L.e("转换后的数据：　"+coin_double/double_8);
                    mBalanceTauTV.setText(""+coin_double/double_8);
                    break;*/
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_send, container, false);

        mBalanceTauTV=view.findViewById(R.id.balance_text);
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intiEvent(view);

        dialog = new AlertDialog.Builder(getActivity())//设置标题的图片.setTitle("我是对话框")//设置对话框的标题
                .setMessage("The chain end is being confirmed, and the results will be obtained after a short time.")//设置对话框的内容
                .create();
    }


    //接收homeFragment 传来的数据
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(FirstEvent data){
        L.e("onEvent:"+data.getMsg());
        mBalanceTauTV.setText(data.getMsg());
    }


    @Override
    public void onResume() {
        super.onResume();

        //showWaitDialog();
       // getBalanceData(SharedPreferencesHelper.getInstance(getActivity()).getString("email",""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void intiEvent(View view) {
        amountET=view.findViewById(R.id.amount);
        addressET=view.findViewById(R.id.address);
        sendBT=view.findViewById(R.id.send_tau);
        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getBalanceData(email);

                afterGetBalanceUpdateUTXOToCreateTX();
            }
        });

       /* historyRl=view.findViewById(R.id.history);
        historyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HistoryTransationActivity.class));
            }
        });*/
    }

    private void createTransation() {
        amount=amountET.getText().toString().trim();
        to_address=addressET.getText().toString().trim();

        if (to_address == null || to_address.length() == 0) {
            showToast("please enter your address");
            return;
        }
        if (amount == null || amount.length() == 0) {
            showToast("please enter your amount");
            return;
        }

        to_address="TTLUavJdzySpS6omCfmpyoNHKozXvB5xYq";
        Double amount_conv = new Double( Double.valueOf(amount) * Math.pow(10,8));
        L.e("double"+amount_conv);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String amount_=nf.format(amount_conv);
        L.e("乘以１０的８次方后的　"+amount_);

        String PrivKey= SharedPreferencesHelper.getInstance(getActivity()).getString("Privkey","私钥为空");
        Log.e("PrivKey",PrivKey);
        if(PrivKey==null){
            return;
        }
        String newPrivKeyStr = null;
        try {
            newPrivKeyStr = Utils.convertWIFPrivkeyIntoPrivkey(PrivKey);
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
            return;
        }
        ECKey key = new ECKey(new BigInteger(newPrivKeyStr, 16));
        L.e("Compressed key hash:" + Utils.bytesToHexString(key.getCompressedPubKeyHash()));
        KeyStore.getInstance().addKey(key);
        HashMap<String, BigInteger> receipts = new HashMap<String, BigInteger>();
        receipts.put(to_address, new BigInteger(amount_, 10));
        Wallet wallet = Wallet.getInstance();
        Transaction tx = new Transaction(NetworkParameters.mainNet());
        CreateTransactionResult result = wallet.createTransaction(receipts, false, null, tx);
        if (result.failReason == TransactionFailReason.NO_ERROR) {

            L.e("Create tx success");
            L.e("构建的交易tx如下: ");
            L.e(tx.toString());
            txid_from_tx=tx.getHashAsString();
            SharedPreferencesHelper.getInstance(getActivity()).putString("txid",txid_from_tx);
            //插入数据到TransactionHistoryDB
            list=SendAndReceiveActivity.utxo_list;

            transactionHistory=new TransactionHistory();
            transactionHistory.setTxId(txid_from_tx);
            transactionHistory.setConfirmations(0);
            // transactionHistory.setResult(false);
            transactionHistory.setToAddress(to_address);
           // transactionHistory.setValue(amount);
            //transactionHistory.setBlockheight(list.get(0).getBlockheight());
           // transactionHistory.setBlocktime(list.get(0).getBlocktime());
            TransactionHistoryDaoUtils.getInstance().insertTransactionHistoryData(transactionHistory);

            L.e("txid="+ txid_from_tx);
           /* try {
                JSONObject jsonObject = new JSONObject(tx.toString());
                L.e("jsonObject转化为字符串："+jsonObject.toString());
                L.e("取出txid的值："+jsonObject.getString("txid"));
                SharedPreferencesHelper.getInstance(getActivity()).putString("txid",jsonObject.getString("txid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            L.e("转化成１６进制字符串的交易（tx.dumpIntoHexStr）:"+tx.dumpIntoHexStr());
            String hex_after_base64= MD5_BASE64Util.EncoderByMd5_BASE64(tx.dumpIntoHexStr());
            L.e("经过BASE64加密之后的交易（tx.dumpIntoHexStr_after_base64）: "+hex_after_base64);
            showWaitDialog("create a transaction...");
            //sendTransation
            sendRawTransation(hex_after_base64);
        } else {
            L.e("Create tx failed");
            L.e("error code:" + result.failReason.getCode() + ", " + result.failReason.getMsg());
            hideWaitDialog();
        }
    }

    private void afterGetBalanceUpdateUTXOToCreateTX() {
        String utxo= String.valueOf(SendAndReceiveActivity.utxo);
        L.e("utxo="+utxo);
        String balance=String.valueOf(SendAndReceiveActivity.balance);
        L.e("balance="+balance);
        if (balance.equals(utxo)){
            L.e("进ｉｆ");
            List<UTXORecord> utxoRecord= UTXORecordDaoUtils.getInstance().queryAllData();
            long sum = 0;
            L.e("utxoRecord对象个数: "+utxoRecord.size());
            for (int i=0;i<utxoRecord.size();i++){
                long v=utxoRecord.get(i).getValue().longValue();
                sum+=v;
                L.e("进ｆｏｒ循环");
            }
            L.e("sum:"+sum);
            L.e("conver="+conv2str(utxo));
            L.e("sum转化为字符串="+String.valueOf(sum));
            if (conv2str(utxo).equals(String.valueOf(sum))){
                createTransation();
                L.e("求和ｖａｌｕｅ与ｕｘｔｏ比较后构建ｔｘ并发送");
            }else {
                showWaitDialog();
                getUTXOList();
            }

        }else {
            showWaitDialog();
            getUTXOList();
            L.e("进ｅｌｓｅ");
        }
    }

    private void getUTXOList() {

        Map<String,String> address=new HashMap<>();

        String addre=SharedPreferencesHelper.getInstance(getActivity()).getString("Address","Address");
        L.e("getUTXOList_Address: "+addre);
        String pub=SharedPreferencesHelper.getInstance(getActivity()).getString("Pubkey","Pubkey");
        L.e("getUTXOList_publ: "+pub);
        String Priv=SharedPreferencesHelper.getInstance(getActivity()).getString("Privkey","Privkey");
        L.e("getUTXOList_Privkey: "+Priv);

        address.put("address",addre);
        ApiService apiService=NetWorkManager.getApiService();
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
                        list=utxoListRetUTXOList.getUtxosX();
                        L.e("getUtxosX的个数",list.size()+"");
                        for (int i=0;i<list.size();i++){

                            L.e("========================"+i);

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
                            L.e(""+utxoListRetUTXOList.getUtxosX().get(i).getValue());
                            //把5.00000000转化成50000000
                            Double d = new Double(utxoListRetUTXOList.getUtxosX().get(i).getValue())*Math.pow(10,8);
                            L.e("double"+d);
                            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                            nf.setGroupingUsed(false);
                            L.e("d:="+nf.format(d));
                            utxoRecord.setValue( new BigInteger(nf.format(d)));
                            utxoRecordList.add(utxoRecord);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete");
                        UTXORecordDaoUtils.getInstance().deleteAllData();
                        UTXORecordDaoUtils.getInstance().insertOrReplaceMultiData(utxoRecordList);
                        hideWaitDialog();
                    }
                });
    }

    private String conv2str(String str) {
     Double d = new Double(str);
     L.e("double="+d);
     java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
     nf.setGroupingUsed(false);
     return nf.format(d);
    }


    private void sendRawTransation(String tx_hex) {
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
                        L.e(hexRet.getStatus());
                        L.e("getMessage="+hexRet.getMessage());
                        errorMessg=hexRet.getMessage();
                        get_txid_after_sendTX=hexRet.getRet();
                        L.e(get_txid_after_sendTX);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                        handler_.sendEmptyMessage(0x35);

                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete");
                        hideWaitDialog();
                        handler_.sendEmptyMessage(0x33);
                    }
                });
    }


    private void getRawTransation() {

        Map<String,String> txid=new HashMap<>();

        String strt_txid=SharedPreferencesHelper.getInstance(getActivity()).getString("txid","txid");
        L.e("txid:"+strt_txid);
        txid.put("txid",strt_txid);
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

                        L.e("Message: "+rawTX.getMessage());
                        L.e("Status: "+rawTX.getStatus());
                        L.e("getConfirmations: "+rawTX.getRet().getConfirmations());

                        //list=SendAndReceiveActivity.utxo_list;
                       // TransactionHistory transactionHistory=new TransactionHistory();
                        List<TransactionHistory> transactionHistoryList=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx);

                        transactionHistoryList.get(0).setConfirmations(rawTX.getRet().getConfirmations());
                        transactionHistoryList.get(0).setValue(amount);

                        int blockTime=rawTX.getRet().getBlocktime();
                        L.e("blockTime:"+blockTime);
                        transactionHistoryList.get(0).setBlocktime(blockTime);


                       /* Calendar calendar=Calendar.getInstance();
                        int year=calendar.get(Calendar.YEAR);
                        int month=calendar.get(Calendar.MONTH)+1;
                        int day=calendar.get(Calendar.DAY_OF_MONTH);
                        int hour= calendar.get(Calendar.HOUR_OF_DAY);
                        int minute=calendar.get(Calendar.MINUTE);
                        int second=calendar.get(Calendar.SECOND);

                        String time=month+", "+day+", "+year+", "+hour+":"+minute+":"+second;
                        L.e("time"+time);
                        transactionHistoryList.get(0).setTime(time);*/
                        transactionHistoryList.get(0).setResult(true);
                        //transactionHistory.setTxId(txid_from_tx);
                        //transactionHistory.setToAddress(to_address);
                       // TransactionHistoryDaoUtils.getInstance().deleteAllData();
                        TransactionHistoryDaoUtils.getInstance().insertOrReplaceData( transactionHistoryList.get(0));

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        L.e("onerror");
                    }

                    @Override
                    public void onComplete() {

                        //handler_.sendEmptyMessage(0x34);
                        L.e("getRawTX_onComplete");


                       // showToast("Successful trade");
                    }
                });
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
                        status=balanceRetBalance.getStatus();
                        double_coins=balanceRetBalance.getRet().getCoins();
                        reward= balanceRetBalance.getRet().getRewards();

                        SharedPreferencesHelper.getInstance(getActivity()).putString("balance",""+double_coins);
                        SharedPreferencesHelper.getInstance(getActivity()).putString("reward",""+reward);
                        SharedPreferencesHelper.getInstance(getActivity()).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.e("error");
                        e.printStackTrace();
                        handler_.sendEmptyMessage(0x20);
                    }

                    @Override
                    public void onComplete() {
                        handler_.sendEmptyMessage(0x21);
                        hideWaitDialog();
                        L.e("complete");
                    }
                });
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public DialogWaitting showWaitDialog() {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(getActivity());
        }
        mWaitDialog.show();
        return mWaitDialog;
    }

    public void showWaitDialog(String text) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(getActivity());
        }
        mWaitDialog.show(text);
    }

    public void hideWaitDialog() {
        if (null != mWaitDialog) {
            mWaitDialog.dismiss();
        }
    }

    public void showToast(String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }


}
