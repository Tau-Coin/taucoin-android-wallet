package com.mofei.tau.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by ly on 18-11-30
 *
 * @version 1.0
 * @description:
 */

public class EditTextJudgeNumberWatcher implements TextWatcher {
    private EditText editText;

    public EditTextJudgeNumberWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        judgeNumber(editable,editText);
    }

    public static void judgeNumber(Editable edt,EditText editText) {
        String temp = edt.toString();
        int posDot = temp.indexOf(".");
        int index = editText.getSelectionStart();

        if (posDot < 0) {
            if (temp.length() <= 8) {
                return;
            } else {
                edt.delete(index-1, index);
                return;
            }
        }

        if (posDot > 8) {
            edt.delete(index-1, index);
            return;
        }

        if (temp.length() - posDot - 1 > 2) {
            edt.delete(index-1, index);
            return;
        }
    }
}