package io.taucoin.android.wallet.module.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;
import com.tau.activity.BaseActivity;
import com.tau.activity.SendAndReceiveActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Logger.i("SplashActivity onCreate");

        // delay 3 seconds jump
        Observable.timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mDisposableObserver);
    }

    private DisposableObserver<Long> mDisposableObserver = new DisposableObserver<Long>() {

        @Override
        public void onNext(Long aLong) {

        }

        @Override
        public void onComplete() {
            splashJump();
        }

        @Override
        public void onError(Throwable e) {
            Logger.e(e, "SplashActivity jump error");
        }
    };

    private void splashJump() {
        Intent intent = new Intent(this, SendAndReceiveActivity.class);
        startActivity(intent);
        finish();
        Logger.i("Jump to MainActivity");
    }

    /**
     * Shielded return key
     * */
    @Override
    public void onBackPressed() {

    }
}