package io.taucoin.android.wallet.module.model;

import java.util.List;

import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;

public interface ITxModel {
    void getBalance(TAUObserver<RetResult<BalanceBean>> observer);

    void isAnyTxPending(LogicObserver<Boolean> observer);

    void getUTXOList();

    void getUTXOListLocal(LogicObserver<List<UTXORecord>> observer);

    void checkRawTransaction(String txId, LogicObserver<Boolean> observer);

    void getTxPendingList(LogicObserver<List<TransactionHistory>> observer);

    void createTransaction(TransactionHistory txHistory, LogicObserver<String> observer);

    void sendRawTransaction(String tx_hex, TAUObserver<RetResult<String>> observer);

    void updateTransactionHistory(TransactionHistory txHistory);

    void insertTransactionHistory(TransactionHistory txHistory);

    void queryTransactionHistory(int pageNo, String time, LogicObserver<List<TransactionHistory>> logicObserver);

}