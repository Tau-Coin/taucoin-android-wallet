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

    public static void post(MessageEvent messageEvent){
        EventBus.getDefault().post(messageEvent);
    }
}