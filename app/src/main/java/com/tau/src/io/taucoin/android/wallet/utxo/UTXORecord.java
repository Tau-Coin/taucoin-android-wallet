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

/**
 * UTXO data is pulled from centeric server. This class is the wrapper of raw data.
 * Raw data with json format is as follow:
 * {
       "confirmations": 3652,
        "txid": "b4dd97ffbec0957e2074adcf42ab4cfb02aa673a2e2e4f2f175eaeb53857beff",
        "vout": 1,
        "value": 431.00000000,
        "scriptPubKey": {
            "asm": "OP_DUP OP_HASH160 e43971c8443fb5f154f757bb65baa88b0cab676e OP_EQUALVERIFY OP_CHECKSIG",
            "hex": "76a914e43971c8443fb5f154f757bb65baa88b0cab676e88ac",
            "reqSigs": 1,
            "type": "pubkeyhash",
            "addresses": [
                "TWmwt7BzYCvFt1mTjXevHwbtCrXKV2uxhY"
            ]
         },
        "version": 1,
        "coinbase": false,
        "bestblockhash": "e2a3daa47a557c69f5e2b67aa9f3fa3c2d1be3a79be3374c5d721b92042f8297",
        "bestblockheight": 66489,
        "bestblocktime": 1540456419,
        "blockhash": "c5f6dc874f75f6a17878a84f0f73ba8b17f3a43a4de9049719e9c402bdbd5835",
        "blockheight": 62838,
        "blocktime": 1540235469
    }, 
 */
public class UTXORecord implements Comparable<UTXORecord> {

    // TXID with HEX fromat string
    public String txId;

    // Spent or not
    public boolean spent;

    // Address
    public String address;

    // vout index
    public long vout;

    // value
    public BigInteger value;

    // confirmations
    public long confirmations;

    // scriptPubKey
    public ScriptPubkey scriptPubKey;

    // version
    public long version;

    // Is coinbase?
    public boolean coinbase;
    
    // bestblockhash with HEX fromat string
    public String bestblockhash;

    // bestblockheight
    public long bestblockheight;

    // bestblocktime
    public long bestblocktime;

    // blockhash
    public String blockhash;

    // blockheight
    public long blockheight;

    // blocktime
    public long blocktime;

    @Override
    public int compareTo(UTXORecord other) {
        return this.value.compareTo(other.value);
    }
}
