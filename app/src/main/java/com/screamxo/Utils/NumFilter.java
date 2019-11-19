package com.screamxo.Utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class NumFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; ++i)
        {
            if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.]*").matcher(String.valueOf(source.charAt(i))).matches())
            {
                return "";
            }
        }

        return null;
    }
}
