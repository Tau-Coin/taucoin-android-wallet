package io.taucoin.android.wallet.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.taucoin.android.wallet.db.greendao.DaoMaster;
import io.taucoin.android.wallet.db.greendao.KeyValueDao;
import io.taucoin.android.wallet.db.greendao.TransactionHistoryDao;
import io.taucoin.android.wallet.db.greendao.UTXORecordDao;


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
      /*  //----------------------------使用sql实现升级逻辑
        if (oldVersion == newVersion) {
            Log.e("onUpgrade", "数据库是最新版本,无需升级");
            return;
        }
        Log.e("onUpgrade", "数据库从版本" + oldVersion + "升级到版本" + newVersion);
        switch (oldVersion) {
            case 1:
                String sql = "";
                db.execSQL(sql);
            case 2:
            default:
                break;
        }*/

        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        /*MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
            }, MovieCollectDao.class);*/
        //操作数据库的更新 有几个表升级都可以传入到下面
        //MigrationHelper2.getInstance().migrate(db,TransactionHistoryDao.class,UTXORecordDao.class);
       // MigrationHelper.migrate(db,TransactionHistoryDao.class, UTXORecordDao.class, KeyValueDao.class);

    }



}