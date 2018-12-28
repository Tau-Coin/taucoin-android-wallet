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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * Created by ly on 2018/1/23.
 */

public class MD5_BASE64Util {
    /**
     * MD5 and base64 encryption
     */

    public  static String EncoderByMd5_BASE64(String str){
        if (str==null)
            return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            return base64en.encode(str.getBytes("utf-8"));
           // return base64en.encode(md5.digest(str.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }


    }

}
