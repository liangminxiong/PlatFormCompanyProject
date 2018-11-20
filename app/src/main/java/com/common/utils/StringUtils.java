package com.common.utils;

import android.text.TextUtils;

/*数据处理*/
public class StringUtils {

    public static String getStringDouble(double distance) {
        if (distance > 1000) {
            distance = distance / 1000;
        }
//        new java.text.DecimalFormat("0.00");
//        String .format("%.2f");
//        double f = 111231.5585;
//    BigDecimal b = new BigDecimal(f);
//    //保留2位小数
//    double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return df.format(distance);

    }

    public static String getStringDistance(double distance) {
//        new java.text.DecimalFormat("0.00");
//        String .format("%.2f");
//        double f = 111231.5585;
//    BigDecimal b = new BigDecimal(f);
//    //保留2位小数
//    double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        return df.format(distance);

    }

    public static String getTimeNoZero(String string) {
        String time = "";
        if (!TextUtils.isEmpty(string)) {
            int length = string.length();
            if (length > 1) {
                String substring = string.substring(0, 1);
                if (substring.contains("0")) {
                    time = string.substring(1, 2);
                } else {
                    time = string;
                }
            }
        }
        return time;
    }

    /*空字符串返回无*/
    public static String isEntryStrWu(String string) {
        return TextUtils.isEmpty(string) ? "无" : string;
    }

    /*空字符串返回空*/
    public static String isEntryStrNull(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

    /*空字符串返回0*/
    public static String isEntryStrZero(String string) {
        return TextUtils.isEmpty(string) ? "0" : string;
    }

    /*去掉时间末尾 .0*/
    public static String returnStrTime(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String[] strings = string.split("\\.");
        return strings[0];
    }
}
