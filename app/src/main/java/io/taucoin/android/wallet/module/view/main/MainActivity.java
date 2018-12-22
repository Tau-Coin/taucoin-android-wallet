package io.taucoin.android.wallet.module.view.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.mofei.tau.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.module.presenter.TxService;
import io.taucoin.android.wallet.module.view.main.iview.IMainView;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.ActivityManager;
import io.taucoin.foundation.util.DrawablesUtil;


public class MainActivity extends BaseActivity implements IMainView {
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_send_receive)
    RadioButton rbSendReceive;
    @BindView(R.id.rb_manager)
    RadioButton rbManager;

    private Fragment[] mFragments = new Fragment[3];
    private Subject<Integer> mBackClick = PublishSubject.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_main);
        ButterKnife.bind(this);
        initBottomTabView();
        changeTab(0);
        exitApp();
    }

    @Override
    public void initBottomTabView() {
        DrawablesUtil.setTopDrawable(rbHome, R.drawable.selector_tab_home,35, 25);
        DrawablesUtil.setTopDrawable(rbSendReceive, R.drawable.selector_tab_send, 35, 25);
        DrawablesUtil.setTopDrawable(rbManager, R.drawable.selector_tab_manage, 35, 25);
    }

    @Override
    public void changeTab(int tabIndex) {
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(null == mFragments[tabIndex]){
            if(tabIndex == 0){
                fragment = new HomeFragment();
            }else if(tabIndex == 1){
                fragment = new SendReceiveFragment();
            }else if(tabIndex == 2){
                fragment = new ManageFragment();
            }
            mFragments[tabIndex] = fragment;
        }else{
            fragment = mFragments[tabIndex];
        }
        if(null != fragment){
            fragmentTransaction.replace(R.id.tab_container, fragment);
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }

    @OnCheckedChanged({R.id.rb_home, R.id.rb_manager, R.id.rb_send_receive})
    public void onRadioCheck(CompoundButton view, boolean isChanged) {
        switch (view.getId()) {
            case R.id.rb_home:
                if (isChanged) {
                    changeTab(0);
                }
                break;
            case R.id.rb_send_receive:
                if (isChanged) {
                    changeTab(1);
                }
                break;
            case R.id.rb_manager:
                if (isChanged) {
                    changeTab(2);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mBackClick.onNext(1);
    }

    @SuppressLint("CheckResult")
    private void exitApp() {
        mBackClick.mergeWith(mBackClick.debounce(2000, TimeUnit.MILLISECONDS)
            .map(i -> 0))
            .scan((prev, cur) -> {
                if (cur == 0) return 0;
                return prev + 1;
            })
            .filter(v -> v > 0)
            .subscribe(v -> {
                if (v == 1) {
                    ToastUtils.showLongToast(R.string.main_exit);
                } else if (v == 2) {
                    ActivityManager.finishAll();
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TxService.stopService();
    }
}
