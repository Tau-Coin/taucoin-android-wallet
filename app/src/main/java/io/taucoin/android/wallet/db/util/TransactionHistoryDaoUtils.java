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
package io.taucoin.android.wallet.db.util;

import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.TransactionHistoryDao;

import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.foundation.util.StringUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @version 1.0
 * Created by ly on 18-10-31
 * @version 2.0
 * Edited by yang
 * @description: TransactionHistory
 */
public class TransactionHistoryDaoUtils {

    private final GreenDaoManager daoManager;
    private static TransactionHistoryDaoUtils mTransactionHistoryDaoUtils;

    private TransactionHistoryDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static TransactionHistoryDaoUtils getInstance() {
        if (mTransactionHistoryDaoUtils == null) {
            mTransactionHistoryDaoUtils = new TransactionHistoryDaoUtils();
        }
        return mTransactionHistoryDaoUtils;
    }

    private TransactionHistoryDao getTransactionHistoryDao() {
        return daoManager.getDaoSession().getTransactionHistoryDao();
    }


    public boolean isAnyTxPending(String formAddress) {
        long size = getTxPendingList(formAddress).size();
        return size > 0;
    }

    public List<TransactionHistory> getTxPendingList(String formAddress) {
        QueryBuilder qb = getTransactionHistoryDao().queryBuilder();
        return getTransactionHistoryDao().queryBuilder()
        .where(TransactionHistoryDao.Properties.Confirmations.lt(1),
                TransactionHistoryDao.Properties.FromAddress.eq(formAddress),
                TransactionHistoryDao.Properties.SentOrReceived.eq(TransmitKey.TxType.SEND),
                qb.or(TransactionHistoryDao.Properties.Result.eq(TransmitKey.TxResult.CONFIRMING),
                    TransactionHistoryDao.Properties.Result.eq(TransmitKey.TxResult.SUCCESSFUL)))
        .list();
    }

    public TransactionHistory queryTransactionById(String txId) {
        List<TransactionHistory> list = getTransactionHistoryDao().queryBuilder()
                .where(TransactionHistoryDao.Properties.TxId.eq(txId))
                .orderDesc(TransactionHistoryDao.Properties.Time)
                .list();
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public void insertOrReplace(TransactionHistory tx) {
        getTransactionHistoryDao().insertOrReplace(tx);
    }

    public void saveAddOut(TransactionHistory tx) {
        QueryBuilder<TransactionHistory> db = getTransactionHistoryDao().queryBuilder();
        db.where(TransactionHistoryDao.Properties.TxId.eq(tx.getTxId()),
            TransactionHistoryDao.Properties.FromAddress.eq(tx.getFromAddress()),
            TransactionHistoryDao.Properties.ToAddress.eq(tx.getToAddress()),
            TransactionHistoryDao.Properties.SentOrReceived.eq(tx.getSentOrReceived())
        );

        List<TransactionHistory> list = db.list();
        if(list.size() > 0){
            TransactionHistory bean = list.get(0);
            if(StringUtil.isNotSame(bean.getResult(), tx.getResult())){
                bean.setResult(tx.getResult());
                insertOrReplace(bean);
            }
        }else{
            insertOrReplace(tx);
        }
    }

    public List<TransactionHistory> queryData(int pageNo, String time, String address) {
         QueryBuilder<TransactionHistory> db = getTransactionHistoryDao().queryBuilder();
         db.where(TransactionHistoryDao.Properties.Time.lt(time),
                db.or(db.and(TransactionHistoryDao.Properties.FromAddress.eq(address),
                    TransactionHistoryDao.Properties.SentOrReceived.eq(TransmitKey.TxType.SEND)),
                    db.and(TransactionHistoryDao.Properties.ToAddress.eq(address),
                    TransactionHistoryDao.Properties.SentOrReceived.eq(TransmitKey.TxType.RECEIVE)))
                )
            .orderDesc(TransactionHistoryDao.Properties.Time, TransactionHistoryDao.Properties.Blocktime)
            .offset((pageNo - 1) * TransmitKey.PAGE_SIZE).limit(TransmitKey.PAGE_SIZE);
        return db.list();
    }

    public void updateOldTxHistory() {
        QueryBuilder<TransactionHistory> builder = getTransactionHistoryDao().queryBuilder();
        builder.where(builder.or(TransactionHistoryDao.Properties.FromAddress.eq(""),
                TransactionHistoryDao.Properties.FromAddress.isNull()));
        builder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /** It's your latest time to accept your address here.*/
    public String getNewestTxTime(String address) {
        long time = 0L;
        QueryBuilder<TransactionHistory> db = getTransactionHistoryDao().queryBuilder();
        db.where(TransactionHistoryDao.Properties.Result.isNull(),
            db.or(db.and(TransactionHistoryDao.Properties.FromAddress.eq(address),
                TransactionHistoryDao.Properties.SentOrReceived.eq(TransmitKey.TxType.SEND)),
                db.and(TransactionHistoryDao.Properties.ToAddress.eq(address),
                TransactionHistoryDao.Properties.SentOrReceived.eq(TransmitKey.TxType.RECEIVE)))
        )
        .orderDesc(TransactionHistoryDao.Properties.Blocktime);
        List<TransactionHistory> list = db.list();
        if(list.size() > 0){
            time = list.get(0).getBlocktime();
        }
        return String.valueOf(time);
    }
}