/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.db.util;

import java.util.List;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.greendao.KeyValueDao;
import io.taucoin.android.wallet.module.bean.BalanceBean;

/**
 * @version 1.0
 * Created by ly on 18-10-31
 * @version 2.0
 * Edited by yang
 * @description: KeyValue
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
        }else{
            result.setAddress(keyValue.getAddress());
            result.setPubkey(keyValue.getPubkey());
            result.setPrivkey(keyValue.getPrivkey());
        }
        getKeyValueDao().insertOrReplace(result);
        return result;
    }

    public void update(KeyValue keyValue) {
        getKeyValueDao().insertOrReplace(keyValue);
    }
}
