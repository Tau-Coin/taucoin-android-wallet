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

import android.support.v4.app.FragmentActivity;

import com.mofei.tau.R;

import java.util.ArrayList;
import java.util.List;

import io.taucoin.foundation.util.StringUtil;
import io.taucoin.foundation.util.permission.EasyPermissions;

public class PermissionUtils {
    static final int REQUEST_PERMISSIONS_CAMERA = 2;
    public static final int REQUEST_PERMISSIONS_RECORD_STORAGE = 3;

    /*Check if the user has completely prohibited pop-up permission requests*/
    public static void checkUserBanPermission(FragmentActivity activity, String permission, int resMsg) {
        if (StringUtil.isEmpty(permission)) {
            return;
        }
        List<String> deniedPerms = new ArrayList<>();
        deniedPerms.add(permission);
        checkUserBanPermission(activity, deniedPerms, resMsg);
    }

    static void checkUserBanPermission(FragmentActivity activity, List<String> deniedPerms, int resMsg) {
        String message = activity.getString(resMsg);
        CharSequence positiveButton = activity.getString(R.string.common_setting);
        CharSequence negativeButton = activity.getString(R.string.common_cancel);
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(activity, message, positiveButton, negativeButton, deniedPerms);
    }
}