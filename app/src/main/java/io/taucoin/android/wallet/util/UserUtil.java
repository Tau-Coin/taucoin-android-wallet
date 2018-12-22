package io.taucoin.android.wallet.util;

import android.graphics.Bitmap;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.util.StringUtil;

public class UserUtil {

    private static String parseNickName(KeyValue keyValue) {
        String nickName = null;
        if(keyValue != null){
            nickName = keyValue.getNickName();
            if(StringUtil.isEmpty(nickName)){
                String address = keyValue.getAddress();
                if(StringUtil.isNotEmpty((address))){
                    int length = address.length();
                    nickName = length < 6 ? address : address.substring(length - 6);
                }
            }
        }
        return nickName;
    }

    public static void setNickName(TextView tvNick) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            return;
        }
        String nickName = parseNickName(keyValue);
        tvNick.setText(nickName);
    }

    public static void setBalance(TextView tvBalance) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            return;
        }
        setBalance(tvBalance, keyValue.getBalance());
    }

    public static void setBalance(TextView tvBalance, long balance) {
        String balanceStr = tvBalance.getResources().getString(R.string.common_balance);
        balanceStr = String.format(balanceStr, FmtMicrometer.fmtBalance(balance));
        tvBalance.setText(Html.fromHtml(balanceStr));
    }

    public static void setAvatar(ImageView ivAvatar) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue != null){
            Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                Bitmap bitmap = FileUtil.getBitmap(keyValue.getHeaderImage());
                emitter.onNext(bitmap);
            }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new LogicObserver<Bitmap>() {
                    @Override
                    public void handleData(Bitmap bitmap) {
                        ivAvatar.setImageBitmap(bitmap);
                    }
                });
        }
    }
}