package com.yuefeng.utils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.common.utils.LogUtils;
import com.yuefeng.ui.MyApplication;

/**
 * 百度地图定位工具类
 */
public class BdLocationUtil {

    private LocationClient locationClient;
    private static String address;

    /**
     * 单例
     *
     * @return
     */
    public static BdLocationUtil getInstance() {
        return LocationHolder.INSTANCE;
    }

    private static class LocationHolder {
        private static final BdLocationUtil INSTANCE = new BdLocationUtil();
    }

    private BdLocationUtil() {
    }

    /**
     * 定位回调的接口
     */
    public interface MyLocationListener {
        void myLocation(BDLocation location);
    }

    /**
     * 获取当前位置
     */
    public void requestLocation(final MyLocationListener listener, int time) {
        //声明LocationClient类
        locationClient = new LocationClient(MyApplication.getContext());
        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                // 是否打开GPS
        option.setCoorType("bd09ll");           // 设置返回值的坐标类型
        option.setScanSpan(time);              // 设置定时定位的时间间隔。单位毫秒
        option.setIsNeedAddress(true);          //可选，设置是否需要地址信息，默认不需要
        option.setIgnoreKillProcess(false);     //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClient.setLocOption(option);
        // 注册位置监听器 BDLocationListener
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                listener.myLocation(location);
                locationClient.stop();
            }
        });
        locationClient.start();
        /*
         * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
         * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
         * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
         * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
         */
        locationClient.requestLocation();
    }

    public void stopLocation() {
        if (locationClient != null) {
            locationClient.stop();
            locationClient = null;
        }
    }

    public static LatLng ConverCommonToBaidu(LatLng sourceLatLng) {
        // 将COMMON坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    public static LatLng ConverGpsToBaidu(LatLng sourceLatLng) {
        // 将Gps设备获取坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();

        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    /**
     * 让地图在中间
     *
     * @param bm
     * @param ll
     */
    public static void MoveMapToCenter(BaiduMap bm, LatLng ll, int zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoom);
        bm.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    /*坐标转地址*/
    public static String returnAddress(final GeoCoder geoCoder, final LatLng latLng) {
        if (latLng == null) {
            return "未匹配到相应地址";
        }
        /*newVersion 1 最新数据  0 旧数据*/
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).newVersion(1));
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

//            private StringBuffer stringBuffer;

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    address = "未匹配到相应地址";
                } else {
                    //获取反向地理编码结果
                    address = result.getAddress();
                    LogUtils.d("+_+_+_++_+_" + address);
                }
            }
        });
        return address;
    }

    public static String GetPlayIndexString(int index) {
        try {
            if (index < 10)
                return "00" + index;
            else if (index < 100)
                return "0" + index;
            else
                return String.valueOf(index);
        } catch (Exception e) {
            return "000";
        }
    }

}

