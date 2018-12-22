package io.taucoin.android.wallet.module.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mofei.tau.R;

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

public class HomeFragment extends BaseFragment implements IHomeView {

    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    private TxPresenter mTxPresenter;

    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mTxPresenter = new TxPresenter(this);
        getBalanceLocal();
        ProgressManager.showProgressDialog(getActivity());
        TxService.startTxService(TransmitKey.ServiceType.GET_HOME_DATA);
        return view;
    }

    private void getBalanceLocal() {
        mTxPresenter.getBalanceLocal();
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object object){
        UserUtil.setNickName(tvNick);
        UserUtil.setAvatar(ivHeaderPic);
        UserUtil.setBalance(tvBalance);
    }

    @Override
    public void initView() {
        UserUtil.setBalance(tvBalance, 0L);
    }
}