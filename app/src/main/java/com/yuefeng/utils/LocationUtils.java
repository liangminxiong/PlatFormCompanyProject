package com.yuefeng.utils;


import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.common.utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*处理定位信息实体类*/
public class LocationUtils implements MyLocationListener.OnLocationResultListener {

    // 客户端定位对象
    private LocationClient mLocationClient;
    // 地址位置监听对象
    private MyLocationListener myListener = new MyLocationListener();
    // 用来存储获取的定位信息
    private Map<String, Object> map = new HashMap<String, Object>();
    // 获取反地理编码对象
    private GeoCoder mGeoCoder = GeoCoder.newInstance();

    public LocationUtils(Context context) {
        mLocationClient = new LocationClient(context);
        // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        // 注册监听函数
        setLocationOptions();
    }

    /**
     * 设置定位参数
     */
    private void setLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        // 设置定位模式为高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 返回的定位结果是百度经纬度,默认值gcj02
//        option.setCoorType("bd09ll");
        // 设置发起定位请求的间隔时间为5000ms
        option.setScanSpan(5000);
        // 返回的定位结果包含地址信息
        option.setIsNeedAddress(true);
        // 返回的定位结果包含手机机头的方向
        option.setNeedDeviceDirect(true);
        // 打开GPS
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        // 注册信息监听对象
        myListener.registerOnLocationResultListener(this);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        mLocationClient.stop();
    }

    @Override
    public void onResultData(Map<String, Object> map) {
        // 如果返回了结果呢，就停止定位了
        stopLocation();
        this.map = map;
        // 如果没有获取到
        if (!map.keySet().contains("address")) {
            // 传入经纬度
            getAddress(Double.parseDouble(map.get("latitude").toString()),
                    Double.parseDouble(map.get("lontitude").toString()));
        } else {
            if (mOnResultMapListener != null) {
                mOnResultMapListener.onGeoCodeResult(map);
            }
        }

    }

    /**
     * 进行反地理编码	 *
     * * @param latitude* 纬度信息
     * * @param lontitude* 经度信息
     */
    public void getAddress(double latitude, double lontitude) {
        LatLng mLatLng = new LatLng(latitude, lontitude);
        // 反地理编码请求参数对象
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置请求参数
        mReverseGeoCodeOption.location(ConverGpsToBaidu(mLatLng));
//        mReverseGeoCodeOption.newVersion(1);
        // 发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
        // 设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
    }

    /**
     * 进行反地理编码	 *
     * * @param latitude* 纬度信息
     * * @param lontitude* 经度信息
     */
    public void getAddressLatlng(LatLng latLng) {
        // 反地理编码请求参数对象
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置请求参数
        mReverseGeoCodeOption.location(latLng);
//        mReverseGeoCodeOption.newVersion(1);
        // 发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
        // 设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
    }

    /**
     * 根据城市,地址信息进行地理编码	 *
     * * @param city* 城市,不能为null
     * * @param address	 * 详细地址,不为null
     */
    public void getLocation(String city, String address) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(address)) {
            return;
        }
        GeoCodeOption mGeoCodeOption = new GeoCodeOption();
        mGeoCodeOption.address(address);
        mGeoCodeOption.city(city);
        mGeoCoder.geocode(mGeoCodeOption);
        // 设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
    }

    /**
     * 编码查询结果监听者
     */
    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            // 反地理编码查询结果回调函数
            // 将地理位置信息载入到集合中
            String address = result.getAddress();
            if (TextUtils.isEmpty(address)) {
                address = "未匹配到相应地址!";
            }
            LogUtils.d("=====address===" + address);
            map.put("address", address);
            if (mOnResultMapListener != null) {
                mOnResultMapListener.onReverseGeoCodeResult(map);
            }
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            // 地理编码查询结果回调函数
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("latitude", result.getLocation().latitude);
            map.put("longitude", result.getLocation().longitude);
            map.put("address", result.getAddress());
            if (mOnResultMapListener != null) {
                mOnResultMapListener.onGeoCodeResult(map);
            }
        }
    };

    /**
     * 释放资源
     */
    public void onDestory() {
        // 释放该地理编码查询对象
        mGeoCoder.destroy();
    }

    /**
     * 注册结果监听回调	 *
     * * @param mOnResultMapListener
     */
    public void registerOnResult(OnResultMapListener mOnResultMapListener) {
        this.mOnResultMapListener = mOnResultMapListener;
    }

    private OnResultMapListener mOnResultMapListener;

    /**
     * 数据结果回传	 * 	 * @author NapoleonBai	 *
     */
    public interface OnResultMapListener {
        /**
         * 反地理编码执行回传
         * * @param map
         */
        void onReverseGeoCodeResult(Map<String, Object> map);

        /**
         * 地理编码执行回传		 * 		 * @param map
         */
        void onGeoCodeResult(Map<String, Object> map);

    }


    private LatLng ConverCommonToBaidu(LatLng sourceLatLng) {
        // 将COMMON坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    private LatLng ConverGpsToBaidu(LatLng sourceLatLng) {
        // 将Gps设备获取坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();

        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    /**
     * 计算多边形面积
     *
     * @return
     */
    public static String getArea(List<LatLng> pts) {
        double totalArea = 0;// 初始化总面积
        double LowX = 0.0;
        double LowY = 0.0;
        double MiddleX = 0.0;
        double MiddleY = 0.0;
        double HighX = 0.0;
        double HighY = 0.0;
        double AM = 0.0;
        double BM = 0.0;
        double CM = 0.0;
        double AL = 0.0;
        double BL = 0.0;
        double CL = 0.0;
        double AH = 0.0;
        double BH = 0.0;
        double CH = 0.0;
        double CoefficientL = 0.0;
        double CoefficientH = 0.0;
        double ALtangent = 0.0;
        double BLtangent = 0.0;
        double CLtangent = 0.0;
        double AHtangent = 0.0;
        double BHtangent = 0.0;
        double CHtangent = 0.0;
        double ANormalLine = 0.0;
        double BNormalLine = 0.0;
        double CNormalLine = 0.0;
        double OrientationValue = 0.0;
        double AngleCos = 0.0;
        double Sum1 = 0.0;
        double Sum2 = 0.0;
        double Count2 = 0;
        double Count1 = 0;
        double Sum = 0.0;
        double Radius = 6378137.0;// WGS84椭球半径
        int Count = pts.size();
        //最少3个点
        if (Count < 3) {
            return "无法计算面积";
        }
        for (int i = 0; i < Count; i++) {
            if (i == 0) {
                LowX = pts.get(Count - 1).longitude * Math.PI / 180;
                LowY = pts.get(Count - 1).latitude * Math.PI / 180;
                MiddleX = pts.get(0).longitude * Math.PI / 180;
                MiddleY = pts.get(0).latitude * Math.PI / 180;
                HighX = pts.get(1).longitude * Math.PI / 180;
                HighY = pts.get(1).latitude * Math.PI / 180;
            } else if (i == Count - 1) {
                LowX = pts.get(Count - 2).longitude * Math.PI / 180;
                LowY = pts.get(Count - 2).latitude * Math.PI / 180;
                MiddleX = pts.get(Count - 1).longitude * Math.PI / 180;
                MiddleY = pts.get(Count - 1).latitude * Math.PI / 180;
                HighX = pts.get(0).longitude * Math.PI / 180;
                HighY = pts.get(0).latitude * Math.PI / 180;
            } else {
                LowX = pts.get(i - 1).longitude * Math.PI / 180;
                LowY = pts.get(i - 1).latitude * Math.PI / 180;
                MiddleX = pts.get(i).longitude * Math.PI / 180;
                MiddleY = pts.get(i).latitude * Math.PI / 180;
                HighX = pts.get(i + 1).longitude * Math.PI / 180;
                HighY = pts.get(i + 1).latitude * Math.PI / 180;
            }
            AM = Math.cos(MiddleY) * Math.cos(MiddleX);
            BM = Math.cos(MiddleY) * Math.sin(MiddleX);
            CM = Math.sin(MiddleY);
            AL = Math.cos(LowY) * Math.cos(LowX);
            BL = Math.cos(LowY) * Math.sin(LowX);
            CL = Math.sin(LowY);
            AH = Math.cos(HighY) * Math.cos(HighX);
            BH = Math.cos(HighY) * Math.sin(HighX);
            CH = Math.sin(HighY);
            CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
            CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);
            ALtangent = CoefficientL * AL - AM;
            BLtangent = CoefficientL * BL - BM;
            CLtangent = CoefficientL * CL - CM;
            AHtangent = CoefficientH * AH - AM;
            BHtangent = CoefficientH * BH - BM;
            CHtangent = CoefficientH * CH - CM;
            AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent

                    + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));
            AngleCos = Math.acos(AngleCos);
            ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
            BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
            CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;
            if (AM != 0)
                OrientationValue = ANormalLine / AM;
            else if (BM != 0)
                OrientationValue = BNormalLine / BM;
            else
                OrientationValue = CNormalLine / CM;
            if (OrientationValue > 0) {
                Sum1 += AngleCos;
                Count1++;
            } else {
                Sum2 += AngleCos;
                Count2++;
            }
        }

        double tempSum1, tempSum2;
        tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2);
        tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2;
        if (Sum1 > Sum2) {
            if ((tempSum1 - (Count - 2) * Math.PI) < 1)
                Sum = tempSum1;
            else
                Sum = tempSum2;
        } else {
            if ((tempSum2 - (Count - 2) * Math.PI) < 1)
                Sum = tempSum2;
            else
                Sum = tempSum1;
        }
        totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;

        return String.valueOf(Math.floor(totalArea)); // 返回总面积
    }

}
