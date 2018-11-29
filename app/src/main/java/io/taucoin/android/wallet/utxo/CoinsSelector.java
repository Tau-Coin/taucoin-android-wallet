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

package io.taucoin.android.wallet.utxo;

import io.taucoin.android.wallet.blockchain.Constants;
import com.mofei.tau.transaction.UTXORecord;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.AbstractMap.SimpleEntry;

public class CoinsSelector {

    // select coins with less value.
    private static BigInteger SelectCoinsMinConf(BigInteger nTargetValue, long nConf, ArrayList<UTXORecord> vCoins,
        ArrayList<UTXORecord> coinsRet) {
        coinsRet.clear();
        BigInteger nValueRet = BigInteger.ZERO;

        BigInteger coinLowestLargerValue = Constants.MAX_MONEY;
        UTXORecord coinLowestLargerUTXO = null;
        ArrayList<UTXORecord> vValue = new ArrayList<UTXORecord>();
        BigInteger nTotalLower = BigInteger.ZERO;

        Collections.shuffle(vCoins);

        for (UTXORecord coin : vCoins) {
            if (coin.confirmations < nConf) {
                continue;
            }

            if (coin.value.compareTo(nTargetValue) == 0) {
                coinsRet.add(coin);
                nValueRet = nValueRet.add(coin.value);
                return nValueRet;
            } else if (coin.value.compareTo(nTargetValue.add(Constants.MIN_CHANGE)) < 0) {
                vValue.add(coin);
                nTotalLower = nTotalLower.add(coin.value);
            } else if (coin.value.compareTo(coinLowestLargerValue) < 0) {
                coinLowestLargerValue = coin.value;
                coinLowestLargerUTXO  = coin;
            }
        }

        // If some UTXO's value equals target value, return this utxo directly.
        if (nTotalLower.compareTo(nTargetValue) == 0)
        {
            for (UTXORecord c : vValue)
            {
                coinsRet.add(c);
            }
            nValueRet = nTargetValue;
            return nValueRet;
        } else if (nTotalLower.compareTo(nTargetValue) < 0) {
            if (coinLowestLargerUTXO == null) {
                for (UTXORecord c : vValue)
                {
                    coinsRet.add(c);
                }
                nValueRet = nTotalLower;
                return nValueRet;
            }
            coinsRet.add(coinLowestLargerUTXO);
            nValueRet = coinLowestLargerValue;
            return nValueRet;
        }

        // For the remaining coditions, select less value.
        Collections.sort(vValue);

        for (UTXORecord c : vValue) {
            if (nValueRet.compareTo(nTargetValue) < 0) {
                coinsRet.add(c);
                nValueRet = nValueRet.add(c.value);
            }
        }

        return nValueRet;
    }

    public static BigInteger SelectCoins(ArrayList<UTXORecord> vAvailableCoins, BigInteger nTargetValue,
            ArrayList<UTXORecord> setCoinsRet)
    {
        BigInteger nValueRet = BigInteger.ZERO;
        nValueRet = SelectCoinsMinConf(nTargetValue, 6L, vAvailableCoins, setCoinsRet);
        if (nValueRet.compareTo(nTargetValue) < 0) {
             nValueRet = SelectCoinsMinConf(nTargetValue, 1L, vAvailableCoins, setCoinsRet);
        }          

        return nValueRet;
    }
}
