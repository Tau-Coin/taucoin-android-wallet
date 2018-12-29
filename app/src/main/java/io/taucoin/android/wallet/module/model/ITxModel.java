/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.module.model;

import java.util.List;

import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.module.bean.AddOutBean;
import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.net.callback.TAUObserver;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.net.callback.LogicObserver;
import io.taucoin.foundation.net.callback.RetResult;

public interface ITxModel {
    /** Get balance from the server */
    void getBalance(TAUObserver<RetResult<BalanceBean>> observer);

    /** Whether a Pending transaction */
    void isAnyTxPending(LogicObserver<Boolean> observer);

    /** Get UTXO list from the server */
    void getUTXOList();

    /** Get local UTXO list */
    void getUTXOListLocal(LogicObserver<List<UTXORecord>> observer);

    /** Detecting whether a transaction enters the trading pool and block chain */
    void checkRawTransaction(String txId, LogicObserver<Boolean> observer);

    /** Get the list of transactions to be Pending */
    void getTxPendingList(LogicObserver<List<TransactionHistory>> observer);

    /** Create transaction data */
    void createTransaction(TransactionHistory txHistory, LogicObserver<String> observer);

    /** Send transaction to the server */
    void sendRawTransaction(String tx_hex, TAUObserver<RetResult<String>> observer);

    /** Update local transaction history */
    void updateTransactionHistory(TransactionHistory txHistory, LogicObserver<Boolean> observer);

    /** Insert local transaction history */
    void insertTransactionHistory(TransactionHistory txHistory);

    /** Get local transaction history */
    void queryTransactionHistory(int pageNo, String time, LogicObserver<List<TransactionHistory>> logicObserver);

    /** Get the transaction history of the server  */
    void getAddOuts(TAUObserver<DataResult<List<AddOutBean>>> observer);

    /** Save the transaction history of the server  */
    void saveAddOuts(List<AddOutBean> list, LogicObserver<Boolean> observer);

}