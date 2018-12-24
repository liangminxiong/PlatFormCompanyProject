package com.yuefeng.features.ui.activity.Lllegalwork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.common.base.codereview.BaseActivity;
import com.common.location.LocationHelper;
import com.common.location.MyLocationListener;
import com.common.utils.Constans;
import com.common.utils.DensityUtil;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.LllegalworMsgBean;
import com.yuefeng.features.ui.activity.nativ.DemoGuideActivity;
import com.yuefeng.utils.BdLocationUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    /*导航*/
    private static final int authBaseRequestCode = 1;
    private boolean hasInitSuccess = false;
    private BNRoutePlanNode mStartNode = null;
    private String mSDCardPath = null;

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private String beginAddress;
    private String endAddress;
    private SucessCacheSureDialog successDialog;
    private LocationHelper mLocationHelper;
    private double longitude;
    private double latitude;


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
            endAddress = msgDataBean.getAddress();
            tvAddress.setText(endAddress);
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
            requestPermissions(latLngSecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
    }

    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     *
     * @param latLngSecond
     */
    @SuppressLint("CheckResult")
    private void requestPermissions(final LatLng latLngSecond) {
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
                        getLocation(latLngSecond);
                    }
                });
    }

    /*定位*/
    private void getLocation(final LatLng latLngSecond) {
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
                    requestPermissions(latLngSecond);
                    return;
                }
//                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                beginAddress = location.getAddrStr();
                if (isFirstLoc) {
                    latLng = new LatLng(latitude, longitude);
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
        }, Constans.BDLOCATION_TIME,true);
        initLocation();
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
                String mile = "km";
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
                initNavicar();
                break;
            case R.id.search_history_lllegal:
                Intent intent = new Intent(LllegalWorkDetailActivity.this, HistoryLllegalWorkActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("id", id);
                startActivity(intent);
                break;

        }
    }


    /*=====================begin 导航 =======================*/

    /*导航追车*/
    private void initNavicar() {

        double distance = DistanceUtil.getDistance(latLng, latLngSecond);
        distance = Math.abs(distance);

        /*初始化导航*/

        if (distance <= 500) {
            showSuccessToast("距离太短无法导航");
            return;
        }

//        BNRoutePlanNode sNode = new BNRoutePlanNode(mlatLng.longitude, mlatLng.latitude, beginAddress, beginAddress, coType);
//        BNRoutePlanNode eNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, endAddress, endAddress, coType);

//        if ((latLng.longitude > 140.0) || (latLng.longitude < 65.0) || (latLng.latitude > 56.0) || (latLng.latitude < 12.0)) {
//            showSuccessToast("定位出问题，请检查");
//            return;
//        }
//        if ((latLngSecond.longitude > 140.0) || (latLngSecond.longitude < 65.0) || (latLngSecond.latitude > 56.0) || (latLngSecond.latitude < 12.0)) {
//            showSuccessToast("定位出问题，请检查");
//            return;
//        }

        if (TextUtils.isEmpty(endAddress) || endAddress.equals("暂无地址!")) {
            showSuccessToast("目的地不明确");
            return;
        }
        if (TextUtils.isEmpty(beginAddress) || beginAddress.equals("暂无地址!")) {
            showSuccessToast("定位出问题，请检查");
            return;
        }

        if (initDirs()) {
            initNavi();
        }

        successDialog = new SucessCacheSureDialog(this);
        successDialog.setTextContent("是否进行导航?");

        successDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                    routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                }

                if (!isFinishing()) {
                    successDialog.dismiss();
                }
            }

            @Override
            public void cancle() {
                if (!isFinishing()) {
                    successDialog.dismiss();
                }
            }
        });
        if (!isFinishing()) {
            successDialog.show();
        }

    }


    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), Constans.APP_FOLDER_NAME, Constans.BAIDU_APPID);

        // 不使用内置TTS
//        BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {

                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                    }
                }
        );
    }

    // 外置tts时需要实现的接口回调
    private IBNTTSManager.IBNOuterTTSPlayerCallback mTTSCallback = new IBNTTSManager.IBNOuterTTSPlayerCallback() {

        @Override
        public int getTTSState() {
//            /** 播放器空闲 */
//            int PLAYER_STATE_IDLE = 1;
//            /** 播放器正在播报 */
//            int PLAYER_STATE_PLAYING = 2;
            return PLAYER_STATE_IDLE;
        }

        @Override
        public int playTTSText(String text, String s1, int i, String s2) {
            return 0;
        }

        @Override
        public void stopTTS() {
        }
    };

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, Constans.APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, Constans.APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
//                        String result;
//                        if (0 == status) {
//                            result = "key校验成功!";
//                        } else {
//                            result = "key校验失败, " + msg;
//                        }
//                        showToast(result);
                    }

                    @Override
                    public void initStart() {
//                        showToast("百度导航引擎初始化开始");
                    }

                    @Override
                    public void initSuccess() {
//                        showToast("百度导航引擎初始化成功");
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
//                        showToast("百度导航引擎初始化失败");
                    }
                });

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void routeplanToNavi(final int coType) {
        if (!hasInitSuccess) {
            showSuccessToast("还未初始化!");
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, beginAddress, beginAddress, coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(latLngSecond.longitude, latLngSecond.latitude, endAddress, endAddress, coType);
        switch (coType) {
            case BNRoutePlanNode.CoordinateType.BD09LL: {
                sNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, beginAddress, beginAddress, coType);
                eNode = new BNRoutePlanNode(latLngSecond.longitude, latLngSecond.latitude, endAddress, endAddress, coType);
                break;
            }
            default:
                break;
        }

        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                showSuccessToast("算路开始");
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                showSuccessToast("算路成功");
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                showSuccessToast("算路失败");
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                showSuccessToast("算路成功准备进入导航");
                                Intent intent = new Intent(LllegalWorkDetailActivity.this, DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constans.ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    showSuccessToast("缺少导航基本的权限!");
                    return;
                }
            }
            initNavi();
        }
    }

    /*=====================end 导航 =======================*/

}
