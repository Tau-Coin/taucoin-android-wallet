package com.mofei.tau.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.db.greendao.TransactionHistoryDaoUtils;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.entity.req_parameter.Logout;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.res_post.UTXOList;
import com.mofei.tau.fragment.HomeFragment;
import com.mofei.tau.fragment.ManageFragment;
import com.mofei.tau.fragment.ReceiveFragment;
import com.mofei.tau.fragment.SendFragment;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.transaction.BlockChainConnector;
import com.mofei.tau.transaction.KeyValue;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;
import com.mofei.tau.util.UserInfoUtils;
import com.mofei.tau.view.CustomToolBar;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SendAndReceiveActivity extends BaseActivity implements View.OnClickListener {

    private Button logoutButton;
    private TextView userNameTV,about,help;

    private CustomToolBar mMainCustomToolBar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout layout;
    private int status;
    //声明四个Tab分别对应的Fragment
    private HomeFragment homeFragment;
    private SendFragment sendFragment;
    private ManageFragment manageFragment;
    private ReceiveFragment receiveFragment;

    private FrameLayout frameLayoutFragment;
    private RadioGroup radioGroup;
    private RadioButton homeRadioButton,sendRadioButton,manageRadioButton,receiveRadioButton;

   // private ViewPager viewPager;
    public static final int[] image={R.drawable.aa,R.drawable.ab,R.drawable.ac};
    private List<ImageView> imageViewsList;

    private Handler handler=new Handler();

    private List<UTXORecord> utxoRecordList;
    public static List<UTXOList.UtxosBean> utxo_list;
    public static double balance;
    public static double utxo;

    private String oldBalance;

    public static final int TX_CONFIRMED = 0x46;
    public static final int BALANCE_CHANGED = 0x47;
    private static Set<Handler> txConfirmedListeners = new HashSet<>();
    private static Set<Handler> balanceChangeListeners = new HashSet<>();

    private static long previousGetTime = 0;

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            L.e("SendAndReceiveActivity handler:" + msg.what);
            switch (msg.what) {
                case 0x13:
                    L.i("handler");
                    if(status==0){
                        finish();
                        SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).putBoolean("isLogin",false);
                        L.i( "logout");
                        showToast("logout successful");
                    }
                    break;

                case BlockChainConnector.GET_BALANCE_COMPLETED:
                    getBalanceHandler(msg);
                    break;
                case BlockChainConnector.GET_UTXOLIST_COMPLETED:
                    getUTXOListHandler(msg);
                    break;
                case BlockChainConnector.GET_RAW_TX_COMPLETED:
                    getRawTxHandler(msg);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_and_receive);

        initViews();
        initEvents();

        initData();
        selectTab(0);

        titleBar();

    }

    private void titleBar() {
        mMainCustomToolBar = findViewById(R.id.send_receive_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("TAUcoin");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.getLeftTextView().setText("");
        mMainCustomToolBar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(layout);
            }
        });
    }

    private void initViews() {
        frameLayoutFragment=findViewById(R.id.fragment);
        radioGroup=findViewById(R.id.tab_radiogroup);
        homeRadioButton=findViewById(R.id.home);
        sendRadioButton=findViewById(R.id.send);
        manageRadioButton=findViewById(R.id.manage);
        receiveRadioButton=findViewById(R.id.receive);
        mDrawerLayout=findViewById(R.id.drawerLayout);
        layout=findViewById(R.id.slidingMenu);

        userNameTV=findViewById(R.id.username);
        about=findViewById(R.id.about);
        help=findViewById(R.id.help);
        logoutButton=findViewById(R.id.logout);

        initIcon();

        userNameTV.setText(UserInfoUtils.getCurrentEmail(SendAndReceiveActivity.this));

    }

    private void initIcon() {

        Drawable drawable_news = getResources().getDrawable(R.drawable.selector_tab_home);
        drawable_news.setBounds(0, 0, 70, 50);
        homeRadioButton.setCompoundDrawables(null, drawable_news, null, null);
        Drawable drawable_live = getResources().getDrawable(R.drawable.selector_tab_send);
        drawable_live.setBounds(0, 0, 70, 50);
        sendRadioButton.setCompoundDrawables(null, drawable_live, null, null);
        Drawable drawable_tuijian = getResources().getDrawable(R.drawable.selector_tab_receive);
        drawable_tuijian.setBounds(0, 0, 70, 50);
        receiveRadioButton.setCompoundDrawables(null, drawable_tuijian, null, null);
        Drawable drawable_me = getResources().getDrawable(R.drawable.selector_tab_manage);
        drawable_me.setBounds(0, 0, 70, 50);
        manageRadioButton.setCompoundDrawables(null, drawable_me, null, null);
    }


    private void initData() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                view.setClickable(true);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        imageViewsList= new ArrayList<ImageView>();
        for (int i=0;i<image.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(image[i]);
            imageViewsList.add(imageView);
        }
    }
    private void initEvents() {
        homeRadioButton.setOnClickListener(this);
        sendRadioButton.setOnClickListener(this);
        manageRadioButton.setOnClickListener(this);
        receiveRadioButton.setOnClickListener(this);

        logoutButton.setOnClickListener(this);
        about. setOnClickListener(this);
        help.setOnClickListener(this);


       // showWaitDialog();
        oldBalance = SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("utxo","");
        getBalance();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                selectTab(0);
                break;
            case R.id.send:
                selectTab(1);
                break;
            case R.id.manage:
                selectTab(2);
                break;
            case R.id.receive:
                selectTab(3);
                break;
            case R.id.about_us:
                Intent aboutUsIntent = new Intent();
                aboutUsIntent.setAction(Intent.ACTION_VIEW);
                String github_url = "https://www.taucoin.io/";
                Uri git_uri = Uri.parse(github_url);
                aboutUsIntent.setData(git_uri);
                startActivity(aboutUsIntent);
                break;

            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class));
                break;

            case R.id.help:
                Intent helpintent = new Intent();
                helpintent.setAction(Intent.ACTION_VIEW);
                String bitcointalk = "https://bitcointalk.org/index.php?topic=4757879";
                Uri bitcointalk_uri = Uri.parse(bitcointalk);
                helpintent.setData(bitcointalk_uri);
                startActivity(helpintent);

                break;
            case R.id.logout:
                showWaitDialog();
                logout();
                break;
        }

    }


    public void selectTab(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment, homeFragment);
                } else {

                    transaction.show(homeFragment);
                }

                break;
            case 1:
                if (sendFragment == null) {
                    sendFragment = new SendFragment();
                    transaction.add(R.id.fragment, sendFragment);
                } else {

                    transaction.show(sendFragment);
                }
                break;
            case 2:
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fragment, manageFragment);
                } else {
                    transaction.show(manageFragment);
                }
                break;
            case 3:
                if (receiveFragment == null) {
                    receiveFragment = new ReceiveFragment();
                    transaction.add(R.id.fragment, receiveFragment);
                } else {
                    transaction.show(receiveFragment);
                }
                break;

        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (sendFragment != null) {
            transaction.hide(sendFragment);
        }
        if (manageFragment != null) {
            transaction.hide(manageFragment);
        }
        if (receiveFragment != null) {
            transaction.hide(receiveFragment);
        }

    }

    private void logout() {
        ApiService apiService= NetWorkManager.getApiService();
        Observable<Logout> observable=apiService.logout();
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Logout>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Logout logout) {

                        L.i(logout.getStatus());
                        L.i(logout.getMessage());
                        L.e("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {

                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        h.sendEmptyMessage(0x13);
                        startActivity(new Intent(SendAndReceiveActivity.this,LoginActivity2.class));
                        //  showToast("login successful");
                        L.i("onComplete");
                    }
                });
    }

    public void getBalance() {
        L.d("SendAndReceiveActivity: getBalance entry");
        long now = System.currentTimeMillis();
        if (now - previousGetTime <= 60 * 1000) {
            L.d("No need to getBalance");
            return;
        }
        previousGetTime = now;

        BlockChainConnector.getInstance().getBalanceData(UserInfoUtils.getCurrentEmail(SendAndReceiveActivity.this),
                h.obtainMessage(BlockChainConnector.GET_BALANCE_COMPLETED));
    }
    private void getBalanceHandler(Message message) {
        L.d("getBalanceHandler entry:" + message.arg1);
        if (message.arg1 == -1) {
             L.e("Can't get balance");
             hideWaitDialog();
        } else if (message.arg1 == 0) {
            String newBalance = SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("utxo","");
            if (newBalance.isEmpty()) {
                L.e("Invalid context");
            }

            L.d("Old balance:" + oldBalance + ",new Balance:" + newBalance);
            if (newBalance.equals(oldBalance)) {
                List<UTXORecord> utxoRecord= UTXORecordDaoUtils.getInstance().queryAllData();
                long sum = 0;
                for (int i=0;i<utxoRecord.size();i++){
                    long v=utxoRecord.get(i).getValue().longValue();
                    sum+=v;
                }

                Double amount_conv = new Double(Double.valueOf(newBalance)/* * Math.pow(10,8)*/);
                L.e("double"+amount_conv);
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                String amount_=nf.format(amount_conv);

                if (amount_.equals(String.valueOf(sum))) {
                    L.d("UTXO db is consistent with blockchain");
                } else {
                    BlockChainConnector.getInstance().getUTXOList(
                            h.obtainMessage(BlockChainConnector.GET_UTXOLIST_COMPLETED));
                }

            } else {
                notifyBalanceChange();
                BlockChainConnector.getInstance().getUTXOList(
                        h.obtainMessage(BlockChainConnector.GET_UTXOLIST_COMPLETED));
            }

            oldBalance = newBalance;
            hideWaitDialog();
        } else {
            L.e("getBalance unknown error");
            hideWaitDialog();
        }
    }

    private void getUTXOListHandler(Message message) {
        if (message.arg1 == -1) {
            L.e("Can't get UTXOlist");
            hideWaitDialog();
        } else if (message.arg1 == 0) {
            h.postDelayed(getRawTransationRunable, 1000);
        } else {
            L.e("get UTXOlist unknown error:" + message.arg1);
            hideWaitDialog();
        }
    }

    public void getRawTxDelay(long delay) {
        h.postDelayed(getRawTransationRunable, delay);
    }

    GetRawTransationThread getRawTransationRunable = new GetRawTransationThread();

    private class GetRawTransationThread implements Runnable{
        public void run() {
            List<TransactionHistory> unComformTX=TransactionHistoryDaoUtils.getInstance().queryTransactionHistoryByCondition();
            L.e("unComformTX.size :"+ unComformTX.size());
            if (unComformTX.size() > 0) {
                for (int i=0;i<unComformTX.size();i++){
                    Map<String,String> txid=new HashMap<>();
                    String unComformTXTxid=unComformTX.get(i).getTxId();
                    L.e("txid=>"+unComformTXTxid);
                    txid.put("txid",unComformTXTxid);
                    BlockChainConnector.getInstance().getRawTransation(txid,
                            h.obtainMessage(BlockChainConnector.GET_RAW_TX_COMPLETED));
                }
            }
        }
    }

    private void getRawTxHandler(Message message) {
        if (message.arg1 == 0) {
            BlockChainConnector.GetRawTxResult result = (BlockChainConnector.GetRawTxResult)message.obj;

            int rawTXconfirmations = result.rawTXconfirmations;
            String tx_id = result.txid;
            long blockTime = result.blockTime;

            List<TransactionHistory> transactionHistoryList=TransactionHistoryDaoUtils.getInstance().queryTransactionByName(tx_id);
            if (rawTXconfirmations > 0){
                transactionHistoryList.get(0).setResult("Successful");
                transactionHistoryList.get(0).setConfirmations(rawTXconfirmations);
                transactionHistoryList.get(0).setBlocktime(blockTime);
                TransactionHistoryDaoUtils.getInstance().insertOrReplaceData( transactionHistoryList.get(0));
            }

            if (rawTXconfirmations < 2) {
                L.d("Continue get raw tx:" + rawTXconfirmations);
                h.postDelayed(getRawTransationRunable, 60000);
            } else {
                L.d("Stop get raw tx:" + rawTXconfirmations + ", txid:" + tx_id);
                notifyTxConfirmed(tx_id, rawTXconfirmations);
                hideWaitDialog();
            }
            L.d("getRawTransation Successful");
        } else if (message.arg1 == -1) {
            L.e("Get raw tx error, try again");
            h.postDelayed(getRawTransationRunable, 1000);
        } else {
            L.e("Get raw tx unknown error:" + message.arg1);
            hideWaitDialog();
        }
    }

    public static void registerTxConfirmedListener(Handler listener) {
        txConfirmedListeners.add(listener);
    }

    public static void removeTxConfirmedListener(Handler listener) {
        txConfirmedListeners.remove(listener);
    }

    private static void notifyTxConfirmed(String txId, int confirmations) {
        for (Handler listner : txConfirmedListeners) {
            Message message = listner.obtainMessage(TX_CONFIRMED, confirmations, 0, txId);
            message.sendToTarget();
        }
    }

    public static void registerBalanceChangeListener(Handler listener) {
        balanceChangeListeners.add(listener);
    }

    public static void removeBalanceChangeListener(Handler listener) {
        balanceChangeListeners.remove(listener);
    }

    private static void notifyBalanceChange() {
        for (Handler listener : balanceChangeListeners) {
            Message message = listener.obtainMessage(BALANCE_CHANGED);
            message.sendToTarget();
        }
    }
}
