package com.common.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Administrator on 2018/7/4.
 */

public class LocationHelper implements LocationListener {

    private static final String TAG = "tag";

    private LocationManager mLocationManager;
    private MyLocationListener mListener;
    private Context mContext;
    private int count = 0;
    private int multipleNumber;

    public LocationHelper(Context context, int multipleNumber) {
        count = 0;
        this.multipleNumber = multipleNumber;
        mContext = context.getApplicationContext();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void initLocation(MyLocationListener listener) {
        mListener = listener;
        Location location;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "LocationManager.GPS_PROVIDER");
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                mListener.updateLastLocation(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        } else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d(TAG, "LocationManager.NETWORK_PROVIDER");
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                mListener.updateLastLocation(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    public void removeLocationUpdatesListener() {
        count = 0;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    // 定位坐标发生改变
    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null) {
            if (count % multipleNumber == 0) {
                mListener.updateLocation(location);
                Log.d(TAG, "onLocationChanged: " + count);
            }
            count++;
            if (count > 65530) {
                count = 0;
            }
        }
    }

    // 定位服务状态改变
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");
        if (mListener != null) {
            mListener.updateStatus(provider, status, extras);
        }
    }

    // 定位服务开启
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    // 定位服务关闭
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }
}
