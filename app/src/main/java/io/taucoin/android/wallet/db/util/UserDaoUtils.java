package io.taucoin.android.wallet.db.util;


import io.taucoin.android.wallet.db.entity.Accounts;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.AccountsDao;

import java.util.List;

/**
 * 增删改查
 *创建一个Dao类，把增删改查的方法封装起来，提高代码的可复用性，简洁性
 */
public class UserDaoUtils {

    private final GreenDaoManager daoManager;
    private static UserDaoUtils mUserDaoUtils;

    public UserDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static UserDaoUtils getInstance() {
        if (mUserDaoUtils == null) {
            mUserDaoUtils = new UserDaoUtils();
        }
        return mUserDaoUtils;
    }

    /**
     * 插入数据 若未建表则先建表
     *
     * @param accounts
     * @return
     */
    public boolean insertUserData(Accounts accounts) {
        boolean flag = false;
        flag = getAccountsDao().insert(accounts) == -1 ? false : true;
        return flag;
    }

    /**
     * 插入或替换数据
     *
     * @param accounts
     * @return
     */
    public boolean insertOrReplaceData(Accounts accounts) {
        boolean flag = false;
        try {
            flag = getAccountsDao().insertOrReplace(accounts) == -1 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入多条数据  子线程完成
     *
     * @param list
     * @return
     */
    public boolean insertOrReplaceMultiData(final List<Accounts> list) {
        boolean flag = false;
        try {
            getAccountsDao().getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Accounts accounts : list) {
                        daoManager.getDaoSession().insertOrReplace(accounts);
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
     *
     * @param accounts
     * @return
     */
    public boolean updateUserData(Accounts accounts) {
        boolean flag = false;
        try {
            getAccountsDao().update(accounts);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据id删除数据
     *
     * @param accounts
     * @return
     */
    public boolean deleteUserData(Accounts accounts) {
        boolean flag = false;
        try {
            getAccountsDao().delete(accounts);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    public boolean deleteAllData() {
        boolean flag = false;
        try {
            getAccountsDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    public Accounts queryUserDataById(long key) {
        return getAccountsDao().load(key);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<Accounts> queryAllData() {
        return getAccountsDao().loadAll();
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
     *
     * @param where
     * @param param
     * @return
     */
    public List<Accounts> queryUserByParams(String where, String... param) {
        return getAccountsDao().queryRaw(where, param);
    }

    public AccountsDao getAccountsDao() {
        return daoManager.getDaoSession().getAccountsDao();
    }
}