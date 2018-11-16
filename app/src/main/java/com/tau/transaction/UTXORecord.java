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

package com.mofei.tau.transaction;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.math.BigInteger;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

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

@Entity
public class UTXORecord implements Comparable<UTXORecord> {

    @Id
    public Long id;

    // TXID with HEX fromat string
    public String txId;

    // Spent or not
    public boolean spent;

    // Address
    public String address;

    // vout index
    public long vout;

    // confirmations
    public long confirmations;

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

    // value
    @Convert(converter = BigIntegerConverter.class, columnType = String.class)
    public BigInteger  value;

    public static class BigIntegerConverter implements PropertyConverter<BigInteger,String>{

        @Override
        public BigInteger convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return new Gson().fromJson(databaseValue, BigInteger.class);
        }

        @Override
        public String convertToDatabaseValue(BigInteger entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return new Gson().toJson(entityProperty);
        }
    }

    // scriptPubKey
    @Convert(converter = ScriptPubkeyConverter.class, columnType = String.class)
    public ScriptPubkey scriptPubKey;

    @Generated(hash = 1084351274)
    public UTXORecord(Long id, String txId, boolean spent, String address, long vout, long confirmations,
            long version, boolean coinbase, String bestblockhash, long bestblockheight, long bestblocktime,
            String blockhash, long blockheight, long blocktime, BigInteger value, ScriptPubkey scriptPubKey) {
        this.id = id;
        this.txId = txId;
        this.spent = spent;
        this.address = address;
        this.vout = vout;
        this.confirmations = confirmations;
        this.version = version;
        this.coinbase = coinbase;
        this.bestblockhash = bestblockhash;
        this.bestblockheight = bestblockheight;
        this.bestblocktime = bestblocktime;
        this.blockhash = blockhash;
        this.blockheight = blockheight;
        this.blocktime = blocktime;
        this.value = value;
        this.scriptPubKey = scriptPubKey;
    }

    @Generated(hash = 1366505595)
    public UTXORecord() {
    }

    public static class ScriptPubkeyConverter implements PropertyConverter<ScriptPubkey,String>{

        @Override
        public ScriptPubkey convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return new Gson().fromJson(databaseValue, ScriptPubkey.class);
        }

        @Override
        public String convertToDatabaseValue(ScriptPubkey entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return new Gson().toJson(entityProperty);
        }
    }

    @Override
    public int compareTo(@NonNull UTXORecord o) {
        return 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxId() {
        return this.txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public boolean getSpent() {
        return this.spent;
    }

    public void setSpent(boolean spent) {
        this.spent = spent;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getVout() {
        return this.vout;
    }

    public void setVout(long vout) {
        this.vout = vout;
    }

    public long getConfirmations() {
        return this.confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean getCoinbase() {
        return this.coinbase;
    }

    public void setCoinbase(boolean coinbase) {
        this.coinbase = coinbase;
    }

    public String getBestblockhash() {
        return this.bestblockhash;
    }

    public void setBestblockhash(String bestblockhash) {
        this.bestblockhash = bestblockhash;
    }

    public long getBestblockheight() {
        return this.bestblockheight;
    }

    public void setBestblockheight(long bestblockheight) {
        this.bestblockheight = bestblockheight;
    }

    public long getBestblocktime() {
        return this.bestblocktime;
    }

    public void setBestblocktime(long bestblocktime) {
        this.bestblocktime = bestblocktime;
    }

    public String getBlockhash() {
        return this.blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public long getBlockheight() {
        return this.blockheight;
    }

    public void setBlockheight(long blockheight) {
        this.blockheight = blockheight;
    }

    public long getBlocktime() {
        return this.blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public ScriptPubkey getScriptPubKey() {
        return this.scriptPubKey;
    }

    public void setScriptPubKey(ScriptPubkey scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }


}
