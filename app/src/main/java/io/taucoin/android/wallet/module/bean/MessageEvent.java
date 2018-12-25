package io.taucoin.android.wallet.module.bean;

public class MessageEvent<T> {

    public enum EventCode {
        BALANCE,
        NICKNAME,
        AVATAR
    }

    private EventCode code;
    private T data;
}