package com.mofei.tau.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.facebook.login.LoginManager;

import com.mofei.tau.R;
import com.mofei.tau.fragment.ManageFragment;
import com.mofei.tau.fragment.ReceiveFragment;
import com.mofei.tau.fragment.SendFragment;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.util.L;
import com.mofei.tau.view.CustomToolBar;

import java.util.ArrayList;
import java.util.List;


public class SendAndReceiveActivity extends BaseActivity implements View.OnClickListener {


    private CustomToolBar mMainCustomToolBar;

    //声明四个Tab分别对应的Fragment
    private SendFragment sendFragment;
    private ManageFragment manageFragment;
    private ReceiveFragment receiveFragment;

    private FrameLayout frameLayoutFragment;
    private RadioGroup radioGroup;
    private RadioButton sendRadioButton,manageRadioButton,receiveRadioButton;

   // private ViewPager viewPager;
    public static final int[] image={R.drawable.aa,R.drawable.ab,R.drawable.ac};
    private List<ImageView> imageViewsList;

    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_and_receive);


        initViews();//初始化控件
        initEvents();//初始化事件
        selectTab(0);//默认选中第一个Tab

       //
        imageViewsList= new ArrayList<ImageView>();
        for (int i=0;i<image.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(image[i]);
            imageViewsList.add(imageView);
        }


        titleBar();


    }


    private void titleBar() {
        //标题栏
        mMainCustomToolBar = findViewById(R.id.send_receive_titlebar);
        mMainCustomToolBar.getTitleTextView().setText("Transactions");
        mMainCustomToolBar.getTitleTextView().setTextColor(Color.WHITE);
        mMainCustomToolBar.getTitleTextView().setTextSize(22);
        mMainCustomToolBar.disableLeftTextView();
        //mMainCustomToolBar.disableRightView();
        mMainCustomToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {

        frameLayoutFragment=findViewById(R.id.fragment);
        radioGroup=findViewById(R.id.tab_radiogroup);
        sendRadioButton=findViewById(R.id.send);
        manageRadioButton=findViewById(R.id.manage);
        receiveRadioButton=findViewById(R.id.receive);


    }

    private void initEvents() {
        //初始化四个Tab的点击事件

        sendRadioButton.setOnClickListener(this);
        manageRadioButton.setOnClickListener(this);
        receiveRadioButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.send:
                selectTab(0);
                break;
            case R.id.manage:
                selectTab(1);
                break;
            case R.id.receive:
                selectTab(2);
                break;
           /* case R.id.send_receive_back_wallet_home:
                finish();
                break;*/
           /* case R.id.transaction_logout:

                break;*/
        }

    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            //当选中点击的是微信的Tab时
            case 0:
                //如果微信对应的Fragment没有实例化，则进行实例化，并显示出来
                if (sendFragment == null) {
                    sendFragment = new SendFragment();
                    transaction.add(R.id.fragment, sendFragment);
                } else {
                    //如果微信对应的Fragment已经实例化，则直接显示出来
                    transaction.show(sendFragment);
                }
                break;
            case 1:
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fragment, manageFragment);
                } else {
                    transaction.show(manageFragment);
                }
                break;
            case 2:

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

}
