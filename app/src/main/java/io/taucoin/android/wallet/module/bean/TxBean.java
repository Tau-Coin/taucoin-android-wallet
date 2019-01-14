package io.taucoin.android.wallet.module.bean;

import com.google.gson.annotations.SerializedName;

public class TxBean {
    private String time;
    private String txid;
    private String vout;
    @SerializedName("addin")
    private String addIn;
    @SerializedName("addout")
    private String addOut;
    @SerializedName("block_height")
    private long blockHeight;
    private String fee;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getVout() {
        return vout;
    }

    public void setVout(String vout) {
        this.vout = vout;
    }

    public String getAddIn() {
        return addIn;
    }

    public void setAddIn(String addIn) {
        this.addIn = addIn;
    }

    public String getAddOut() {
        return addOut;
    }

    public void setAddOut(String addOut) {
        this.addOut = addOut;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
