package com.mofei.tau.entity.res_post;

import java.io.Serializable;

public class Balance<T>  {

    /**


     "status": "0",
     "message": "success",
     "ret":{"pubkey": "030889AE6E237760ED2E70D0CA92F3CDDF7C78DB05D165868849DCAA8C20500F55", "utxo": 10000000, "rewards": 0, "coins": 10000000}

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
