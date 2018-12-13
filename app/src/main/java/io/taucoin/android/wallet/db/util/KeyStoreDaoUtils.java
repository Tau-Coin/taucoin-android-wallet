package io.taucoin.android.wallet.db.util;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.KeyStoreDao;

import io.taucoin.android.wallet.db.entity.KeyStore;
import io.taucoin.android.wallet.db.entity.TransactionHistory;

import java.util.List;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */
public class KeyStoreDaoUtils {

    private final GreenDaoManager daoManager;
    private static KeyStoreDaoUtils mKeyStoreDaoUtils;

    public KeyStoreDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static KeyStoreDaoUtils getInstance() {
        if (mKeyStoreDaoUtils == null) {
            mKeyStoreDaoUtils = new KeyStoreDaoUtils();
        }
        return mKeyStoreDaoUtils;
    }

    /**
     * 插入数据 若未建表则先建表
     *　Insert data without building tables.
     * @param keyStore
     * @return
     */
    public boolean insertKeyStoreData(KeyStore keyStore) {
        boolean flag = false;

        flag = getKeyStoreDao().insert(keyStore) != -1;
        return flag;
    }

    /**
     * 插入或替换数据
     *　Insert or replace data
     * @param keyStore
     * @return
     */
    public boolean insertOrReplaceData(KeyStore keyStore) {
        boolean flag = false;
        try {
            flag = getKeyStoreDao().insertOrReplace(keyStore) != -1;
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
            getKeyStoreDao().getSession().runInTx(new Runnable() {
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
     * @param keyStore
     * @return
     */
    public boolean updateTransactionHistoryData(KeyStore keyStore) {
        boolean flag = false;
        try {
            getKeyStoreDao().update(keyStore);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public boolean deleteTransactionHistoryData(KeyStore keyStore) {
        boolean flag = false;
        try {
            getKeyStoreDao().delete(keyStore);
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
            getKeyStoreDao().deleteByKey(l);
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
            getKeyStoreDao().deleteAll();
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
    public KeyStore queryKeyStoreDataById(long key) {
        return getKeyStoreDao().load(key);
    }

    /**
     * 查询所有数据
     *　Query all data
     * @return
     */
    public List<KeyStore> queryAllData() {
        return getKeyStoreDao().loadAll();
    }

    /**
     * 根据名称查询 以年龄降序排列
     *
     * @param name
     * @return
     */
    /*
    public List<Accounts> queryUserByName(String name) {
        Query<Accounts> build = null;
        try {
            build = getAccountsDao().queryBuilder()
                    .where(AccountsDao.Properties.Name.eq(name))
                    .orderDesc(AccountsDao.Properties.Age)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return build.list();
    }
*/
    /**
     * 根据参数查询
     *　Query based on parameters
     * @param where
     * @param param
     * @return
     */
    public List<KeyStore> queryKeyStoreByParams(String where, String... param) {
        return getKeyStoreDao().queryRaw(where, param);
    }

    public KeyStoreDao getKeyStoreDao() {
        return daoManager.getDaoSession().getKeyStoreDao();
    }

}
