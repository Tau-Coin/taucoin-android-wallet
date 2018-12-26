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

package io.taucoin.android.wallet.core;

import java.math.BigInteger;

public class FeeRate {
    private BigInteger nITauPerK; // unit is itau-per-1,000-bytes

    public FeeRate() {
        nITauPerK = BigInteger.ZERO;
    }

    public FeeRate(BigInteger nITauPerK) {
        this.nITauPerK = nITauPerK;
    }

    public BigInteger getFee(long nBytes) {
        if (nBytes <= 0) {
            return BigInteger.ZERO;
        }

        BigInteger fee = nITauPerK.multiply(BigInteger.valueOf(nBytes)).divide(BigInteger.valueOf((long)1000));

        if (fee.equals(BigInteger.ZERO) && nBytes != 0) {
            if (nITauPerK.compareTo(BigInteger.ZERO) >= 0) {
                fee = BigInteger.ONE;
            } else {
                fee = BigInteger.ONE.multiply(BigInteger.valueOf((long)-1));
            }
        }

        return fee;
    }

    public BigInteger getFee() {
        return nITauPerK;
    }
}
