package com.mofei.tau.transaction;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.math.BigInteger;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by ly on 18-10-30
 *
 * @version 1.0
 * @description:
 */

@Entity
public class TransactionHistory {

    @Id
    private Long id;

    private String txId;

    private String sentOrReceived;

    private String fromAddress;

    private String toAddress;

    //private long time;
    private String time;

    private int confirmations;

    private String  value;

    private String result;

    private String message;

    // blockheight
    public long blockheight;

    // blocktime
    public long blocktime;

    @Generated(hash = 1525617385)
    public TransactionHistory(Long id, String txId, String sentOrReceived,
            String fromAddress, String toAddress, String time, int confirmations,
            String value, String result, String message, long blockheight,
            long blocktime) {
        this.id = id;
        this.txId = txId;
        this.sentOrReceived = sentOrReceived;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.time = time;
        this.confirmations = confirmations;
        this.value = value;
        this.result = result;
        this.message = message;
        this.blockheight = blockheight;
        this.blocktime = blocktime;
    }

    @Generated(hash = 63079048)
    public TransactionHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxId() {
        return this.txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getSentOrReceived() {
        return this.sentOrReceived;
    }

    public void setSentOrReceived(String sentOrReceived) {
        this.sentOrReceived = sentOrReceived;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getConfirmations() {
        return this.confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getBlockheight() {
        return this.blockheight;
    }

    public void setBlockheight(long blockheight) {
        this.blockheight = blockheight;
    }

    public long getBlocktime() {
        return this.blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }

     
    
    
}
