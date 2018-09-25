package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.StatusBarUtil;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.TrackContract;
import com.yuefeng.features.event.TrackEvent;
import com.yuefeng.features.modle.WheelPathDatasBean;
import com.yuefeng.features.presenter.TrackPresenter;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/30.
 */

public class TrackActivity extends BaseActivity implements TrackContract.View {

    private String terminalNO;

    private TextureMapView mMapView;
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
    private TextView tvTodayTime;
    private TextView tvYesterdayTime;
    private TextView tvDaybymeTime;


    private String registNO;
    private String terminal;
    private BaiduMap mBaiduMap;

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

    private boolean isVisible = false;

    private String starttime;

    private String endtime;
    private String lat;
    private String lng;

    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    // 标记是显示Checkbox还是隐藏
    private boolean isHide = false;
    public boolean isFirstLoc = false;
    private BitmapDescriptor location_icon;
    private MarkerOptions oA;
    private Marker mMarker;
    private String startTime;
    private boolean startEndTime;
    private boolean endstartTime;

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private TrackPresenter presenter;
    private String type = "";
    private int imageInt;
    private LatLng latLngTemp=null;

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

        mMapView = findViewById(R.id.mMapView);
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

        presenter = new TrackPresenter(this, this);
        tv_title.setText("车辆轨迹");
        getTeNum();
        mTrackDatas.clear();
        latLngTemp=null;
    }

    private void getTeNum() {
        Bundle extras = getIntent().getExtras();
        terminalNO = (String) extras.get("terminalNO");
        type = (String) extras.get("tyPe");
        if (type.equals("worker")) {
            imageInt = R.drawable.worker;
        } else {
            imageInt = R.drawable.vehicle;
        }
        if (TextUtils.isEmpty(terminalNO)) {
            showErrorToast("该车轨迹无法获取");
            return;
        }

        lat = extras.getString("lat");
        lng = extras.getString("lng");
        ll_content.setVisibility(View.GONE);

        initTitle();

        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lat)) {
            initMap();
        } else {
            double la = Double.parseDouble(lat);
            double lg = Double.parseDouble(lng);
            initMapView(la, lg);
        }
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
        getTrackData(terminal, starttime, endtime);
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
        initMap();

        presenter.getGpsDatasByTer(ApiService.getGpsDatasByTer, terminalNO, startTime, endTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeTrackEvent(TrackEvent event) {
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

            case Constans.TRACK_ERROR:
                showSuccessToast("获取轨迹失败，请重试");
                break;

        }

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
    public void initData() {

    }

    @Override
    protected void setLisenter() {
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
        }
    }


    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);// 缩放控件是否显示
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(0);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initMapView(double la, double lg) {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);// 缩放控件是否显示
        MapStatus ms = new MapStatus.Builder().target(BdLocationUtil.ConverGpsToBaidu(new LatLng(la, lg))).overlook(-20).zoom(14).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
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

    public boolean isTouchMap(float upY) {
        int[] location = new int[2];
        mMapView.getLocationInWindow(location);
        if (upY > location[1]) {
            //mapView.dispatchTouchEvent(ev);
            return true;
        }
        return false;
    }


    private void tv_today_time() {
        ll_content.setVisibility(View.GONE);
        String toTodayTime = TimeUtils.getStartTimeofDay();
        String currentTime = TimeUtils.getCurrentTime();
        tvTodayTime.setBackgroundResource(R.color.titel_color);
        tvTodayTime.setTextColor(getResources().getColor(R.color.white));
        tvYesterdayTime.setBackgroundResource(R.color.white);
        tvYesterdayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tvDaybymeTime.setBackgroundResource(R.color.white);
        tvDaybymeTime.setTextColor(getResources().getColor(R.color.titel_color));
        if (mHandler != null) {
            mHandler.sendEmptyMessage(ENDPOINT);
        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
        getTrackData(terminal, toTodayTime, currentTime);
        tv_start.setSelected(false);
        tv_end.setSelected(false);
    }

    @SuppressLint("SetTextI18n")
    private void tvYesterdayTime() {
        ll_content.setVisibility(View.GONE);
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
        if (mHandler != null) {
            mHandler.sendEmptyMessage(ENDPOINT);
        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
        getTrackData(terminal, yesterDayTime + " 00:00:00" + "", toTodayTime);
    }

    private void tvDaybymeTime() {
        ll_content.setVisibility(View.VISIBLE);
        tvDaybymeTime.setBackgroundResource(R.color.titel_color);
        tvDaybymeTime.setTextColor(getResources().getColor(R.color.white));
        tvYesterdayTime.setBackgroundResource(R.color.white);
        tvYesterdayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tvTodayTime.setBackgroundResource(R.color.white);
        tvTodayTime.setTextColor(getResources().getColor(R.color.titel_color));
        tv_start.setSelected(true);
        tv_end.setSelected(true);
        if (mHandler != null) {
            mHandler.sendEmptyMessage(ENDPOINT);
        }
        mSeekBar.setProgress(0);
        latLngTemp = null;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100)
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (!isFirstLoc) {
                isFirstLoc = true;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }


    private void initTrackData(List<WheelPathDatasBean> mTrackDatas) {
        try {
            mSeekBar.setMax(mTrackDatas.size());
            if (mTrackDatas.size() > 0) {
                initTrackLine(mTrackDatas);
                ReplayTrack(0);
            } else {
//                Global.showToast("暂无轨迹");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 划线
     *
     * @param mTrackDatas
     */

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
                }else {
                    if (latLngTemp == p1) {
                        continue;
                    }else {
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
            LogUtils.d("============p===" + p1);
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


    private void rl_search() {
        getTrackData(terminal, tv_start.getText().toString().trim(), tv_end.getText().toString().trim());
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
                Log.d("tag", "onTimeSelect:111 " + startTime);
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
                    showSuccessToast("轨迹播放完毕！");
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
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
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
        }
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        PlayIndex = 0;
    }
}
