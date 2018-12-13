package com.tau.entity.res_put;

import java.io.Serializable;

public class Login0<T>  {

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
