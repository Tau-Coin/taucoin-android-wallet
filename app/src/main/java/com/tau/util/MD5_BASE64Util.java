package com.mofei.tau.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * Created by ly on 2018/1/23.
 */

public class MD5_BASE64Util {
    /**
     * MD5和base64加密
     * @param str 待加密的字符串
     */

    public  static String EncoderByMd5_BASE64(String str){
        if (str==null)
            return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
           // return base64en.encode(str.getBytes("utf-8"));
            return base64en.encode(md5.digest(str.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }


    }

}
