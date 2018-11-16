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

package com.mofei.tau.src.io.taucoin.android.wallet;

import com.mofei.tau.src.com.google.bitcoin.core.AddressFormatException;
import com.mofei.tau.src.com.google.bitcoin.core.ECKey;
import com.mofei.tau.src.com.google.bitcoin.core.NetworkParameters;
import com.mofei.tau.src.com.google.bitcoin.core.Utils;

import com.mofei.tau.src.io.taucoin.android.wallet.keystore.KeyStore;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.Transaction;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.TransactionFailReason;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.CreateTransactionResult;

import java.math.BigInteger;
import java.util.*;

/**
 *
Privkey[64]: ADE5E0BBE556CE1F849E1DB3623217D1912042C367D3DAF38E0F4B085BBF4F2A
Pubkey[130]: 04AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3186593C2B4444E56316C991F8658564FA38C07DD94E0A02B541115AFAD427FE0
compressed pubkey: 02AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3
sha: 9AF8A27BE1F2EA71EBEB45EF899D89DDD8A73F2B508911176CE39F18D4853014
rmd: 418B5E153ECD8D53FD2E430D4FD7BAAF6EC891A021
sha: 3DFB663196CE9E0F7836115FCA8D86BB5B50A730487A295CB1A0F1CF22671212
sha: 7B53C4C86D367C85C5615360A1F7FB2EE82AD269861CB26A50A9443D8BA5EBED
before base58: 418B5E153ECD8D53FD2E430D4FD7BAAF6EC891A0217B53C4C8
addr: TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs
raw compressed privkey: 80ADE5E0BBE556CE1F849E1DB3623217D1912042C367D3DAF38E0F4B085BBF4F2A01
compressed private key: L33kD6h1JcJhuVdBysEYyjrD9SS5m1VPFJ9jPj2c1eYatCsPXaT8
{
	 privkey:L33kD6h1JcJhuVdBysEYyjrD9SS5m1VPFJ9jPj2c1eYatCsPXaT8
	 pubkey :02AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3
	 address:TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs
}
 */

public class WalletTest {

    public static void main(String[] args) {
        // ECKey test
        /**
        {
            Privkey[64]: ADE5E0BBE556CE1F849E1DB3623217D1912042C367D3DAF38E0F4B085BBF4F2A
            Pubkey[130]: 04AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3186593C2B4444E56316C991F8658564FA38C07DD94E0A02B541115AFAD427FE0
        }
         */
        String privKeyStr = "ADE5E0BBE556CE1F849E1DB3623217D1912042C367D3DAF38E0F4B085BBF4F2A";
        String targetPubkey = "04ae0cda5bc3ae95bf14bd69c26dd201dfa1274f5fefbf41efd02e734185409be3186593c2b4444e56316c991f8658564fa38c07dd94e0a02b541115afad427fe0";
        //byte[] privKeyBytes = Utils.hexStringToBytes(privKeyStr);
        //BigInteger privKeyGI = new BigInteger(privKeyBytes);
        BigInteger privKeyGI = new BigInteger(privKeyStr, 16);
        ECKey privKey = new ECKey(privKeyGI);
        byte[] pubKeyBytes = privKey.getPubKey();
        String pubkeyStr = Utils.bytesToHexString(pubKeyBytes);
        //System.out.println("pubkey str:" + pubkeyStr);
        if (pubkeyStr.equals(targetPubkey)) {
            System.out.println("Privkey and pubkey test pass!");
        } else {
            System.out.println("Privkey and pubkey test failed!");
        }

        // WIF private key test
        /**
	       privkey:L33kD6h1JcJhuVdBysEYyjrD9SS5m1VPFJ9jPj2c1eYatCsPXaT8
	       pubkey :02AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3
	        address:TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs
         */
        String wifPrivKey = "L33kD6h1JcJhuVdBysEYyjrD9SS5m1VPFJ9jPj2c1eYatCsPXaT8";
        String targetPrivkey = "ade5e0bbe556ce1f849e1db3623217d1912042c367d3daf38e0f4b085bbf4f2a";
        String privKeyStrtest = null;
        try {
            privKeyStrtest = Utils.convertWIFPrivkeyIntoPrivkey(wifPrivKey);
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
        }

        if (targetPrivkey.equals(privKeyStrtest)) {
            System.out.println("WIF Privkey test pass!");
        } else {
            System.out.println("WIF Privkey test failed!");
        }

        // Address test
        String compressedAddress = "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs";
        String hash160Address = null;
        try {
            hash160Address = Utils.getHash160FromTauAddress(compressedAddress);
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
        }
        String pubkeyStr2 = "02AE0CDA5BC3AE95BF14BD69C26DD201DFA1274F5FEFBF41EFD02E734185409BE3";
        String hash160 = Utils.bytesToHexString(Utils.sha256hash160(Utils.hexStringToBytes(pubkeyStr2)));
        if (hash160.equals(hash160Address)) {
            System.out.println("TAU Address test pass!");
        } else {
            System.out.println("TAU Address test failed!");
            System.out.println("result:" + hash160Address);
            System.out.println("excepted:" + hash160);
        }

        // Wallet transaction test
        // First, add key
        /**
         * Txid:9d2db8d4aa6fdda261bfbfea2af36fddc9155a4afce5345842f8ef7921dd020f
         * {
         "hex": "01000000013ce4c2432d1695e7901e23e89d0f163280b5da226e78e6f5c399c31ec17e51ab010000006a47304402207171a8900d3be04412c9f9e8c57ef7064fef6138f207b24816c272466aa7aad80220327e8516af996b5fdc0d190b31b409e31af2c23bac3eb3e7cb5cbfd171651e1b012102ae0cda5bc3ae95bf14bd69c26dd201dfa1274f5fefbf41efd02e734185409be3feffffff000200e1f505000000001976a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac488c124e020000001976a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188acaf290100",
         "txid": "9d2db8d4aa6fdda261bfbfea2af36fddc9155a4afce5345842f8ef7921dd020f",
         "hash": "9d2db8d4aa6fdda261bfbfea2af36fddc9155a4afce5345842f8ef7921dd020f",
         "size": 226,
         "vsize": 226,
         "version": 1,
         "locktime": 76207,
         "vin": [
         {
         "txid": "ab517ec11ec399c3f5e6786e22dab58032160f9de8231e90e795162d43c2e43c",
         "vout": 1,
         "scriptSig": {
         "asm": "304402207171a8900d3be04412c9f9e8c57ef7064fef6138f207b24816c272466aa7aad80220327e8516af996b5fdc0d190b31b409e31af2c23bac3eb3e7cb5cbfd171651e1b[ALL] 02ae0cda5bc3ae95bf14bd69c26dd201dfa1274f5fefbf41efd02e734185409be3",
         "hex": "47304402207171a8900d3be04412c9f9e8c57ef7064fef6138f207b24816c272466aa7aad80220327e8516af996b5fdc0d190b31b409e31af2c23bac3eb3e7cb5cbfd171651e1b012102ae0cda5bc3ae95bf14bd69c26dd201dfa1274f5fefbf41efd02e734185409be3"
         },
         "sequence": 4294967294
         }
         ],
         "vreward": [],
         "vout": [
         {
         "value": 1.00000000,
         "n": 0,
         "scriptPubKey": {
         "asm": "OP_DUP OP_HASH160 8b5e153ecd8d53fd2e430d4fd7baaf6ec891a021 OP_EQUALVERIFY OP_CHECKSIG",
         "hex": "76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac",
         "reqSigs": 1,
         "type": "pubkeyhash",
         "addresses": [
         "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs"
         ]
         }
         },
         {
         "value": 98.99773000,
         "n": 1,
         "scriptPubKey": {
         "asm": "OP_DUP OP_HASH160 8b5e153ecd8d53fd2e430d4fd7baaf6ec891a021 OP_EQUALVERIFY OP_CHECKSIG",
         "hex": "76a9148b5e153ecd8d53fd2e430d4fd7baaf6ec891a02188ac",
         "reqSigs": 1,
         "type": "pubkeyhash",
         "addresses": [
         "TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs"
         ]
         }
         }
         ],
         "blockhash": "11836e1e41953bd3398a7b782f5feeeef3edf6b3e8f3d74866dc38e69a94fb87",
         "confirmations": 6,
         "time": 1541050912,
         "blocktime": 1541050912}

         */
        String newPrivKeyStr = null;
        try {
            newPrivKeyStr = Utils.convertWIFPrivkeyIntoPrivkey(wifPrivKey);
        } catch (AddressFormatException e) {
            System.out.println(e.toString());
            return;
        }
        ECKey testkey = new ECKey(new BigInteger(newPrivKeyStr, 16));

        // Signature test
        //System.out.println("Pubkey:" + Utils.bytesToHexString(testkey.getPubKey()));
        //byte[] signHash = Utils.hexStringToBytes("c74a31bd998195dd4b189bec701c4e9c5a69c2d3fe45bc8a201c79f44232f78a");
        //byte[] signature = testkey.sign(signHash);
        //System.out.println("Signature:" + Utils.bytesToHexString(signature));

        KeyStore.getInstance().addKey(testkey);

        HashMap<String, BigInteger> receipts = new HashMap<String, BigInteger>();
        receipts.put("TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs", new BigInteger("700000000", 10));

        Wallet wallet = Wallet.getInstance();
        Transaction tx = new Transaction(NetworkParameters.mainNet());
        CreateTransactionResult result = wallet.createTransaction(receipts, false, null, tx);

        if (result.failReason == TransactionFailReason.NO_ERROR) {
            System.out.println("Create tx success");
            System.out.println(tx.toString());
            System.out.println("Hex:");
            //
            System.out.println(tx.dumpIntoHexStr());
        } else {
            System.out.println("Create tx failed");
            System.out.println("error code:" + result.failReason.getCode() + ", " + result.failReason.getMsg());
        }
    }

    private static void setEnumTest(TransactionFailReason enumtest) {
        enumtest = TransactionFailReason.TX_TOO_LARGE;
    }

    private static void setBigIntegerTest(BigInteger test) {
        test = BigInteger.ONE;
    }
}
