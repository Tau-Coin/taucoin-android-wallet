package io.taucoin.android.wallet.db.util;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.UTXORecordDao;

import io.taucoin.android.wallet.db.entity.UTXORecord;

import java.util.List;

/**
 * Created by ly on 18-10-31
 *
 * @version 1.0
 * @description:
 */
public class UTXORecordDaoUtils {

    private final GreenDaoManager daoManager;
    private static UTXORecordDaoUtils mUserDaoUtils;

    public UTXORecordDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static UTXORecordDaoUtils getInstance() {
        if (mUserDaoUtils == null) {
            mUserDaoUtils = new UTXORecordDaoUtils();
        }
        return mUserDaoUtils;
    }

    /**
     * 插入数据 若未建表则先建表
     * Insert data without building tables.
     *
     * @param utxoRecord
     * @return
     */
    public boolean insertUTXORecordData(UTXORecord utxoRecord) {
        boolean flag = false;
        flag = getUTXORecordDao().insert(utxoRecord) == -1 ? false : true;
        return flag;
    }

    /**
     * 插入或替换数据
     * Insert or replace data
     *
     * @param utxoRecord
     * @return
     */
    public boolean insertOrReplaceData(UTXORecord utxoRecord) {
        boolean flag = false;
        try {
            flag = getUTXORecordDao().insertOrReplace(utxoRecord) == -1 ? false : true;
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
    public boolean insertOrReplaceMultiData(final List<UTXORecord> list) {
        boolean flag = false;
        try {
            getUTXORecordDao().getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (UTXORecord utxoRecord : list) {
                        daoManager.getDaoSession().insertOrReplace(utxoRecord);
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
     *　updata data
     * @param utxoRecord
     * @return
     */
    public boolean updateUTXORecordData(UTXORecord utxoRecord) {
        boolean flag = false;
        try {
            getUTXORecordDao().update(utxoRecord);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据id删除数据
     * delete data by id
     * @param utxoRecord
     * @return
     */
    public boolean deleteUserData(UTXORecord utxoRecord) {
        boolean flag = false;
        try {
            getUTXORecordDao().delete(utxoRecord);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有数据
     * delete all data
     * @return
     */
    public boolean deleteAllData() {
        boolean flag = false;
        try {
            getUTXORecordDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据主键查询
     * query by primary key
     * @param key
     * @return
     */
    public UTXORecord queryUTXORecordDataById(long key) {
        return getUTXORecordDao().load(key);
    }

    /**
     * 查询所有数据
     * query all
     * @return
     */
    public List<UTXORecord> queryAllData() {
        return getUTXORecordDao().loadAll();
    }

    /* *//**
     * 根据名称查询 以年龄降序排列
     *
     * @param name
     * @return
     *//*
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
     * query by param
     * @param where
     * @param param
     * @return
     */
    public List<UTXORecord> queryUTXORecordByParams(String where, String... param) {
        return getUTXORecordDao().queryRaw(where, param);
    }

    public UTXORecordDao getUTXORecordDao() {
        return daoManager.getDaoSession().getUTXORecordDao();
    }



}
