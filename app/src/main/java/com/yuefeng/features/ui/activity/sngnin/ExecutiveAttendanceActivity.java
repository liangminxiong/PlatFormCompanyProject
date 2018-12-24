package com.yuefeng.features.ui.activity.sngnin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.modle.NewMsgListDataBean;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/*主管考勤*/
public class ExecutiveAttendanceActivity extends BaseActivity {

    @BindView(R.id.tv_title_setting)
    TextView mTvSetting;
    @BindView(R.id.baidumap)
    TextureMapView mMapView;

    private boolean isFirstLocation = true;
    private LatLng mLatLng;
    BitmapDescriptor map_location = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    public MarkerOptions ooA;
    private Marker mMarker;
    private BaiduMap mBaiduMap;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_executiveattendance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setTitle("主管考勤");
        mTvSetting.setText("选择人员");
        initBaiduMap();
    }

    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(true);// 缩放控件是否显示
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setOverlookingGesturesEnabled(false);
        settings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
        settings.setAllGesturesEnabled(true);
        // 地图初始化

        latitude = MyApplication.latitude;
        longitude = MyApplication.longitude;
        address = MyApplication.address;
        if (latitude > 0 && longitude > 0 && !TextUtils.isEmpty(address)) {
            mLatLng = BdLocationUtil.ConverCommonToBaidu(new LatLng(latitude, longitude));
            BdLocationUtil.MoveMapToCenterWithBitmap(mBaiduMap, mLatLng, Constans.BAIDU_ZOOM_FOUTTEEN
                    , map_location, ooA, mMarker);
        } else {
            requestPermissions();
        }
    }


    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        try {
            RxPermissions rxPermission = new RxPermissions(this);
            rxPermission.request(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                startLocation();
                            } else {
                                showSuccessToast("App未能获取定位权限，功能不能正常使用.");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLocation() {
        try {
            BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
                @Override
                public void myLocation(BDLocation location) {
                    if (location == null) {
                        requestPermissions();
                        return;
                    }
                    address = location.getAddrStr();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                        int length = address.length();
                        address = address.substring(2, length);
                        if (latitude > 0 && longitude > 0) {
                            MyApplication.latitude = latitude;
                            MyApplication.longitude = longitude;
                            MyApplication.address = address;

                        }
                        if (isFirstLocation) {
                            isFirstLocation = false;
                            MapStatus ms = new MapStatus.Builder().target(mLatLng)
                                    .overlook(-20).zoom(17).build();
                            BdLocationUtil.MoveMapToCenter(mBaiduMap, mLatLng, Constans.BAIDU_ZOOM_FOUTTEEN);
                            ooA = new MarkerOptions().icon(map_location).zIndex(13);
                            ooA.position(mLatLng);
                            mMarker = null;
                            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                            isFirstLocation = false;
                        }
                    }
                }
            }, Constans.BDLOCATION_TIME, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick(R.id.tv_title_setting)
    public void onClickView(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_title_setting:
                showSuccessToast("选择人员");
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.NEW_MSG_SUCCESS://展示最新消息
                List<NewMsgListDataBean> list = (List<NewMsgListDataBean>) event.getData();
                if (list.size() > 0) {
//                    showAdapterDatasList(list);
                } else {
                    showSuccessToast("无最新消息");
                }
                break;

            case Constans.NEW_MSG_ERROR:
//                addNativeDatas();
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        map_location.recycle();
        EventBus.getDefault().unregister(this);
    }
}
