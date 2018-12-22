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

package io.taucoin.android.wallet.db.entity;

/**
 * UTXO data is pulled from centeric server. This class is the wrapper of raw data.
 * Raw data with json format is as follow:
 *
        "scriptPubKey": {
            "asm": "OP_DUP OP_HASH160 e43971c8443fb5f154f757bb65baa88b0cab676e OP_EQUALVERIFY OP_CHECKSIG",
            "hex": "76a914e43971c8443fb5f154f757bb65baa88b0cab676e88ac",
            "reqSigs": 1,
            "type": "pubkeyhash",
            "addresses": [
                "TWmwt7BzYCvFt1mTjXevHwbtCrXKV2uxhY"
            ]
         },
 */

public class ScriptPubkey {

    public String asm;
    public String hex;
   // public int reqSigs;
   //  public String type;
   // public String address;


    public String getAsm() {
        return asm;
    }

    public void setAsm(String asm) {
        this.asm = asm;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    /*public int getReqSigs() {
        return reqSigs;
    }

    public void setReqSigs(int reqSigs) {
        this.reqSigs = reqSigs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }*/


}
