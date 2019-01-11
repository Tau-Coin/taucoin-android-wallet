package io.taucoin.android.wallet.module.view;

import android.content.Intent;
import android.os.Bundle;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.module.view.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.taucoin.android.wallet.net.callback.CommonObserver;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Is it the root of the task stack?
//        if (!this.isTaskRoot()) {
//            Intent intent = getIntent();
//            if (intent != null) {
//                String action = intent.getAction();
//                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
//                    finish();
//                }
//            }
//        } else {
            // Open for the first time
            setContentView(R.layout.activity_splash);

            Logger.i("SplashActivity onCreate");

            // delay 3 seconds jump
            Observable.timer(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mDisposableObserver);
//        }
    }

    private CommonObserver<Long> mDisposableObserver = new CommonObserver<Long>() {

        @Override
        public void onComplete() {
            splashJump();
        }
    };

    private void splashJump() {
        Intent intent = new Intent(this, MainActivity.class);
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