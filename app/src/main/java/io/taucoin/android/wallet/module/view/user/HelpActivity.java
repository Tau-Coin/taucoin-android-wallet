package io.taucoin.android.wallet.module.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mofei.tau.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.HelpBean;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.util.ProgressManager;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.list_view_help)
    ListView listViewHelp;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private TxPresenter mTxPresenter;
    private HelpAdapter mHelpAdapter;
    private List<HelpBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        mTxPresenter = new TxPresenter();
        initView();
        ProgressManager.showProgressDialog(this);
        getHelpData();
    }

    private void getHelpData() {
        data.clear();
        for (int i = 0; i < 5; i++) {
            HelpBean help = new HelpBean();
            help.setTitle(getText(R.string.keys_how_import).toString());
            help.setUrl("https://www.baidu.com");
            data.add(help);
        }
        mHelpAdapter.setListData(data);
        ProgressManager.closeProgressDialog();
        refreshLayout.finishRefresh();
    }

    private void initView() {
        refreshLayout.setOnRefreshListener(this);
        mHelpAdapter = new HelpAdapter();
        listViewHelp.setAdapter(mHelpAdapter);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getHelpData();
    }

    @OnItemClick(R.id.list_view_help)
    void onItemClick(AdapterView<?> parent, View view, int position, long id){
        HelpBean helpBean = data.get(position);
        if(helpBean != null){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(TransmitKey.TITLE, helpBean.getTitle());
            intent.putExtra(TransmitKey.URL, helpBean.getUrl());
            startActivity(intent);
        }
    }
}