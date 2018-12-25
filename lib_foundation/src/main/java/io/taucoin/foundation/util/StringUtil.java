package io.taucoin.foundation.util;

import android.view.View;
import android.widget.TextView;

import com.github.naturs.logger.Logger;

import java.math.BigInteger;

public class StringUtil {

    public static String formatString(String str, Object... replace) {
        try {
            if (isNotEmpty(str)) {
                str = String.format(str, replace);
            }
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }

        return str;
    }

    public static String formatString(String str, double replace) {
        try {
            if (isNotEmpty(str)) {
                str = String.format(str, String.valueOf(replace));
            }
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
        return str;
    }

    public static boolean isSame(String a, String b) {
        if (isEmpty(a)) {
            return isEmpty(b);
        } else if (isEmpty(b)) {
            return isEmpty(a);
        } else {
            return a.equals(b);
        }
    }

    public static boolean isNotSame(String a, String b) {
        return !isSame(a, b);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    public static String trim(String string) {
        if (isEmpty(string)) {
            return "";
        }
        return string.trim();
    }

    /**
     * <p>Bytes to hex string</p>
     *
     * @param bytes       this source bytes array
     * @param lengthToPad length to pad
     * @return hex string
     */
    public static String toHexString(byte[] bytes, int lengthToPad) {
        BigInteger hash = new BigInteger(1, bytes);
        String digest = hash.toString(16);

        while (digest.length() < lengthToPad) {
            digest = "0" + digest;
        }
        return digest;
    }

    public static double toDouble(String num) {
        try{
            return Double.valueOf(num);
        }catch (Exception e){
            return 0;
        }
    }

    public static String getText(TextView view) {
        try{
            return view.getText().toString();
        }catch (Exception e){
            return "";
        }
    }

    public static int getIntText(TextView view) {
        try{
            return Integer.valueOf(getText(view));
        }catch (Exception e){
            return 0;
        }
    }

    public static String getTag(View view) {
        try{
            return view.getTag().toString();
        }catch (Exception e){
            return "";
        }
    }

    public static int getIntTag(View view) {
        try{
            return Integer.valueOf(getTag(view));
        }catch (Exception e){
            return 0;
        }
    }

    public static int getIntString(String data) {
        try{
            return Integer.valueOf(data);
        }catch (Exception e){
            return 0;
        }
    }

    public static String getString(int data) {
        try{
            return String.valueOf(data);
        }catch (Exception e){
            return "";
        }
    }

    public static String getString(Object data) {
        try{
            return data.toString();
        }catch (Exception e){
            return "";
        }
    }

    public static String encryptPhone(String photo) {
        if(isNotEmpty(photo) && photo.length() > 7){
            photo = photo.substring(0, 3) + "****" + photo.substring(7);
        }
        return photo;
    }
    public static boolean isAddressValid(String address) {
        String regex = "^T[a-zA-Z0-9_]{33,}$";
        return address.matches(regex);
    }
}
