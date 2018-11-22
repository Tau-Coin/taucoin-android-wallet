package com.mofei.tau.activity;

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
import com.mofei.tau.transaction.KeyValue;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;
import com.mofei.tau.view.CustomToolBar;

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


public class SendAndReceiveActivity extends BaseActivity implements View.OnClickListener {

    private Button balanceButton,harvestClubButton,keyAddressesButton,bountyButton,sendReceiveButton,logoutButton,buyCoinsButton,abourtUs;
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


    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x13:
                    L.i("handler");
                    if(status==0){
                        finish();
                        SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).putBoolean("isLogin",false);
                        L.i( "logout");
                        // startActivity(new Intent(MainActivity.this,LoginActivity2.class));
                        showToast("logout successful");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_and_receive);


        initViews();//初始化控件
        initEvents();//初始化事件

        initData();
        selectTab(0);//默认选中第一个Tab

        titleBar();

    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.send_receive_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("TAUcoin");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.getLeftTextView().setText("");
        mMainCustomToolBar.getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("点击菜单栏");
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

        abourtUs=findViewById(R.id.about_us);
        userNameTV=findViewById(R.id.username);
        about=findViewById(R.id.about);
        help=findViewById(R.id.help);
        logoutButton=findViewById(R.id.logout);

        initIcon();

        userNameTV.setText(SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("user_nane",""));

    }

    private void initIcon() {
        //定义底部标签图片大小和位置
        Drawable drawable_news = getResources().getDrawable(R.drawable.selector_tab_home);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_news.setBounds(0, 0, 70, 50);
        //设置图片在文字的哪个方向
        homeRadioButton.setCompoundDrawables(null, drawable_news, null, null);
        //定义底部标签图片大小和位置
        Drawable drawable_live = getResources().getDrawable(R.drawable.selector_tab_send);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_live.setBounds(0, 0, 70, 50);
        //设置图片在文字的哪个方向
        sendRadioButton.setCompoundDrawables(null, drawable_live, null, null);
        //定义底部标签图片大小和位置
        Drawable drawable_tuijian = getResources().getDrawable(R.drawable.selector_tab_receive);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_tuijian.setBounds(0, 0, 70, 50);
        //设置图片在文字的哪个方向
        receiveRadioButton.setCompoundDrawables(null, drawable_tuijian, null, null);
        //定义底部标签图片大小和位置
        Drawable drawable_me = getResources().getDrawable(R.drawable.selector_tab_manage);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_me.setBounds(0, 0, 70, 50);
        //设置图片在文字的哪个方向
        manageRadioButton.setCompoundDrawables(null, drawable_me, null, null); }


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
        //初始化四个Tab的点击事件
        homeRadioButton.setOnClickListener(this);
        sendRadioButton.setOnClickListener(this);
        manageRadioButton.setOnClickListener(this);
        receiveRadioButton.setOnClickListener(this);

        logoutButton.setOnClickListener(this);
        about. setOnClickListener(this);
        help.setOnClickListener(this);


        showWaitDialog();
        //getUTXOList();
        getBalanceData(SharedPreferencesHelper.getInstance(this).getString("email",""));

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
           /* case R.id.send_receive_back_wallet_home:
                finish();
                break;*/
           /* case R.id.transaction_logout:

                break;*/
        }

    }

    //进行选中Tab的处理
    public void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment, homeFragment);
                } else {
                    //如果微信对应的Fragment已经实例化，则直接显示出来
                    transaction.show(homeFragment);
                }
                break;
            case 1:

                if (sendFragment == null) {
                    sendFragment = new SendFragment();
                    transaction.add(R.id.fragment, sendFragment);
                } else {
                    //如果微信对应的Fragment已经实例化，则直接显示出来
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
        //不要忘记提交事务
        transaction.commit();
    }

    //将san个的Fragment隐藏
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


    public void getBalanceData(String email) {
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
                        SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).putString("balance",""+balanceRetBalance.getRet().getCoins());
                        SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).putString("reward",""+balanceRetBalance.getRet().getRewards());
                        SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        //balance,reward,utxo插入KeyBD
                        KeyValue key=new KeyValue();
                        key.setPubkey(SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("Pubkey","Pubkey"));
                        key.setPrivkey(SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("Privkey","Privkey"));
                        key.setPubkey(SharedPreferencesHelper.getInstance(SendAndReceiveActivity.this).getString("Address","Address"));
                        key.setUtxo((long) balanceRetBalance.getRet().getUtxo());
                        key.setBalance((long) balanceRetBalance.getRet().getCoins());
                        key.setReward((long) balanceRetBalance.getRet().getRewards());

                      //  KeyDaoUtils.getInstance().deleteAllData();
                      //  KeyDaoUtils.getInstance().insertKeyStoreData(key);

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

                    }

                    @Override
                    public void onComplete() {

                        getUTXOList();

                        L.e("complete");
                    }
                });
    }

    public void getUTXOList() {
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

                        hideWaitDialog();
                        L.e("onError");
                        e.printStackTrace();
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
}
