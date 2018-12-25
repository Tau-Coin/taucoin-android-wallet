package io.taucoin.android.wallet.db.util;

import java.util.List;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.greendao.KeyValueDao;
import io.taucoin.android.wallet.module.bean.BalanceBean;

/**
 * Created by ly on 18-10-31
 *
 * @version 2.0
 * @description:
 */
public class KeyValueDaoUtils {

    private final GreenDaoManager daoManager;
    private static KeyValueDaoUtils mUserDaoUtils;

    private KeyValueDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static KeyValueDaoUtils getInstance() {
        if (mUserDaoUtils == null) {
            mUserDaoUtils = new KeyValueDaoUtils();
        }
        return mUserDaoUtils;
    }

    private KeyValueDao getKeyValueDao() {
        return daoManager.getDaoSession().getKeyValueDao();
    }


    public KeyValue queryByPubicKey(String pubicKey) {
        List<KeyValue> list = getKeyValueDao().queryBuilder()
                .where(KeyValueDao.Properties.Pubkey.eq(pubicKey))
                .orderDesc(KeyValueDao.Properties.Id)
                .list();
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public KeyValue insertOrReplace(BalanceBean balance) {
        KeyValue entry = KeyValueDaoUtils.getInstance().queryByPubicKey(balance.getPubkey());
        if(entry == null){
            entry = new KeyValue();
        }
        entry.setUtxo((long) balance.getUtxo());
        entry.setBalance((long) balance.getCoins());
        entry.setReward((long) balance.getRewards());
        getKeyValueDao().insertOrReplace(entry);
        return entry;
    }

    public KeyValue insertOrReplace(KeyValue keyValue) {
        KeyValue result = KeyValueDaoUtils.getInstance().queryByPubicKey(keyValue.getPubkey());
        if(result == null){
            result = keyValue;
            getKeyValueDao().insertOrReplace(keyValue);
        }
        return result;
    }

    public void update(KeyValue keyValue) {
        getKeyValueDao().insertOrReplace(keyValue);
    }

    public void delete(KeyValue keyValue) {
        getKeyValueDao().queryBuilder()
        .where(KeyValueDao.Properties.Pubkey.eq(keyValue.getPubkey()))
        .buildDelete()
        .executeDeleteWithoutDetachingEntities();
    }
}
