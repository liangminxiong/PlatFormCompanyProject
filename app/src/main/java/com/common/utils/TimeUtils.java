package com.common.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created  on 2018-05-18.
 * author:seven
 * email:seven2016s@163.com
 */

public class TimeUtils {

    public static String getCurrentTime2() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH) + 1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);
        String tH = "";
        String tM = "";
        if (hour < 10) {
            tH = "0" + hour;
        } else {
            tH = String.valueOf(hour);
        }
        if (minute < 10) {
            tM = "0" + minute;
        } else {
            tM = String.valueOf(minute);
        }

//        time2.setText("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        time = year + "-" + month + "-" + day + " " + tH + ":" + tM + ":" + second;
        return time;
    }

    /*开始时间*/
    public static String getDayStartTime() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        time = year + "-" + month + "-" + day + " " + "00:00:00";
        return time;
    }

    public static String getYear() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        time = year + "";
        return time;
    }

    public static String getYearMonthDay() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH) + 1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
//        int minute = calendar.get(Calendar.MINUTE);
//秒
//        int second = calendar.get(Calendar.SECOND);

//        time2.setText("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);

        time = year + "-" + month + "-" + day;
        return time;
    }

    public static String getHourMinute() {
        String time = "";
        String tH = "";
        String tM = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年月日
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
//        int second = calendar.get(Calendar.SECOND);

//        time2.setText("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        if (hour < 10) {
            tH = "0" + hour;
        } else {
            tH = String.valueOf(hour);
        }
        if (minute < 10) {
            tM = "0" + minute;
        } else {
            tM = String.valueOf(minute);
        }
        time = tH + ":" + tM;
        return time;
    }

    public static String getHourMinuteSecond() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年月日
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);

//        time2.setText("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        time = hour + ":" + minute + ":" + second;
        return time;
    }

    public static String getMonthStartTime() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年月日
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        time = year + "-" + month + "-01 00:00:00";
        return time;
    }


    /*
     * time:时间毫秒值
     * inFormat:时间格式
     * */
    public static String longToString(long time, String inFormat) {
        Date date = new Date(time);
        DateFormat format = new SimpleDateFormat(inFormat);
        return format.format(date);
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转Boolean
     *
     * @param str
     * @param defValue
     * @return
     */
    public static boolean toBoolean(String str, boolean defValue) {
        // Log.v("JJ", "str:" + str + "defValue:" +defValue);
        if (str == null)
            return defValue;
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
        }
        Log.v("JJ", "defValue:" + defValue);
        return defValue;
    }

    /**
     * 返回long类型的今天的日期，格式为：XXXXXXXX
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * 返回String类型的今天的日期，格式为：XXXX年XX月XX日
     *
     * @return
     */
    public static String getToday2() {
        long today = getToday();

        String day = String.valueOf(today);
        String year = day.substring(0, 4);
        String mouth = day.substring(4, 6);
        String d = day.substring(6, 8);

        return year.concat("年").concat(mouth).concat("月").concat(d).concat("日");
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormaterMonth = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-01 00:00:00");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormaterWeek = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormaterMonthWithOutTime = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-01");
        }
    };

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate, dateFormater2);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }


    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate1
     * @return boolean
     */
    public static boolean isSameday(String sdate1, String sdate2) {
        boolean b = false;
        Date time1 = toDate(sdate1, dateFormater);
        Date time2 = toDate(sdate2, dateFormater2);
        if (time1 != null && time2 != null) {
            String s1 = dateFormater2.get().format(time1);
            String s2 = dateFormater2.get().format(time2);
            if (s1.equals(s2)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串时间距离今天还有多少天
     *
     * @param sdate
     * @return int
     */
    public static String getDateFormater3(String sdate) {
        try {
            Date time = toDate(sdate, dateFormater3);
            if (time != null) {
                return dateFormater3.get().format(time);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "1900-01-01 00:00";
    }

    /**
     * 判断给定字符串时间距离今天还有多少天
     *
     * @param sdate
     * @return int
     */
    public static int getDateSpan(String sdate) {
        if (sdate == null) {
            return 0;
        }

        Date time = toDate(sdate, dateFormater2);
        Date today = new Date();
        if (time != null) {
            long temp = time.getTime() - today.getTime(); // 相差毫秒数
            long days = temp / 1000 / 3600 / 24; // 相差天数
            Log.v("JJ", "相差:" + days);
            if (days > 0)
                return (int) days;
        }
        return 0;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormaterChinese = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    /**
     * 判断给定两个字符串时间是否是开始时间小于结束时间
     *
     * @param start HH:mm:ss
     * @return end
     */
    public static boolean getBoolenStartEndTime(String start, String end) {
        if (start.equals("") || end.equals("")) {
            return false;
        }
        Date stime = toDate(start, dateFormaterChinese);
        Date etime = toDate(end, dateFormaterChinese);
        if (stime != null && etime != null) {

            long t = etime.getTime() - stime.getTime(); // 相差毫秒数
            if (t >= 0)
                return true;
        }
        return false;
    }

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getTimeHourMin(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getTimeOnlyHourMin(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    /**
     * 判断给定字符串时间距离小时
     *
     * @param starttime
     * @return int
     */
    public static float getHourSpan(String starttime, String endtime) {

        if (starttime == null || endtime == null) {
            return 0;
        }

        try {
            Date stime = toDate(starttime, dateFormater);
            Date etime = toDate(endtime, dateFormater);
            if (stime != null && etime != null) {
                long temp = etime.getTime() - stime.getTime(); // 相差毫秒数
                float hours = (float) (temp / 1000 / 3600.0); // 相差小时
                if (hours > 0)
                    return hours;
            }
        } catch (Exception e) {
            Log.v("JJ",
                    "getHourSpan:" + starttime + " " + endtime + e.toString());
        }
        return 0;
    }

    /**
     * 判断给定字符串时间距离天数
     *
     * @param starttime
     * @return int
     */
    public static boolean getMouthDaySpan(String starttime, String endtime) {

        if (TextUtils.isEmpty(starttime) || TextUtils.isEmpty(endtime)) {
            return false;
        }

        try {
            Date stime = toDate(starttime, dateFormater);
            Date etime = toDate(endtime, dateFormater);
            if (stime != null && etime != null) {
                long temp = etime.getTime() - stime.getTime(); // 相差毫秒数
                float hours = (float) (temp / 1000 / 3600.0 / 24); // 相差天
                if (hours > 7)
                    return true;
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }

    /**
     * 判断给定两个字符串时间是否是开始时间小于结束时间
     * <p>
     * yyyy-MM-dd HH:mm:ss
     *
     * @return int
     */
    public static boolean isTimeLessThan(String start, String end) {
        if (start.equals("") || end.equals("")) {
            return false;
        }
        Date stime = toDate(start, dateFormater);
        Date etime = toDate(end, dateFormater);
        if (stime != null && etime != null) {

            long t = etime.getTime() - stime.getTime(); // 相差毫秒数
            if (t >= 0)
                return true;
        }
        return false;
    }


    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate, ThreadLocal<SimpleDateFormat> sdf) {

        try {
            return sdf.get().parse(sdate);
        } catch (ParseException e) {
            Log.v("JJ", sdate + ":" + e.toString());
            return null;
        }
    }

    public static String toDateTime(String sdate) {
        Date date = toDate(sdate, dateFormater);
        if (date != null)
            return dateFormater2.get().format(date);
        return "";
    }

    public static String yesterDay(String time) {
        Date date = toDate(time, dateFormater2);
        if (date != null) {
            date = new Date(date.getTime() - 24 * 60 * 60 * 1000);
            return dateFormater2.get().format(date);
        }
        return "";
    }

    /**
     * 当天开始时间
     *
     * @return
     */
    public static String toTodayS() {
        Date today = new Date();
        return dateFormater2.get().format(today) + " 00:00:00";
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static String toTodayE() {
        Date today = new Date();
        return dateFormater2.get().format(today) + " 23:59:59";
    }

    /**
     * @return
     */
    public static String toWeekS() {
        Date today = new Date();
        return dateFormaterWeek.get().format(
                new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000));
    }

    /**
     * @return
     */
    public static String toLast30DayS() {
        Date today = new Date();
        today.setMonth(today.getMonth() - 1);
        return dateFormaterWeek.get().format(today);
    }

    /**
     * 当月结束时间
     *
     * @return
     */
    public static String toMonthS() {
        Date today = new Date();
        return dateFormaterMonth.get().format(today);
    }

    /**
     * 当天开始时间
     *
     * @return
     */
    public static String toTodayWithOutTime() {
        Date today = new Date();
        return dateFormater2.get().format(today);
    }

    /**
     * 当周时间
     *
     * @return
     */
    public static String toWeekWithOutTime() {
        Date today = new Date();
        String lastWeekDate = getTimeOfWeekEnd();
        String firstWeekDate = getTimeOfWeekStart();
        String time = firstWeekDate + " - " + lastWeekDate;
        return time;
    }

    //    每周开始时间
    public static String getTimeOfWeekStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        String format = dateFormater2.get().format(ca.getTimeInMillis());
        return format;
    }

    //    每周结束时间
    public static String getTimeOfWeekEnd() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 7);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, 7);
        String format = dateFormater2.get().format(ca.getTimeInMillis());
        return format;
    }

    /**
     * 当月份时间
     *
     * @return
     */
    public static String toMonthOutTime() {
        Date today = new Date();
        String firstDate = dateFormaterMonthWithOutTime.get().format(today);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDate = dateFormater2.get().format(ca.getTime());

        return firstDate + " - " + lastDate;
    }

    /**
     * 获取某一天
     *
     * @param date 标准时间
     * @return String
     */
    public static String getDay(String date) {
        //2015-05-09 00:00:00
        String day = date.substring(8, 10);
        String temp = day;
        char charAt = temp.charAt(0);
        if (charAt == '0') {
            day = day.substring(1, 2);
        }
        return day;
    }

    /**
     * 获取某一天
     *
     * @param date 标准时间
     * @return int
     */
    public static int getDayByString(String date) {
        String day = getDay(date);
        return toInt(day, 0);
    }

    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static String getStartTimeofDay() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date curDate = new Date(System.currentTimeMillis());
        String curD = formatter.format(curDate);
        return curD;
    }

    /**
     * 获取当前时间，标准时间
     *
     * @return
     */
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curD = formatter.format(curDate);
        return curD;
    }

    /**
     * 获取一个小时之前的时间
     *
     * @return
     */
    public static String getLastHoursTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date hourDate = new Date(System.currentTimeMillis() - 3600000);
        String hDate = formatter.format(hourDate);
        return hDate;
    }

    /**
     * 昨天开始时间
     *
     * @return
     */
    public static String getYesterdayStartTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date hourDate = new Date(System.currentTimeMillis() - (24 * 3600000));
        String hDate = formatter.format(hourDate);
        return hDate;
    }

    /**
     * 获取3个小时之前的时间
     *
     * @return
     */
    public static String getThreeHoursTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date hourDate = new Date(System.currentTimeMillis() - 10800000);
        String hDate = formatter.format(hourDate);
        return hDate;
    }


    /*毫秒转 HH:mm:ss*/
    public static String dateFormatFromSeconds(int seconds) {

        //初始化format格式
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //设置时区，跳过此步骤会默认设置为"GMT+08:00" 得到的结果会多出来8个小时
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return format.format(seconds * 1000);
    }

    /*
     * 将时分秒转为秒数
     * */
    public static String formatTurnSecond(String time) {
        String second = "";
        int hour;
        int min;
        int sec;
        String[] my = time.split(":");
        if (time.length() > 6) {
            hour = Integer.parseInt(my[0]);
            min = Integer.parseInt(my[1]);
            sec = Integer.parseInt(my[2]);
            long totalSec = hour * 3600 + min * 60 + sec;
            second = String.valueOf(totalSec);
        } else {
            min = Integer.parseInt(my[0]);
            sec = Integer.parseInt(my[1]);
            long totalSec = min * 60 + sec;
            second = String.valueOf(totalSec);
        }


        return second;
    }

    /*
     * 将时分秒转为秒数
     * */
    public static String formatHourMin(String times) {
        String time = "";
        if (!TextUtils.isEmpty(times)) {
            if (times.length() > 16) {
                time = times.substring(11, 16);
            }
        }
        return time;
    }


    /*
     * 将时分秒转为秒数
     * */
    public static long formatTurnSecond2(String time) {
        String s = time;
        int index1 = s.indexOf(":");
        int index2 = s.indexOf(":", index1 + 1);
        int hh = Integer.parseInt(s.substring(0, index1));
        int mi = Integer.parseInt(s.substring(index1 + 1, index2));
        int ss = Integer.parseInt(s.substring(index2 + 1));
        return hh * 60 * 60 + mi * 60 + ss;
    }

    /**
     * 返回 HH：mm:ss
     *
     * @return
     */
    public static String FormatMiss(int time) {
        String hh = time / 3600 > 9 ? time / 3600 + "" : "0" + time / 3600;
        String mm = (time % 3600) / 60 > 9 ? (time % 3600) / 60 + "" : "0" + (time % 3600) / 60;
        String ss = (time % 3600) % 60 > 9 ? (time % 3600) % 60 + "" : "0" + (time % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }


    /**
     * 分析acc的状态
     *
     * @return
     */
    public static String AnalysisGpsStatus(String gpsStatus) {
        if (isEmpty(gpsStatus))
            return "未知";

        boolean isContainsOpen = gpsStatus.contains("ACC开");
        if (isContainsOpen) {
            return "开";
        }
        boolean isContainsClose = gpsStatus.contains("ACC关");
        if (isContainsClose) {
            return "关";
        }
        return "未知";
    }

    public static boolean checkFieldValueNull(Object bean) {
        boolean result = true;
        if (bean == null) {
            return true;
        }
        Class<?> cls = bean.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
                if (fieldVal != null) {
                    if ("".equals(fieldVal)) {
                        result = true;
                    } else {
                        result = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase() + fieldName.substring(startIndex + 1);
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSuccessJson(String result) {
        JSONObject obj = null;
        boolean success;
        try {
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            success = obj.getBoolean("success");
            return success;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    public static String byettoString(File file) {
        String string = "";
        byte[] byetsFromFile = getByetsFromFile(file);
        string = Base64.encodeToString(byetsFromFile, Base64.DEFAULT);
        return string;
    }

    //    文件转流
    public static byte[] getByetsFromFile(File file) {
        FileInputStream is = null;
        // 获取文件大小
        long length = file.length();
        Log.d("tag", "getByetsFromFile: " + length);
        // 创建一个数据来保存文件数据
        byte[] fileData = new byte[(int) length];

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bytesRead = 0;
        // 读取数据到byte数组中
        while (bytesRead != fileData.length) {
            try {
                bytesRead += is.read(fileData, bytesRead, fileData.length - bytesRead);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return fileData;
    }
}
