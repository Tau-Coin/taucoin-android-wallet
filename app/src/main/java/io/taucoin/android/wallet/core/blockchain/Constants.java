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

package io.taucoin.android.wallet.core.blockchain;

import java.math.BigInteger;

public class Constants {
    public static final BigInteger COIN = new BigInteger("100000000", 10);

    public static final BigInteger CENT = new BigInteger("1000000", 10);

    public static final BigInteger MIN_CHANGE = CENT;

    public static final BigInteger MAX_MONEY = new BigInteger("10000000000", 10).multiply(COIN);

    public static final BigInteger DEFAULT_MIN_RELAY_TX_FEE = CENT;

    // 10 * COIN
    public static final BigInteger DEFAULT_TRANSACTION_MAXFEE = new BigInteger("1000000000", 10);

    public static final long MAX_STANDARD_TX_WEIGHT = 400000;

    public static boolean duringMoneyRange(BigInteger money) {
        return money.compareTo(MIN_CHANGE) >= 0 && money.compareTo(MAX_MONEY) < 0;
    }
}
