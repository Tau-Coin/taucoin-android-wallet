package io.taucoin.android.wallet.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import io.taucoin.android.wallet.db.entity.TransactionHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRANSACTION_HISTORY".
*/
public class TransactionHistoryDao extends AbstractDao<TransactionHistory, Long> {

    public static final String TABLENAME = "TRANSACTION_HISTORY";

    /**
     * Properties of entity TransactionHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TxId = new Property(1, String.class, "txId", false, "TX_ID");
        public final static Property SentOrReceived = new Property(2, String.class, "sentOrReceived", false, "SENT_OR_RECEIVED");
        public final static Property FromAddress = new Property(3, String.class, "fromAddress", false, "FROM_ADDRESS");
        public final static Property ToAddress = new Property(4, String.class, "toAddress", false, "TO_ADDRESS");
        public final static Property Time = new Property(5, String.class, "time", false, "TIME");
        public final static Property Confirmations = new Property(6, int.class, "confirmations", false, "CONFIRMATIONS");
        public final static Property Value = new Property(7, String.class, "value", false, "VALUE");
        public final static Property Result = new Property(8, String.class, "result", false, "RESULT");
        public final static Property Message = new Property(9, String.class, "message", false, "MESSAGE");
        public final static Property Blockheight = new Property(10, long.class, "blockheight", false, "BLOCKHEIGHT");
        public final static Property Blocktime = new Property(11, long.class, "blocktime", false, "BLOCKTIME");
        public final static Property Memo = new Property(12, String.class, "memo", false, "MEMO");
        public final static Property Fee = new Property(13, String.class, "fee", false, "FEE");
    }


    public TransactionHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public TransactionHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRANSACTION_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TX_ID\" TEXT," + // 1: txId
                "\"SENT_OR_RECEIVED\" TEXT," + // 2: sentOrReceived
                "\"FROM_ADDRESS\" TEXT," + // 3: fromAddress
                "\"TO_ADDRESS\" TEXT," + // 4: toAddress
                "\"TIME\" TEXT," + // 5: time
                "\"CONFIRMATIONS\" INTEGER NOT NULL ," + // 6: confirmations
                "\"VALUE\" TEXT," + // 7: value
                "\"RESULT\" TEXT," + // 8: result
                "\"MESSAGE\" TEXT," + // 9: message
                "\"BLOCKHEIGHT\" INTEGER NOT NULL ," + // 10: blockheight
                "\"BLOCKTIME\" INTEGER NOT NULL ," + // 11: blocktime
                "\"MEMO\" TEXT," + // 12: memo
                "\"FEE\" TEXT);"); // 13: fee
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRANSACTION_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TransactionHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String txId = entity.getTxId();
        if (txId != null) {
            stmt.bindString(2, txId);
        }
 
        String sentOrReceived = entity.getSentOrReceived();
        if (sentOrReceived != null) {
            stmt.bindString(3, sentOrReceived);
        }
 
        String fromAddress = entity.getFromAddress();
        if (fromAddress != null) {
            stmt.bindString(4, fromAddress);
        }
 
        String toAddress = entity.getToAddress();
        if (toAddress != null) {
            stmt.bindString(5, toAddress);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(6, time);
        }
        stmt.bindLong(7, entity.getConfirmations());
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(9, result);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(10, message);
        }
        stmt.bindLong(11, entity.getBlockheight());
        stmt.bindLong(12, entity.getBlocktime());
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(13, memo);
        }
 
        String fee = entity.getFee();
        if (fee != null) {
            stmt.bindString(14, fee);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TransactionHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String txId = entity.getTxId();
        if (txId != null) {
            stmt.bindString(2, txId);
        }
 
        String sentOrReceived = entity.getSentOrReceived();
        if (sentOrReceived != null) {
            stmt.bindString(3, sentOrReceived);
        }
 
        String fromAddress = entity.getFromAddress();
        if (fromAddress != null) {
            stmt.bindString(4, fromAddress);
        }
 
        String toAddress = entity.getToAddress();
        if (toAddress != null) {
            stmt.bindString(5, toAddress);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(6, time);
        }
        stmt.bindLong(7, entity.getConfirmations());
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(9, result);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(10, message);
        }
        stmt.bindLong(11, entity.getBlockheight());
        stmt.bindLong(12, entity.getBlocktime());
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(13, memo);
        }
 
        String fee = entity.getFee();
        if (fee != null) {
            stmt.bindString(14, fee);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TransactionHistory readEntity(Cursor cursor, int offset) {
        TransactionHistory entity = new TransactionHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // txId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sentOrReceived
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fromAddress
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // toAddress
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // time
            cursor.getInt(offset + 6), // confirmations
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // value
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // result
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // message
            cursor.getLong(offset + 10), // blockheight
            cursor.getLong(offset + 11), // blocktime
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // memo
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // fee
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TransactionHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTxId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSentOrReceived(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFromAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setToAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setConfirmations(cursor.getInt(offset + 6));
        entity.setValue(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setResult(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setMessage(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBlockheight(cursor.getLong(offset + 10));
        entity.setBlocktime(cursor.getLong(offset + 11));
        entity.setMemo(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFee(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TransactionHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TransactionHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TransactionHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
