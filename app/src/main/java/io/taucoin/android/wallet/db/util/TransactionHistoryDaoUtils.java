package io.taucoin.android.wallet.db.util;

import com.mofei.tau.BuildConfig;

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

    public TransactionHistoryDaoUtils() {
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
        List<TransactionHistory> list = getTransactionHistoryDao().queryBuilder()
        .where(TransactionHistoryDao.Properties.Confirmations.lt(1),
                TransactionHistoryDao.Properties.FromAddress.eq(formAddress),
                qb.or(TransactionHistoryDao.Properties.Result.eq("Confirming"),
                    TransactionHistoryDao.Properties.Result.eq("Successful")))
        .list();
        return list;
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

    public List<TransactionHistory> queryAllData(String address) {
        return getTransactionHistoryDao().queryBuilder()
                .where(TransactionHistoryDao.Properties.FromAddress.eq(address))
                .orderDesc(TransactionHistoryDao.Properties.Time)
                .list();
    }
}
