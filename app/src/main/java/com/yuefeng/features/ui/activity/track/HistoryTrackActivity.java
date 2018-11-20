package com.yuefeng.features.ui.activity.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.common.utils.TimeUtils;
import com.common.view.popuwindow.TreesListsPopupWindow;
import com.common.view.timeview.TimePickerView;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.cartreeList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.CarListContract;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.features.modle.WheelPathDatasBean;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.presenter.CarListPresenter;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.CarStateIconUtils;
import com.yuefeng.utils.DatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/30.
 * 历史轨迹
 */

public class HistoryTrackActivity extends BaseActivity implements CarListContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.llll)
    LinearLayout llll;
    @BindView(R.id.tv_title_setting)
    TextView tvTitleSetting;

    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.mSeekBar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.btn_SlowPlay)
    ImageView btnSlowPlay;
    @BindView(R.id.btn_Play)
    ImageView btnPlay;
    @BindView(R.id.btn_FastPlay)
    ImageView btnFastPlay;
    @BindView(R.id.iv_showtime)
    ImageView ivShowtime;

    @BindDrawable(R.drawable.list)
    Drawable list_tree;

    private String terminal = "";

    private boolean IsPlaying = false;
    private Thread playThread = null;
    private static int PlayIndex = 0;
    private static final int UPDATEUI = 0;
    private static final int ENDPOINT = 1;
    private Handler mHandler;

    private List<WheelPathDatasBean> mTrackDatas = new ArrayList<>();
    private Polyline mPolyline;
    private TimePickerView timePickerView;

    private String starttime;
    private String endtime;

    private BaiduMap mBaiduMap;
    // 标记是显示Checkbox还是隐藏
    private boolean isHide = false;
    public boolean isFirstLoc = true;
    private Marker mMarker;
    private String startTime;
    private boolean startEndTime;
    private boolean endstartTime;

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
    private MarkerOptions ooA;
    private String trackDataTn;
    /*选中的车集合*/
    private List<String> selectList = new ArrayList<>();


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_historytrack;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new CarListPresenter(this, this);
        initTitle();
        initUI();

        getTeNum();
        mTrackDatas.clear();
        latLngTemp = null;

    }

    private void initUI() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        terminal = (String) bundle.get("terminalNO");
        type = (String) bundle.get("TYPE");
        String carNum = (String) bundle.get("carNum");
        assert type != null;
//        assert terminal != null;
        LogUtils.d(terminal + "+++ terminal111 ++ " + type);
        if (type.equals("worker")) {
            if (!TextUtils.isEmpty(carNum)) {
                tvTitle.setText(carNum);
            }
            imageInt = R.drawable.worker;
            getTrackData(terminal, tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim());
        } else if (type.equals("vehicle")) {
            if (!TextUtils.isEmpty(carNum)) {
                tvTitle.setText(carNum);
            }
            imageInt = R.drawable.guiji_ljc_01;
            getTrackData(terminal, tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim());
        } else {
            getCarList();
            imageInt = R.drawable.worker;
            tvTitle.setText("历史轨迹");
            tvTitleSetting.setBackground(list_tree);
        }
        requestPermissions(imageInt);
    }

    /*车辆列表*/
    private void getCarList() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
            presenter.getCarListInfos(ApiService.GETVEHICLETREE, pid, userid, "0");
        }
    }

    private void getTeNum() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        mHandler = new MyHandler();
    }

    private void initTitle() {
        starttime = TimeUtils.getThreeHoursTime();
        endtime = TimeUtils.getCurrentTime();
        tvStartTime.setText(starttime);
        tvEndTime.setText(endtime);
        tvStartTime.setSelected(false);
        tvEndTime.setSelected(false);
    }

    /*获取轨迹*/
    private void getTrackData(String terminal, String startTime, String endTime) {

//        boolean twoDayOffset2 = TimeUtils.isTimeLessThan(startTime, endTime);
//        if (!twoDayOffset2) {
//            showSuccessToast("结束时间不能小于开始时间");
//            return;
//        }
        boolean mouthDaySpan = TimeUtils.getMouthDaySpan(startTime, endTime);
        if (mouthDaySpan) {
            showSuccessToast("时间间隔超过一周");
            return;
        }
        rlTime.setVisibility(View.INVISIBLE);
        ivShowtime.setVisibility(View.VISIBLE);
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
                    filterTrackDatas(mTrackDatas);
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
            case Constans.TRACK_ERROR:
                showSuccessToast("访问失败!");
                break;
            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }

    }

    private void filterTrackDatas(List<WheelPathDatasBean> trackDatas) {
        double lat = 0.0;
        double lng = 0.0;
        if (mSeekBar != null) {
            mSeekBar.setMax(trackDatas.size());
        }
        List<WheelPathDatasBean> list = new ArrayList<>();
        for (WheelPathDatasBean mTrackData : trackDatas) {
            if ((lng != mTrackData.getLo()) && (lat != mTrackData.getLa())) {
                list.add(mTrackData);
            }
            lat = mTrackData.getLa();
            lng = mTrackData.getLo();
        }
        mTrackDatas.clear();
        mTrackDatas.addAll(list);
        showCarDetailInfos(mTrackDatas);
        ReplayTrack(0);
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
                BdLocationUtil.MoveMapToCenter(mBaiduMap, lls.get(lls.size() - 1), Constans.BAIDU_ZOOM_EIGHTEEN);
            }
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
     * @param imageInt
     */
    @SuppressLint("CheckResult")
    private void requestPermissions(final int imageInt) {
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
                        getLocation(imageInt);
                    }
                });
    }

    /*定位*/
    private void getLocation(final int imageInt) {
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
                        requestPermissions(imageInt);
                        return;
                    }
//                    if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    address = location.getAddrStr();
                    if (!TextUtils.isEmpty(address)) {
                        int length = address.length();
                        address = address.substring(2, length);
                    }
                    latLngTemp = new LatLng(latitude, longitude);
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                                .overlook(-20).zoom(14).build();
                        ooA = new MarkerOptions();
                        ooA.icon(BitmapDescriptorFactory.fromResource(imageInt));
                        ooA.zIndex(10);
                        ooA.position(latLngTemp);
//                        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                        mMarker = null;
                        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    }
//                    } else {
//                        requestPermissions();
//                    }

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
    }

    @Override
    protected void widgetClick(View v) {
    }

    /*弹框选车*/
    private void chooseCar() {
        initPopupView();
    }

    /*车辆列表*/
    private void initPopupView() {
        try {
            selectList.clear();
            popupWindow = new TreesListsPopupWindow(this, datas, true,true);
            popupWindow.setTitleText("车辆列表");
            popupWindow.setSettingText(ResourcesUtils.getString(R.string.sure));

            popupWindow.setOnItemClickListener(new TreesListsPopupWindow.OnItemClickListener() {
                @Override
                public void onGoBack(String name, String terminala, String id,boolean isGetDatas) {
                    carNumber =name;
                    terminal = terminala;
                    if (isGetDatas) {
                        getSelectCarInfos(name, terminal);
                    }
                }

                @Override
                public void onSure(String name, String terminala, String id,boolean isGetDatas) {
                    carNumber =name;
                    terminal = terminala;
                    if (isGetDatas) {
                        getSelectCarInfos(name, terminal);
                    }
                }

                @Override
                public void onSelectCar(String carNumber, String terminal, String id,boolean isGetDatas) {
//                    getSelectCarInfos(carNumber, terminal);
                }
            });

            popupWindow.showAtLocation(llll, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*获取轨迹*/
    private void getSelectCarInfos(String carNumber, String terminal) {
        if (!TextUtils.isEmpty(terminal)) {
            tvTitle.setText(carNumber);
            String startTime = tvStartTime.getText().toString().trim();
            String endTime = tvEndTime.getText().toString().trim();
            getTrackData(terminal, startTime, endTime);
        } else {
            tvTitle.setText("历史轨迹");
        }
    }


    /*查询*/
    private void rl_search() {
        if (!TextUtils.isEmpty(terminal)) {
            rlTime.setVisibility(View.INVISIBLE);
            ivShowtime.setVisibility(View.VISIBLE);
            getTrackData(terminal, tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim());
        } else {
            if (type.equals("2")) {
                showSuccessToast("请先选车");
            }

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
            if (index >= mTrackDatas.size()) {
                return;
            }
            WheelPathDatasBean trackData = mTrackDatas.get(index);
            double Latitude = trackData.getLa();
            double Longitude = trackData.getLo();
            LatLng p1 = BdLocationUtil.ConverGpsToBaidu(new LatLng(Latitude, Longitude));// 转经纬度;
            double ang = mTrackDatas.get(index).getAng();
            if (type.equals("worker")) {
//                imageInt = R.drawable.worker;
            } else {
                imageInt = CarStateIconUtils.getImageInt("2", ang);
            }
//        p1 = new LatLng(23.2313123, 113.43214);
            if (mMarker != null) {
                mMarker.remove();
            }
            BitmapDescriptor map_location = BitmapDescriptorFactory.fromResource(imageInt);

//            if (index == 0) {
            ooA = new MarkerOptions().position(p1).zIndex(9).draggable(true).icon(map_location);
            BdLocationUtil.MoveMapToCenter(mBaiduMap, p1, 14);
//            } else {
//                ooA = new MarkerOptions().position(p1).zIndex(9).draggable(true).icon(map_location);
//                BdLocationUtil.MoveMapToCenter(mBaiduMap, p1, 14);
//            }
            trackDataTn = trackData.getTn();
            trackDataTn = TextUtils.isEmpty(trackDataTn) ? String.valueOf(index) : trackDataTn;
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
            mMarker.setTitle(trackDataTn);
            mSeekBar.setProgress(index);
            tvSpeed.setText(trackData.getSp() + "km/h");
        } catch (Exception e) {
           e.printStackTrace();
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
                if (!TextUtils.isEmpty(tvEndTime.getText().toString().trim())) {
                    endstartTime = TimeUtils.getBoolenStartEndTime(startTime, endtime);
                    if (endstartTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tvStartTime.setText(startTime);
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
                if (!TextUtils.isEmpty(tvStartTime.getText().toString().trim())) {
                    startEndTime = TimeUtils.getBoolenStartEndTime(tvStartTime.getText().toString().trim(),
                            tvEndTime.getText().toString().trim());
                    if (startEndTime) {
                        showSuccessToast("请重新选择时间");
                        return;
                    }
                }
                tvEndTime.setText(endtime);
            }
        });
        timePickerView.show();
    }

    private void btn_Play() {
        if (TextUtils.isEmpty(terminal)) {
            if (type.equals("2")) {
                showSuccessToast("请先选车");
            }
            return;
        }
        if (!IsPlaying) {
            if (mTrackDatas.size() < 1) {
                showSuccessToast("轨迹数据为空，请先加载！");
                return;
            }
            if (playThread == null)
                playThread = new PlayThread();
            playThread.start();
            btnPlay.setImageResource(R.drawable.pause);
        } else {
            IsPlaying = false;
            try {
                if (playThread != null && playThread.isAlive())
                    playThread.interrupt();
                playThread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnPlay.setImageResource(R.drawable.broadcast_hitory);
        }
    }


    private void fastPlay() {
        if (PlayIndex + 3 < mTrackDatas.size())
            PlayIndex += 3;
    }

    private void slowPlay() {
        if (PlayIndex - 3 > 0)
            PlayIndex -= 3;
    }


    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_search, R.id.mSeekBar, R.id.iv_showtime,
            R.id.tv_speed, R.id.btn_SlowPlay, R.id.btn_Play, R.id.btn_FastPlay, R.id.tv_title_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                tv_start();
                break;
            case R.id.tv_end_time:
                tv_end();
                break;
            case R.id.tv_search:
                rl_search();
                break;
            case R.id.mSeekBar:
                break;
            case R.id.btn_SlowPlay:
                slowPlay();
                break;
            case R.id.btn_Play:
                btn_Play();
                break;
            case R.id.btn_FastPlay:
                fastPlay();
                break;
            case R.id.tv_title_setting:
                chooseCar();
                break;
            case R.id.iv_showtime:
                ivShowtime.setVisibility(View.INVISIBLE);
                rlTime.setVisibility(View.VISIBLE);
                break;
        }
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
                    IsPlaying = false;
                    PlayIndex = 0;
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
                mSeekBar.setProgress(PlayIndex);
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
            btnPlay.setImageResource(R.drawable.broadcast_hitory);
        } catch (Exception e) {
            e.printStackTrace();
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
        BdLocationUtil.getInstance().stopLocation();
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
