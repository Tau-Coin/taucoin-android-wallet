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
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.ReferralInfo;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.model.IUserModel;
import io.taucoin.android.wallet.module.model.UserModel;
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
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.DrawablesUtil;
import io.taucoin.foundation.util.StringUtil;

public class HomeFragment extends BaseFragment implements IHomeView {

    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
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
    @BindView(R.id.ll_referral_link)
    View llReferralLink;
    @BindView(R.id.iv_referral_link)
    View ivReferralLink;

    private IUserModel userModel;
    public static boolean mIsToast = false;
    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        userModel = new UserModel();
        initView();
        ProgressManager.showProgressDialog(getActivity());
        TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
        return view;
    }

    @OnClick({R.id.tv_nick, R.id.iv_referral_link, R.id.tv_exchange, R.id.tv_p2p, R.id.tv_referral_link, R.id.iv_right})
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
                } else if(UserUtil.isHaveLlink()){
                    ActivityUtil.openUri(getActivity(), StringUtil.getText(tvReferralLink));
                }
                break;
            case R.id.tv_exchange:
            case R.id.tv_p2p:
                ActivityUtil.openUri(getActivity(), TransmitKey.ExternalUrl.P2P_EXCHANGE);
                break;
            case R.id.iv_right:
                if (!UserUtil.isImportKey()) {
                    ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
                } else {
                    ProgressManager.showProgressDialog(getActivity());
                    mIsToast = true;
                    onRefresh(null);
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("all")
    @OnLongClick(R.id.tv_referral_link)
    boolean copyReferralLink() {
        if(UserUtil.isHaveLlink()){
            CopyManager.copyText(StringUtil.getText(tvReferralLink));
            ToastUtils.showShortToast(R.string.main_referral_link_copied);
        }
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
                loadReferralView(null);
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
            case INVITED:
                UserUtil.setInvitedView(tvYourInvited);
                break;
            case REFERRAL:
                loadReferralView(object.getData());
                break;
            default:
                break;
        }
    }

    private void loadReferralView(Object object) {
        llReferralLink.setVisibility(View.GONE);
        if(UserUtil.isImportKey()){
            ivReferralLink.setVisibility(UserUtil.isHaveLlink() ? View.VISIBLE : View.INVISIBLE);
            llReferralLink.setVisibility(View.VISIBLE);
            UserUtil.loadReferralView(tvReferralLink, object);
            UserUtil.setInvitedView(tvYourInvited);
            userModel.getReferralInfo(new LogicObserver<ReferralInfo>() {
                @Override
                public void handleData(ReferralInfo referralInfo) {
                    UserUtil.loadRewardView(referralInfo, tvYourReferral, tvFriendReferral);
                }
            });

        }
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
        TxService.startTxService(TransmitKey.ServiceType.GET_UTXO_LIST);
        TxService.startTxService(TransmitKey.ServiceType.GET_REFERRAL_INFO);

        if (!UserUtil.isImportKey()) {
            refreshLayout.finishRefresh(1000);
        }
    }
}