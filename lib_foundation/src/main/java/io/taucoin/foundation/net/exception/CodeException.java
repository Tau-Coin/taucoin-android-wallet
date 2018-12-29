package io.taucoin.foundation.net.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CodeException {

    /*network error*/
    public static final int NETWORK_ERROR = 0x1;
    /*http error*/
    public static final int HTTP_ERROR = 0x2;
    /*json error*/
    public static final int JSON_ERROR = 0x3;
    /*unknown error*/
    public static final int UNKNOWN_ERROR = 0x4;
    /*runtime error*/
    public static final int RUNTIME_ERROR = 0x5;
    /*un know host error*/
    public static final int UN_KNOW_HOST_ERROR = 0x6;


    @IntDef({NETWORK_ERROR, HTTP_ERROR, RUNTIME_ERROR, UNKNOWN_ERROR, JSON_ERROR, UN_KNOW_HOST_ERROR})
    @Retention(RetentionPolicy.SOURCE)

    public @interface CodeEp {
    }

    public static Throwable getError(){
        return new Throwable("unknown error");
    }
}
