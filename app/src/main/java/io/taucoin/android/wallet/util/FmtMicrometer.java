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
package io.taucoin.android.wallet.util;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class FmtMicrometer {

    public static String fmtBalance(Long balance) {
        Long double_8 = 100000000L;
        double_8.doubleValue();
        DecimalFormat df = new DecimalFormat("###,##0.########");
        return df.format(balance.doubleValue() / double_8.doubleValue());
    }

    public static String fmtFormat(String num) {
        try {
            double number = Double.valueOf(num);
            DecimalFormat df = new DecimalFormat("0.00######");
            return df.format(number);
        } catch (Exception e) {
            return num;
        }
    }

    public static BigInteger fmtUTXOValue(String value) {
        Double d = Double.valueOf(value) * Math.pow(10, 8);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return new BigInteger(nf.format(d));
    }

    public static String fmtTxValue(String value) {
        Double d = Double.valueOf(value) * Math.pow(10, 8);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(d);
    }
}
