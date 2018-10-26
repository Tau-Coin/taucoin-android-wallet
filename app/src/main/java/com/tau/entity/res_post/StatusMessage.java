package com.mofei.tau.entity.res_post;

import java.io.Serializable;

public class StatusMessage {
    /**
     * "status": "0",
     "message": "success",
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
}
