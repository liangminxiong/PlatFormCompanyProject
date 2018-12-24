package com.yuefeng.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Polyline;
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

import static android.content.Context.LOCATION_SERVICE;

/**
 * 百度地图定位工具类
 */
public class BdLocationUtil {

    private LocationClient locationClient;
    private static String address;
    private static BdLocationUtil instance;

    /**
     * 单例
     *
     * @return
     */
    public static BdLocationUtil getInstance() {
        if (instance == null) {
            synchronized (BdLocationUtil.class) {
                if (instance == null) {
                    instance = new BdLocationUtil();
                }
            }
        }
        return instance;
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
    public void requestLocation(final MyLocationListener listener, int time, final boolean isLocaOne) {
        //声明LocationClient类
        locationClient = new LocationClient(MyApplication.getContext());
        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                // 是否打开GPS
        option.setPriority(LocationClientOption.GpsFirst);
//        option.setCoorType("bd09ll");           // 设置返回值的坐标类型
        option.setScanSpan(time);              // 设置定时定位的时间间隔。单位毫秒
        option.setIsNeedAddress(true);          //可选，设置是否需要地址信息，默认不需要
        option.setIgnoreKillProcess(false);     //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClient.setLocOption(option);
        // 注册位置监听器 BDLocationListener
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    listener.myLocation(location);
                    if (isLocaOne) {
                        locationClient.stop();
                    }
                }
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
//        if (locationClient != null) {
//            locationClient.stop();
//            locationClient = null;
//        }
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

    public static void MoveMapToCenterWithBitmap(BaiduMap bm, LatLng ll
            , int zoom, BitmapDescriptor icon, MarkerOptions ooA, Marker mMarker) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoom);
        ooA = new MarkerOptions().icon(icon).zIndex(13);
        ooA.position(ll);
        mMarker = null;
        mMarker = (Marker) (bm.addOverlay(ooA));
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

    public Location startLocationServise(Context context) {
        Location location = null;
        Context applicationContext = context.getApplicationContext();
        LocationManager mLocationManager = (LocationManager) applicationContext.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//无海拔要求   criteria.setBearingRequired(false);//无方位要求
        criteria.setCostAllowed(true);//允许产生资费   criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

// 获取最佳服务对象
        assert mLocationManager != null;
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return location;
        }
        location = mLocationManager.getLastKnownLocation(provider);

        return location;
    }

    /**
     * 根据点获取图标转的角度
     */
    public static double getAngle(Polyline mPolyline, int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    public static double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    public static double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    public static double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

}

