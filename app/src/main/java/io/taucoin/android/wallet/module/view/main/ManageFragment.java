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
import de.hdodenhof.circleimageview.CircleImageView;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.base.BaseFragment;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.module.view.main.iview.IManageView;
import io.taucoin.android.wallet.module.view.manage.HelpActivity;
import io.taucoin.android.wallet.module.view.manage.ImportKeyActivity;
import io.taucoin.android.wallet.module.view.manage.KeysActivity;
import io.taucoin.android.wallet.module.view.manage.ProfileActivity;
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
        onEvent(null);
    }

    @OnClick({R.id.iv_header_pic, R.id.tv_nick, R.id.item_keys, R.id.item_address_note, R.id.item_help})
    public void onClick(View view) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null && view.getId() != R.id.item_help){
            Intent intent = new Intent(getActivity(), ImportKeyActivity.class);
            startActivity(intent);
            return;
        }
       switch (view.getId()){
           case R.id.iv_header_pic:
           case R.id.tv_nick:
               Intent intent = new Intent(getActivity(), ProfileActivity.class);
               startActivity(intent);
               break;
           case R.id.item_keys:
               intent = new Intent(getActivity(), KeysActivity.class);
               startActivity(intent);
               break;
           case R.id.item_address_note:
               ToastUtils.showLongToast(R.string.manager_address_note);
               break;
           case R.id.item_help:
               intent = new Intent(getActivity(), HelpActivity.class);
               startActivity(intent);
               break;
           default:
               break;
       }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(Object object){
        UserUtil.setNickName(tvNick);
        UserUtil.setAvatar(ivHeaderPic);
    }
}