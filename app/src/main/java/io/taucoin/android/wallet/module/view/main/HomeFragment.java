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
import de.hdodenhof.circleimageview.CircleImageView;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.service.TxService;
import io.taucoin.android.wallet.module.view.main.iview.IHomeView;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.UserUtil;

public class HomeFragment extends BaseFragment implements IHomeView {

    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

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

    @OnClick({R.id.iv_header_pic, R.id.tv_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header_pic:
            case R.id.tv_nick:
                if (!UserUtil.isImportKey()) {
                    ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent object) {
        if(object == null){
            return;
        }
        switch (object.getCode()){
            case ALL:
                UserUtil.setBalance(tvBalance);
                UserUtil.setNickName(tvNick);
                UserUtil.setAvatar(ivHeaderPic);
                break;
            case BALANCE:
                if(refreshLayout != null && refreshLayout.isRefreshing()){
                    refreshLayout.finishRefresh();
                }
                UserUtil.setBalance(tvBalance);
                break;
            case NICKNAME:
                UserUtil.setNickName(tvNick);
                break;
            case AVATAR:
                UserUtil.setAvatar(ivHeaderPic);
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(this);
        onEvent(EventBusUtil.getMessageEvent(MessageEvent.EventCode.ALL));
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        TxService.startTxService(TransmitKey.ServiceType.GET_BALANCE);

        if(!UserUtil.isImportKey()){
            refreshLayout.finishRefresh(1000);
        }
    }
}