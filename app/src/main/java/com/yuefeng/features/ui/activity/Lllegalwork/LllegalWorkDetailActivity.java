package com.yuefeng.features.ui.activity.Lllegalwork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.common.utils.DensityUtil;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.LllegalworMsgBean;
import com.yuefeng.utils.BdLocationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/*违规作业*/
public class LllegalWorkDetailActivity extends BaseActivity {

    private static final String TAG = "tag";
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.ll_infos)
    LinearLayout ll_infos;
    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.view_line_gray)
    RelativeLayout viewLineGray;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_native)
    TextView tvNative;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.search_history_lllegal)
    TextView searchHistoryLllegal;
    @BindView(R.id.ll_content_visible)
    LinearLayout llContentVisible;

    private boolean isVisibleOrGone = true;
    public boolean isFirstLoc = true;
    private BitmapDescriptor location_icon = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    private MarkerOptions oA;
    private Marker mMarker;
    private LatLng latLng;
    private BaiduMap baiduMap;
    private Polyline mPolyline;

    private String id;
    private String type;
    private LatLng latLngSecond;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_lllegalworkdetail;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        getCarListInfos();
        tvAddress.setTextSize(15);
        tvNative.setTextSize(15);
        tvDistance.setTextSize(13);
        tvType.setTextSize(13);
        tvClass.setTextSize(13);
        searchHistoryLllegal.setTextSize(15);
    }

    /*车辆列表*/
    @SuppressLint("SetTextI18n")
    private void getCarListInfos() {
        try {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            LllegalworMsgBean msgDataBean = (LllegalworMsgBean) bundle.getSerializable("DetailInfos");
            type = (String) bundle.get("type");
            String isVisible = (String) bundle.get("isVisible");
            id = (String) bundle.get("id");
            assert isVisible != null;
            if (isVisible.equals("0")) {
                searchHistoryLllegal.setVisibility(View.INVISIBLE);
            }
            assert type != null;
            if (type.equals("car")) {
                location_icon = BitmapDescriptorFactory.fromResource(R.drawable.vehicle);
            } else {
                location_icon = BitmapDescriptorFactory.fromResource(R.drawable.worker);
            }
            assert msgDataBean != null;
            String title = msgDataBean.getName();
            title = TextUtils.isEmpty(title) ? "" : title;
            tv_title.setText(title);
            tvAddress.setText(msgDataBean.getAddress());
            id = msgDataBean.getPersonalid();
            String team = msgDataBean.getTeam();
            String contents = msgDataBean.getContents();
            contents = TextUtils.isEmpty(contents) ? "无" : contents;
            team = TextUtils.isEmpty(team) ? "无" : team;
            tvType.setText("违规类型:" + contents);
            tvClass.setText("所属班组:" + team);
            String lat = msgDataBean.getLat();
            String lon = msgDataBean.getLon();
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
                Double latitude = Double.valueOf(lat);
                Double longitude = Double.valueOf(lon);
                latLngSecond = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
            }
            requestPermissions();
        } catch (Exception e) {
            e.printStackTrace();
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
                        }
                        getLocation();
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

        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    requestPermissions();
                    return;
                }
//                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    MapStatus ms = new MapStatus.Builder().target(latLng)
                            .overlook(-20).zoom(14).build();
                    oA = new MarkerOptions().icon(location_icon).zIndex(10);
                    oA.position(latLng);
                    mMarker = null;
                    mMarker = (Marker) (baiduMap.addOverlay(oA));
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    BdLocationUtil.MoveMapToCenter(baiduMap, latLng, 14);
                    showCarDetailInfos(latLng, latLngSecond);
                }

            }
        }, Constans.BDLOCATION_TIME);
    }


    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    /*show轨迹  划线*/
    @SuppressLint("SetTextI18n")
    private void showCarDetailInfos(LatLng latLng, LatLng latLngSecond) {
        try {
            if (latLng != null && latLngSecond != null) {
                double distance = DistanceUtil.getDistance(latLng, latLngSecond);
                distance = Math.abs(distance);
                String mile = "米";
                if (distance > 1000) {
                    distance = distance / 1000;
                    mile = "km";
                }
                String stringDouble = StringUtils.getStringDouble(distance);
                tvDistance.setText("已偏移" + stringDouble + mile);
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
    }


    @OnClick({R.id.view_line_gray, R.id.tv_native, R.id.search_history_lllegal, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_line_gray:
                if (isVisibleOrGone) {
                    ViewUtils.setLLHightOrWidth(ll_infos, (int) DensityUtil.dip2px(LllegalWorkDetailActivity.this, 98),
                            ActionBar.LayoutParams.MATCH_PARENT);
                    llContentVisible.setVisibility(View.GONE);
                    isVisibleOrGone = false;
                } else {
                    ViewUtils.setLLHightOrWidth(ll_infos, (int) DensityUtil.dip2px(LllegalWorkDetailActivity.this, 220),
                            ActionBar.LayoutParams.MATCH_PARENT);
                    llContentVisible.setVisibility(View.VISIBLE);
                    isVisibleOrGone = true;
                }
                assert baiduMap != null;
                BdLocationUtil.MoveMapToCenter(baiduMap, latLng, Constans.UPDATAAPP_SUCCESS);
                break;
            case R.id.tv_native:
                showSuccessToast("导航");
                break;
            case R.id.search_history_lllegal:
                Intent intent = new Intent(LllegalWorkDetailActivity.this, HistoryLllegalWorkActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("id", id);
                startActivity(intent);
                break;

            case R.id.tv_back:
                finish();
                break;
        }
    }

}
