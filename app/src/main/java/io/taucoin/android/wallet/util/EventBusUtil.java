package io.taucoin.android.wallet.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

import io.taucoin.android.wallet.module.bean.MessageEvent;

public class EventBusUtil {
    public static boolean isRegistered(FragmentActivity activity){
        return EventBus.getDefault().isRegistered(activity);
    }

    public static boolean isRegistered(Fragment fragment){
        return EventBus.getDefault().isRegistered(fragment);
    }

    public static void register(FragmentActivity activity){
        EventBus.getDefault().register(activity);
    }

    public static void unregister(FragmentActivity activity){
        EventBus.getDefault().unregister(activity);
    }

    public static void register(Fragment fragment){
        EventBus.getDefault().register(fragment);
    }

    public static void unregister(Fragment fragment){
        EventBus.getDefault().unregister(fragment);
    }

    public static void post(MessageEvent.EventCode code){
        MessageEvent msg = new MessageEvent();
        msg.setCode(code);
        EventBus.getDefault().post(msg);
    }

    public static void postSticky(MessageEvent.EventCode code){
        MessageEvent msg = new MessageEvent();
        msg.setCode(code);
        EventBus.getDefault().postSticky(msg);
    }

    public static MessageEvent getMessageEvent(MessageEvent.EventCode code){
        MessageEvent msg = new MessageEvent();
        msg.setCode(code);
        return msg;
    }
}