/**
 * Copyright 2018 TauCoin Core Developers.
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

package io.taucoin.android.wallet.core.utxo;

import com.github.naturs.logger.Logger;
import java.util.ArrayList;
import java.util.List;

import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.db.util.UTXORecordDaoUtils;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;

public class UTXOManager {

    private static UTXOManager sSingleton = null;

    private UTXOManager() {
    }

    public synchronized static UTXOManager getInstance() {
        if (sSingleton == null) {
            sSingleton = new UTXOManager();
        }

        return sSingleton;
    }

    /**
     * Get available coins from database.
     * Note: don't load utxo with value 0.
     */
    public boolean getAvailableCoins(String address, ArrayList<UTXORecord> vCoins) {
        /**
        if (address == null) {
            // get all conis from utxo database
            return false;
        } else {
            // get  coins by specified address
            return false;
        }
        */
        String formAddress = SharedPreferencesHelper.getInstance().getString(TransmitKey.ADDRESS, "");
        List<UTXORecord> utxoRecordList= UTXORecordDaoUtils.getInstance().queryByAddress(formAddress);

        for (int i=0;i<utxoRecordList.size();i++){

            UTXORecord utxoRecord=utxoRecordList.get(i);
//            Logger.e(utxoRecord.getTxId()+" getTxId");
//            Logger.e(utxoRecord.getAddress()+" getAddress");
//            Logger.e(utxoRecord.getVout()+" getVout");
//            Logger.e(utxoRecord.getValue()+" getValue");
//            Logger.e(utxoRecord.getConfirmations()+" getConfirmations");
//            Logger.e(utxoRecord.getScriptPubKey().getAsm()+" getScriptPubKey_getAsm");
//            Logger.e(utxoRecord.getScriptPubKey().getHex()+" getScriptPubKey_getHex");
//
//            Logger.e("---------------");
            vCoins.add(utxoRecord);
        }
        return true;
    }

    public boolean markCoinsSpent(ArrayList<UTXORecord> vCoins) {
        // TODO: mark these coins spent
        return true;
    }
}