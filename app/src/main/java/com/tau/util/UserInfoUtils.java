package com.mofei.tau.util;

import android.content.Context;

import com.mofei.tau.activity.LoginActivity2;
import com.mofei.tau.info.SharedPreferencesHelper;

/**
 * Created by ly on 18-11-28
 *
 * @version 1.0
 * @description:
 */
public class UserInfoUtils {

    public static String getCurrentEmail(Context context) {
        return SharedPreferencesHelper.getInstance(context).getString("email", "");
    }

    public static String getPublicKey(Context context) {
        String user = getCurrentEmail(context);
        return SharedPreferencesHelper.getInstance(context).getString(user + "Pubkey", "");
    }

    public static String getPrivateKey(Context context) {
        String user = getCurrentEmail(context);
        return SharedPreferencesHelper.getInstance(context).getString(user + "Privkey", "");
    }

    public static String getAddress(Context context) {
        String user = getCurrentEmail(context);
        return SharedPreferencesHelper.getInstance(context).getString(user + "Address", "");
    }

    public static void setCurrentEmail(Context context, String email) {
        SharedPreferencesHelper.getInstance(context).putString("email", email);
    }

    public static void setPublicKey(Context context, String pubkey) {
        String user = getCurrentEmail(context);
        SharedPreferencesHelper.getInstance(context).putString(user + "Pubkey", pubkey);
    }

    public static void setPrivateKey(Context context, String privkey) {
        String user = getCurrentEmail(context);
        SharedPreferencesHelper.getInstance(context).putString(user + "Privkey", privkey);
    }

    public static void setAddress(Context context, String address) {
        String user = getCurrentEmail(context);
        SharedPreferencesHelper.getInstance(context).putString(user + "Address", address);
    }

    // SharedPreference upgrade
    public static void sharedPreferenceUpgrade(Context context) {
        // Firstly, get old email, pubkey and privkey
        String oldEmail = getCurrentEmail(context);
        String oldPubkey = SharedPreferencesHelper.getInstance(context).getString("Pubkey", "");
        String oldPrivkey = SharedPreferencesHelper.getInstance(context).getString("Privkey", "");
        String oldAddress = SharedPreferencesHelper.getInstance(context).getString("Address", "");

        L.d("onUpgrade, email:" + oldEmail + ",address:" + oldAddress + ", pubkey:" + oldPubkey
                + ", privkey:" + oldPrivkey);
        if (!oldEmail.isEmpty() && !oldAddress.isEmpty() && !oldPubkey.isEmpty() && !oldPrivkey.isEmpty()) {
            setCurrentEmail(context, oldEmail);
            setPublicKey(context, oldPubkey);
            setPrivateKey(context, oldPrivkey);
            setAddress(context, oldAddress);
        } else {
            L.d("Can't restore old data");
        }
    }
}
