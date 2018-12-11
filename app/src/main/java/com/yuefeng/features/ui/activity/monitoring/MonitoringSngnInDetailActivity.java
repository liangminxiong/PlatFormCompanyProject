package com.yuefeng.features.ui.activity.monitoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.HistorySngnInListDataBean;
import com.yuefeng.photo.utils.ImageHelper;
import com.yuefeng.photo.view.MyGridView2;
import com.yuefeng.utils.BdLocationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/*作业监察历史签到详情*/
public class MonitoringSngnInDetailActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.iv_line)
    ImageView ivLine;
    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.type)
    TextView tvType;
    @BindView(R.id.name)
    TextView nameType;
    @BindView(R.id.tv_type_area)
    TextView tvTypeArea;
    @BindView(R.id.tv_theme)
    TextView tvName;
    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.time)
    TextView greateTime;
    @BindView(R.id.gridview)
    MyGridView2 gridview;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private boolean isFirstLoc = true;
    private BaiduMap baiduMap;
    private MarkerOptions ooA;
    private Marker mMarker;
    BitmapDescriptor personalImage = BitmapDescriptorFactory.fromResource(R.drawable.caiji_location);
    BitmapDescriptor beginImage = BitmapDescriptorFactory.fromResource(R.drawable.start);
    BitmapDescriptor endImage = BitmapDescriptorFactory.fromResource(R.drawable.destination);
    private double latitude;
    private double longitude;
    private String address;

    /*条件选择框*/
    private Polyline mPolyline;
    private String type;

    /*是否采集*/
    private HistorySngnInListDataBean msgBean;
    private LatLng latLng;
    private String name;
    private String time;
    private String contents;
    private String typeName;
    private boolean isVisible = true;
    private String imageUrls;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_positionacquistiondetail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initUI();
        isVisible = true;
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        try {
            tvBack.setText(R.string.back);
            tvTitle.setText("历史签到");
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            msgBean = (HistorySngnInListDataBean) bundle.getSerializable("dataBean");
            assert msgBean != null;
            name = msgBean.getMemo();
            time = msgBean.getTime();
            typeName = msgBean.getPersonalName();

            name = StringUtils.isEntryStrWu(name);
            time = StringUtils.returnStrTime(time);
            contents = StringUtils.isEntryStrWu(contents);
            typeName = StringUtils.isEntryStrWu(typeName);
//            tvType.setText("");
//            tvTypeName.setText("");
//            tvTypeArea.setText("");
            ViewUtils.setTvVisibleOrGone(tvType,false);
            ViewUtils.setTvVisibleOrGone(tvTypeName,false);
            ViewUtils.setTvVisibleOrGone(tvTypeArea,false);
            nameType.setText("描述");
//            tvTypeName.setText(typeName);
            tvName.setText(name);
            tvTime.setText(time);
            imageUrls = msgBean.getImgurl();
            if (TextUtils.isEmpty(imageUrls)) {
                imageUrls = ",,";
            }
            ImageHelper.showImageBitmap(gridview, MonitoringSngnInDetailActivity.this, imageUrls);

            String lat = msgBean.getLat();
            String lon = msgBean.getLon();
            LatLng latLng = null;
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
                latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                initBaiduMap();
                showDataImageIntoMap(latLng);
            } else {
                requestPermissions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDataImageIntoMap(LatLng latLng) {

        if (baiduMap != null) {
            //清除上一次轨迹，避免重叠绘画
            baiduMap.clear();
            MarkerOptions oStart = new MarkerOptions();
            oStart.position(latLng);
            oStart.icon(personalImage);
            baiduMap.addOverlay(oStart);
            BdLocationUtil.MoveMapToCenter(baiduMap, latLng, Constans.BAIDU_ZOOM_EIGHTEEN);
        }
    }


    @Override
    protected void initData() {

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
        initBaiduMap();
        boolean gpsOPen = LocationGpsUtils.isGpsOPen(this);
        if (!gpsOPen) {
            showSuccessToast("GPS未开启，定位有偏差");
        }
        useBdGpsLocation();
    }

    private void initBaiduMap() {
        baiduMap = mapview.getMap();
        // 地图初始化
        mapview.showZoomControls(false);// 缩放控件是否显示

        // 开启定位图层
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化

        baiduMap.showMapPoi(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    private void useBdGpsLocation() {
        try {
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
                    if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                        int length = address.length();
                        address = address.substring(2, length);
                    }
                    firstLocation(latitude, longitude, address);
                }
            }, Constans.BDLOCATION_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstLocation(double latitude, double longitude, String address) {
        try {
            if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                int length = address.length();
                address = address.substring(2, length);
            }
            latLng = new LatLng(latitude, longitude);
            if (isFirstLoc) {
                isFirstLoc = false;
                PreferencesUtils.putString(MonitoringSngnInDetailActivity.this, Constans.ADDRESS, address);
                MapStatus ms = new MapStatus.Builder().target(latLng)
                        .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                ooA = new MarkerOptions().icon(personalImage).zIndex(10);
//                ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                ooA.position(latLng);
                mMarker = null;
                mMarker = (Marker) (baiduMap.addOverlay(ooA));
//                ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        assert mapview != null;
        mapview.getMap().clear();
        mapview.onDestroy();
        mapview = null;
        beginImage.recycle();
        endImage.recycle();
    }

    @OnClick(R.id.iv_line)
    public void onViewClicked() {
        if (isVisible) {
            ViewUtils.setTvIsGone(tvTime, isVisible);
            ViewUtils.setTvIsGone(greateTime, isVisible);
            gridview.setVisibility(View.GONE);
            ViewUtils.setTvIsGone(tvImage, isVisible);
            isVisible = false;
        } else {
            ViewUtils.setTvIsGone(tvImage, isVisible);
            gridview.setVisibility(View.VISIBLE);
            ViewUtils.setTvIsGone(greateTime, isVisible);
            ViewUtils.setTvIsGone(tvTime, isVisible);
            isVisible = true;
        }

    }

}
