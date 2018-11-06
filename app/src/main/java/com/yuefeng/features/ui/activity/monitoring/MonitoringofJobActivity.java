package com.yuefeng.features.ui.activity.monitoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.common.base.codereview.BaseActivity;
import com.common.location.LocationHelper;
import com.common.location.MyLocationListener;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.MonitoringOfJobContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.presenter.monitoring.MonitoringOfJobPresenter;
import com.yuefeng.features.ui.activity.ProblemUpdateActivity;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/*作业监察*/
public class MonitoringofJobActivity extends BaseActivity implements MonitoringOfJobContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_root_position)
    LinearLayout ll_root_position;
    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.tv_title_setting)
    TextView tvTitleSetting;


    @BindView(R.id.btn_beginorstop)
    Button btnBeginorstop;
    @BindView(R.id.rl_upload)
    RelativeLayout rlupload;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R.id.tv_plan_count)
    TextView tvPlanCount;
    @BindView(R.id.tv_actual_count)
    TextView tvActualCount;
    @BindView(R.id.rl_select_type)
    RelativeLayout rlSelectType;
    @BindView(R.id.ct_timer)
    Chronometer ctTimer;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_signin)
    TextView tvSignin;
    @BindView(R.id.iv_stop)
    ImageView ivStop;


    private boolean isFirstLoc = true;
    private BaiduMap baiduMap;
    private MarkerOptions ooA;
    private Marker mMarker;
    BitmapDescriptor personalImage = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    BitmapDescriptor beginImage = BitmapDescriptorFactory.fromResource(R.drawable.start);
    BitmapDescriptor endImage = BitmapDescriptorFactory.fromResource(R.drawable.destination);
    private double latitude;
    private double longitude;
    private String address;

    private long mRecordTime;
    private LatLng latLng;
    private LocationHelper mLocationHelper;
    private LatLng latLngTemp = null;
    /*距离*/
    private double distance = 0;
    /*条件选择框*/
    List<LatLng> points = new ArrayList<>();
    private Polyline mPolyline;
    private MonitoringOfJobPresenter presenter;

    /*是否采集*/
    private boolean isPositionAcquisition = false;
    private boolean isStartTime = true;
    /*选择网格还是路段*/
    private int typePosition = 0;
    private String area;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_monitoringofjob;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        tv_title.setText(R.string.problem_updata);
        tvTitleSetting.setText("历史");
        presenter = new MonitoringOfJobPresenter(this, this);
        isPositionAcquisition = false;
    }

    private void initChronometer() {
        ctTimer.setBase(SystemClock.elapsedRealtime());
        ctTimer.setFormat("%s");
    }


    @Override
    protected void initData() {
        requestPermissions();
        initChronometer();
    }

    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
                        } else {
                            getLocation();
                        }
                    }
                });
    }

    /*定位*/
    private void getLocation() {
        baiduMap = mapview.getMap();
        // 地图初始化
        mapview.showZoomControls(false);// 缩放控件是否显示

        // 开启定位图层
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化

        baiduMap.showMapPoi(true);
        boolean gpsOPen = LocationGpsUtils.isGpsOPen(this);
        if (!gpsOPen) {
            showSuccessToast("GPS未开启，定位有偏差");
        }
        useBdGpsLocation();
    }


    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    private void useBdGpsLocation() {
        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    requestPermissions();
                    return;
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                }
                firstLocation(latitude, longitude, address);
            }
        }, Constans.BDLOCATION_TIME);
        initLocation();
    }

    private void firstLocation(double latitude, double longitude, String address) {
        try {
            if (!TextUtils.isEmpty(address)) {
                int length = address.length();
                address = address.substring(2, length);
            }
            latLngTemp = new LatLng(latitude, longitude);
            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                        .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                ooA = new MarkerOptions().icon(personalImage).zIndex(10);
                ooA.position(latLngTemp);
                mMarker = null;
                mMarker = (Marker) (baiduMap.addOverlay(ooA));
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
//            BdLocationUtil.MoveMapToCenter(baiduMap, latLngTemp, 14);
                PreferencesUtils.putString(MonitoringofJobActivity.this, "Fengrun", "");
                PreferencesUtils.putString(MonitoringofJobActivity.this, "mAddress", address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*定时刷新*/
    private void initLocation() {
        mLocationHelper = new LocationHelper(this, Constans.TEN);
        mLocationHelper.initLocation(new MyLocationListener() {

            @Override
            public void updateLastLocation(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateLocation(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
                longitude = latLng.longitude;
                latitude = latLng.latitude;
                starDrawTrackLine(latLng);
            }

            @Override
            public void updateStatus(String provider, int status, Bundle extras) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateGpsStatus(GpsStatus gpsStatus) {

            }
        });
    }

    private void starDrawTrackLine(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        double distance = DistanceUtil.getDistance(latLngTemp, latLng);
        if (distance > 0) {
            latLngTemp = latLng;
            drawTrackLine(latLngTemp);
        }
    }

    /*划线*/
    private void drawTrackLine(LatLng latLng) {
        try {
            if (isPositionAcquisition) {
                points.add(latLng);//如果要运动完成后画整个轨迹，位置点都在这个集合中
                if (points.size() > 1 && baiduMap != null) {
                    //清除上一次轨迹，避免重叠绘画
                    baiduMap.clear();
                    //起始点图层也会被清除，重新绘画
                    MarkerOptions oStart = new MarkerOptions();
                    oStart.position(points.get(0));
                    oStart.icon(beginImage);
                    baiduMap.addOverlay(oStart);

                    OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.RED).points(points);
                    mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                    BdLocationUtil.MoveMapToCenter(baiduMap, points.get(points.size() - 1), Constans.BAIDU_ZOOM_EIGHTEEN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposePositionAcquisitionEvent(PositionAcquisitionEvent event) {
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:
                showSuccessDialog("上传成功，是否退出当前界面?");
                break;
            case Constans.MSGCOLECTION_ERROR://上传失败

                break;
            case Constans.GETCAIJI_SSUCESS://获采集类型成功
                break;
            case Constans.GETCAIJI_ERROR://获采集类型失败
//                showSuccessToast("发布失败");
                break;
            default:

                break;
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BdLocationUtil.getInstance().stopLocation();//停止定位
        if (mLocationHelper != null) {
            mLocationHelper.removeLocationUpdatesListener();
        }
        assert mapview != null;
        mapview.getMap().clear();
        mapview.onDestroy();
        mapview = null;
        beginImage.recycle();
        endImage.recycle();
    }

    /*发布*/
    private void tv_release() {
//        if (presenter != null && points.size() > 0) {
//
//            String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
//            String userId = PreferencesUtils.getString(this, Constans.ID, "");
//            lnglat = presenter.getLnglatStr(points);
//            presenter.upLoadmapInfo(ApiService.UPLOADMAPINFO, pid, userId, typeId, tyepName, name, lnglat,
//                    area, mImagesArrays);
//        }
    }


    /*距离或者面积*/
    private void initAreaOrDistance() {
        LatLng latLngTemp = null;
        area = "";
        if (points.size() > 2) {
            if (typePosition == 0) {//网格
                points.add(new LatLng(points.get(0).latitude, points.get(0).longitude));
                area = LocationUtils.getArea(points);//面积
            } else {//路段
                for (int i = 0; i < points.size(); i++) {
                    if (latLngTemp != null) {
                        distance = distance + DistanceUtil.getDistance(latLngTemp, points.get(i));
                    }
                    latLngTemp = points.get(i);
                }
                distance = distance / 1000.0;
                area = StringUtils.getStringDistance(distance);
            }
        }

        //起始点图层也会被清除，重新绘画
        if (points.size() > 1) {
            //清除上一次轨迹，避免重叠绘画
            baiduMap.clear();
            //起始点图层也会被清除，重新绘画
            MarkerOptions oStart = new MarkerOptions();
            oStart.position(points.get(0));
            oStart.icon(beginImage);
            baiduMap.addOverlay(oStart);

            OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.RED).points(points);
            mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
            BdLocationUtil.MoveMapToCenter(baiduMap, points.get(points.size() - 1), Constans.BAIDU_ZOOM_EIGHTEEN);
            MarkerOptions oEnd = new MarkerOptions();
            oEnd.position(points.get(points.size() - 2));
            oEnd.icon(endImage);
            baiduMap.addOverlay(oEnd);
        }
    }


    /*开始监察*/
    private void beginOrStop() {
        points.add(new LatLng(latitude, longitude));
        initCtStart();
        ViewUtils.setRlInVisible(rlSelectType, true);
        ViewUtils.setBtnInVisible(btnBeginorstop, true);
        ViewUtils.setRlInVisible(rlupload, false);
        ctTimer.setVisibility(View.VISIBLE);
    }

    /*计时器start*/
    private void initCtStart() {
        if (ctTimer != null)
            if (mRecordTime != 0) {
                ctTimer.setBase(ctTimer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
            } else {
                ctTimer.setBase(SystemClock.elapsedRealtime());
            }
        assert ctTimer != null;
        ctTimer.start();
    }

    /*计时器stop*/
    private void initCtStop() {
        if (ctTimer != null) {
            ctTimer.stop();
        }
        mRecordTime = SystemClock.elapsedRealtime();
    }


    @OnClick({R.id.tv_upload, R.id.tv_signin, R.id.iv_stop, R.id.btn_beginorstop, R.id.tv_title_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                initCtStop();
                startActivity(new Intent(MonitoringofJobActivity.this, ProblemUpdateActivity.class));
                break;
            case R.id.tv_signin:
                initCtStop();
                startActivity(new Intent(MonitoringofJobActivity.this, MonitoringSngnInActivity.class));
                break;
            case R.id.iv_stop:
                if (isStartTime) {
                    initCtStart();
                    isStartTime = false;
                    isPositionAcquisition = true;
                } else {
                    initCtStop();
                    isStartTime = true;
                    isPositionAcquisition = false;
                }
                break;
            case R.id.btn_beginorstop:
                beginOrStop();
                break;
            case R.id.tv_title_setting:
                showSuccessToast("历史");
                break;
        }

    }
}
