package com.mofei.tau.db.greendao;

import com.mofei.tau.db.GreenDaoManager;
import com.mofei.tau.transaction.KeyValue;

import java.util.List;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */
public class KeyDaoUtils {

    private final GreenDaoManager daoManager;
    private static KeyDaoUtils mKeyDaoUtils;

    public KeyDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static KeyDaoUtils getInstance() {
        if (mKeyDaoUtils == null) {
            mKeyDaoUtils = new KeyDaoUtils();
        }
        return mKeyDaoUtils;
    }

    /**
     * 插入数据 若未建表则先建表
     *　Insert data without building tables.
     * @param key
     * @return
     */
    public boolean insertKeyStoreData(KeyValue key) {
        boolean flag = false;

        flag = getKeyDao().insert(key) == -1 ? false : true;
        return flag;
    }

    /**
     * 插入或替换数据
     *　Insert or replace data
     * @param key
     * @return
     */
    public boolean insertOrReplaceData(KeyValue key) {
        boolean flag = false;
        try {
            flag = getKeyDao().insertOrReplace(key) == -1 ? false : true;
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
    public boolean insertOrReplaceMultiData(final List<KeyValue> list) {
        boolean flag = false;
        try {
            getKeyDao().getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (KeyValue key : list) {
                        daoManager.getDaoSession().insertOrReplace(key);
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
     * @param key
     * @return
     */
    public boolean updateKeyValueData(KeyValue key) {
        boolean flag = false;
        try {
            getKeyDao().update(key);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public boolean deleteKeyValueData(KeyValue key) {
        boolean flag = false;
        try {
            getKeyDao().delete(key);
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
    public boolean deleteKeyValueByKey(Long l) {
        boolean flag = false;
        try {
            getKeyDao().deleteByKey(l);
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
            getKeyDao().deleteAll();
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
    public KeyValue queryKeyStoreDataById(long key) {
        return getKeyDao().load(key);
    }

    /**
     * 查询所有数据
     *　Query all data
     * @return
     */
    public List<KeyValue> queryAllData() {
        return getKeyDao().loadAll();
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
    public List<KeyValue> queryKeyStoreByParams(String where, String... param) {
        return getKeyDao().queryRaw(where, param);
    }

    public KeyValueDao getKeyDao() {
        return daoManager.getDaoSession().getKeyValueDao();
    }

}
