package io.taucoin.android.wallet.db.util;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.TransactionHistoryDao;

import io.taucoin.android.wallet.db.entity.TransactionHistory;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by ly on 18-10-31
 *
 * @version 1.0
 * @description:
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

    /**
     * 插入数据 若未建表则先建表
     *　Insert data without building tables.
     * @param transactionHistory
     * @return
     */
    public boolean insertTransactionHistoryData(TransactionHistory transactionHistory) {
        boolean flag = false;

        flag = getTransactionHistoryDao().insert(transactionHistory) != -1;
        return flag;
    }

    /**
     * 插入或替换数据
     *　Insert or replace data
     * @param transactionHistory
     * @return
     */
    public boolean insertOrReplaceData(TransactionHistory transactionHistory) {
        boolean flag = false;
        try {
            flag = getTransactionHistoryDao().insertOrReplace(transactionHistory) != -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入多条数据  子线程完成
     *　Insert multiple data sub threads to complete
     * @param list
     * @return
     */
    public boolean insertOrReplaceMultiData(final List<TransactionHistory> list) {
        boolean flag = false;
        try {
            getTransactionHistoryDao().getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (TransactionHistory transactionHistory : list) {
                        daoManager.getDaoSession().insertOrReplace(transactionHistory);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 更新数据
     *　Update data
     * @param transactionHistory
     * @return
     */
    public boolean updateTransactionHistoryData(TransactionHistory transactionHistory) {
        boolean flag = false;
        try {
            getTransactionHistoryDao().update(transactionHistory);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public boolean deleteTransactionHistoryData(TransactionHistory transactionHistory) {
        boolean flag = false;
        try {
            getTransactionHistoryDao().delete(transactionHistory);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据id删除数据
     *
     * @param l
     * @return
     */
    public boolean deleteTransactionHistoryByKey(Long l) {
        boolean flag = false;
        try {
            getTransactionHistoryDao().deleteByKey(l);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有数据
     *　deleteAllData
     * @return
     */
    public boolean deleteAllData() {
        boolean flag = false;
        try {
            getTransactionHistoryDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据主键查询
     *　Query by primary key
     * @param key
     * @return
     */
    public TransactionHistory queryTransactionHistoryDataById(long key) {
        return getTransactionHistoryDao().load(key);
    }

    /**
     * 查询所有数据
     *　Query all data
     * @return
     */
    public List<TransactionHistory> queryAllData() {
        return getTransactionHistoryDao().loadAll();
    }


    /*public List<TransactionHistory> queryConfirmationsLow2() {
        return (List<TransactionHistory>) getTransactionHistoryDao().queryBuilder().where();
    }*/


    /**
     * 根据名称查询 以年龄降序排列
     *
     * @param name
     * @return
     */

    public List<TransactionHistory> queryTransactionByName(String name) {
        Query<TransactionHistory> build = null;
        try {
            build = getTransactionHistoryDao().queryBuilder()
                    .where(TransactionHistoryDao.Properties.TxId.eq(name))
                    .orderDesc(TransactionHistoryDao.Properties.Time)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return build.list();
    }
    /**
     * 根据参数查询
     *　Query based on parameters
     * @param where
     * @param param
     * @return
     */
    public List<TransactionHistory> queryTransactionHistoryByParams(String where, String... param) {
        return getTransactionHistoryDao().queryRaw(where, param);
    }

    public List<TransactionHistory>  queryTransactionHistoryByCondition(){

        return getTransactionHistoryDao().queryBuilder().where(TransactionHistoryDao.Properties.Confirmations.lt(2),TransactionHistoryDao.Properties.Result.notEq("Failed")).list();
    }



    public TransactionHistoryDao getTransactionHistoryDao() {
        return daoManager.getDaoSession().getTransactionHistoryDao();
    }

}
