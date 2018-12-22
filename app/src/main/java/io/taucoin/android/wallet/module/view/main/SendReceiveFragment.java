package io.taucoin.android.wallet.module.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.module.view.main.iview.ISendReceiveView;
import io.taucoin.android.wallet.module.view.user.ImportKeyActivity;
import io.taucoin.android.wallet.util.CopyManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.widget.EmptyLayout;

public class SendReceiveFragment extends BaseFragment implements ISendReceiveView {

    @BindView(R.id.balance_text)
    TextView balanceText;
    @BindView(R.id.btn_receive)
    Button btnReceive;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.iv_tx_log_tips)
    ImageView ivTxLogTips;
    @BindView(R.id.list_view_log)
    ExpandableListView listViewLog;
    @BindView(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private TxPresenter mTxPresenter;
    private HistoryExpandableListAdapter mAdapter;
    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_receive, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        mTxPresenter = new TxPresenter(this);
        initData();
        return view;
    }

    @Override
    public void initData() {
        mTxPresenter.getBalanceLocal();
        mTxPresenter.queryTransactionHistory();
    }

    @Override
    public void loadTransactionHistory(List<TransactionHistory> txHistories) {
        mTxPresenter.queryTransactionHistory();
        mAdapter.setHistoryList(txHistories);
        emptyLayout.setVisibility(txHistories.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initView() {
        mAdapter = new HistoryExpandableListAdapter();
        listViewLog.setAdapter(mAdapter);
        UserUtil.setBalance(balanceText, 0L);
    }

    @OnClick({R.id.btn_receive, R.id.btn_send, R.id.iv_tx_log_tips})
    void onClick(View view){
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            Intent intent = new Intent(getActivity(), ImportKeyActivity.class);
            startActivity(intent);
            return;
        }
        switch (view.getId()){
            case R.id.btn_receive:
                CopyManager.copyText(keyValue.getAddress());
                ToastUtils.showLongToast(R.string.keys_address_copy);
                break;
            case R.id.btn_send:
                Intent intent = new Intent(getActivity(), SendActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_tx_log_tips:
               ToastUtils.showLongToast(R.string.tx_log_tips);
                break;
            default:
                break;
        }

    }

    @Override
    public void initListener() {
        listViewLog.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        listViewLog.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        listViewLog.setOnGroupExpandListener(groupPosition -> {
            int groupCount = listViewLog.getExpandableListAdapter().getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                if (groupPosition != i) {
                    listViewLog.collapseGroup(i);
                }
            }
        });
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object object){
        UserUtil.setBalance(balanceText);
    }
}