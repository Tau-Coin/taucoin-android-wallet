package io.taucoin.android.wallet.module.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.service.TxService;
import io.taucoin.android.wallet.module.view.main.iview.IHomeView;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.module.view.manage.ProfileActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.CopyManager;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.foundation.util.DrawablesUtil;
import io.taucoin.foundation.util.StringUtil;

public class HomeFragment extends BaseFragment implements IHomeView {

    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_referral_link)
    ImageView ivReferralLink;
    @BindView(R.id.tv_referral_link)
    TextView tvReferralLink;
    @BindView(R.id.tv_your_invited)
    TextView tvYourInvited;
    @BindView(R.id.tv_your_referral)
    TextView tvYourReferral;
    @BindView(R.id.tv_friend_referral)
    TextView tvFriendReferral;
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.tv_p2p)
    TextView tvP2p;

    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        ProgressManager.showProgressDialog(getActivity());
        TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
        TxService.startTxService(TransmitKey.ServiceType.GET_INFO);
        return view;
    }

    @OnClick({R.id.tv_nick, R.id.iv_referral_link, R.id.tv_exchange, R.id.tv_p2p, R.id.tv_referral_link})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nick:
                if (!UserUtil.isImportKey()) {
                    ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
                } else {
                    ActivityUtil.startActivity(getActivity(), ProfileActivity.class);
                }
                break;
            case R.id.iv_referral_link:
                if (!UserUtil.isImportKey()) {
                    ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
                } else {
                    copyReferralLink();
                }
                break;
            case R.id.tv_referral_link:
                if (!UserUtil.isImportKey()) {
                    ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
                } else {
                    ActivityUtil.openUri(getActivity(), StringUtil.getText(tvReferralLink));
                }
                break;
            case R.id.tv_exchange:
                ActivityUtil.openUri(getActivity(), TransmitKey.ExternalUrl.EXCHANGE);
                break;
            case R.id.tv_p2p:
                ActivityUtil.openUri(getActivity(), TransmitKey.ExternalUrl.P2P_BUY_SELL);
                break;
            default:
                break;
        }
    }

    @OnLongClick(R.id.tv_referral_link)
    boolean copyReferralLink() {
        CopyManager.copyText(StringUtil.getText(tvReferralLink));
        ToastUtils.showShortToast(R.string.main_referral_link_copied);
        return true;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent object) {
        if (object == null) {
            return;
        }
        switch (object.getCode()) {
            case ALL:
                UserUtil.setBalance(tvBalance);
                UserUtil.setNickName(tvNick);
                loadReferralView();
                break;
            case BALANCE:
                if (refreshLayout != null && refreshLayout.isRefreshing()) {
                    refreshLayout.finishRefresh();
                }
                UserUtil.setBalance(tvBalance);
                break;
            case NICKNAME:
                UserUtil.setNickName(tvNick);
                break;
            default:
                break;
        }
    }

    private void loadReferralView() {
        UserUtil.loadReferralView(tvReferralLink, tvYourReferral, tvFriendReferral);
        UserUtil.setInvitedView(tvYourInvited);
    }

    @Override
    public void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(this);
        onEvent(EventBusUtil.getMessageEvent(MessageEvent.EventCode.ALL));

        DrawablesUtil.setUnderLine(tvExchange);
        DrawablesUtil.setUnderLine(tvP2p);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        TxService.startTxService(TransmitKey.ServiceType.GET_BALANCE);

        if (!UserUtil.isImportKey()) {
            refreshLayout.finishRefresh(1000);
        }
    }
}