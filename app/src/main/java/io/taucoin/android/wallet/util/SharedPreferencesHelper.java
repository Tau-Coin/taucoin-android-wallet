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
import android.content.SharedPreferences;
import android.util.Log;

import io.taucoin.android.wallet.MyApplication;

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper sharedPreferencesHelper = null;

    public static SharedPreferencesHelper getInstance() {
        Context context = MyApplication.getInstance();
        if (sharedPreferencesHelper == null) {
            synchronized (SharedPreferencesHelper.class) {
                if (sharedPreferencesHelper == null) {
                    sharedPreferencesHelper = new SharedPreferencesHelper();
                    sharedPreferencesHelper.setContext(context);
                    return sharedPreferencesHelper;
                }
            }
        }

        return sharedPreferencesHelper;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean getBoolean(String key, boolean defValue) {
        try {
            return getSP().getBoolean(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
            return defValue;
        }
    }

    public void putBoolean(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putBoolean(key, value);
            editor.apply();
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
        }
    }

    public long getLong(String key, long defValue) {
        try {
            return getSP().getLong(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
            return defValue;
        }
    }
    public void putLong(String key, long value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putLong(key, value);
            editor.apply();
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return getSP().getInt(key, defaultValue);
        } catch (Exception e) {
            Log.d("hcj", "" + e);
            return defaultValue;

        }
    }

    public void putInt(String key, int value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
            Log.d("hcj", "" + e);
        }
    }

    public String getString(String key, String defValue) {
        try {
            return getSP().getString(key, defValue);
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
            return defValue;
        }
    }

    public void putString(String key, String value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putString(key, value);
            editor.apply();
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
        }
    }

    public void clear() {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.clear();
            editor.apply();
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
        }
    }

    public SharedPreferences getSP() {
        return context.getSharedPreferences("sp", Context.MODE_MULTI_PROCESS);
    }
}
