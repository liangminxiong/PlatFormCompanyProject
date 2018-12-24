package com.yuefeng.features.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.common.utils.ToastUtils;

/**
 * Created by Administrator on 2018/2/5.
 */

public class LocationService extends Service {
    private static final String TAG = "MyService";
    //查询到的城市信息
    private BDLocation bdLocation = null;
    //设置为静态成员方便在 Activity 中引用
    public static Handler handler;
    //Activity 与 Service 交互的通道
    private MyBinder binder = new MyBinder();
    //定位客户端
    private LocationClient client;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public LocationService(){}

    public void startLocation(Context context,int time) {
        getLocation(context,time);
    }

    /**
     * 初始化定位选项
     */
    private void getLocation(Context context,int time) {
        client = new LocationClient(context);
        //注册监听接口
        client.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        //每 time更新一次定位
        option.setScanSpan(time);
        //开启描述性信息，不开启不会返回城市名等信息
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        //开始定位
        client.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回与 Activity 的交互通道
        return binder;
    }

    /**
     * 在代理中返回真正地城市名信息
     */
    public class MyBinder extends Binder {
        public BDLocation getCity() {
            return bdLocation;
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                ToastUtils.showToast("定位失败");
                return;
            }
            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                //也可以使用 bdLocation.getCity()
                if (bdLocation.getDistrict() != null) {
                    Log.d(TAG, "onReceiveLocation: " + bdLocation.getDistrict());
                    if (handler != null) {
                        //向实例化 Handler 的线程发送消息
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //服务销毁停止定位
        if (client != null) {
            client.stop();
        }
    }
}