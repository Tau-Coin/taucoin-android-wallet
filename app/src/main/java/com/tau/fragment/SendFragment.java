package com.mofei.tau.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.InputType;
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
import com.google.bitcoin.core.Sha256Hash;
import com.google.gson.JsonObject;
import com.mofei.tau.R;

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
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;
import io.taucoin.android.wallet.Wallet;
import io.taucoin.android.wallet.keystore.KeyStore;
import io.taucoin.android.wallet.transactions.CreateTransactionResult;
import io.taucoin.android.wallet.transactions.Transaction;
import io.taucoin.android.wallet.transactions.TransactionFailReason;

import com.mofei.tau.transaction.BlockChainConnector;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.EditTextJudgeNumberWatcher;
import com.mofei.tau.util.L;
import com.mofei.tau.util.MD5_BASE64Util;
import com.mofei.tau.util.UserInfoUtils;
import com.mofei.tau.view.DialogWaitting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private String get_txid_after_sendTX;
    private String amount;
    private double double_coins;
    private double reward;
    private int status;
    AlertDialog dialog;
    private TransactionHistory transactionHistory;
    private LinearLayout balanceHoneLL;
    private String errorMessage;
    private String hexRetStatus;
    private String errorMessg;
    private  int confirmations;
    private String rawTXStatus;
    private String rawTXMessage;
    private int rawTXconfirmations;
    private int  blockTime;
    private String txid_;
    private String unComformTXTxid;
    private String tx_id;

    private Activity activity;

   // private Handler handler = new Handler();;

    public static final int RAW_TRANSACTION_COMFIRMED = 0x45;

    @SuppressLint("HandlerLeak")
    public Handler handler_ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x20:
                    if(status==401){
                        showToast("Email does not exist");
                    }else if(status==402){
                        showToast("Fail to get balance");
                    }
                    break;
                case BlockChainConnector.SEND_RAW_TX_COMPLETED:
                    onSendRawTxHandler(msg);
                    break;
                case RAW_TRANSACTION_COMFIRMED:
                    break;
                case SendAndReceiveActivity.TX_CONFIRMED:
                    onTxConfirmedHandler(msg);
                    break;
                case BlockChainConnector.GET_BALANCE_COMPLETED:
                    L.d("Update balance finished after sending raw tx");
                    break;
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

        activity = this.getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intiEvent(view);

        dialog = new AlertDialog.Builder(activity)//设置标题的图片.setTitle("我是对话框")//设置对话框的标题
                .setMessage("The chain end is being confirmed, and the results will be obtained after a short time.")//设置对话框的内容
                .create();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(FirstEvent data){
        L.e("onEvent:"+data.getMsg());
        mBalanceTauTV.setText(data.getMsg());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void intiEvent(View view) {
        amountET=view.findViewById(R.id.amount);
        amountET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amountET.addTextChangedListener(new EditTextJudgeNumberWatcher(amountET));
        addressET=view.findViewById(R.id.address);
        sendBT=view.findViewById(R.id.send_tau);
        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitTransaction();
            }
        });

        // Sync balance and UTXOlist
        if (!Wallet.getInstance().isAnyTxPending()) {
            ((SendAndReceiveActivity)this.activity).getBalance();
        }
        SendAndReceiveActivity.registerTxConfirmedListener(handler_);
    }

    //第一步更新Balance和UTXO
    private void onSubmitTransaction() {

        if (Wallet.getInstance().isAnyTxPending()) {
            showToast("Previous transaction is under confirmation");
            return;
        }

        amount = amountET.getText().toString().trim();
        to_address = addressET.getText().toString().trim();
        if (to_address == null || to_address.length() == 0) {
            showToast("Please enter address");
            return;
        }

        if (!isAddressValid(to_address)){
            showToast("Incorrect address");
            return;
        }

        SharedPreferencesHelper.getInstance(activity).putString("to_address",to_address);
        if (amount == null || amount.length() == 0) {
            showToast("Please enter your amount");
            return;
        }

        /*if (!isNumeric(amount)) {
            showToast("Illegal amount");
            return;
        }*/

        Double amount_conv = new Double( Double.valueOf(amount) * Math.pow(10,8));
        L.e("double"+amount_conv);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String amount_=nf.format(amount_conv);
        L.e("乘以１０的８次方后的　"+amount_);

        String balanceStr = SharedPreferencesHelper.getInstance(this.activity).getString("balance","");
        double totalBalance = Double.valueOf(balanceStr);
        L.d("balanceStr :"+balanceStr);
        if (Double.valueOf(amount_) >= totalBalance) {
            L.d("balanceStr=====");
            showToast("Balance is not enough");
            return;
        }

        String privKey = UserInfoUtils.getPrivateKey(activity);
        if (privKey.isEmpty()) {
            showToast("Please import your private key");
            return;
        }

        createTransation(amount_);
        amountET.setText("");
        addressET.setText("");

    }

    public static boolean isNumeric(String str){
           for (int i = str.length();--i>=0;){
               if (!Character.isDigit(str.charAt(i))){
                   return false;
               }
           }
            return true;
    }
    //第二步构建交易
    private void createTransation(String amount_) {
        String PrivKey= UserInfoUtils.getPrivateKey(activity);
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
            L.e(tx.toString());
            txid_from_tx=tx.getHashAsString();
            SharedPreferencesHelper.getInstance(activity).putString("txid",txid_from_tx);
            //list=SendAndReceiveActivity.utxo_list;

            transactionHistory=new TransactionHistory();
            transactionHistory.setTxId(txid_from_tx);
            transactionHistory.setConfirmations(0);
            transactionHistory.setResult("sending");
            // transactionHistory.setResult(false);
            transactionHistory.setToAddress(to_address);
            transactionHistory.setValue(amount);
            transactionHistory.setTime("");
            //transactionHistory.setBlockheight(list.get(0).getBlockheight());
            // transactionHistory.setBlocktime(list.get(0).getBlocktime());
            TransactionHistoryDaoUtils.getInstance().insertTransactionHistoryData(transactionHistory);
            Wallet.getInstance().addPendingTx(tx);

            L.e("txid="+ txid_from_tx);

            L.e("tx.dumpIntoHexStr:"+tx.dumpIntoHexStr());
            String hex_after_base64= MD5_BASE64Util.EncoderByMd5_BASE64(tx.dumpIntoHexStr());
            L.e("tx.dumpIntoHexStr_after_base64: "+hex_after_base64);
            showWaitDialog("create a transaction...");
            //sendTransation
            BlockChainConnector.getInstance().sendRawTransation(hex_after_base64, handler_.obtainMessage(BlockChainConnector.SEND_RAW_TX_COMPLETED));
            showToast("send successfully");
        } else {
            L.e("Create tx failed");
            L.e("error code:" + result.failReason.getCode() + ", " + result.failReason.getMsg());
            errorMessage=result.failReason.getMsg();
            showToast(errorMessage);
            hideWaitDialog();
        }
    }

    private void onSendRawTxHandler(Message message) {
        if (message.arg1 == 0) {
            BlockChainConnector.SendRawTxResult result = (BlockChainConnector.SendRawTxResult)message.obj;

            hexRetStatus = result.hexRetStatus;
            errorMessg = result.hexRetMessage;

            if (hexRetStatus.equals("0")){
                L.e("Enter the timer chain and confirm for a moment before getting the result");
                //dialog.show();

                TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
                transactionHistory.setResult("Confirming");
                TransactionHistoryDaoUtils.getInstance().insertOrReplaceData(transactionHistory);

                ((SendAndReceiveActivity)this.activity).getRawTxDelay(60000);
            }else if(hexRetStatus.equals("401")){
                TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
                transactionHistory.setResult("Failed");
                transactionHistory.setMessage(result.hexRetMessage);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                transactionHistory.setTime(date);
                showToast(errorMessg);
                L.e("链端返回401");
            }else if(hexRetStatus.equals("402")){
                TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
                transactionHistory.setResult("Failed");
                transactionHistory.setMessage(result.hexRetMessage);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                transactionHistory.setTime(date);
                L.e("链端返回40２");
                showToast(errorMessg);
            }
            hideWaitDialog();
        } else if (message.arg1 == -1) {
            L.e("Send raw tx error:" + message.arg1);
            hideWaitDialog();
            TransactionHistory transactionHistory=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(txid_from_tx).get(0);
            transactionHistory.setResult("Failed");
            transactionHistory.setMessage("Error in network, send failed");
            TransactionHistoryDaoUtils.getInstance().insertOrReplaceData(transactionHistory);
            hideWaitDialog();
        } else {
            L.e("Send raw tx unknown error:" + message.arg1);
            hideWaitDialog();
        }
    }

    private void onTxConfirmedHandler(Message msg) {
        L.d("tx confirmed:" + msg.obj + ", confirmed:" + msg.arg1);
        if (msg.arg1 >= 2) {
            Wallet.getInstance().removePendingTx(new Sha256Hash((String)msg.obj));
            ((SendAndReceiveActivity)this.activity).getBalance();
        }
    }

    private String conv2str(String str) {
        Double d = new Double(str);
        L.e("double="+d);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    public boolean isAddressValid(String address) {
        // String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String regex = "^T[a-zA-Z0-9_]{33,}$";
        return address.matches(regex);
    }

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
            mWaitDialog = new DialogWaitting(activity);
        }
        mWaitDialog.show();
        return mWaitDialog;
    }

    public void showWaitDialog(String text) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(activity);
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
            mToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}