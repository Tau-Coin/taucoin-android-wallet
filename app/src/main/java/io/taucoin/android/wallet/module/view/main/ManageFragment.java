package io.taucoin.android.wallet.module.view.main;

import android.content.Intent;
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
import butterknife.OnClick;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.service.UpgradeService;
import io.taucoin.android.wallet.module.view.main.iview.IManageView;
import io.taucoin.android.wallet.module.view.manage.HelpActivity;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.module.view.manage.KeysActivity;
import io.taucoin.android.wallet.module.view.manage.ProfileActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.AppUtil;

public class ManageFragment extends BaseFragment implements IManageView {

    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.version_upgrade)
    View versionUpgrade;

    @Override
    public View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void initView() {
        String versionName = getResources().getString(R.string.manager_version);
        versionName = String.format(versionName, AppUtil.getVersionName(getActivity()));
        tvVersion.setText(versionName);
        onEvent(EventBusUtil.getMessageEvent(MessageEvent.EventCode.ALL));
    }

    @OnClick({R.id.tv_nick, R.id.item_keys, R.id.item_address_note, R.id.item_help, R.id.tv_version, R.id.iv_left_back,R.id.item_pass})
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.tv_nick:
               if(UserUtil.isImportKey()){
                   ActivityUtil.startActivity(getActivity(), ProfileActivity.class);
               }else{
                   ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
               }
               break;
           case R.id.item_keys:
               if(UserUtil.isImportKey()){
                   ActivityUtil.startActivity(getActivity(), KeysActivity.class);
               }else{
                   ActivityUtil.startActivity(getActivity(), ImportKeyActivity.class);
               }
               break;
           case R.id.item_address_note:
               ToastUtils.showLongToast(R.string.manager_address_note);
               break;
           case R.id.item_help:
               ActivityUtil.startActivity(getActivity(), HelpActivity.class);
               break;
           case R.id.tv_version:
               ProgressManager.showProgressDialog(getActivity());
               Intent intent = new Intent();
               intent.putExtra(TransmitKey.ISSHOWTIP, true);
               UpgradeService.startUpdateService(intent);
               break;
           case R.id.iv_left_back:
               startActivity(new Intent(getActivity(), LockActivity.class));
               getActivity().finish();
               break;
           case R.id.item_pass:
               Intent intentPass=new Intent(getActivity(), LockActivity.class);
               intentPass.putExtra("refer","pass");
               startActivity(intentPass);
               getActivity().finish();
               break;
           default:
               break;
       }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            showUpgradeView();
        }
    }

    private void showUpgradeView() {
        boolean isUpgrade = SharedPreferencesHelper.getInstance().getBoolean(TransmitKey.UPGRADE, false);
        versionUpgrade.setVisibility(isUpgrade ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent object) {
        if(object == null){
            return;
        }
        switch (object.getCode()){
            case ALL:
                UserUtil.setNickName(tvNick);
                break;
            case NICKNAME:
                UserUtil.setNickName(tvNick);
                break;
            case UPGRADE:
                showUpgradeView();
                break;
            default:
                break;
        }
    }
}