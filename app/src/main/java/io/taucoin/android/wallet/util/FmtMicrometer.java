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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import io.taucoin.foundation.util.StringUtil;

public class FmtMicrometer {
    
    private static String mDecimal8 = "100000000";

    static String fmtBalance(Long balance) {
        DecimalFormat df = getDecimalFormatInstance();
        df.applyPattern("###,##0.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        BigDecimal bigDecimal = new BigDecimal(balance);
        bigDecimal = bigDecimal.divide(new BigDecimal(mDecimal8), 8, RoundingMode.HALF_UP);
        return df.format(bigDecimal);
    }

    private static DecimalFormat getDecimalFormatInstance() {
        DecimalFormat df;
        try{
            df = (DecimalFormat)NumberFormat.getInstance(Locale.CHINA);
        }catch (Exception e){
            df = new DecimalFormat();
        }
        return df;
    }

    public static String fmtAmount(String amount) {
        try {
            BigDecimal bigDecimal = new BigDecimal(amount);
            bigDecimal = bigDecimal.divide(new BigDecimal(mDecimal8), 8, RoundingMode.HALF_UP);
            return bigDecimal.toString();
        } catch (Exception e) {
            return amount;
        }
    }

    public static String fmtFormat(String num) {
        try {
            BigDecimal number = new BigDecimal(num);
            DecimalFormat df = getDecimalFormatInstance();
            df.applyPattern("0.00######");
            return df.format(number);
        } catch (Exception e) {
            return num;
        }
    }

    public static String fmtFormatFee(String num) {
        try {
            BigDecimal number = new BigDecimal(num);
            DecimalFormat df = getDecimalFormatInstance();
            df.applyPattern("0.########");
            return df.format(number);
        } catch (Exception e) {
            return num;
        }
    }

    public static BigInteger fmtUTXOValue(String value) {
        try{
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.multiply(new BigDecimal(mDecimal8));
            DecimalFormat df = getDecimalFormatInstance();
            df.applyPattern("0");
            String valueFormat = df.format(bigDecimal);
            if(StringUtil.isNotEmpty(valueFormat) && valueFormat.contains(",")){
                valueFormat = valueFormat.replaceAll(",", "");
            }
            return new BigInteger(valueFormat);
        }catch (Exception ignore){
        }
        return new BigInteger("0");
    }

    public static String fmtTxValue(String value) {
        try{
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.multiply(new BigDecimal(mDecimal8));

            DecimalFormat df = getDecimalFormatInstance();
            df.applyPattern("0");
            return df.format(bigDecimal);
        }catch (Exception ignore){

        }
        return new BigInteger("0").toString();
    }
}
