package io.taucoin.android.wallet.db;


import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.db.greendao.DaoMaster;
import io.taucoin.android.wallet.db.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
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
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(MyApplication.getInstance(), DB_NAME, null);
            Database db = helper.getWritableDb();

           // Database database=helper.getEncryptedReadableDb("mima123");// Encrypt
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