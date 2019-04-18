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

import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class FixMemLeak {
    private static Field field;
    private static boolean hasField = true;

    public static void fixLeak(Context context) {
        fixHuaWeiLeak(context);
        fixSamSungLeak(context);
    }

    private static void fixHuaWeiLeak(Context context) {
        if (!hasField) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView"};
        for (String param : arr) {
            try {
                if (field == null) {
                    field = imm.getClass().getDeclaredField(param);
                }
                if (field == null) {
                    hasField = false;
                }
                if (field != null) {
                    field.setAccessible(true);
                    field.set(imm, null);
                }
            } catch (Throwable ignore) {
            }
        }
    }
    private static void fixSamSungLeak(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.MANUFACTURER.equals("samsung")) {
                Object systemService = context.getSystemService(Class.forName("com.samsung.android.content.clipboard.SemClipboardManager"));
                Field mContext = systemService.getClass().getDeclaredField("mContext");
                mContext.setAccessible(true);
                mContext.set(systemService, null);
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignore) {
        }
    }
}