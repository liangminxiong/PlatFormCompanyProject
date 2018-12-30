package com.yuefeng.features.ui.activity.sngnin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.ResourcesUtils;
import com.common.utils.RxHelper;
import com.common.utils.StringUtils;
import com.common.utils.TimeUtils;
import com.common.view.popuwindow.PersonalZhuListPopupWindow;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.ExecutiveAttendanceContract;
import com.yuefeng.features.modle.zhuguanSign.GetSignJsonMsgBean;
import com.yuefeng.features.modle.zhuguanSign.ZhuGuanSignListBean;
import com.yuefeng.features.presenter.zhuguansign.ExecutiveAttendancePresenter;
import com.yuefeng.personaltree.model.PersonalParentBean;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.ui.view.BaiDuMapMakerView;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.PersonalDatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/*主管考勤*/
public class ExecutiveAttendanceActivity extends BaseActivity implements ExecutiveAttendanceContract.View, BaiduMap.OnMapStatusChangeListener {

    @BindView(R.id.tv_title_setting)
    TextView mTvSetting;
    @BindView(R.id.baidumap)
    TextureMapView mMapView;

    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    private boolean isFirstLocation = true;
    private LatLng mLatLng;
    BitmapDescriptor map_location = BitmapDescriptorFactory.fromResource(R.drawable.zhuguan_yidong);
    BitmapDescriptor map_location_no = BitmapDescriptorFactory.fromResource(R.drawable.zhuguan_lixian);
    public MarkerOptions ooA;
    private Marker mMarker;
    private BaiduMap mBaiduMap;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFirstMoveCenter = true;

    private myMarkerClickListener myMarkerClickListener = null;
    private List<Marker> mks = new ArrayList<Marker>();
    private BaiDuMapMakerView mBaiMakerView;
    private PersonalZhuListPopupWindow mPopupWindowTree;

    /*列表数据源*/
    private List<Node> nodeList = new ArrayList<>();
    private ExecutiveAttendancePresenter mPresenter;
    private String mPid;
    private List<PersonalParentBean> treeListData = new ArrayList<>();
    private String mIdFlags;
    private boolean isFirstGetData = true;
    private TextOptions mOoText;
    private BitmapDescriptor mDescriptor;
    private UiSettings mSettings;

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
        mPresenter = new ExecutiveAttendancePresenter(this, this);
        setTitle("主管考勤");
        mTvSetting.setBackgroundResource(R.drawable.list);
        initBaiduMap();
        getNetDatas();
    }

    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);// 缩放控件是否显示
        mSettings = mBaiduMap.getUiSettings();
        mSettings.setCompassEnabled(true);
        mSettings.setOverlookingGesturesEnabled(false);
        mSettings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
        mSettings.setAllGesturesEnabled(true);
        mBaiduMap.setMaxAndMinZoomLevel(18, 3);
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 地图初始化

        requestPermissions();
        mBaiMakerView = new BaiDuMapMakerView(ExecutiveAttendanceActivity.this);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (null != mBaiMakerView) {
                    mBaiMakerView.hideMarkerView();
                }
                //获取地图缩放级别
                float zoom = mBaiduMap.getMapStatus().zoom;
                LogUtils.d("=====zoom" + zoom);
                if (zoom >= 18.0f && mSettings != null) {
                    mSettings.setCompassEnabled(true);
                    mSettings.setOverlookingGesturesEnabled(false);
                    mSettings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
                    mSettings.setAllGesturesEnabled(true);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

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
                    mLatLng = new LatLng(latitude, longitude);
                    if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                        int length = address.length();
                        address = address.substring(2, length);
                        if (latitude > 0 && longitude > 0) {
                            MyApplication.latitude = latitude;
                            MyApplication.longitude = longitude;
                            MyApplication.address = address;
                        }
                        if (isFirstLocation) {
//                            MapStatus ms = new MapStatus.Builder().target(mLatLng)
//                                    .overlook(-20).zoom(17).build();
//                            BdLocationUtil.MoveMapToCenter(mBaiduMap, mLatLng, Constans.BAIDU_ZOOM_TWENTY_ONE);
//                            ooA = new MarkerOptions().icon(map_location).zIndex(13);
//                            ooA.position(mLatLng);
//                            mMarker = null;
//                            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
//                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                            isFirstLocation = false;
                            BdLocationUtil.MoveMapToCenter(mBaiduMap, mLatLng, Constans.BAIDU_ZOOM_FOUTTEEN);
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

    private void getNetDatas() {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            showSuccessToast("请检查网络配置");
            return;
        }
        if (mPresenter != null) {
            mPid = PreferencesUtils.getString(ExecutiveAttendanceActivity.this, Constans.ORGID, "");
//            mPid = "ea9b4033ffffee0101ed1860a1febcfb";//张三pid
            mPresenter.getSignTree(ApiService.GETSIGNTREE, mPid);
        }
    }

    /*获取主管id列表*/
    private void getNetIdDatas() {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            showSuccessToast("请检查网络配置");
            return;
        }
        if (mPresenter != null) {
            mPid = PreferencesUtils.getString(ExecutiveAttendanceActivity.this, Constans.ORGID, "");
            mPresenter.getSignJson(ApiService.GETSIGNJSON, mPid);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick({R.id.tv_title_setting})
    public void onClickView(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_title_setting:
                initTreeListPopupView();
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.ATTENDANCETRESS_SUCCESS://树
                treeListData.clear();
                treeListData = (List<PersonalParentBean>) event.getData();
                if (treeListData.size() > 0) {
                    showPersonallistDatas(treeListData);
                } else {
                    showSuccessToast("旗下无主管");
                }
                break;

            case Constans.ATTENDANCETRESS_ERROR:
                showSureGetAgainDataDialog("加载数据失败,是否重新加载?");
                break;
            case Constans.ATTENDANCELNGLAT_SUCCESS://主管实时位置
//                if (mBaiduMap != null) {
//                    mBaiduMap.clear();
//                }
                List<ZhuGuanSignListBean> latListData = (List<ZhuGuanSignListBean>) event.getData();
                if (latListData.size() > 0) {
                    showPersonnel(latListData);
                }
                break;
            case Constans.ATTENDANCELNGLAT_ERROR:
//                addNativeDatas();
                break;
            case Constans.ATTENDANCELIST_SUCCESS:
                List<GetSignJsonMsgBean> listData = (List<GetSignJsonMsgBean>) event.getData();
                if (listData.size() > 0) {
                    showPersonnelListId(listData);
                }
                break;
            case Constans.ATTENDANCELIST_ERROR:
//                addNativeDatas();
                break;

        }
    }

    /*id*/
    private void showPersonnelListId(List<GetSignJsonMsgBean> listData) {
        mIdFlags = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.setLength(0);
        for (GetSignJsonMsgBean listDatum : listData) {
            if (TextUtils.isEmpty(mIdFlags)) {
                stringBuffer.append(listDatum.getId());
            } else {
                stringBuffer.append(",").append(listDatum.getId());
            }
        }

        getLatLng(mIdFlags, true);
    }

    @Override
    public void getOtherDatas() {
        super.getOtherDatas();
        getNetIdDatas();
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
        getNetDatas();
    }

    /*展示数据*/
    private void showPersonallistDatas(List<PersonalParentBean> organs) {
        try {
            nodeList.clear();
            nodeList = PersonalDatasUtils.ReturnPersonalTreesDatas(organs);
            if (nodeList.size() > 0) {
                getIdFlags(nodeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIdFlags(List<Node> nodeList) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.setLength(0);
        for (Node node : nodeList) {
            String personalName = (String) node.getName();
            String userId = (String) (String) node.getId();
            String terminal = (String) node.getTerminalNO();
            if (!TextUtils.isEmpty(personalName) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(terminal)) {
                if (stringBuffer.length() <= 0) {
                    stringBuffer.append(userId);
                } else {
                    stringBuffer.append(",").append(userId);
                }
            }
        }
        mIdFlags = stringBuffer.toString().trim();

        initCountDown();
    }

    private void initCountDown() {
        try {
            Observable.interval(0, 120, TimeUnit.SECONDS)
                    .compose(RxHelper.<Long>rxSchedulerHelper())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Long value) {
                            isFirstGetData = false;
                            getLatLng(mIdFlags, isFirstGetData);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*人员列表*/
    private void initTreeListPopupView() {
        try {
            mPopupWindowTree = new PersonalZhuListPopupWindow(this, nodeList, true);
            mPopupWindowTree.setTitleText("选择人员");
            mPopupWindowTree.setSettingText("确定");
            mPopupWindowTree.setOnItemClickListener(new PersonalZhuListPopupWindow.OnItemClickListener() {
                @Override
                public void onGoBack(String listName, String userId, String terminal) {
                    if (!TextUtils.isEmpty(userId)) {
                        mIdFlags = userId;
                        getLatLng(userId, true);
                    }
                }


                @Override
                public void onSure(String listName, String userId, String terminal) {
                    if (!TextUtils.isEmpty(userId)) {
                        mIdFlags = userId;
                        getLatLng(userId, true);
                    }
                }

                @Override
                public void onSelectCar(String carNumber, String userId, String terminal) {
                    if (!TextUtils.isEmpty(userId)) {
                        mIdFlags = userId;
                    }
                }
            });

            mPopupWindowTree.showAtLocation(llParent, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*获取实时数据*/
    private void getLatLng(String userId, boolean isFirstGetData) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            showSuccessToast("请检查网络配置");
            return;
        }
        if (null != mPresenter) {
            mPresenter.getPersonalMonitor(ApiService.GETPERSONALMONITOR, userId, isFirstGetData);
        }
    }


    /*地图Maker*/
    private void showPersonnel(List<ZhuGuanSignListBean> latListData) {
        try {
            mBaiduMap.clear();
            mks.clear();
            for (int i = 0; i < latListData.size(); i++) {
                showPersonal(latListData.get(i));
                Thread.sleep(200);
            }
            if (null == myMarkerClickListener) {
                myMarkerClickListener = new myMarkerClickListener(mks);
            }
            if (mBaiduMap != null) {
                mBaiduMap.setOnMarkerClickListener(myMarkerClickListener);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*showMarker*/
    private void showPersonal(final ZhuGuanSignListBean personalinfoListBean) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);

                        String latitude = personalinfoListBean.getLat();
                        String longitude = personalinfoListBean.getLng();
                        LogUtils.d("=====lat 00======" + latitude + " ++ " + longitude);
                        if (!TextUtils.isEmpty(latitude) || !TextUtils.isEmpty(longitude)) {
                            LatLng latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));

                            // 构建MarkerOption，用于在地图上添加Marker
                            String time = StringUtils.returnStrTime(personalinfoListBean.getTime());
                            String name = StringUtils.isEntryStrWu(personalinfoListBean.getName());
                            String minTime = TimeUtils.getTenBeforeMinTime();
                            boolean timeLessThan = TimeUtils.isTimeLessThan(minTime, time);
                            mDescriptor = getBitmapDescriptor(name, timeLessThan);

                            ooA = new MarkerOptions().icon(mDescriptor).position(latLng);
//                            if (timeLessThan) {
//                            ooA = new MarkerOptions().icon(map_location).position(latLng);
//                            } else {
//                                ooA = new MarkerOptions().icon(map_location_no).position(latLng);
//                            }
//
                            mMarker = null;
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Personal", (Serializable) (personalinfoListBean));
                            ooA.extraInfo(bundle);
                            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
                            try {
                                if (mMarker != null) {
                                    mMarker.setTitle("worker");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mks.add(mMarker);


//                            //添加文字
//                            if (mOoText == null) {
//                                mOoText = new TextOptions().bgColor(Color.YELLOW)
//                                        .fontSize(24)
//                                        .fontColor(getResources().getColor(R.color.red));
//                            }
//
//                            mOoText.position(latLng);
//                            mOoText.text(name);
//                            mBaiduMap.addOverlay(mOoText);
                            if (isFirstMoveCenter) {
                                if ((Double.valueOf(longitude) < 140.0) || (Double.valueOf(longitude) > 65.0)
                                        || (Double.valueOf(latitude) < 56.0) || (Double.valueOf(latitude) > 12.0)) {
                                    isFirstMoveCenter = false;
                                    BdLocationUtil.MoveMapToCenter(mBaiduMap, latLng, Constans.BAIDU_ZOOM_TWENTY_ONE);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BitmapDescriptor getBitmapDescriptor(String name, boolean isLiXing) {
        BitmapDescriptor bttmap = null;
        View item_view = ResourcesUtils.inflate(R.layout.frag_near_marker);
        TextView tv_storeName = (TextView) item_view.findViewById(R.id.tv_marker_name);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.iv_marker_type);

// 设置布局中文字
        tv_storeName.setText(name);

// 设置图标
        if (isLiXing) {
            imageView.setImageResource(R.drawable.zhuguan_yidong);
        } else {
            imageView.setImageResource(R.drawable.zhuguan_lixian);
        }
        bttmap = BitmapDescriptorFactory.fromView(item_view);
        return bttmap;

    }

    /*地图缩放级别*/
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
///获取地图缩放级别
        float zoom = mBaiduMap.getMapStatus().zoom;
        //根据获取到的地图中心点(图标地点)坐标获取地址
        LogUtils.d("======zoom00=" + zoom);
        if (zoom >= 15.0f) {

        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
///获取地图缩放级别
        float zoom = mBaiduMap.getMapStatus().zoom;
        //根据获取到的地图中心点(图标地点)坐标获取地址
        LogUtils.d("======zoom11=" + zoom);
//        if (zoom >= 18.0f && mSettings != null) {
//        }
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
//获取地图缩放级别
        float zoom = mBaiduMap.getMapStatus().zoom;
        //根据获取到的地图中心点(图标地点)坐标获取地址
        LogUtils.d("======zoom22=" + zoom);

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
//获取地图缩放级别
        float zoom = mBaiduMap.getMapStatus().zoom;
        //根据获取到的地图中心点(图标地点)坐标获取地址
        if (zoom >= 20.0f && mSettings != null) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(Constans.BAIDU_ZOOM_NIGHTH_F).build()));
        }
    }

    private class myMarkerClickListener implements BaiduMap.OnMarkerClickListener {
        private List<Marker> markerList;


        public myMarkerClickListener(List<Marker> markerList) {
            this.markerList = markerList;
        }

        @Override
        public boolean onMarkerClick(final Marker marker) {
            String title = marker.getTitle();
            if (TextUtils.isEmpty(title)) {
                return false;
            }

            ZhuGuanSignListBean personalList = (ZhuGuanSignListBean) marker.getExtraInfo().getSerializable("Personal");

            if (null == mBaiMakerView) {
                mBaiMakerView = new BaiDuMapMakerView(ExecutiveAttendanceActivity.this);
            }
            if (null != mBaiduMap) {
                mBaiMakerView.showViewData(mBaiduMap, personalList);
            }
            return true;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        map_location.recycle();
        map_location_no.recycle();
        EventBus.getDefault().unregister(this);
    }
}
