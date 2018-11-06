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

import java.math.BigInteger;
import java.util.ArrayList;

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

        ScriptPubkey scriptPubkey1 = new ScriptPubkey();
        scriptPubkey1.asm = "OP_DUP OP_HASH160 8b5e153ecd8d53fd2e430d4fd7baaf6ec891a021 OP_EQUALVERIFY OP_CHECKSIG";
        scriptPubkey1.hex = "76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac";

        UTXORecord utxo1 = new UTXORecord();
        utxo1.txId = "f604e96b9f304a2032d9bfbfa578c9a8b84576054f9180c3575beb3dded95846";
        utxo1.address = "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs";
        utxo1.vout = 1L;
        utxo1.value = new BigInteger("8999400000", 10);
        utxo1.confirmations = 84;
        utxo1.scriptPubKey = scriptPubkey1;

        vCoins.add(utxo1);

        ScriptPubkey scriptPubkey2 = new ScriptPubkey();
        scriptPubkey2.asm = "OP_DUP OP_HASH160 8b5e153ecd8d53fd2e430d4fd7baaf6ec891a021 OP_EQUALVERIFY OP_CHECKSIG";
        scriptPubkey2.hex = "76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac";

        UTXORecord utxo2 = new UTXORecord();
        utxo2.txId = "061a13728f58404832281758c963add8096e8e6614c718b44b634dc9e73d9a8f";
        utxo2.address = "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs";
        utxo2.vout = 1L;
        utxo2.value = new BigInteger("100000000", 10);
        utxo2.confirmations = 84;
        utxo2.scriptPubKey = scriptPubkey2;

        vCoins.add(utxo2);

        UTXORecord utxo3 = new UTXORecord();
        utxo3.txId = "ce047a1978ae0160cc8f130b24672a810e93216f18de7b64ee90dc5160ba383b";
        utxo3.address = "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs";
        utxo3.vout = 0L;
        utxo3.value = new BigInteger("600000000", 10);
        utxo3.confirmations = 84;
        utxo3.scriptPubKey = scriptPubkey2;

        vCoins.add(utxo3);

        UTXORecord utxo4 = new UTXORecord();
        utxo4.txId = "ce047a1978ae0160cc8f130b24672a810e93216f18de7b64ee90dc5160ba383b";
        utxo4.address = "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs";
        utxo4.vout = 1L;
        utxo4.value = new BigInteger("100000000", 10);
        utxo4.confirmations = 84;
        utxo4.scriptPubKey = scriptPubkey2;

        vCoins.add(utxo4);

        return true;
    }

    public boolean markCoinsSpent(ArrayList<UTXORecord> vCoins) {
        // TODO: mark these coins spent
        return true;
    }
}
