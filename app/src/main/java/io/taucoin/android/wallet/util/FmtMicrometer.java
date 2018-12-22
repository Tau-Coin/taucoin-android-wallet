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
