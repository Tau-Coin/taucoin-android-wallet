package io.taucoin.android.wallet.module.model;

import java.util.List;

import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.net.callBack.TAUObserver;
import io.taucoin.foundation.net.callback.LogicObserver;

public interface ITxModel {
    void getBalance(TAUObserver observer);

    void getBalanceLocal(LogicObserver observer);

    void isAnyTxPending(LogicObserver observer);

    void getUTXOList();

    void getUTXOListLocal(LogicObserver<List<TransactionHistory>> observer);

    void checkRawTransaction(String txId, LogicObserver<Boolean> observer);

    void getTxPendingList(LogicObserver<List<TransactionHistory>> observer);

//    void createTransaction(LogicObserver observer);
//
//    void sendRawTransaction(String tx_hex);
}
