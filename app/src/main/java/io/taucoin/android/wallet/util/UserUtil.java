/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.ReferralInfo;
import io.taucoin.android.wallet.widget.BreakTextSpan;
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
        if(tvNick == null){
            return;
        }
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            return;
        }
        String nickName = parseNickName(keyValue);
        tvNick.setText(nickName);
        Logger.d("UserUtil.setNickName=" + nickName);
    }

    public static void setBalance(TextView tvBalance) {
        if(tvBalance == null){
            return;
        }
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            setBalance(tvBalance, 0L);
            return;
        }
        setBalance(tvBalance, keyValue.getUtxo());
    }

    private static void setBalance(TextView tvBalance, long balance) {
        String balanceStr = MyApplication.getInstance().getResources().getString(R.string.common_balance);
        balanceStr = String.format(balanceStr, FmtMicrometer.fmtBalance(balance));
        tvBalance.setText(Html.fromHtml(balanceStr));
        Logger.d("UserUtil.setBalance=" + balanceStr);
    }

    public static boolean isHaveLlink() {
        KeyValue keyValue = MyApplication.getKeyValue();
        return  keyValue != null && StringUtil.isNotEmpty(keyValue.getReferralLink());
    }

    public static boolean isImportKey() {
        KeyValue keyValue = MyApplication.getKeyValue();
        return  keyValue != null;
    }

    public static void setAddress(TextView tvAddress) {
        if(tvAddress == null){
            return;
        }
        KeyValue keyValue = MyApplication.getKeyValue();
        String newAddress = "";
        if(keyValue != null){
            newAddress = keyValue.getAddress();
        }
        String oldAddress = StringUtil.getText(tvAddress);
        if(StringUtil.isNotSame(newAddress, oldAddress)){
            String address = MyApplication.getInstance().getResources().getString(R.string.send_tx_address);
            address = String.format(address, newAddress);
            tvAddress.setText(address);
        }
        int visibility = StringUtil.isEmpty(newAddress) ? View.GONE : View.VISIBLE;
        tvAddress.setVisibility(visibility);
    }

    public static void loadReferralView(TextView tvReferralLink, Object object) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            return;
        }
        if(tvReferralLink != null){
            String link = keyValue.getReferralLink();
            if(object != null && StringUtil.isEmpty(link)){
                if(object instanceof Boolean){
                    Boolean isSuccess = (Boolean) object;
                    if(!isSuccess){
                        link = ResourcesUtil.getText(R.string.main_referral_link_failed);
                    }
                }
            }
            if(StringUtil.isEmpty(link)){
                link = StringUtil.getText(tvReferralLink);
            }
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append(link);
            stringBuilder.setSpan(new BreakTextSpan(tvReferralLink, link), 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvReferralLink.setText(stringBuilder);
        }
    }

    public static void loadRewardView(ReferralInfo referralInfo, TextView tvYourInvited, TextView tvFriendReferral) {
        if(tvYourInvited != null){
            String referral = ResourcesUtil.getText(R.string.main_your_referral);
            long num = referralInfo.getInvitedReward();
            referral = String.format(referral, num);
            tvYourInvited.setText(Html.fromHtml(referral));
        }
        if(tvFriendReferral != null){
            String referral = ResourcesUtil.getText(R.string.main_friend_referral);
            long num = referralInfo.getFriendReward();
            referral = String.format(referral, num);
            tvFriendReferral.setText(Html.fromHtml(referral));
        }
    }

    public static void setInvitedView(TextView tvYourInvited) {
        KeyValue keyValue = MyApplication.getKeyValue();
        if(keyValue == null){
            return;
        }
        if(tvYourInvited != null){
            String invitedFriends = ResourcesUtil.getText(R.string.main_invited_friends);
            int num = keyValue.getInvitedFriends();
            invitedFriends = String.format(invitedFriends, num);
            tvYourInvited.setText(Html.fromHtml(invitedFriends));
        }
    }
}