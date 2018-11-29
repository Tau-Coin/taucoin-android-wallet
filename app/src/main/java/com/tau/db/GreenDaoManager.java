package com.mofei.tau.db;


import com.mofei.tau.activity.MyApplication;
import com.mofei.tau.db.greendao.DaoMaster;
import com.mofei.tau.db.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * GreenDaoManager中创建数据库,并对其进行管理：
 * Create and manage database in GreenDaoManager:
 */
public class GreenDaoManager {
    private static final String DB_NAME = "taucoin_db";
    private static GreenDaoManager mInstance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    private GreenDaoManager() {
        if (mInstance == null) {
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(MyApplication.getContext(), DB_NAME, null);
            Database db = helper.getWritableDb();

           // Database database=helper.getEncryptedReadableDb("mima123");//加密
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}