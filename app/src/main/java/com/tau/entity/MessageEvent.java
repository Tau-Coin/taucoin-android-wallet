package com.mofei.tau.entity;

/**
 * Created by uid11497 on 2018/2/1.
 */

public class MessageEvent {

    private boolean isSuccess;
    private String tag;
    private String mMsg;

    public MessageEvent(String msg) {

    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }
}
