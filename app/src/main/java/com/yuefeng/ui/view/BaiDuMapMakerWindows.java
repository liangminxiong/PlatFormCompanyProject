package com.yuefeng.ui.view;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.common.Model.BaiduMarkerBean;
import com.common.utils.ResourcesUtils;
import com.yuefeng.commondemo.R;

/*marker弹框*/
public class BaiDuMapMakerWindows {


    private TextView mTvCarNumber, mTvTimes, mTvComtent, mTvAddress;
    private View popoverView;
    private InfoWindow mInfoWindow;

    public BaiDuMapMakerWindows() {
        popoverView = ResourcesUtils.inflate(R.layout.baidumap_infowindow);
        mTvCarNumber = (TextView) popoverView.findViewById(R.id.tv_car_number);
        mTvTimes = (TextView) popoverView.findViewById(R.id.tv_times);
        mTvComtent = (TextView) popoverView.findViewById(R.id.tv_comtent);
        mTvAddress = (TextView) popoverView.findViewById(R.id.tv_address);
    }

    /**
     * @param mBaiduMap
     * @param ll
     */
    @SuppressLint("SetTextI18n")
    public void showViewData(BaiduMap mBaiduMap, LatLng ll, BaiduMarkerBean markerBean) {
        try {

            mTvCarNumber.setText(markerBean.getName());
            mTvTimes.setText(markerBean.getTime());
            mTvComtent.setText(markerBean.getContent());
            mTvAddress.setText(markerBean.getAddress());

            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                public void onInfoWindowClick() {
                    //				LatLng ll = mMarker.getPosition();
                    //				LatLng llNew = new LatLng(ll.latitude ,ll.longitude);
                    //				mMarker.setPosition(llNew);
                    //				mBaiduMap.hideInfoWindow();
                    //				mInfoWindow = null ;
                }
            };
            if (mInfoWindow != null) {
                mBaiduMap.hideInfoWindow();
                mInfoWindow = null;
            }
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(popoverView), ll, -40, listener);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
