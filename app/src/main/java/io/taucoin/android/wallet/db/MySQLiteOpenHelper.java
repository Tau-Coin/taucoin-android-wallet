package io.taucoin.android.wallet.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.naturs.logger.Logger;

import org.greenrobot.greendao.database.Database;

import io.taucoin.android.wallet.db.greendao.DaoMaster;
import io.taucoin.android.wallet.db.greendao.KeyValueDao;
import io.taucoin.android.wallet.db.greendao.TransactionHistoryDao;
import io.taucoin.android.wallet.db.greendao.UTXORecordDao;
import io.taucoin.android.wallet.db.util.MigrationHelper;


/**
 * 处理数据库升级的逻辑：
 * Logic for handling database upgrade:
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        if (oldVersion == newVersion) {
            Logger.d("onUpgrade", "The latest version of the database does not need to be upgraded");
            return;
        }
        Logger.d("onUpgrade", "Database upgrade start");
        Log.d("onUpgrade", "Database version update form " + oldVersion + " to " + newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, TransactionHistoryDao.class, UTXORecordDao.class, KeyValueDao.class);
    }



}