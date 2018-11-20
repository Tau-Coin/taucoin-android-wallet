package com.mofei.tau.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;
import com.mofei.tau.adapter.HistoryEventRecycleAdapter;
import com.mofei.tau.db.greendao.TransactionHistoryDaoUtils;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.util.L;
import com.mofei.tau.view.DialogWaitting;
import com.mofei.tau.view.SwipeRecyclerView;

import java.util.ArrayList;
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


    private SwipeRefreshLayout swipeRefreshLayout;
    // private RecyclerView historyRecyclerView;
    private SwipeRecyclerView swipeRecyclerView;
    private HistoryEventRecycleAdapter historyEventRecycleAdapter;
    private List<TransactionHistory> txList;


    Handler handler_ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        initEvent(view);
    }

    private void initView(View view) {
        mAddressTV=view.findViewById(R.id.address_text);
        mAddressTV.setText(SharedPreferencesHelper.getInstance(getActivity()).getString("Address",""));
        mBalanceTauTV=view.findViewById(R.id.balance_tau_fs);
    }

    private void initEvent(View view) {
        txList=new ArrayList<>();
        List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        if (!tempTXHistoryList.isEmpty()){
            txList.clear();
            txList.addAll(tempTXHistoryList);
        }

       swipeRecyclerView=view.findViewById(R.id.history_recycleView);
         /*swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        //Set the background color of the drop-down progress bar, default white.

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //Set the color theme of the drop-down progress bar, the parameter is a variable parameter, and is the resource ID. Set up to four different colors, and each turn displays a color.
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED);
        //To set up listeners, you need to override the onRefresh () method, which is called when the top drop-down occurs, which implements the logic of requesting data, sets the drop-down progress bar to disappear, and so on.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
                if (!tempTXHistoryList.isEmpty()){
                    txList.clear();
                    txList.addAll(tempTXHistoryList);
                }
                historyEventRecycleAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });*/

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
    }

    @Override
    public void onResume() {
        super.onResume();
        showWaitDialog();
        getBalanceData(SharedPreferencesHelper.getInstance(getActivity()).getString("email",""));
    }

    public void getBalanceData(String email) {
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
