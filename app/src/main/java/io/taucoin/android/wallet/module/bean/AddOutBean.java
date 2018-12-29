package io.taucoin.android.wallet.module.bean;

public class AddOutBean {

    private String time;
    private String txid;
    private String vout;
    private String addIn;
    private String addOut;

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
}