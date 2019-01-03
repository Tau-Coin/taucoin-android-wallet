package io.taucoin.android.wallet.module.view.main;

import android.os.Bundle;
import android.view.Gravity;
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
import de.hdodenhof.circleimageview.CircleImageView;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.module.view.main.iview.IManageView;
import io.taucoin.android.wallet.module.view.manage.HelpActivity;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.module.view.manage.KeysActivity;
import io.taucoin.android.wallet.module.view.manage.ProfileActivity;
import io.taucoin.android.wallet.util.ActivityUtil;
import io.taucoin.android.wallet.util.EventBusUtil;
import io.taucoin.android.wallet.util.UserUtil;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.AppUtil;

public class ManageFragment extends BaseFragment implements IManageView {

    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_version)
    TextView tvVersion;

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

    @OnClick({R.id.iv_header_pic, R.id.tv_nick, R.id.item_keys, R.id.item_address_note, R.id.item_help})
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.iv_header_pic:
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
                UserUtil.setNickName(tvNick);
                UserUtil.setAvatar(ivHeaderPic);
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
}