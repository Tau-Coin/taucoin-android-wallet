package io.taucoin.android.wallet.util;


import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * Amount input filter, limit decimal point input digit
 *
 */
public class MoneyValueFilter extends DigitsKeyListener {

    private static final String TAG = "MoneyValueFilter";

    public MoneyValueFilter() {
        super(false, true);
    }

    private int digits = 2;

    public MoneyValueFilter setDigits(int d) {
        digits = d;
        return this;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dStart, int dEnd) {
        CharSequence out = super.filter(source, start, end, dest, dStart, dEnd);

        // if changed, replace the source
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }

        int len = end - start;

        // if deleting, source is empty
        // and deleting can't break anything
        if (len == 0) {
            return source;
        }

        if (source.toString().equals(".") && dStart == 0) {
            return "0.";
        }
//
//        if (!source.toString().equals(".") && dest.toString().equals("0")) {
//            return "";
//        }

        int dLen = dest.length();

        // Find the position of the decimal .
        for (int i = 0; i < dStart; i++) {
            if (dest.charAt(i) == '.') {
                // being here means, that a number has
                // been inserted after the dot
                // check if the amount of digits is right
                return (dLen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);
            }
        }
        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                // being here means, dot has been inserted
                // check if the amount of digits is right
                if ((dLen - dEnd) + (end - (i + 1)) > digits)
                    return "";
                else
                    break;  // return new SpannableStringBuilder(source, start, end);
            }
        }
        // if the dot is after the inserted part,
        // nothing can break
        return new SpannableStringBuilder(source, start, end);
    }
}