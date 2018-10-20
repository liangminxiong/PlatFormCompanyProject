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

import java.util.HashMap;
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
//                address = "暂无地址!";
                address = "";
            }
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

}
