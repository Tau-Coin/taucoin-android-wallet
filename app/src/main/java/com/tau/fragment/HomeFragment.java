package com.tau.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;
import com.tau.activity.DetailsActivity;
import com.tau.activity.SendAndReceiveActivity;
import com.tau.adapter.HistoryEventRecycleAdapter;
import io.taucoin.android.wallet.db.util.TransactionHistoryDaoUtils;
import com.tau.entity.FirstEvent;
import com.tau.entity.res_post.Balance;
import com.tau.entity.res_post.BalanceRet;
import com.tau.info.SharedPreferencesHelper;
import io.taucoin.android.wallet.net.service.ApiService;
import io.taucoin.foundation.net.NetWorkManager;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import com.tau.util.L;
import com.tau.util.UserInfoUtils;
import com.tau.view.DialogWaitting;
import com.tau.view.SwipeRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import org.greenrobot.eventbus.EventBus;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView mAddressTV,mBalanceTauTV;
    private double double_coins;
    private double reward;
    private int status;
    private DialogWaitting mWaitDialog = null;
    private Toast mToast = null;
    private LinearLayout watchOutLL;
    private ImageView copyIV;

    private SwipeRefreshLayout homeBalabceSwipeRefreshLayout;
    // private RecyclerView historyRecyclerView;
    private SwipeRecyclerView swipeRecyclerView;
    private HistoryEventRecycleAdapter historyEventRecycleAdapter;
    private List<TransactionHistory> txList;

    private RefreshLayout refreshLayout;

    private Activity activity;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendAndReceiveActivity.BALANCE_CHANGED:
                    updateBalance();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);

        watchOutLL=view.findViewById(R.id.watch_at);
        watchOutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DetailsActivity.class));
               // watchOutLL.setVisibility(View.GONE);
            }
        });
        L.d("onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = this.getActivity();

        L.d("onViewCreated");

        initView(view);

        initEvent(view);
    }

    private void initView(View view) {
        mAddressTV=view.findViewById(R.id.address_text);
        mAddressTV.setText(UserInfoUtils.getAddress(getActivity()));
        mBalanceTauTV=view.findViewById(R.id.balance_tau_fs);
        copyIV=view.findViewById(R.id.copy);
        copyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCopy();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d("updateBalance");
       // updateBalance();
    }

    private void updateBalance() {
        String b= SharedPreferencesHelper.getInstance(getActivity()).getString("balance","");
        L.e("balance :"+b);
        if(!b.equals("")){
            L.e("TXHistory的值不为空");
            Double double_8=new Double("100000000");
            Double coin_double=new Double(b);
            L.e("转换后的数据：　"+coin_double/double_8);
            double balance=coin_double/double_8;
            mBalanceTauTV.setText(""+balance);
            //向sendFragment传递数据
            EventBus.getDefault().postSticky(new FirstEvent(balance+""));
        }
    }

    private void initEvent(View view) {
        homeBalabceSwipeRefreshLayout=view.findViewById(R.id.refreshBalanceLayout);
        int ProgressBackgroundColor=Color.parseColor("#F19322");
        homeBalabceSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        homeBalabceSwipeRefreshLayout.setColorSchemeColors(ProgressBackgroundColor);
        homeBalabceSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public Object activity;
            @Override
            public void onRefresh() {

                ((SendAndReceiveActivity)HomeFragment.this.activity).getBalance();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeBalabceSwipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        ((SendAndReceiveActivity)this.activity).registerBalanceChangeListener(handler);

        txList=new ArrayList<>();
        loadData();
        updateBalance();
        swipeRecyclerView=view.findViewById(R.id.history_recycleView);
        // List<TransactionHistory> transactionHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        historyEventRecycleAdapter=new HistoryEventRecycleAdapter(getActivity(),txList);
        /**
         * 设置布局方向
         * Setting layout direction
         */
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        swipeRecyclerView.setLayoutManager(layoutManager);
        /**
         * 设置间隔线
         * Setting intervals
         */
        swipeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        /**
         * 设置删除动画
         * Set delete animation
         */
        swipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * 设置适配器
         * Setup adapter
         */
        swipeRecyclerView.setAdapter(historyEventRecycleAdapter);
        /**
         * 设置右删除监听
         * Set right delete listen.
         */
        swipeRecyclerView.setRightClickListener(new SwipeRecyclerView.OnRightClickListener() {
            @Override
            public void onRightClick(int position, String id) {
                L.e("删除数据"+position);
                TransactionHistoryDaoUtils.getInstance().deleteTransactionHistoryData(txList.get(position));
                txList.remove(position);
                historyEventRecycleAdapter.notifyDataSetChanged();
                showToast("delete successfully");
            }
        });

        refreshLayout = view.findViewById(R.id.historyRefreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                loadData();
                historyEventRecycleAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);//传入false表示加载失败
            }
        });
    }

    private void loadData() {
        List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        if (!tempTXHistoryList.isEmpty()){
            txList.clear();
            for (int i=0;i<tempTXHistoryList.size();i++){
                txList.add(0,tempTXHistoryList.get(i));
            }
            // txList.addAll(tempTXHistoryList);
        }
    }


    public void onClickCopy() {
        ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(mAddressTV.getText());
        showToast("copy successfully");
    }

    public void getBalanceData(String email) {
        Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        ApiService apiService= NetWorkManager.createApiService(ApiService.class);
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
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        L.e("complete");
                    }
                });
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
