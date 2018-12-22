package io.taucoin.android.wallet.module.view.main.iview;

import java.util.List;

import io.taucoin.android.wallet.db.entity.TransactionHistory;

public interface ISendReceiveView {

    void initData();
    void initView();
    void initListener();
    void loadTransactionHistory(List<TransactionHistory> txHistories);
}
