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

import android.net.Uri;

import io.taucoin.foundation.util.StringUtil;

public class UriUtil {

    public static String getBaseUrl(Uri uri) {
        if(uri != null){
            return uri.getScheme() +
                    "://" +
                    uri.getAuthority() +
                    "/";
        }
        return "";
    }

    public static String getPath(Uri uri) {
        if(uri != null){
            String path = uri.getPath();
            if(StringUtil.isNotEmpty(uri.getQuery())){
                path +="?" + uri.getQuery();
            }
            return path;
        }
        return "";
    }
}