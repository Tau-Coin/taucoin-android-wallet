package io.taucoin.android.wallet.module.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mofei.tau.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.presenter.TxService;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.module.view.main.iview.IHomeView;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.foundation.util.StringUtil;

public class HomeFragment extends BaseFragment implements IHomeView {

    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private TxPresenter mTxPresenter;

    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mTxPresenter = new TxPresenter(this);
        initView();
        ProgressManager.showProgressDialog(getActivity());
        TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
        return view;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(Object object){
        String tag = StringUtil.getString(object);
        if(refreshLayout != null && StringUtil.isSame(tag, TransmitKey.ServiceType.GET_BALANCE)){
            refreshLayout.finishRefresh();
        }
        UserUtil.setNickName(tvNick);
        UserUtil.setAvatar(ivHeaderPic);
        UserUtil.setBalance(tvBalance);
    }

    @Override
    public void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(this);
        onEvent(null);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        TxService.startTxService(TransmitKey.ServiceType.GET_BALANCE);
    }
}