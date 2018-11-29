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

package com.mofei.tau.google.wallet.keystore;

import com.google.bitcoin.core.ECKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class KeyStore implements Serializable {
    private static final long serialVersionUID = 2L;

    /** A list of public/private EC keys owned by this user. */
    private final ArrayList<ECKey> keychain;

    private static KeyStore sSingleton = null;

    private void loadKeychainFromDB() {
        // TODO: initialize key chain from key store db
    }

    protected KeyStore() {
        keychain = new ArrayList<ECKey>();
        loadKeychainFromDB();
    }

    public synchronized static KeyStore getInstance() {
        if (sSingleton == null) {
            sSingleton = new KeyStore();
        }

        return sSingleton;
    }

    /**
     * Adds the given ECKey to the wallet. There is currently no way to delete keys (that would result in coin loss).
     */
    public synchronized void addKey(ECKey key) {
        assert !keychain.contains(key);
        keychain.add(key);
    }

    /**
     * Locates a keypair from the keychain given the hash of the public key. This is needed when finding out which
     * key we need to use to redeem a transaction output.
     * @return ECKey object or null if no such key was found.
     */
    public synchronized ECKey findKeyFromPubHash(byte[] pubkeyHash) {
        for (ECKey key : keychain) {
            if (Arrays.equals(key.getCompressedPubKeyHash(), pubkeyHash)) return key;
        }
        return null;
    }
}
