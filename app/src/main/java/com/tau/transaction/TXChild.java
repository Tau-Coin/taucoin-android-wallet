package com.mofei.tau.transaction;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description:
 */
public class TXChild {
    private String address;
    private String txId;
    private String txFee;
    private String txBlockHeight;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTxFee() {
        return txFee;
    }

    public void setTxFee(String txFee) {
        this.txFee = txFee;
    }

    public String getTxBlockHeight() {
        return txBlockHeight;
    }

    public void setTxBlockHeight(String txBlockHeight) {
        this.txBlockHeight = txBlockHeight;
    }
}
