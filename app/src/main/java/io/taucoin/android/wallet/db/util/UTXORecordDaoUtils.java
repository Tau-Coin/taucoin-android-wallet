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

import org.greenrobot.greendao.query.QueryBuilder;

import io.taucoin.android.wallet.db.GreenDaoManager;
import io.taucoin.android.wallet.db.greendao.UTXORecordDao;

import io.taucoin.android.wallet.db.entity.UTXORecord;

import java.util.List;

/**
 * @version 1.0
 * Created by ly on 18-10-31
 * @version 2.0
 * Edited by yang
 * @description: UTXORecord
 */
public class UTXORecordDaoUtils {

    private final GreenDaoManager daoManager;
    private static UTXORecordDaoUtils mUserDaoUtils;

    private UTXORecordDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static UTXORecordDaoUtils getInstance() {
        if (mUserDaoUtils == null) {
            mUserDaoUtils = new UTXORecordDaoUtils();
        }
        return mUserDaoUtils;
    }

    /**
     * delete address data
     */
    public void deleteByAddress(String address) {
        QueryBuilder<UTXORecord> builder = getUTXORecordDao()
                .queryBuilder()
                .where(UTXORecordDao.Properties.Address.eq(address));
        builder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * query address data
     */
    public List<UTXORecord> queryByAddress(String address) {
        QueryBuilder<UTXORecord> builder = getUTXORecordDao()
                .queryBuilder()
                .where(UTXORecordDao.Properties.Address.eq(address));
        return builder.list();
    }

    /**
     * query address data
     */
    public void insertOrReplace(List<UTXORecord> list) {
        for (UTXORecord utxo : list) {
            getUTXORecordDao().insertOrReplace(utxo);
        }
    }

    private UTXORecordDao getUTXORecordDao() {
        return daoManager.getDaoSession().getUTXORecordDao();
    }
}