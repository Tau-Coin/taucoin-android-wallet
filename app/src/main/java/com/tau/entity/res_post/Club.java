package com.mofei.tau.entity.res_post;

import android.renderscript.ScriptIntrinsicYuvToRGB;

public class Club<T> {
    /**
     * {
     "status": "0",
     "message": "success",
     "ret":
     {
     "clubid": "0",
     "clubname": "tauminer0",
     "clubaddress": "Txkai123dhjkli9…",
     "cpower": "10000",
     "spower": "100",
     }
     }
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getRet() {
        return ret;
    }

    public void setRet(T ret) {
        this.ret = ret;
    }

    private T ret;


}
