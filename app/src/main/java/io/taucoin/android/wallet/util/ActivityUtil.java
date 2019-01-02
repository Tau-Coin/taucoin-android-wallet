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

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Description: Activity tools
 * Author:wuQy
 * Date: 2019/01/02
 */
public class ActivityUtil {

    public static void startActivity(FragmentActivity context, Class<?> zClass){
        Intent intent = new Intent(context, zClass);
        context.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class<?> zClass){
        Intent intent = new Intent(fragment.getActivity(), zClass);
        fragment.startActivity(intent);
    }

    public static void startActivity(Intent intent, FragmentActivity context, Class<?> zClass){
        intent.setClass(context, zClass);
        context.startActivity(intent);
    }

    public static void startActivity(Intent intent, Fragment fragment, Class<?> zClass){
        intent.setClass(fragment.getActivity(), zClass);
        fragment.startActivity(intent);
    }
}