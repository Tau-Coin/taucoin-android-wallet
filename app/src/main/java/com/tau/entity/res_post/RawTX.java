package com.mofei.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTX <T>{
    /**
     * "status": "0",
     "message": "ok",
     "ret":
     */
    private String status;
    private String message;
    private T ret;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
