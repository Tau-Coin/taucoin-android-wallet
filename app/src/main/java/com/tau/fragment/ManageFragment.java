package com.mofei.tau.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mofei.tau.R;
import com.mofei.tau.activity.BalanceActivity;
import com.mofei.tau.activity.KeysAddressesActivity;
import com.mofei.tau.adapter.HistoryEventRecycleAdapter;
import com.mofei.tau.db.greendao.TransactionHistoryDaoUtils;
import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.transaction.TXChild;
import com.mofei.tau.transaction.TXGroup;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;
import com.mofei.tau.view.CustomToolBar;
import com.mofei.tau.view.SwipeRecyclerView;
import com.mofei.tau.view.expanableLV.ExtendableListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {

  //  private RelativeLayout coinsRL,transationsRL;

    /*private SwipeRefreshLayout swipeRefreshLayout;
    // private RecyclerView historyRecyclerView;
    private SwipeRecyclerView swipeRecyclerView;
    private HistoryEventRecycleAdapter historyEventRecycleAdapter;
    private List<TransactionHistory> txList;
    */
    private Toast mToast = null;


    //ExpandableListView
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListView txExpandableListView;
    private List<TXGroup> groupArray;
    private List<List<TXChild>> childArray;
    private List<TXChild> txChildList;
    private ExtendableListViewAdapter extendableListViewAdapter;

    private TXGroup txGroup;
    private TXChild txChild;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initEvent(view);

        initView(view);

    }

    private void initView(View view) {

        txExpandableListView=view.findViewById(R.id.expend_list);

        //List<UTXORecord> txUTXORecordList=UTXORecordDaoUtils.getInstance().queryAllData();
        List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        groupArray=new ArrayList<>();
        childArray=new ArrayList<>();
        if (!tempTXHistoryList.isEmpty()){
            groupArray.clear();
            childArray.clear();
            for (int i=0;i<tempTXHistoryList.size();i++){
                txGroup=new TXGroup();

                Double double_8=new Double("100000000");
                String s=String.valueOf(tempTXHistoryList.get(i).getValue());
                if(s!=null){
                    Double coin_double=new Double(s);
                    L.e("转换后的数据：　"+coin_double/double_8);
                    txGroup.setAmount(digitalConversionTool(s));
                }
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tempTXHistoryList.get(i).getBlocktime() * 1000));
                L.e("date"+date);
                txGroup.setTime(String.valueOf(date));
                groupArray.add(txGroup);

                txChild=new TXChild();
                txChild.setAddress(tempTXHistoryList.get(i).getToAddress());
                txChild.setTxId(tempTXHistoryList.get(i).getTxId());
                txChild.setTxFee("0.11");
                //txChild.setTxBlockHeight(String.valueOf(tempTXHistoryList.get(i).getBlockheight()));
                txChildList=new ArrayList<>();
                txChildList.add(txChild);

                childArray.add(txChildList);
            }

        }


        extendableListViewAdapter=new ExtendableListViewAdapter(getActivity(),groupArray,childArray);
        txExpandableListView.setAdapter(extendableListViewAdapter);
        //设置分组的监听
        txExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // showToast(gs[groupPosition]);
                 return false;
            }
        });
        //设置子项布局监听
        txExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               // showToast(cs[groupPosition][childPosition]);
                return true;
            }
        });


        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        //Set the background color of the drop-down progress bar, default white.
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //Set the color theme of the drop-down progress bar, the parameter is a variable parameter, and is the resource ID. Set up to four different colors, and each turn displays a color.
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED);
        //To set up listeners, you need to override the onRefresh () method, which is called when the top drop-down occurs, which implements the logic of requesting data, sets the drop-down progress bar to disappear, and so on.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                L.e("进刷新");
                List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
                //groupArray=new ArrayList<>();
               // childArray=new ArrayList<>();
                if (!tempTXHistoryList.isEmpty()){
                    groupArray.clear();
                    childArray.clear();
                    for (int i=0;i<tempTXHistoryList.size();i++){
                        TXGroup txGroup=new TXGroup();

                        // Double double_8=new Double("100000000");
                        // Double coin_double=new Double(String.valueOf(tempTXHistoryList.get(i).getValue()));
                        // L.e("转换后的数据：　"+coin_double/double_8);
                        String amount=digitalConversionTool(String.valueOf(tempTXHistoryList.get(i).getValue()));
                        L.e("amount "+amount);
                        txGroup.setAmount(amount);

                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tempTXHistoryList.get(i).getBlocktime() * 1000));
                        L.e("date"+date);
                        txGroup.setTime(String.valueOf(date));
                        groupArray.add(txGroup);

                        TXChild txChild=new TXChild();
                        txChild.setAddress(SharedPreferencesHelper.getInstance(getActivity()).getString("Address",""));
                        txChild.setTxId(tempTXHistoryList.get(i).getTxId());
                        txChild.setTxFee("0.11");
                        txChild.setTxBlockHeight(String.valueOf(tempTXHistoryList.get(i).getBlockheight()));
                        txChildList=new ArrayList<>();
                        txChildList.add(txChild);

                        childArray.add(txChildList);
                    }

                }

                extendableListViewAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

    }

    public String digitalConversionTool(String string){
        //把5.00000000转化成50000000
        Double d = new Double(string);
        L.e("double"+d);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        L.e("转化后　"+nf.format(d));
        return nf.format(d);

    }


   /* private void initEvent(View view) {
        txList=new ArrayList<>();
        List<TransactionHistory> tempTXHistoryList= TransactionHistoryDaoUtils.getInstance().queryAllData();
        if (!tempTXHistoryList.isEmpty()){
            txList.clear();
            txList.addAll(tempTXHistoryList);
        }


        swipeRecyclerView=view.findViewById(R.id.history_recycleView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
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
        historyEventRecycleAdapter=new HistoryEventRecycleAdapter(getActivity(),txList);
         *//**
         * 设置布局方向
         * Setting layout direction
         *//*
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        swipeRecyclerView.setLayoutManager(layoutManager);
        *//**
         * 设置间隔线
         * Setting intervals
         *//*
        swipeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        *//**
         * 设置删除动画
         * Set delete animation
         *//*
        swipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        *//**
         * 设置适配器
         * Setup adapter
         *//*
        swipeRecyclerView.setAdapter(historyEventRecycleAdapter);
         *//**
         * 设置右删除监听
         * Set right delete listen.
         *//*
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
    }*/


    public void showToast(String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
