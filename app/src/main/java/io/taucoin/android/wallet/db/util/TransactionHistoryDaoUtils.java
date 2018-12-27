package io.taucoin.android.wallet.db.util;

import com.mofei.tau.BuildConfig;

import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.TransactionHistoryDao;

import io.taucoin.android.wallet.db.entity.TransactionHistory;
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
        if(BuildConfig.DEBUG){
            return false;
        }
        long size = getTxPendingList(formAddress).size();
        return size > 0;
    }

    public List<TransactionHistory> getTxPendingList(String formAddress) {
        QueryBuilder qb = getTransactionHistoryDao().queryBuilder();
        return getTransactionHistoryDao().queryBuilder()
        .where(TransactionHistoryDao.Properties.Confirmations.lt(1),
                TransactionHistoryDao.Properties.FromAddress.eq(formAddress),
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

    public long insertOrReplace(TransactionHistory tx) {
        return getTransactionHistoryDao().insertOrReplace(tx);
    }

    public List<TransactionHistory> queryData(int pageNo, String time, String address) {
        return getTransactionHistoryDao().queryBuilder()
                .where(TransactionHistoryDao.Properties.FromAddress.eq(address),
                        TransactionHistoryDao.Properties.Time.lt(time))
                .orderDesc(TransactionHistoryDao.Properties.Time)
                .offset((pageNo - 1) * TransmitKey.PAGE_SIZE).limit(TransmitKey.PAGE_SIZE)
                .list();
    }

    public void updateOldTxHistory(String address) {
        QueryBuilder<TransactionHistory> db = getTransactionHistoryDao().queryBuilder();
        db.where(db.or(TransactionHistoryDao.Properties.FromAddress.eq(""),
                TransactionHistoryDao.Properties.FromAddress.isNull()));
        List<TransactionHistory> list = db.list();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFromAddress(address);
        }
        getTransactionHistoryDao().updateInTx(list);
    }
}