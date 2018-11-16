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

package com.mofei.tau.src.io.taucoin.android.wallet.utxo;

import com.mofei.tau.db.greendao.UTXORecordDaoUtils;
import com.mofei.tau.transaction.ScriptPubkey;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UTXOManager {

    private static UTXOManager sSingleton = null;

    protected UTXOManager() {
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

        List<UTXORecord> utxoRecordList= UTXORecordDaoUtils.getInstance().queryAllData();

        L.e( "数据库里的数据："+utxoRecordList.size());
        for (int i=0;i<utxoRecordList.size();i++){

            UTXORecord utxoRecord=utxoRecordList.get(i);
            L.e(utxoRecord.getTxId()+" getTxId");
            L.e(utxoRecord.getAddress()+" getAddress");
            L.e(utxoRecord.getVout()+" getVout");
            L.e(utxoRecord.getValue()+" getValue");
            L.e(utxoRecord.getConfirmations()+" getConfirmations");
            L.e(utxoRecord.getScriptPubKey().getAsm()+" getScriptPubKey_getAsm");
            L.e(utxoRecord.getScriptPubKey().getHex()+" getScriptPubKey_getHex");

            L.e("---------------");
            vCoins.add(utxoRecord);
        }

        return true;
    }

    public boolean markCoinsSpent(ArrayList<UTXORecord> vCoins) {
        // TODO: mark these coins spent
        return true;
    }
}
