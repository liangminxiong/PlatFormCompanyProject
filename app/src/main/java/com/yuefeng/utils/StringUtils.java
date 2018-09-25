package com.yuefeng.utils;

import android.text.TextUtils;

public class StringUtils {

    /*去掉时间末尾 .0*/
    public static String returnStrTime(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String[] strings = string.split("\\.");
        return strings[0];
    }

    /*裁剪时间时分，月日*/
    public static String[] returnTimeYearHour(String string) {
        String[] strings = string.split(" ");
        return strings;
    }

    /*去掉时间末尾 .0*/
    public static String returnStrTxt(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return string;
    }
}
