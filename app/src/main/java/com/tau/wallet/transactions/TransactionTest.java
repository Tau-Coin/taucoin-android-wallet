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

package com.tau.wallet.transactions;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ScriptException;

/**
getrawtransaction 49670b86ee8b9ddbc0105c03bead1865353533f1a5ecc82d07542d975e415239 1
{
  "hex": "0100000001223515d03afd2a01d22ae1f2629025e517ff22e7728e832bd6b3133aa229f293340000006b483045022100852e0b169887f64f902cfbafc094ea483eb31361ee1ba1b48ed957ee188642da02206cd9c235a3a71cb2c3d80ec0dc61eac7231528d21e5e7d60537052b67e20bef9012103223a1a185973672259eeb0f70166aa1e77a67fac1f24f55d419f6270fc3af89bfeffffff000248fd37a40b0000001976a914bb9c21f5e99f8905f42f2734c8fb2d3ec48139e788ac00000000000000001976a914e43971c8443fb5f154f757bb65baa88b0cab676e88acf9020100",
  "txid": "49670b86ee8b9ddbc0105c03bead1865353533f1a5ecc82d07542d975e415239",
  "hash": "49670b86ee8b9ddbc0105c03bead1865353533f1a5ecc82d07542d975e415239",
  "size": 227,
  "vsize": 227,
  "version": 1,
  "locktime": 66297,
  "vin": [
    {
      "txid": "93f229a23a13b3d62b838e72e722ff17e5259062f2e12ad2012afd3ad0153522",
      "vout": 52,
      "scriptSig": {
        "asm": "3045022100852e0b169887f64f902cfbafc094ea483eb31361ee1ba1b48ed957ee188642da02206cd9c235a3a71cb2c3d80ec0dc61eac7231528d21e5e7d60537052b67e20bef9[ALL] 03223a1a185973672259eeb0f70166aa1e77a67fac1f24f55d419f6270fc3af89b",
        "hex": "483045022100852e0b169887f64f902cfbafc094ea483eb31361ee1ba1b48ed957ee188642da02206cd9c235a3a71cb2c3d80ec0dc61eac7231528d21e5e7d60537052b67e20bef9012103223a1a185973672259eeb0f70166aa1e77a67fac1f24f55d419f6270fc3af89b"
      },
      "sequence": 4294967294
    }
  ],
  "vreward": [
  ],
  "vout": [
    {
      "value": 499.99773000,
      "n": 0,
      "scriptPubKey": {
        "asm": "OP_DUP OP_HASH160 bb9c21f5e99f8905f42f2734c8fb2d3ec48139e7 OP_EQUALVERIFY OP_CHECKSIG",
        "hex": "76a914bb9c21f5e99f8905f42f2734c8fb2d3ec48139e788ac",
        "reqSigs": 1,
        "type": "pubkeyhash",
        "addresses": [
          "TT5CQDdb2TnRzQNxAJNDGnGjruGDdJQdWg"
        ]
      }
    }, 
    {
      "value": 0.00000000,
      "n": 1,
      "scriptPubKey": {
        "asm": "OP_DUP OP_HASH160 e43971c8443fb5f154f757bb65baa88b0cab676e OP_EQUALVERIFY OP_CHECKSIG",
        "hex": "76a914e43971c8443fb5f154f757bb65baa88b0cab676e88ac",
        "reqSigs": 1,
        "type": "pubkeyhash",
        "addresses": [
          "TWmwt7BzYCvFt1mTjXevHwbtCrXKV2uxhY"
        ]
      }
    }
  ],
  "blockhash": "253c501bc19e2d1e2e3f9f5fd56e547b89d8a59557331625a1ada2bd66b35028",
  "confirmations": 9770,
  "time": 1540445504,
  "blocktime": 1540445504
}
*/

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;
import com.tau.wallet.keystore.KeyStore;

import java.math.BigInteger;

public class TransactionTest {

    public static void main(String[] args) {
        /**
        String targetTxHex = "0100000001223515d03afd2a01d22ae1f2629025e517ff22e7728e832bd6b3133aa229f293340000006b483045022100852e0b169887f64f902cfbafc094ea483eb31361ee1ba1b48ed957ee188642da02206cd9c235a3a71cb2c3d80ec0dc61eac7231528d21e5e7d60537052b67e20bef9012103223a1a185973672259eeb0f70166aa1e77a67fac1f24f55d419f6270fc3af89bfeffffff000248fd37a40b0000001976a914bb9c21f5e99f8905f42f2734c8fb2d3ec48139e788ac00000000000000001976a914e43971c8443fb5f154f757bb65baa88b0cab676e88acf9020100";
        Transaction tx = new Transaction(NetworkParameters.mainNet());
        tx.lockTime = 66297L;

        TransactionOutPoint outpoint = new TransactionOutPoint(NetworkParameters.mainNet(),
                52L, "93f229a23a13b3d62b838e72e722ff17e5259062f2e12ad2012afd3ad0153522",
                Utils.hexStringToBytes("76a914bb9c21f5e99f8905f42f2734c8fb2d3ec48139e788ac"));
        TransactionInput input = new TransactionInput(NetworkParameters.mainNet(), outpoint);
        //input.scriptBytes = TransactionInput.EMPTY_ARRAY;
        input.scriptBytes = Utils.hexStringToBytes("483045022100852e0b169887f64f902cfbafc094ea483eb31361ee1ba1b48ed957ee188642da02206cd9c235a3a71cb2c3d80ec0dc61eac7231528d21e5e7d60537052b67e20bef9012103223a1a185973672259eeb0f70166aa1e77a67fac1f24f55d419f6270fc3af89b");
        input.sequence = 4294967294L;
        tx.inputs.add(input);

        TransactionOutput output1 = new TransactionOutput(NetworkParameters.mainNet(), new BigInteger("49999773000", 10),
                Utils.hexStringToBytes("76a914bb9c21f5e99f8905f42f2734c8fb2d3ec48139e788ac"));
        TransactionOutput output2 = new TransactionOutput(NetworkParameters.mainNet(), BigInteger.ZERO,
                Utils.hexStringToBytes("76a914e43971c8443fb5f154f757bb65baa88b0cab676e88ac"));
        tx.outputs.add(output1);
        tx.outputs.add(output2);

        String txHexStr = tx.dumpIntoHexStr();
        System.out.println("TX hash:" + tx.getHashAsString());
        System.out.println("TX Hex:" + txHexStr);
        if (targetTxHex.equals(txHexStr)) {
            System.out.println("TX Serialize Test Passed!");
        } else {
            System.out.println("TX Serialize Test Failed!");
        }
         */

        // Tx signature test
        Transaction tx2 = new Transaction(NetworkParameters.mainNet());
        tx2.lockTime = 78810L;

        TransactionOutPoint outpoint1 = new TransactionOutPoint(NetworkParameters.mainNet(),
                0L, "061a13728f58404832281758c963add8096e8e6614c718b44b634dc9e73d9a8f",
                Utils.hexStringToBytes("76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac"));
        TransactionInput input1 = new TransactionInput(NetworkParameters.mainNet(), outpoint1);
        input1.scriptBytes = TransactionInput.EMPTY_ARRAY;
        input1.sequence = 4294967294L;
        tx2.inputs.add(input1);

        TransactionOutput output3 = new TransactionOutput(NetworkParameters.mainNet(), new BigInteger("600000000", 10),
                Utils.hexStringToBytes("76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac"));
        TransactionOutput output4 = new TransactionOutput(NetworkParameters.mainNet(), new BigInteger("100000000", 10),
                Utils.hexStringToBytes("76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac"));
        tx2.outputs.add(output3);
        tx2.outputs.add(output4);

        String wifPrivKey = "L33kD6h1JcJhuVdBysEYyjrD9SS5m1VPFJ9jPj2c1eYatCsPXaT8";
        String newPrivKeyStr = null;
        try {
            newPrivKeyStr = Utils.convertWIFPrivkeyIntoPrivkey(wifPrivKey);
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
            return;
        }
        ECKey testkey = new ECKey(new BigInteger(newPrivKeyStr, 16));
        KeyStore.getInstance().addKey(testkey);

        try {
            tx2.signInputsAndRewards(Transaction.SigHash.ALL, KeyStore.getInstance());
        } catch (ScriptException e) {
        }

        String txHexStr2 = tx2.dumpIntoHexStr();
        System.out.println("TX hash:" + tx2.toString());
        System.out.println("TX hash:" + tx2.getHashAsString());
        System.out.println("TX Hex:" + txHexStr2);
    }
}
