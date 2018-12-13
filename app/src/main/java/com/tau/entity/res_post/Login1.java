package com.tau.entity.res_post;

import java.io.Serializable;

public class Login1<T> implements Serializable {
    /**
     * {
     "status": "0",
     "message": "success",
     "ret":
     {
     {
     "coins": "1000",
     "utxo": "900",
     "reward": "100",
     }
     {
     "clubid": "0",
     "clubname": "tauminer0",
     "clubaddress": "Txkai123dhjkli9…",
     "cpower": "10000",
     "spower": "100",
     }
     }
     }
     */

    private int status;
    private String message;
    private T ret;

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




}
