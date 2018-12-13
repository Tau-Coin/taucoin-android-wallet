package com.tau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mofei.tau.R;
import com.tau.entity.req_parameter.Logout;
import com.tau.entity.res_post.Balance;
import com.tau.entity.res_post.BalanceRet;
import com.tau.info.SharedPreferencesHelper;
import com.tau.info.key_address.taucoin.KeyGenerator;

import io.taucoin.android.wallet.net.callBack.TAUObserver;
import io.taucoin.android.wallet.net.service.ApiService;
import io.taucoin.foundation.net.NetWorkManager;
import com.tau.util.L;
import com.tau.view.CustomToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button balanceButton,harvestClubButton,keyAddressesButton,bountyButton,sendReceiveButton,logoutButton,buyCoinsButton,abourtUs;

    Button test;

    private TextView userNameTV,about,help;

    private static KeyGenerator instance;

    private CustomToolBar mMainCustomToolBar;

    private DrawerLayout mDrawerLayout;

    private RelativeLayout layout;

    private TextView mBalanceTauTV;

    private double double_coins;

    private double reward;

    private int status;


    private ViewPager viewPager;
    public static final int[] image={R.drawable.a,R.drawable.b,R.drawable.c};
    private List<ImageView> imageViewsList;
    private List<ImageView> imageViewsDotList;
    private Handler handler=new Handler();

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x13:
                    L.i("handler");
                    if(status==0){
                        finish();
                        SharedPreferencesHelper.getInstance(MainActivity.this).putBoolean("isLogin",false);
                        L.i( "logout");
                       // startActivity(new Intent(MainActivity.this,LoginActivity2.class));
                        showToast("logout successful");
                    }
                    break;

                case 0x20:
                    if(status==401){
                        showToast("Email does not exist");
                    }else if(status==402){
                        showToast("Fail to get balance");
                    }
                    break;
                case 0x21:
                    Double double_8=new Double("100000000");
                    Double coin_double=new Double(double_coins);
                    L.e("转换后的数据：　"+coin_double/double_8);
                    mBalanceTauTV.setText(""+coin_double/double_8);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    private void initView() {
       // balanceButton=findViewById(R.id.balance);
        harvestClubButton=findViewById(R.id.harvest_club);
        keyAddressesButton=findViewById(R.id.key_addresses);
       // bountyButton=findViewById(R.id.bounty);
        sendReceiveButton=findViewById(R.id.send_and_receive);
        logoutButton=findViewById(R.id.logout);
       // buyCoinsButton=findViewById(R.id.buyCoins);
        abourtUs=findViewById(R.id.about_us);
        userNameTV=findViewById(R.id.username);
        about=findViewById(R.id.about);
        help=findViewById(R.id.help);
        mBalanceTauTV=findViewById(R.id.balance_tau);

        test=findViewById(R.id.test);


       String user_name=getIntent().getStringExtra("user_name");
        //L.e(user_name);
       userNameTV.setText(user_name);
       userNameTV.setText(SharedPreferencesHelper.getInstance(MainActivity.this).getString("user_nane",""));
       mDrawerLayout=findViewById(R.id.drawerLayout);
       layout=findViewById(R.id.slidingMenu);


       titleBar();


    }

    private void titleBar() {
        //标题栏
        mMainCustomToolBar=findViewById(R.id.main_titlebar);
        mMainCustomToolBar.getTitleTextView().setText(R.string.testnet);
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

    private void initData() {
       // balanceButton.setOnClickListener(this);
        harvestClubButton.setOnClickListener( this);
        keyAddressesButton.setOnClickListener( this);
      //  bountyButton.setOnClickListener(this);
        sendReceiveButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
       // buyCoinsButton.setOnClickListener(this);
        abourtUs. setOnClickListener(this);
        about.setOnClickListener(this);
        help.setOnClickListener(this);

        test.setOnClickListener(this);

        showWaitDialog();
        getBalanceData(SharedPreferencesHelper.getInstance(MainActivity.this).getString("email",""));

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

        //
        imageViewsList= new ArrayList<ImageView>();
        for (int i=0;i<image.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(image[i]);
            imageViewsList.add(imageView);
        }

       /* viewPager=findViewById(R.id.ad_paper);
        viewPager.setAdapter(new ADAdapter());
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);

        handler.postDelayed(new ADRunble(),3000);
        imageViewsDotList=new ArrayList<>();
        imageViewsDotList.add(findViewById(R.id.dot1));
        imageViewsDotList.add(findViewById(R.id.dot2));
        imageViewsDotList.add(findViewById(R.id.dot3));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                for (int i1=0;i1<imageViewsDotList.size();i1++){

                    if(i1==i%imageViewsDotList.size()){
                        imageViewsDotList.get(i1).setImageResource(R.mipmap.round_r);
                    }else {
                        imageViewsDotList.get(i1).setImageResource(R.mipmap.round_w);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

    }

   /* class ADRunble implements  Runnable{
        @Override
        public void run() {
            int position=viewPager.getCurrentItem();
            position++;
            if(position>Integer.MAX_VALUE){
                position=0;
            }
            viewPager.setCurrentItem(position);
            handler.postDelayed(new ADRunble(),3000);

        }
    }*/
    //广告适配器
   /* class ADAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // return imageViewsList.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //container.removeView(imageViewsList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView view=imageViewsList.get(position % imageViewsList.size());
            ViewParent parent=view.getParent();
            if(parent!=null){
                ((ViewGroup)parent).removeView(view);
            }
            container.addView(view);
            return view;
        }
    }*/

    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance = new KeyGenerator();
        }
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.balance:
                startActivity(new Intent(MainActivity.this,BalanceActivity.class));
                break;*/
            case R.id.harvest_club:
                startActivity(new Intent(MainActivity.this,HarvestClubActivity.class));
                break;
            case R.id.key_addresses:
                startActivity(new Intent(MainActivity.this,KeysAddressesActivity.class));
                break;
            /*case R.id.bounty:
                startActivity(new Intent(MainActivity.this,BountyActivity.class));
                break;*/
            case R.id.send_and_receive:
                startActivity(new Intent(MainActivity.this,SendAndReceiveActivity.class));
                break;
            case R.id.logout:
                showWaitDialog();
                logout();
                break;
            case R.id.buyCoins:
                startActivity(new Intent(MainActivity.this,BuyCoinsActivity.class));
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

            case R.id.test:
                startActivity(new Intent(this,TextActivity.class));
                break;
        }
    }

    private void logout() {
       // LoginManager.getInstance().logOut();
        ApiService apiService = NetWorkManager.createApiService(ApiService.class);
        Observable<Logout> observable = apiService.logout();
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAUObserver<Logout>() {

                    @Override
                    public void handleData(Logout logout) {
                        super.handleData(logout);

                        hideWaitDialog();
                        h.sendEmptyMessage(0x13);
                        startActivity(new Intent(MainActivity.this,LoginActivity2.class));
                    }

                    @Override
                    public void handleError(String msg, int msgCode) {
                        super.handleError(msg, msgCode);
                        hideWaitDialog();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    public void getBalanceData(String email) {
        Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        NetWorkManager.createApiService(ApiService.class)
            .getBalance2(emailMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new TAUObserver<Balance<BalanceRet>>() {

                        @Override
                        public void handleData(Balance<BalanceRet> balanceRetBalance) {
                            super.handleData(balanceRetBalance);

                        L.e(balanceRetBalance.getStatus()+"");
                        L.e(balanceRetBalance.getMessage());
                        status=balanceRetBalance.getStatus();
                        double_coins=balanceRetBalance.getRet().getCoins();
                        reward= balanceRetBalance.getRet().getRewards();

                        SharedPreferencesHelper.getInstance(MainActivity.this).putString("balance",""+double_coins);
                        SharedPreferencesHelper.getInstance(MainActivity.this).putString("reward",""+reward);
                        SharedPreferencesHelper.getInstance(MainActivity.this).putString("utxo",""+balanceRetBalance.getRet().getUtxo());

                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");


                        h.sendEmptyMessage(0x21);
                        hideWaitDialog();
                        L.e("complete");

                    }

                    @Override
                    public void handleError(String msg, int msgCode) {
                        super.handleError(msg, msgCode);
                        hideWaitDialog();
                        L.e("error");
                        h.sendEmptyMessage(0x20);
                    }
                });
    }
}
