package com.common.utils;

import android.text.TextUtils;

import java.util.Random;

/*数据处理*/
public class StringUtils {

    public static String getRandomString(int length) {
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //2.  由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //3.  长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //从62个的数字或字母中选择
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    public static String get32String(int count) {
//这里的32是生成32位随机码，根据你的需求，自定义
        return getRandomString(count);
    }


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

    /*返回人2字*/
    public static String returnUserTwoLenght(String string) {
        String text = "";
        if (!TextUtils.isEmpty(string)) {
            int length = string.length();
            if (length > 2) {
                text = string.substring(length - 2, length);
            } else {
                text = string;
            }
        } else {
            text = "无题";
        }
        return text;
    }

    /*返回机构2字*/
    public static String returnOrganTwoLenght(String string) {
        String text = "";
        if (!TextUtils.isEmpty(string)) {
            int length = string.length();
            if (length > 2) {
                text = string.substring(0, 2);
            } else {
                text = string;
            }
        } else {
            text = "无题";
        }
        return text;
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
