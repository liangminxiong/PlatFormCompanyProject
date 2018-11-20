package com.yuefeng.features.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.common.view.dialog.ShowPersonalpop;
import com.common.view.dialog.ShowProblempop;
import com.common.view.dialog.SucessCacheSureDialog;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.MyJobViewPagerAdapter;
import com.yuefeng.features.contract.JobMoniroringContract;
import com.yuefeng.features.event.JobMonitoringEvent;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.PersonalinfoListBean;
import com.yuefeng.features.modle.QuestionListBean;
import com.yuefeng.features.modle.VehicleinfoListBean;
import com.yuefeng.features.presenter.JobMonitoringPresenter;
import com.yuefeng.features.ui.activity.nativ.DemoGuideActivity;
import com.yuefeng.features.ui.activity.track.HistoryTrackActivity;
import com.yuefeng.features.ui.activity.video.VideoCameraActivity;
import com.yuefeng.features.ui.fragment.PersonalFragment;
import com.yuefeng.features.ui.fragment.QuestionListFragment;
import com.yuefeng.features.ui.fragment.VehicleListFragment;
import com.yuefeng.ui.base.fragment.TabItemInfo;
import com.yuefeng.ui.view.PersonaCarProblemPopupWindow;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.FilterMonitoringUtils;
import com.yuefeng.utils.LocationUtils;
import com.yuefeng.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/*定位信息*/

public class JobMonitoringActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener, JobMoniroringContract.View, LocationUtils.OnResultMapListener {

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title_setting)
    TextView tv_title_setting;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.ll_tab)
    LinearLayout ll_tab;
    @BindView(R.id.ll_root)
    LinearLayoutCompat ll_root;
    @BindView(R.id.ll_tab_visible)
    LinearLayout ll_tab_visible;
    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.iv_isvisibility)
    ImageView iv_isvisibility;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<TabItemInfo> tabItemInfos;
    //TabLayout标签
    private MyJobViewPagerAdapter viewPagerAdapter;
    List<String> stringList = new ArrayList<>();
    private TabItemInfo tabItemInfoA;
    private TabItemInfo tabItemInfoB;
    private TabItemInfo tabItemInfoC;
    private JobMonitoringPresenter presenter;
    private boolean isGoneOrVisibility = true;
    private boolean isFirstLoc = true;
    private BaiduMap baiduMap;
    BitmapDescriptor worker = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    BitmapDescriptor problem = BitmapDescriptorFactory.fromResource(R.drawable.problem);
    BitmapDescriptor vehicle = BitmapDescriptorFactory.fromResource(R.drawable.vehicle);
    private LatLng latLng;
    private LatLng mlatLng;
    private MarkerOptions ooA;
    private Marker mMarker;
    public static GeoCoder geoCoder;
    private List<Marker> mks = new ArrayList<Marker>();
    private String name;
    private String position;
    private String tel;
    private String className;
    private String latitude;
    private String longitude;
    private String time;
    private String type;
    private String state;
    private int colorInt;
    private String des;
    private String uploadpeoplename;
    private String terminalNO;

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
    private String decimalFormat;
    private double distance;
    private SucessCacheSureDialog successDialog;
    private ShowPersonalpop phototpop;

    private LocationUtils mLocationUtils;
    private PersonalFragment personalFragment;
    private VehicleListFragment vehicleListFragment;
    private QuestionListFragment questionListFragment;
    private boolean isVisible;
    List<PersonalinfoListBean> personalinfoList = new ArrayList<>();
    List<VehicleinfoListBean> vehicleinfoList = new ArrayList<>();
    List<QuestionListBean> questionList = new ArrayList<>();
    private List<Node> mNodeList = new ArrayList<>();
    private int mSize;
    private int mSize_v;
    private int mSize_q;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_jobmonitoring;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        try {
            presenter = new JobMonitoringPresenter(this, this);
            initUI();
            initViewPager();
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            isGoneOrVisibility = true;

            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            geoCoder = GeoCoder.newInstance();

            // 创建定位管理信息对象
            mLocationUtils = new LocationUtils(getApplicationContext());
//         开启定位
            mLocationUtils.startLocation();
            mLocationUtils.registerOnResult(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        tv_back.setText(R.string.back);
        tv_title.setText("定位信息");
        tv_title_setting.setBackgroundResource(R.drawable.list);
    }

    @SuppressLint("InflateParams")
    private void initViewPager() {
        try {
            //设置TabLayout点击事件
            tabLayout.addOnTabSelectedListener(this);
            tabItemInfos = new ArrayList<>();
            personalFragment = new PersonalFragment();
            tabItemInfoA = new TabItemInfo(personalFragment, R.drawable.tab_button_selector, R.string.personal);
            tabItemInfos.add(tabItemInfoA);
            vehicleListFragment = new VehicleListFragment();
            tabItemInfoB = new TabItemInfo(vehicleListFragment, R.drawable.tab_button_selector, R.string.vehicle);
            tabItemInfos.add(tabItemInfoB);
            questionListFragment = new QuestionListFragment();
            tabItemInfoC = new TabItemInfo(questionListFragment, R.drawable.tab_button_selector, R.string.problem);
            tabItemInfos.add(tabItemInfoC);

            viewPagerAdapter = new MyJobViewPagerAdapter(getSupportFragmentManager(), tabItemInfos, JobMonitoringActivity.this);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOffscreenPageLimit(tabItemInfos.size());
            tabLayout.setupWithViewPager(viewPager);
            isFirstError = false;
            initBaiduMap();
            requestPermissions();

            isFirstLoc = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabViewCount(List<String> stringList) {
        try {
            tabItemInfoA.setTabCount("人员(" + stringList.get(0) + ")");
            tabItemInfoB.setTabCount("车辆(" + stringList.get(1) + ")");
            tabItemInfoC.setTabCount("问题(" + stringList.get(2) + ")");
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    if (tab.getCustomView() != null) {
                        TextView countView = (TextView) tab.getCustomView().findViewById(R.id.tab_name);
                        countView.setText(stringList.get(i));
                    } else {
                        tab.setCustomView(viewPagerAdapter.getTabView(i));
                    }
                }
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

    @OnClick({R.id.tv_title_setting, R.id.iv_isvisibility, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_isvisibility:
                showViewPagerVisibilty();
                break;
            case R.id.tv_title_setting:
                showFilterDatas();
                break;
            case R.id.tv_back:
                finish();
                break;
        }

    }

    /*展示数据*/
    private void showFilterDatas() {
        try {
            showLoadingDialog("请稍等...");
            if (mNodeList.size() > 0) {
                PersonaCarProblemPopupWindow filterPopuWindow =
                        new PersonaCarProblemPopupWindow(this, mNodeList, false);
                filterPopuWindow.setTitleText("人员、车辆或者问题");
                filterPopuWindow.setSettingText(getString(R.string.sure));
                filterPopuWindow.showAtLocation(ll_root, Gravity.BOTTOM, 0, 0);
            }
            dismissLoadingDialog();
        } catch (Exception e) {
            dismissLoadingDialog();
            e.printStackTrace();
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
                            if (!granted) {
                                showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
                            }
                            getLocation();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {
        try {
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
                    beginAddress = location.getAddrStr();
                    if (!TextUtils.isEmpty(beginAddress)) {
                        int length = beginAddress.length();
                        beginAddress = beginAddress.substring(2, length);
                    }
                    mlatLng = new LatLng(latitude, longitude);
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        MapStatus ms = new MapStatus.Builder().target(mlatLng)
                                .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                        ooA = new MarkerOptions().icon(worker).zIndex(10);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                        ooA.position(mlatLng);
                        mMarker = null;
                        mMarker = (Marker) (baiduMap.addOverlay(ooA));
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    }
//                } else {
//                    requestPermissions();
//                }
                }
            }, Constans.BDLOCATION_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (geoCoder != null) {
            geoCoder.destroy();
        }
        BdLocationUtil.getInstance().stopLocation();
    }

    private void showViewPagerVisibilty() {
        try {
            if (isGoneOrVisibility) {
                viewPager.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.up);
                iv_isvisibility.setImageBitmap(bitmap);
                isGoneOrVisibility = false;
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.down);
                iv_isvisibility.setImageBitmap(bitmap);
                isGoneOrVisibility = true;
                viewPager.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        getQuestingCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeJobMonitoringEvent(JobMonitoringEvent event) {
        try {
            switch (event.getWhat()) {
                case Constans.JOB_SSUCESS://展示
                    GetJobMonitotingMsgBean bean = (GetJobMonitotingMsgBean) event.getData();
                    if (bean != null) {
                        showCountData(bean);
                    }
                    break;

                case Constans.JOB_ERROR:
                    showErrorToast("加载失败，请重试");
//                    if (isFirstError) {
//                        isFirstError = true;
//                    }
                    initListdatas("人员(0)", "车辆(0)", "问题(0)");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFirstError = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeJobMonitoringFragmentEvent(JobMonitoringFragmentEvent event) {
        if (baiduMap != null) {
            baiduMap.clear();
            mks.clear();
        }
        isFirstLoc = true;
        isGoneOrVisibility = false;
        showViewPagerVisibilty();
        switch (event.getWhat()) {
            case Constans.PERSONAL_SSUCESS://人员:
                personalinfoList.clear();
                personalinfoList = (List<PersonalinfoListBean>) event.getData();
                mSize = 0;
                mSize = personalinfoList.size();
                EventBus.getDefault().postSticky(new CommonEvent(Constans.JOB_P_SSUCESS, personalinfoList));
                if (mSize > 0) {
                    for (PersonalinfoListBean personalinfoListBean : personalinfoList) {
                        if (mSize > 1) {
                            showPersonal(personalinfoListBean, false);
                        } else {
                            showPersonal(personalinfoListBean, true);
                        }
                    }
                }
                break;
            case Constans.VEHICLE_SSUCESS://车辆:
                vehicleinfoList.clear();
                vehicleinfoList = (List<VehicleinfoListBean>) event.getData();
                mSize_v = 0;
                mSize_v = vehicleinfoList.size();
                EventBus.getDefault().postSticky(new CommonEvent(Constans.JOB_V_SSUCESS, vehicleinfoList));
                if (mSize_v > 0) {
                    for (VehicleinfoListBean vehicleinfoListBean : vehicleinfoList) {
                        if (mSize_v > 1) {
                            showVehicle(vehicleinfoListBean, false);
                        } else {
                            showVehicle(vehicleinfoListBean, true);
                        }
                    }
                }
                break;
            case Constans.PROBLEM_SSUCESS://问题:
                questionList.clear();
                questionList = (List<QuestionListBean>) event.getData();
                mSize_q = 0;
                mSize_q = questionList.size();
                EventBus.getDefault().postSticky(new CommonEvent(Constans.JOB_Q_SSUCESS, questionList));
                if (mSize_q > 0) {
                    for (QuestionListBean questionListBean : questionList) {
                        if (mSize_q > 1) {
                            showProblem(questionListBean, false);
                        } else {
                            showProblem(questionListBean, true);
                        }
                    }
                }
                break;
            case Constans.JOB_ERROR:
                break;
        }
        initListdatas("人员(" + mSize + ")", "车辆(" + mSize_v + ")", "问题(" + mSize_q + ")");
    }

    /*选中人员*/
    private void showPersonal(final PersonalinfoListBean personalinfoListBean, boolean single) {
//        try {
        if (single) {
            if (baiduMap != null) {
                baiduMap.clear();
                mks.clear();
            }
            isFirstLoc = true;
            showDialogPersonal(personalinfoListBean);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String latitude = personalinfoListBean.getLatitude();
                String longitude = personalinfoListBean.getLongitude();
                if (latitude.equals("0.0") && longitude.equals("0.0")) {
                    return;
                }
                if (!TextUtils.isEmpty(latitude) || !TextUtils.isEmpty(longitude)) {
                    LatLng latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
                    // 构建MarkerOption，用于在地图上添加Marker
                    String stateType = personalinfoListBean.getStateType();
                    if (!TimeUtils.isEmpty(stateType)) {
                        int resource = StringUtils.getPersonalBitmapResource(stateType);
                        worker = BitmapDescriptorFactory.fromResource(resource);
                    }
                    ooA = new MarkerOptions().icon(worker);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                    ooA.position(latLng);
                    mMarker = null;
//            mMarker = (Marker) (baiduMap.addOverlay(ooA));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("personalList", (Serializable) (personalinfoListBean));
                    ooA.extraInfo(bundle);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                    if (null == baiduMap) {
                        return;
                    }
                    mMarker = (Marker) (baiduMap.addOverlay(ooA));
                    try {
                        if (mMarker != null) {
                            mMarker.setTitle("worker");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mks.add(mMarker);
                    if (isFirstLoc) {
                        if ((Double.valueOf(longitude) < 140.0) || (Double.valueOf(longitude) > 65.0)
                                || (Double.valueOf(latitude) < 56.0) || (Double.valueOf(latitude) > 12.0)) {
                            isFirstLoc = false;
                            BdLocationUtil.MoveMapToCenter(baiduMap, BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude),
                                    Double.valueOf(longitude))), Constans.BAIDU_ZOOM_EIGHTEEN);
                        }
                    }
                }
            }
        }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /*选中车辆*/
    private void showVehicle(final VehicleinfoListBean vehicleinfoListBean, boolean single) {
//        try {
        if (single) {
            showVehicleDialog(vehicleinfoListBean);
            if (baiduMap != null) {
                baiduMap.clear();
                mks.clear();
            }
            isFirstLoc = true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String latitude = vehicleinfoListBean.getLatitude();
                String longitude = vehicleinfoListBean.getLongitude();
                LogUtils.d("latlng == " + latitude + "+++ " + longitude);
                if (latitude.equals("0.0") && longitude.equals("0.0")) {
                    return;
                }
                if (!TextUtils.isEmpty(latitude) || !TextUtils.isEmpty(longitude)) {
                    LatLng latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
                    String stateType = vehicleinfoListBean.getStateType();
                    if (!TimeUtils.isEmpty(stateType)) {
                        int resource = StringUtils.getVehicleBitmapResource(stateType);
                        vehicle = BitmapDescriptorFactory.fromResource(resource);
                    }
                    ooA = new MarkerOptions().icon(vehicle);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);

                    ooA.position(latLng);
                    mMarker = null;
//            mMarker = (Marker) (baiduMap.addOverlay(ooA));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vehicleList", (Serializable) (vehicleinfoListBean));
                    ooA.extraInfo(bundle);
                    if (null == baiduMap) {
                        return;
                    }
                    mMarker = (Marker) (baiduMap.addOverlay(ooA));
                    try {
                        if (mMarker != null) {
                            mMarker.setTitle("vehicle");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mks.add(mMarker);
                    if (isFirstLoc) {
                        if ((Double.valueOf(longitude) < 140.0) || (Double.valueOf(longitude) > 65.0)
                                || (Double.valueOf(latitude) < 56.0) || (Double.valueOf(latitude) > 12.0)) {
                            isFirstLoc = false;
                            BdLocationUtil.MoveMapToCenter(baiduMap, BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude),
                                    Double.valueOf(longitude))), Constans.BAIDU_ZOOM_EIGHTEEN);
                        }
                    }
                }
            }
        }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /*选中问题*/
    private void showProblem(final QuestionListBean questionListBean, boolean single) {
        try {
            if (single) {
                if (baiduMap != null) {
                    baiduMap.clear();
                    mks.clear();
                }
                isFirstLoc = true;
                showDialogProblem(questionListBean);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String latitude = questionListBean.getLatitude();
                    String longitude = questionListBean.getLongitude();

                    if (latitude.equals("0.0") && longitude.equals("0.0")) {
                        return;
                    }
                    if (!TextUtils.isEmpty(latitude) || !TextUtils.isEmpty(longitude)) {
                        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));

                        String stateType = questionListBean.getState();
                        if (!TimeUtils.isEmpty(stateType)) {
                            int resource = StringUtils.getProblemBitmapResource(stateType);
                            problem = BitmapDescriptorFactory.fromResource(resource);
                        }
                        ooA = new MarkerOptions().icon(problem);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                        ooA.position(latLng);
                        mMarker = null;
//            mMarker = (Marker) (baiduMap.addOverlay(ooA));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("questionList", (Serializable) (questionListBean));
                        ooA.extraInfo(bundle);
                        if (null == baiduMap) {
                            return;
                        }
                        mMarker = (Marker) (baiduMap.addOverlay(ooA));
                        try {
                            if (mMarker != null) {
                                mMarker.setTitle("problem");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mks.add(mMarker);
                        if (isFirstLoc) {
                            if ((Double.valueOf(longitude) < 140.0) || (Double.valueOf(longitude) > 65.0)
                                    || (Double.valueOf(latitude) < 56.0) || (Double.valueOf(latitude) > 12.0)) {
                                isFirstLoc = false;
                                isFirstLoc = false;
                                BdLocationUtil.MoveMapToCenter(baiduMap, new LatLng(Double.valueOf(latitude),
                                        Double.valueOf(longitude)), Constans.BAIDU_ZOOM_EIGHTEEN);
                            }
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*获取数量*/
    private void getQuestingCount() {
        try {
            String mIsreg = "";
            String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
            String userid = PreferencesUtils.getString(this, Constans.ID, "");
            boolean isreg = PreferencesUtils.getBoolean(this, Constans.ISREG);
            if (isreg) {
                mIsreg = "1";
            } else {
                mIsreg = "0";
            }
            /*String function, String userid, String pid, String isreg*/
            presenter.getmonitorinfo(ApiService.GETMONITORINFO, userid, pid, mIsreg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**/
    private void showCountData(GetJobMonitotingMsgBean msg) {
        try {
            String allcount = String.valueOf(msg.getPersonalnum());
            String waitcount = String.valueOf(msg.getVehiclenum());
            String doingcount = String.valueOf(msg.getQuestionnum());
            initListdatas(allcount, waitcount, doingcount);
            mNodeList.clear();
            mNodeList = FilterMonitoringUtils.showFilterDatas(msg);
            showPersonnel(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListdatas(String allcount, String waitcount, String doingcount) {
        try {
            stringList.clear();
            stringList.add(allcount);
            stringList.add(waitcount);
            stringList.add(doingcount);

            initTabViewCount(stringList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            if (viewPagerAdapter != null) {
                viewPagerAdapter.destroy();
                viewPagerAdapter = null;
            }
            tabItemInfos = null;
            tabLayout = null;
            if (mLocationUtils != null) {
                mLocationUtils.onDestory();
            }
            if (baiduMap != null) {
                baiduMap.clear();
            }
            if (mapview != null) {
                mapview.onDestroy();
            }
            vehicle.recycle();
            worker.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*地图*/
    private void showPersonnel(GetJobMonitotingMsgBean data) {
        isFirstLoc = true;
        try {
            baiduMap.clear();
            mks.clear();
            showLoadingDialog("请稍等...");
            List<PersonalinfoListBean> personalinfoList = data.getPersonalinfoList();
            List<VehicleinfoListBean> vehicleinfoList = data.getVehicleinfoList();
            List<QuestionListBean> questionList = data.getQuestionList();

            if (personalinfoList.size() != 0) {
                for (int i = 0; i < personalinfoList.size(); i++) {
                    if (i < 100) {
                        PersonalinfoListBean personalinfoListBean = personalinfoList.get(i);
                        showPersonal(personalinfoListBean, false);
                    }
                }
            }
            if (vehicleinfoList.size() != 0) {

                for (int i = 0; i < vehicleinfoList.size(); i++) {
                    if (i < 100) {
                        VehicleinfoListBean vehicleinfoListBean = vehicleinfoList.get(i);
                        showVehicle(vehicleinfoListBean, false);
                    }
                }
            }
            if (questionList.size() != 0) {
                for (int i = 0; i < questionList.size(); i++) {
                    QuestionListBean questionListBean = questionList.get(i);
                    showProblem(questionListBean, false);
                }
            }
            dismissLoadingDialog();
            if (myMarkerClickListener == null) {
                myMarkerClickListener = new myMarkerClickListener(mks);
            }
            baiduMap.setOnMarkerClickListener(myMarkerClickListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private myMarkerClickListener myMarkerClickListener = null;


    private class myMarkerClickListener implements BaiduMap.OnMarkerClickListener {
        private List<Marker> markerList;
        private PersonalinfoListBean personalList;
        private VehicleinfoListBean vehicleList;
        private QuestionListBean questionList;


        public myMarkerClickListener(List<Marker> markerList) {
            this.markerList = markerList;
        }

        @Override
        public boolean onMarkerClick(final Marker marker) {
            String title = marker.getTitle();
            if (TextUtils.isEmpty(title)) {
                return false;
            }
            if (title.equals("worker")) {
                personalList = (PersonalinfoListBean) marker.getExtraInfo().getSerializable("personalList");
                showDialogPersonal(personalList);
            } else if (title.equals("vehicle")) {
                vehicleList = (VehicleinfoListBean) marker.getExtraInfo().getSerializable("vehicleList");

                showVehicleDialog(vehicleList);
            } else if (title.equals("problem")) {
                questionList = (QuestionListBean) marker.getExtraInfo().getSerializable("questionList");

                showDialogProblem(questionList);
            }

            return true;
        }
    }

    /*底部问题弹框*/
    private void showDialogProblem(QuestionListBean questionList) {
        try {
            name = questionList.getId();
            state = questionList.getState();
            des = questionList.getProblem();
            uploadpeoplename = questionList.getUploadpeoplename();
            latitude = questionList.getLatitude();
            longitude = questionList.getLongitude();
            time = questionList.getUploadtime();
            type = questionList.getType();
            String address = questionList.getAddress();
            if (state.contains("1")) {
                state = getString(R.string.pending_txt);//待处理aa
                colorInt = JobMonitoringActivity.this.getResources().getColor(R.color.yellow);
            } else if (state.contains("2")) {
                state = getString(R.string.processing_txt);//处理中
                colorInt = JobMonitoringActivity.this.getResources().getColor(R.color.green);
            } else if (state.contains("3")) {
                state = getString(R.string.toclosed_txt);//待关闭
                colorInt = JobMonitoringActivity.this.getResources().getColor(R.color.huang_se);
            } else {
                state = getString(R.string.closed_txt);//已关闭
                colorInt = JobMonitoringActivity.this.getResources().getColor(R.color.gray_hand_txt);
            }

            ShowProblempop phototpop = new ShowProblempop(JobMonitoringActivity.this);
            phototpop.setTextContent(name, state, colorInt, des, uploadpeoplename, time, address);
            phototpop.showTakePop(ll_root);
            phototpop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewUtils.setLlInVisible(ll_tab_visible, false);
                }
            });
            ViewUtils.setLlInVisible(ll_tab_visible, ViewUtils.isPopuwindowShowing(phototpop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*弹底车辆部框*/
    private void showVehicleDialog(VehicleinfoListBean vehicleList) {
        try {
            String address = "";
            name = vehicleList.getRegistrationNO();
            position = vehicleList.getStateType();

            tel = "无";
            className = vehicleList.getPid();
            latitude = vehicleList.getLatitude();
            longitude = vehicleList.getLongitude();

            if (!TextUtils.isEmpty(latitude)) {
                latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
//            address = BdLocationUtil.returnAddress(geoCoder, latLng);
                if (mLocationUtils == null) {
                    // 创建定位管理信息对象
                    mLocationUtils = new LocationUtils(getApplicationContext());
//         开启定位
                    mLocationUtils.startLocation();
                    mLocationUtils.registerOnResult(this);
                }
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
            } else {
                address = "暂无地址!";
            }
//        if (TextUtils.isEmpty(address)) {
//            address = "检索当前地址失败!";
//        }
            endAddress = address;
            distance = DistanceUtil.getDistance(mlatLng, BdLocationUtil.ConverGpsToBaidu(latLng));
            distance = Math.abs(distance);
            terminalNO = vehicleList.getTerminalNO();
            final String registrationNO = vehicleList.getRegistrationNO();
            terminalNO = TextUtils.isEmpty(terminalNO) ? "" : terminalNO;
            phototpop = new ShowPersonalpop(JobMonitoringActivity.this);
            isVisible = true;
            phototpop.setTextContent(name, position, tel, className, "暂无地址!", isVisible);
            phototpop.setTakePhotoTouch(new ShowPersonalpop.TakePhotoTouch() {
                @Override
                public void onVideo() {//视频
                    toVideoActivity(terminalNO);
                }

                @Override
                public void takeTrack() {//轨迹
                    intoTrack(terminalNO, "vehicle", registrationNO);
                }

                @Override
                public void takeNativ() {//导航
                    initNavicar(distance);
                }

                @Override
                public void takePhone() {

                }
            });
            phototpop.showTakePop(ll_root);
            phototpop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewUtils.setLlInVisible(ll_tab_visible, false);
                }
            });
            ViewUtils.setLlInVisible(ll_tab_visible, ViewUtils.isPopuwindowShowing(phototpop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*视频监控*/
    private void toVideoActivity(String terminalNO) {
        Intent intent = new Intent();
        intent.setClass(JobMonitoringActivity.this, VideoCameraActivity.class);
        intent.putExtra("terminalNO", terminalNO);
        intent.putExtra("TYPE", "1");
        startActivity(intent);
    }

    /*弹底人员部框*/
    private void showDialogPersonal(final PersonalinfoListBean personalList) {
        try {
            String address = "";
            name = personalList.getName();
            position = personalList.getPosition();


            tel = personalList.getTel();
            className = personalList.getPid();
            latitude = personalList.getLatitude();
            longitude = personalList.getLongitude();
            if (!TextUtils.isEmpty(latitude)) {
                latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
//            address = BdLocationUtil.returnAddress(geoCoder, latLng);
                if (mLocationUtils == null) {
                    // 创建定位管理信息对象
                    mLocationUtils = new LocationUtils(getApplicationContext());
//         开启定位
                    mLocationUtils.startLocation();
                    mLocationUtils.registerOnResult(this);
                }
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
            } else {
                address = "暂无地址";
            }
//        if (TextUtils.isEmpty(address)) {
//            address = "检索当前地址失败!";
//        }
//        endAddress = address;
            terminalNO = personalList.getTerminalNO();
            terminalNO = TextUtils.isEmpty(terminalNO) ? "" : terminalNO;
            distance = DistanceUtil.getDistance(mlatLng, BdLocationUtil.ConverGpsToBaidu(latLng));
            isVisible = false;
            phototpop = new ShowPersonalpop(JobMonitoringActivity.this);
            phototpop.setTextContent(name, position, tel, className, "暂无地址!", isVisible);
            phototpop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewUtils.setLlInVisible(ll_tab_visible, false);
                }
            });
            phototpop.setTakePhotoTouch(new ShowPersonalpop.TakePhotoTouch() {
                @Override
                public void onVideo() {

                }

                @Override
                public void takeTrack() {//轨迹
                    intoTrack(terminalNO, "worker", name);
                }

                @Override
                public void takeNativ() {//导航
                    initNavicar(distance);
                }

                @Override
                public void takePhone() {//打电话
                    if (!TextUtils.isEmpty(tel)) {
                        Uri uri = Uri.parse("tel:" + tel);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                    }
                }
            });
            phototpop.showTakePop(ll_root);

            ViewUtils.setLlInVisible(ll_tab_visible, ViewUtils.isPopuwindowShowing(phototpop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 反地理编码执行回传
     * * @param map
     */
    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        String address = (String) map.get("address");
        LogUtils.d("getAddress111" + address);
        if (TextUtils.isEmpty(address)) {
            address = "暂无地址!";
        }
        endAddress = address;
        if (phototpop != null) {
            phototpop.setTextContent(name, position, tel, className, address, isVisible);
        }
    }

    /**
     * 地理编码执行回传
     * * @param map
     */
    @Override
    public void onGeoCodeResult(Map<String, Object> map) {

    }

    /*跳转轨迹*/
    private void intoTrack(String terminalNO, String type, String carNum) {
        if (latLng != null) {
            Intent intent = new Intent();
            intent.setClass(JobMonitoringActivity.this, HistoryTrackActivity.class);
            intent.putExtra("terminalNO", terminalNO);
//            intent.putExtra("lat", String.valueOf(latLng.latitude));
//            intent.putExtra("lng", String.valueOf(latLng.longitude));
            intent.putExtra("TYPE", type);
            intent.putExtra("carNum", carNum);
            startActivity(intent);
        } else {
            showSuccessToast("暂无轨迹");
        }
    }



    /*=====================begin 导航 =======================*/

    /*导航追车*/
    private void initNavicar(double distance) {

        /*初始化导航*/

        if (distance <= 500) {
            showSuccessToast("距离太短无法导航");
            return;
        }

//        BNRoutePlanNode sNode = new BNRoutePlanNode(mlatLng.longitude, mlatLng.latitude, beginAddress, beginAddress, coType);
//        BNRoutePlanNode eNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, endAddress, endAddress, coType);

        if ((mlatLng.longitude > 140.0) || (mlatLng.longitude < 65.0) || (mlatLng.latitude > 56.0) || (mlatLng.latitude < 12.0)) {
            showSuccessToast("定位出问题，请检查");
            return;
        }
        if ((latLng.longitude > 140.0) || (latLng.longitude < 65.0) || (latLng.latitude > 56.0) || (latLng.latitude < 12.0)) {
            showSuccessToast("定位出问题，请检查");
            return;
        }

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

        BNRoutePlanNode sNode = new BNRoutePlanNode(mlatLng.longitude, mlatLng.latitude, beginAddress, beginAddress, coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, endAddress, endAddress, coType);
        switch (coType) {
            case BNRoutePlanNode.CoordinateType.BD09LL: {
                sNode = new BNRoutePlanNode(mlatLng.longitude, mlatLng.latitude, beginAddress, beginAddress, coType);
                eNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, endAddress, endAddress, coType);
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
                                Intent intent = new Intent(JobMonitoringActivity.this, DemoGuideActivity.class);
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

