package com.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class Height {
    /**
     * {
     "status": "0",
     "message": "ok",
     "height": 83450
     }
     { "status": "401",
     "message": "Fail to get height"
     }
     */

    private String status;
    private String message;
    private int  height;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
