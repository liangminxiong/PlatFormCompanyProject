package com.yuefeng.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现BDLocationListener,获取地理位置信息
 *
 */
public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        // 存储地理位置信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", location.getTime());// 定位时间
        map.put("loc_type", location.getLocType());// 定位方式
        map.put("latitude", location.getLatitude());// 纬度
        map.put("lontitude", location.getLongitude());// 经度
        map.put("radius", location.getRadius());// 定位精度
        map.put("city", location.getCity());// 获取城市
        map.put("street", location.getStreet());// 获取街道信息
        // 如果是GPS定位
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            // 定位速度
            map.put("speed", location.getSpeed());
            // 获取锁定的卫星数
            map.put("satellite", location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            // 如果是网络定位
            // 获取地址信息
            String address = location.getAddrStr();
            int length = address.length();
            address = address.substring(2, length);
            map.put("address", address);
        }
        if (mOnLocationResultListener != null) {
            mOnLocationResultListener.onResultData(map);
        }
    }

    // 委托接口
    private OnLocationResultListener mOnLocationResultListener;

    /**
     * 注册委托接口事件
     *
     * @param mOnLocationResultListener
     */
    public void registerOnLocationResultListener(
            OnLocationResultListener mOnLocationResultListener) {
        this.mOnLocationResultListener = mOnLocationResultListener;
    }

    /**
     * 获取地址信息接口
     *
     * @author NapoleonBai
     *
     */
    public interface OnLocationResultListener {
        void onResultData(Map<String, Object> map);
    }
}

