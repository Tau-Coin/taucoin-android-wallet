package io.taucoin.android.wallet.module.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.module.service.TxService;
import io.taucoin.android.wallet.module.view.main.iview.ISendReceiveView;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.module.view.tx.SendActivity;
import io.taucoin.android.wallet.util.CopyManager;
import io.taucoin.android.wallet.util.DateUtil;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.widget.CommonDialog;
import io.taucoin.android.wallet.widget.EmptyLayout;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.DrawablesUtil;

public class SendReceiveFragment extends BaseFragment implements ISendReceiveView {

    @BindView(R.id.balance_text)
    TextView balanceText;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.iv_tx_log_tips)
    ImageView ivTxLogTips;
    @BindView(R.id.list_view_log)
    ExpandableListView listViewLog;
    @BindView(R.id.empty_layout)
    EmptyLayout emptyLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private TxPresenter mTxPresenter;
    private HistoryExpandableListAdapter mAdapter;
    private int mPageNo = 1;
    private String mTime;
    private boolean mIsToast = false;

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        UserUtil.setAddress(tvAddress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserUtil.setAddress(tvAddress);
    }

    @Override
    public void initData() {
        if(UserUtil.isImportKey()){
            ProgressManager.showProgressDialog(getActivity());
        }
        onEvent(EventBusUtil.getMessageEvent(MessageEvent.EventCode.BALANCE));
        onRefresh(null);
    }

    @Override
    public void initView() {
        mAdapter = new HistoryExpandableListAdapter();
        listViewLog.setAdapter(mAdapter);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        DrawablesUtil.setEndDrawable(tvAddress, R.mipmap.icon_copy, 22);
    }

    @OnClick({R.id.btn_send, R.id.iv_tx_log_tips, R.id.tv_address, R.id.iv_right, R.id.iv_left_back})
    void onClick(View view) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if (keyValue == null && view.getId() != R.id.iv_tx_log_tips) {
            Intent intent = new Intent(getActivity(), ImportKeyActivity.class);
            startActivityForResult(intent, 100);
            return;
        }
        switch (view.getId()) {
            case R.id.btn_send:
                Intent intent = new Intent(getActivity(), SendActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_tx_log_tips:
                showTxLogTipDialog();
                break;
            case R.id.tv_address:
                copyData();
                break;
            case R.id.iv_right:
                ProgressManager.showProgressDialog(getActivity());
                mIsToast = true;
                onRefresh(null);
                break;
            case R.id.iv_left_back:
                startActivity(new Intent(getActivity(), LockActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }

    }

    private void copyData() {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue != null){
            CopyManager.copyText(keyValue.getAddress());
            ToastUtils.showShortToast(R.string.keys_address_copy);
        }
    }

    private void showTxLogTipDialog() {
        TextView textView = new TextView(getActivity());
        textView.setTextAppearance(getActivity(), R.style.style_normal_grey_dark);
        textView.setText(R.string.tx_log_tips);
        textView.setLineSpacing(5.0f, 1.2f);
        new CommonDialog.Builder(getActivity())
                .setContentView(textView)
                .create().show();
    }

    @Override
    public void initListener() {
        listViewLog.setOnGroupClickListener((parent, v, groupPosition, id) -> false);
        listViewLog.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);
        listViewLog.setOnGroupExpandListener(groupPosition -> {
            int groupCount = listViewLog.getExpandableListAdapter().getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                if (groupPosition != i) {
                    listViewLog.collapseGroup(i);
                }
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    @Override
    public void loadTransactionHistory(List<TransactionHistory> txHistories) {
        if(mAdapter == null){
            return;
        }
        if(txHistories == null){
            txHistories = new ArrayList<>();
        }
        mAdapter.setHistoryList(txHistories, mPageNo != 1);
        emptyLayout.setVisibility(mAdapter.getData().size() == 0 ? View.VISIBLE : View.GONE);
        llTip.setVisibility(mAdapter.getData().size() != 0 ? View.VISIBLE : View.GONE);
        refreshLayout.setEnableLoadmore(txHistories.size() % TransmitKey.PAGE_SIZE == 0 && txHistories.size() > 0);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent object) {
        if(object == null){
            ProgressManager.closeProgressDialog();
            return;
        }
        switch (object.getCode()){
            case ALL:
            case BALANCE:
                UserUtil.setBalance(balanceText);
                break;
            case TRANSACTION:
                startRefresh();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPageNo += 1;
        mTxPresenter.queryTransactionHistory(mPageNo, mTime);
    }

    public void startRefresh() {
        mPageNo = 1;
        mTime = DateUtil.getCurrentTime();
        if(mTxPresenter != null){
            mTxPresenter.queryTransactionHistory(mPageNo, mTime);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(UserUtil.isImportKey() && mTxPresenter != null){
            TxService.startTxService(TransmitKey.ServiceType.GET_BALANCE);
            mTxPresenter.getAddOuts(new LogicObserver<Boolean>() {
                @Override
                public void handleData(Boolean isSuccess) {
                    ProgressManager.closeProgressDialog();
                    startRefresh();
                    if(mIsToast && !isSuccess){
                        ToastUtils.showShortToast(R.string.common_refresh_failed);
                    }
                    mIsToast = false;
                }
            });
        }else {
            refreshLayout.finishRefresh(1000);
            ProgressManager.closeProgressDialog();
        }
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        refreshLayout.finishLoadmore();
    }
}