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


import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * Amount input filter, limit decimal point input digit
 *
 */
public class MoneyValueFilter extends DigitsKeyListener {
    private int digits = 2;
    private boolean isEndSpace = false;

    public MoneyValueFilter() {
        super(false, true);
    }

    public MoneyValueFilter setDigits(int d) {
        digits = d;
        return this;
    }

    public MoneyValueFilter setEndSpace() {
        isEndSpace = true;
        return this;
    }

    private String getEndExpand() {
        return isEndSpace ? " " : "";
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dStart, int dEnd) {
        CharSequence out = super.filter(source, start, end, dest, dStart, dEnd);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
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
            stringBuilder.append(source);
            stringBuilder.append(getEndExpand());
            return stringBuilder;
        }

        if (source.toString().equals(".") && dStart == 0) {
            stringBuilder.append("0.");
            stringBuilder.append(getEndExpand());
            return stringBuilder;
        }
//
//        if (!source.toString().equals(".") && dest.toString().equals("0")) {
//            return "";
//        }

        int dLen = dest.length();
        if(isEndSpace){
            dLen -= 1;
            dEnd -= 1;
        }
        // Find the position of the decimal .
        for (int i = 0; i < dStart; i++) {
            if (dest.charAt(i) == '.') {
                // being here means, that a number has
                // been inserted after the dot
                // check if the amount of digits is right
                CharSequence charSequence = (dLen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);

                stringBuilder.append(charSequence);
                stringBuilder.append(getEndExpand());
                return stringBuilder;
            }
        }
        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                // being here means, dot has been inserted
                // check if the amount of digits is right
                if ((dLen - dEnd) + (end - (i + 1)) > digits) {
                    stringBuilder.append("");
                    stringBuilder.append(getEndExpand());
                    return stringBuilder;
                }else
                    break;  // return new SpannableStringBuilder(source, start, end);
            }
        }
        // if the dot is after the inserted part,
        // nothing can break
        stringBuilder.append(new SpannableStringBuilder(source, start, end));
        stringBuilder.append(getEndExpand());
        return stringBuilder;
    }
}