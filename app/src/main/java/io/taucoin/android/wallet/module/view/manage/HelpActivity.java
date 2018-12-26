package io.taucoin.android.wallet.module.view.manage;

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
import io.taucoin.android.wallet.module.presenter.AppPresenter;
import io.taucoin.android.wallet.module.view.manage.iview.IHelpView;
import io.taucoin.android.wallet.util.ProgressManager;

public class HelpActivity extends BaseActivity implements IHelpView {

    @BindView(R.id.list_view_help)
    ListView listViewHelp;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private AppPresenter mAppPresenter;
    private HelpAdapter mHelpAdapter;
    private List<HelpBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        mAppPresenter = new AppPresenter(this);
        initView();
        ProgressManager.showProgressDialog(this);
        getHelpData();
    }

    private void getHelpData() {
        mAppPresenter.getHelpData();
    }


    @Override
    public void loadHelpData(List<HelpBean> data) {
        if(data != null){
            mDataList.clear();
            mDataList.addAll(data);
            mHelpAdapter.setListData(data);
            refreshLayout.finishRefresh();
        }
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
        HelpBean helpBean = mDataList.get(position);
        if(helpBean != null){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(TransmitKey.TITLE, getText(R.string.manager_help));
            intent.putExtra(TransmitKey.URL, helpBean.getLink());
            startActivity(intent);
        }
    }
}