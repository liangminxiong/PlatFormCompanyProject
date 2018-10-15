package com.yuefeng.features.ui.activity.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.ResourcesUtils;
import com.common.utils.StatusBarUtil;
import com.common.utils.TimeUtils;
import com.common.view.popuwindow.TreesListsPopupWindow;
import com.common.view.timeview.TimePickerView;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.cartreeList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.cartreeList.common.OnTreeNodeClickListener;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.CarListContract;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.features.modle.WheelPathDatasBean;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.presenter.CarListPresenter;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.DatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/30.
 * 历史轨迹
 */

public class HistoryTrackActivity extends BaseActivity implements CarListContract.View {

    private String terminalNO;

    private TextureMapView mapview;
    private ImageView btn_Play;
    private ImageView btn_SlowPlay;
    private ImageView btn_FastPlay;
    private TextView tv_speed;
    private TextView tv_gpstime;
    private TextView tv_TrackIndex;
    private TextView tv_TrackCount;
    private LinearLayout llll;
    private RelativeLayout rl_contain;
    private LinearLayout lll;
    private LinearLayout lin_Play_Control;
    private SeekBar mSeekBar;
    private RelativeLayout rl_right;
    private RelativeLayout rl_search;
    private TextView tv_start;
    private TextView tv_end;
    private LinearLayout ll_content;
    private TextView tvTodayTime, tv_car_number;
    private TextView tvYesterdayTime;
    private TextView tvDaybymeTime, tv_title_setting;

    private String registNO;
    private String terminal;

    private boolean IsPlaying = false;
    private Thread playThread = null;
    private static int PlayIndex = 0;
    private static final int UPDATEUI = 0;
    private static final int ENDPOINT = 1;
    private Handler mHandler;

    private List<WheelPathDatasBean> mTrackDatas = new ArrayList<>();
    private Polyline mPolyline;
    private Marker mMarkerA;
    private TimePickerView timePickerView;

    private String starttime;
    private String endtime;

    private BaiduMap mBaiduMap;
    // 标记是显示Checkbox还是隐藏
    private boolean isHide = false;
    public boolean isFirstLoc = true;
    private BitmapDescriptor location_icon = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    private MarkerOptions oA;
    private Marker mMarker;
    private String startTime;
    private boolean startEndTime;
    private boolean endstartTime;

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private CarListPresenter presenter;
    private String type = "";
    private int imageInt;
    private LatLng latLngTemp = null;
    private double latitude;
    private double longitude;
    private String address;
    private LocationHelper mLocationHelper;
    private LatLng latLng;
    private double distance;
    private TreesListsPopupWindow popupWindow;
    private List<CarListInfosMsgBean> beanMsg = new ArrayList<>();
    private SimpleTreeRecyclerAdapter adapter;
    private List<Node> datas = new ArrayList<>();
    private String carNumber;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);

        tv_title_setting = findViewById(R.id.tv_title_setting);
        tv_car_number = findViewById(R.id.tv_car_number);
        tv_car_number.setVisibility(View.VISIBLE);
        mapview = findViewById(R.id.mMapView);
        btn_Play = findViewById(R.id.btn_Play);
        btn_FastPlay = findViewById(R.id.btn_FastPlay);
        btn_SlowPlay = findViewById(R.id.btn_SlowPlay);
        tv_speed = findViewById(R.id.tv_speed);
        tv_gpstime = findViewById(R.id.tv_gpstime);
        tv_TrackIndex = findViewById(R.id.tv_TrackIndex);
        tv_TrackCount = findViewById(R.id.tv_TrackCount);
        llll = findViewById(R.id.llll);
        rl_contain = findViewById(R.id.rl_contain);
        lll = findViewById(R.id.lll);
        lin_Play_Control = findViewById(R.id.lin_Play_Control);
        mSeekBar = findViewById(R.id.mSeekBar);
        rl_search = findViewById(R.id.rl_search);

        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        ll_content = findViewById(R.id.ll_content);

        tvTodayTime = findViewById(R.id.tv_today_time);
        tvYesterdayTime = findViewById(R.id.tv_yesterday_time);
        tvDaybymeTime = findViewById(R.id.tv_daybyme_time);

        presenter = new CarListPresenter(this, this);
        tv_title.setText("历史轨迹");
        getTeNum();
        mTrackDatas.clear();
        latLngTemp = null;
        tv_title_setting.setText("选车");
        getCarList();
        tv_car_number.setText("车牌号码:");
        terminal = "";
    }

    /*车辆列表*/
    private void getCarList() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
            presenter.getCarListInfos(ApiService.LOADVEHICLELIST, pid, userid, "0");
        }
    }

    private void getTeNum() {
        ll_content.setVisibility(View.VISIBLE);

        initTitle();
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        mHandler = new MyHandler();
    }

    private void initTitle() {
        starttime = TimeUtils.getStartTimeofDay();
        endtime = TimeUtils.getCurrentTime();
        tvTodayTime.setBackgroundResource(R.color.titel_color);
        tvTodayTime.setTextColor(getResources().getColor(R.color.white));
        tv_start.setText(starttime);
        tv_end.setText(endtime);
        tv_start.setSelected(false);
        tv_end.setSelected(false);
//        getTrackData(terminal, starttime, endtime);
    }


    private void getTrackData(String terminal, String startTime, String endTime) {

        boolean twoDayOffset2 = TimeUtils.getTwoDayOffset2(startTime, endTime);
        if (!twoDayOffset2) {
            showSuccessToast("结束时间不能小于开始时间");
            return;
        }
        boolean mouthDaySpan = TimeUtils.getMouthDaySpan(startTime, endTime);
        if (mouthDaySpan) {
            showSuccessToast("时间间隔超过一周");
            return;
        }
        if (mBaiduMap != null) {
            mBaiduMap.clear();
        }
        if (presenter != null) {
            presenter.getGpsDatasByTer(ApiService.getGpsDatasByTer, terminal, startTime, endTime);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCarListEvent(CarListEvent event) {
        switch (event.getWhat()) {
            case Constans.TRACK_SSUCESS://展示
                mTrackDatas = (List<WheelPathDatasBean>) event.getData();
                if (mTrackDatas.size() != 0) {
                    if (mBaiduMap != null) {
                        mBaiduMap.clear();
                    }
                    mSeekBar.setMax(mTrackDatas.size());
                    showCarDetailInfos(mTrackDatas);
                    ReplayTrack(0);
                }
                break;

            case Constans.CARLIST_SSUCESS:
                beanMsg = (List<CarListInfosMsgBean>) event.getData();
                if (beanMsg.size() > 0) {
                    showCarlistDatas(beanMsg);
                } else {
                    showSuccessToast("旗下无车辆");
                }
                break;
            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }

    }


    /*展示数据*/
    private void showCarlistDatas(List<CarListInfosMsgBean> organs) {
        datas.clear();
        datas = DatasUtils.ReturnTreesDatas(organs);
    }


    /*show轨迹  划线*/
    private void showCarDetailInfos(List<WheelPathDatasBean> msg) {

        try {
            if (mPolyline != null) {
                mPolyline.remove();
            }
            List<LatLng> lls = new ArrayList<LatLng>();
            lls.clear();

            for (int i = 0; i < msg.size(); i++) {
                double cLat = msg.get(i).getLa();
                double cLon = msg.get(i).getLo();
                String sp = msg.get(i).getSp();
//                if (sp.equals("0")) {
//                    continue;
//                }
                if ((cLon > 140.0) || (cLon < 65.0) || (cLat > 56.0) || (cLat < 12.0)) {
                    continue;
                }
                LatLng p1 = BdLocationUtil.ConverGpsToBaidu(new LatLng(cLat, cLon));// 转经纬度;
                lls.add(p1);
            }
            if (lls.size() > 1) {
                OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.BLUE).points(lls);
                mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                BdLocationUtil.MoveMapToCenter(mBaiduMap, lls.get(lls.size() - 1), 14);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        requestPermissions();
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
        try {
        mBaiduMap = mapview.getMap();
        // 地图初始化
        mapview.showZoomControls(false);// 缩放控件是否显示

        // 开启定位图层
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化

        mBaiduMap.showMapPoi(true);

        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    address = location.getAddrStr();
                    latLngTemp = new LatLng(latitude, longitude);
                    LogUtils.d("getLocation == " + latLngTemp + " ++ " + address);
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                                .overlook(-20).zoom(14).build();
                        oA = new MarkerOptions().icon(location_icon).zIndex(10);
                        oA.position(latLngTemp);
                        mMarker = null;
                        mMarker = (Marker) (mBaiduMap.addOverlay(oA));
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    }
                } else {
                    requestPermissions();
                }

            }
        }, Constans.BDLOCATION_TIME);
        initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*定时刷新*/
    private void initLocation() {
        mLocationHelper = new LocationHelper(this, 5);
        mLocationHelper.initLocation(new MyLocationListener() {

            @Override
            public void updateLastLocation(Location location) {
                latitude = location.getLongitude();
                longitude = location.getLatitude();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateLocation(Location location) {
                latitude = location.getLongitude();
                longitude = location.getLatitude();
                latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
                if (latLng != null && latLngTemp != null) {
                    distance = DistanceUtil.getDistance(latLngTemp, latLng);
                    if (distance < 500) {
                        latLngTemp = latLng;
                    }
                }
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

    protected double getLongitude() {
        return longitude;
    }

    protected double getLatitude() {
        return latitude;
    }

    @Override
    protected void setLisenter() {
        tv_title_setting.setOnClickListener(this);
        btn_FastPlay.setOnClickListener(this);

        btn_Play.setOnClickListener(this);
        btn_SlowPlay.setOnClickListener(this);
        tvTodayTime.setOnClickListener(this);
        tvYesterdayTime.setOnClickListener(this);
        tvDaybymeTime.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_FastPlay:
                fastPlay();
                break;
            case R.id.btn_Play:
                btn_Play();
                break;
            case R.id.btn_SlowPlay:
                slowPlay();
                break;
            case R.id.tv_today_time:
                tv_today_time();
                break;
            case R.id.tv_yesterday_time:
                tvYesterdayTime();
                break;
            case R.id.tv_daybyme_time:
                tvDaybymeTime();
                break;
            case R.id.rl_search:
                rl_search();
                break;
            case R.id.tv_start:
                tv_start();
                break;
            case R.id.tv_end:
                tv_end();
                break;
            case R.id.tv_title_setting:
                chooseCar();
                break;
        }
    }

    /*弹框选车*/
    private void chooseCar() {
        initPopupView();
    }

    /*车辆列表*/
    private void initPopupView() {
        popupWindow = new TreesListsPopupWindow(this);
        popupWindow.setTitleText("车辆列表");
        popupWindow.setSettingText(ResourcesUtils.getString(R.string.sure));

        if (datas.size() > 0) {
            popupWindow.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            if (adapter == null) {
                adapter = new SimpleTreeRecyclerAdapter(popupWindow.recyclerview, this,
                        datas, 1, R.drawable.tree_open, R.drawable.tree_close, true);
            } else {
                adapter.notifyDataSetChanged();
            }
            popupWindow.recyclerview.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                showSelectItemDatas();
            }

        });

        popupWindow.setOnItemClickListener(new TreesListsPopupWindow.OnItemClickListener() {
            @Override
            public void onGoBack() {
                popupWindow.dismiss();
            }

            @Override
            public void onSure() {
                showSelectItemDatas();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(llll, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }

    @SuppressLint("SetTextI18n")
    private void showSelectItemDatas() {
        if (adapter == null) {
            return;
        }
        final List<Node> allNodes = adapter.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).isChecked()) {
                carNumber = allNodes.get(i).getName();
                terminal = allNodes.get(i).getTerminalNO();
            }
        }
        if (!TextUtils.isEmpty(terminal)) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            tv_car_number.setText("车牌号码:" + carNumber);
            String startTime = tv_start.getText().toString().trim();
            String endTime = tv_end.getText().toString().trim();
            getTrackData(terminal, startTime, endTime);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_UP:
//                if (isTouchMap()) {
//
//                }
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }


    private void tv_today_time() {
        ll_content.setVisibility(View.VISIBLE);
        String toTodayTime = TimeUtils.getStartTimeofDay();
        String currentTime = TimeUtils.getCurrentTime();
        tv_start.setText(toTodayTime);
        tv_end.setText(currentTime);
        tvTodayTime.setBackgroundResource(R.color.titel_color);
        tvTodayTime.setTextColor(getResources().getColor(R.color.white));
        tvYesterdayTime.setBackgroundResource(R.color.white);
        tvYesterdayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tvDaybymeTime.setBackgroundResource(R.color.white);
        tvDaybymeTime.setTextColor(getResources().getColor(R.color.titel_color));
//        if (mHandler != null) {
//            mHandler.sendEmptyMessage(ENDPOINT);
//        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
        if (!TextUtils.isEmpty(terminal)) {
            getTrackData(terminal, toTodayTime, currentTime);
        } else {
            showSuccessToast("请先选车");
        }
        tv_start.setSelected(false);
        tv_end.setSelected(false);
    }

    @SuppressLint("SetTextI18n")
    private void tvYesterdayTime() {
        ll_content.setVisibility(View.VISIBLE);
        String toTodayTime = TimeUtils.getStartTimeofDay();
        String yesterDayTime = TimeUtils.yesterDay(toTodayTime);
        tvYesterdayTime.setBackgroundResource(R.color.titel_color);
        tvYesterdayTime.setTextColor(getResources().getColor(R.color.white));
        tvTodayTime.setBackgroundResource(R.color.white);
        tvTodayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tvDaybymeTime.setBackgroundResource(R.color.white);
        tvDaybymeTime.setTextColor(getResources().getColor(R.color.titel_color));
        tv_start.setText(yesterDayTime + " 00:00:00" + "");
        tv_end.setText(yesterDayTime + " 23:59:59" + "");
        tv_start.setSelected(false);
        tv_end.setSelected(false);
//        if (mHandler != null) {
//            mHandler.sendEmptyMessage(ENDPOINT);
//        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
        if (!TextUtils.isEmpty(terminal)) {
            getTrackData(terminal, yesterDayTime + " 00:00:00" + "", toTodayTime);
        } else {
            showSuccessToast("请先选车");
        }
    }

    private void tvDaybymeTime() {
        String toTodayTime = TimeUtils.getStartTimeofDay();
        String currentTime = TimeUtils.getCurrentTime();
        tv_start.setText(toTodayTime);
        tv_end.setText(currentTime);
        ll_content.setVisibility(View.VISIBLE);
        tvDaybymeTime.setBackgroundResource(R.color.titel_color);
        tvDaybymeTime.setTextColor(getResources().getColor(R.color.white));
        tvYesterdayTime.setBackgroundResource(R.color.white);
        tvYesterdayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tvTodayTime.setBackgroundResource(R.color.white);
        tvTodayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tv_start.setSelected(true);
        tv_end.setSelected(true);
//        if (mHandler != null) {
//            mHandler.sendEmptyMessage(ENDPOINT);
//        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
    }

    /*查询*/
    private void rl_search() {
        if (!TextUtils.isEmpty(terminal)) {
            getTrackData(terminal, tv_start.getText().toString().trim(), tv_end.getText().toString().trim());
        } else {
            showSuccessToast("请先选车");
        }
    }


    /**
     * 划线
     *
     * @param mTrackDatas
     */

    @SuppressLint("SetTextI18n")
    private void initTrackLine(List<WheelPathDatasBean> mTrackDatas) {
        try {
            if (mPolyline != null) {
                mPolyline.remove();
            }
            List<LatLng> lls = new ArrayList<LatLng>();
            lls.clear();
            int count = 0;

            for (int i = 0; i < mTrackDatas.size(); i++) {
                double cLat = mTrackDatas.get(i).getLa();
                double cLon = mTrackDatas.get(i).getLo();
                String sp = mTrackDatas.get(i).getSp();
//                if (sp.equals("0")) {
//                    continue;
//                }
                if ((cLon > 140.0) || (cLon < 65.0) || (cLat > 56.0) || (cLat < 12.0)) {
                    continue;
                }
                count++;
                LatLng p1 = BdLocationUtil.ConverGpsToBaidu((new LatLng(cLat, cLon)));// 转经纬度;
                if (latLngTemp == null) {
                    latLngTemp = p1;
                } else {
                    if (latLngTemp == p1) {
                        continue;
                    } else {
                        latLngTemp = p1;
                    }
                }

                lls.add(p1);
            }
            tv_TrackCount.setText(count + "" + "");
            if (lls.size() > 1) {
                OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.BLUE).points(lls);
                mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                BdLocationUtil.MoveMapToCenter(mBaiduMap, lls.get(0), 14);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画车
     *
     * @param index
     */
    @SuppressLint("SetTextI18n")
    private void ReplayTrack(int index) {
        try {
            WheelPathDatasBean trackData = mTrackDatas.get(index);
            double Latitude = trackData.getLa();
            double Longitude = trackData.getLo();
            LatLng p1 = BdLocationUtil.ConverGpsToBaidu(new LatLng(Latitude, Longitude));// 转经纬度;
            double ang = mTrackDatas.get(index).getAng();

            if (mMarkerA != null) {
                mMarkerA.remove();
            }

            if (mMarkerA != null) {
                mMarkerA.remove();
            }

            BitmapDescriptor map_location = BitmapDescriptorFactory.fromResource(imageInt);


            MarkerOptions ooA;

            if (index == 0) {
                ooA = new MarkerOptions().position(p1).zIndex(9).draggable(true).icon(map_location);
                BdLocationUtil.MoveMapToCenter(mBaiduMap, p1, 14);
            } else {
                ooA = new MarkerOptions().position(p1).zIndex(9).draggable(true).icon(map_location);
                BdLocationUtil.MoveMapToCenter(mBaiduMap, p1, 19);
            }
            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
            mMarkerA.setTitle(trackData.getTn());
            mSeekBar.setProgress(index);
            tv_TrackIndex.setText(BdLocationUtil.GetPlayIndexString(index));
            tv_speed.setText("速度：" + trackData.getSp() + "km/h");
            tv_gpstime.setText("定位时间：" + trackData.getSt() + "");

        } catch (Exception e) {
            Log.v("JJ", "ReplayTrack:" + e.toString());
        }
    }

    private void tv_start() {

        if (timePickerView == null) {
            timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        // 时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                startTime = TimeUtils.getTimeHourMin(date);
                if (!TextUtils.isEmpty(tv_end.getText().toString().trim())) {
                    endstartTime = TimeUtils.getBoolenStartEndTime(startTime, endtime);
                    if (endstartTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tv_start.setText(startTime);
            }
        });
        timePickerView.show();
    }

    private void tv_end() {
        if (timePickerView == null) {
            timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        // 时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                endtime = TimeUtils.getTimeHourMin(date);
                Log.d("tag", "onTimeSelect  aa: " + endtime);
                if (!TextUtils.isEmpty(tv_start.getText().toString().trim())) {
                    startEndTime = TimeUtils.getBoolenStartEndTime(startTime, endtime);
                    if (startEndTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tv_end.setText(endtime);
            }
        });
        timePickerView.show();
    }

    private void btn_Play() {
        if (!IsPlaying) {
            if (mTrackDatas.size() < 1) {
                showSuccessToast("轨迹数据为空，请先加载！");
                return;
            }
            if (playThread == null)
                playThread = new PlayThread();
            playThread.start();
            btn_Play.setImageResource(R.drawable.btn_stop_a);
        } else {
            IsPlaying = false;
            try {
                if (playThread != null && playThread.isAlive())
                    playThread.interrupt();
                playThread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            btn_Play.setImageResource(R.drawable.btn_stop_b);
        }
    }


    private void fastPlay() {
        if (PlayIndex + 1 < mTrackDatas.size())
            PlayIndex += 1;
    }

    private void slowPlay() {
        if (PlayIndex - 1 > 0)
            PlayIndex -= 1;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEUI:
                    int index = (Integer) msg.obj;
                    ReplayTrack(index);
                    break;
                case ENDPOINT:
                    freeThread();
                    btn_Play.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.btn_play_selector));
                    IsPlaying = false;
                    PlayIndex = 0;
                    tv_TrackIndex.setText(PlayIndex + "");
//                    showSuccessToast("轨迹播放完毕！");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    class PlayThread extends Thread {
        @Override
        public void run() {
            IsPlaying = true;
            for (; PlayIndex < mTrackDatas.size(); PlayIndex++) {
                if (!IsPlaying) {
                    break;
                }
                Message msg = new Message();
                msg.what = UPDATEUI;
                msg.obj = PlayIndex;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
    }

    private void freeThread() {
        try {
            if (playThread != null && playThread.isAlive())
                playThread.interrupt();
            playThread = null;
            IsPlaying = false;
            btn_Play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play_selector));
        } catch (Exception e) {

        }
    }

    class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress == mTrackDatas.size() - 1) {
                mHandler.sendEmptyMessage(ENDPOINT);
                mSeekBar.setProgress(0);
            } else {
                PlayIndex = progress;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar arg0) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar arg0) {

        }
    }

    @Override
    public void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 关闭定位图层
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            mBaiduMap.setMyLocationEnabled(false);
            mapview.onDestroy();
            mapview = null;
        }
        PlayIndex = 0;
    }
}
