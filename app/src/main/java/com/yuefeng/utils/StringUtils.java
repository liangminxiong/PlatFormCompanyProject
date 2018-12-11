package com.yuefeng.utils;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.yuefeng.commondemo.R;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    private static String[] sSplit;

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

    public static List<LatLng> getLnglat(String lnglat) {
        List<LatLng> lngList = new ArrayList<>();
        lngList.clear();
        if (lnglat.contains(";")) {
            String[] split = lnglat.split(";");
            for (String aSplit : split) {
                String[] list = getStringList(aSplit);
                double lat = Double.valueOf(list[1]);
                double lng = Double.valueOf(list[0]);
                lngList.add(BdLocationUtil.ConverGpsToBaidu(new LatLng(lat, lng)));
            }
        } else {
            String[] list = getStringList(lnglat);
            double lat = Double.valueOf(list[1]);
            double lng = Double.valueOf(list[0]);
            lngList.add(BdLocationUtil.ConverGpsToBaidu(new LatLng(lat, lng)));
        }
        return lngList;
    }

    private static String[] getStringList(String string) {
        if (string.contains(",")) {
            sSplit = string.split(",");
        }
        return sSplit;
    }

    /*图标地图Marker*/
//    0  离线  1 停止 2 行驶 3 等待
    public static int getPersonalBitmapResource(String stateType) {
        int typeInt = 0;
        if (stateType.equals("3")) {
            typeInt = R.drawable.worker_ting10;
        } else if (stateType.equals("1")) {
            typeInt = R.drawable.worker_tingche;
        } else if (stateType.equals("2")) {
            typeInt = R.drawable.worker_yidong;
        } else {
            typeInt = R.drawable.worker_lixian;
        }

        return typeInt;
    }

    /*图标地图Marker*/
//    0  离线  1 停止 2 行驶 3 等待
    public static int getVehicleBitmapResource(String stateType) {
        int typeInt = 0;
        if (stateType.equals("3")) {
            typeInt = R.drawable.vehicle_ting10;
        } else if (stateType.equals("1")) {
            typeInt = R.drawable.vehicle_tingche;
        } else if (stateType.equals("2")) {
            typeInt = R.drawable.vehicle_yidong;
        } else {
            typeInt = R.drawable.vehicle_lixian;
        }

        return typeInt;
    }

    /*图标地图Marker*/
//    0  离线  1 停止 2 行驶 3 等待
    public static int getProblemBitmapResource(String stateType) {
        int typeInt = 0;
        if (stateType.equals("1")) {
            typeInt = R.drawable.problem_tingche;

        } else if (stateType.equals("2")) {
            typeInt = R.drawable.problem_yidong;
        } else if (stateType.equals("3")) {
            typeInt = R.drawable.problem_ting10;
        } else {
            typeInt = R.drawable.problem_lixian;
        }

        return typeInt;
    }
}
