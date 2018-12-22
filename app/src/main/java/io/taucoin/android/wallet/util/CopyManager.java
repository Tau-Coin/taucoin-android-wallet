package io.taucoin.android.wallet.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import io.taucoin.android.wallet.MyApplication;

public class CopyManager {

    public static void copyText(String copyText) {
        Context context = MyApplication.getInstance();
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, copyText));
    }
}
