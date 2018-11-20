package com.mofei.tau.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.mofei.tau.R;
import com.mofei.tau.adapter.HistoryEventRecycleAdapter;
import com.mofei.tau.db.greendao.TransactionHistoryDaoUtils;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.util.L;
import com.mofei.tau.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExhibitTXActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    // private RecyclerView historyRecyclerView;
    private SwipeRecyclerView swipeRecyclerView;
    private HistoryEventRecycleAdapter historyEventRecycleAdapter;
    private List<TransactionHistory> txList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_tx);

        initEvent();
    }


    private void initEvent() {

        txList=new ArrayList<>();
        List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        if (!tempTXHistoryList.isEmpty()){
            txList.clear();
            txList.addAll(tempTXHistoryList);
        }

        swipeRecyclerView=findViewById(R.id.history_recycleView_);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout_);
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
        });

        // List<TransactionHistory> transactionHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        historyEventRecycleAdapter=new HistoryEventRecycleAdapter(this,txList);
        /**
         * 设置布局方向
         * Setting layout direction
         */
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        swipeRecyclerView.setLayoutManager(layoutManager);
        /**
         * 设置间隔线
         * Setting intervals
         */
        swipeRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //
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
}
