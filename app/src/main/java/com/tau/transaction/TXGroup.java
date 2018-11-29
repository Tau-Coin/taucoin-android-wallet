package com.mofei.tau.transaction;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description:
 */
public class TXGroup {
    private String amount;
    private String time;
    public int getConfoirmation() {
        return confoirmation;
    }

    public void setConfoirmation(int confoirmation) {
        this.confoirmation = confoirmation;
    }

    private int confoirmation;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
