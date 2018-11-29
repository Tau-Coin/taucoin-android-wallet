package com.mofei.tau.transaction;

import com.mofei.tau.db.GreenDaoManager;

import io.taucoin.android.wallet.Wallet;

/**
 * Created by ly on 18-11-6
 *
 * @version 1.0
 * @description:
 */
public class WalletUtils {

    private final Wallet wallet;
    private static WalletUtils mWalletUtils;

    public WalletUtils() {
        wallet=Wallet.getInstance();
    }

    public static WalletUtils getInstance() {
        if (mWalletUtils == null) {
            mWalletUtils = new WalletUtils();
        }
        return mWalletUtils;
    }
}
