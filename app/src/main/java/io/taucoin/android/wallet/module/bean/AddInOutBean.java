package io.taucoin.android.wallet.module.bean;

import java.util.List;

public class AddInOutBean {

    private List<TxBean> received;
    private List<TxBean> sent;

    public List<TxBean> getReceived() {
        return received;
    }

    public void setReceived(List<TxBean> received) {
        this.received = received;
    }

    public List<TxBean> getSent() {
        return sent;
    }

    public void setSent(List<TxBean> sent) {
        this.sent = sent;
    }

}