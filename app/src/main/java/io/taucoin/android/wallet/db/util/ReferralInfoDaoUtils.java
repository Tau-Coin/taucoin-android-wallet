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
import io.taucoin.android.wallet.db.entity.ReferralInfo;
import io.taucoin.android.wallet.db.greendao.KeyValueDao;
import io.taucoin.android.wallet.db.greendao.ReferralInfoDao;

/**
 * @version 1.0
 * Created by ly on 18-10-31
 * @version 2.0
 * Edited by yang
 * ReferralInfo
 */
public class ReferralInfoDaoUtils {

    private final GreenDaoManager daoManager;
    private static ReferralInfoDaoUtils mUserDaoUtils;

    private ReferralInfoDaoUtils() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static ReferralInfoDaoUtils getInstance() {
        if (mUserDaoUtils == null) {
            mUserDaoUtils = new ReferralInfoDaoUtils();
        }
        return mUserDaoUtils;
    }

    private ReferralInfoDao getReferralInfoDao() {
        return daoManager.getDaoSession().getReferralInfoDao();
    }


    public ReferralInfo query() {
        List<ReferralInfo> list = getReferralInfoDao().queryBuilder()
                .orderDesc(ReferralInfoDao.Properties.Id)
                .list();
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public void setReferralInfo(long friendReward, long invitedReward) {
        ReferralInfo referralInfo = query();
        if(referralInfo == null){
            referralInfo = new ReferralInfo();
            referralInfo.setFriendReward(friendReward);
            referralInfo.setInvitedReward(invitedReward);
        }
        getReferralInfoDao().insertOrReplace(referralInfo);
    }
}
