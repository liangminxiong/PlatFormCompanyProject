package com.yuefeng.features.ui.activity.monitoring;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.common.view.other.ImitateKeepButton;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.MonitoringOfJobContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.GetMonitoringPlanCountBean;
import com.yuefeng.features.presenter.monitoring.MonitoringOfJobPresenter;
import com.yuefeng.features.ui.activity.ProblemUpdateActivity;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    @BindView(R.id.viewstub)
    ViewStub viewstub;
    @BindView(R.id.viewstub_ct)
    ViewStub viewstubCt;
    @BindView(R.id.viewstub_upload)
    ViewStub viewstubUpload;
    @BindView(R.id.btn_beginorstop)
    Button btnBeginorstop;

    private RelativeLayout rlSelectType;
    private TextView tvUserName;
    private ImageView ivUserIcon;
    private TextView tvPlanCount;
    private TextView tvActualCount;


    private RelativeLayout rlCttime;
    private Chronometer ctTimer;

    private RelativeLayout rlupload;
    private TextView tvUpload, tvNoexit;
    private TextView tvSignin;
    private ImitateKeepButton ivStop;


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
    List<LatLng> points = new ArrayList<>();
    private Polyline mPolyline;
    private MonitoringOfJobPresenter presenter;

    /*是否采集*/
    private boolean isPositionAcquisition = false;
    private boolean isStartTime = true;
    private boolean isPutStartTime = true;
    private String mTimesum;

    public MoLocationListenner myListener = new MoLocationListenner();
    private LocationClient mLocClient;

    private boolean isFirst = true;
    private boolean isSecond = true;
    private boolean isThird = true;

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
        presenter = new MonitoringOfJobPresenter(this, this);
        isPositionAcquisition = false;
        isPutStartTime = true;
        isFirst = true;
        isSecond = true;
        isThird = true;
        initUI();
    }

    private void initUI() {
        tv_title.setText(R.string.problem_updata);
        tvTitleSetting.setText(R.string.history);
        ll_root_position.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ininViewStub();
    }

    private void ininViewStub() {
        if (isFirst) {
            isFirst = false;
            View view = viewstub.inflate();
            rlSelectType = view.findViewById(R.id.rl_select_type);
            tvUserName = view.findViewById(R.id.tv_user_name);
            ivUserIcon = view.findViewById(R.id.iv_user_icon);
            tvPlanCount = view.findViewById(R.id.tv_plan_count);
            tvActualCount = view.findViewById(R.id.tv_actual_count);
        }

        String userName = PreferencesUtils.getString(MonitoringofJobActivity.this, Constans.USERNAME);
        if (!TextUtils.isEmpty(userName)) {
            tvUserName.setText(userName);
        }
        String startTime = TimeUtils.getTimeOfWeekStart();
        String endTime = TimeUtils.getCurrentTime2();
        getDatasByNet(startTime, endTime);

    }

    private void getDatasByNet(String startTime, String endTime) {
        try {

            if (presenter != null) {
                String userid = PreferencesUtils.getString(MonitoringofJobActivity.this, Constans.ID);
                presenter.getJianChaCount(ApiService.GETJIANCHACOUNT, userid, startTime, endTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ininViewStubCtTimer() {
        if (isSecond) {
            isSecond = false;
            View view = viewstubCt.inflate();
            rlCttime = view.findViewById(R.id.rl_cttime);
            ctTimer = view.findViewById(R.id.ct_timer);
            tvNoexit = view.findViewById(R.id.tv_noexit);
            tvNoexit.setOnClickListener(this);
        }
        initChronometer();
    }

    private void ininViewStubUpload() {
        if (isThird) {
            isThird = false;
            View view = viewstubUpload.inflate();
            rlupload = view.findViewById(R.id.rl_upload);
            tvUpload = view.findViewById(R.id.tv_upload);
            tvSignin = view.findViewById(R.id.tv_signin);
            ivStop = view.findViewById(R.id.iv_stop);
            tvUpload.setOnClickListener(this);
            tvSignin.setOnClickListener(this);
        }
        ivStop.setBackgroundResource(R.drawable.jiancha_but_start);
        ivStop.setOnViewShortClick(new ImitateKeepButton.OnViewShortClick() {
            @Override
            public void onShortClick(View view) {
                startCtimer();
            }
        });

        ivStop.setOnViewClick(new PicLongClick());
    }

    private class PicLongClick implements ImitateKeepButton.OnViewClick {

        @Override
        public void onFinish(View view) {
            ivStop.setBackgroundResource(R.drawable.jiancha_but_pause);
            initCtStop();
            tv_release();
            if (baiduMap != null) {
                baiduMap.clear();
            }
            isFirstLoc = true;
        }
    }

    private void initChronometer() {
        ctTimer.setBase(SystemClock.elapsedRealtime());
        ctTimer.setFormat("%s");
    }


    @Override
    protected void initData() {
        getLocation();
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
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(8000);
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();

        baiduMap.showMapPoi(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    /**
     * 定位SDK监听函数
     */
    public class MoLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapview == null) {
                return;
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            address = location.getAddrStr();
            firstLocation(latitude, longitude, address);
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
                MapStatus ms = new MapStatus.Builder().target(latLng)
                        .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                ooA = new MarkerOptions().icon(personalImage).zIndex(10);
                ooA.position(latLng);
//                ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                mMarker = null;
                mMarker = (Marker) (baiduMap.addOverlay(ooA));
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
//            BdLocationUtil.MoveMapToCenter(baiduMap, latLngTemp, 14);
                PreferencesUtils.putString(MyApplication.getContext(), "Fengrun", address);
                PreferencesUtils.putString(MyApplication.getContext(), "mAddress", address);
                PreferencesUtils.putString(MonitoringofJobActivity.this, Constans.STARTADDRESS, address);
            }
            PreferencesUtils.putString(MonitoringofJobActivity.this, Constans.ENDADDRESS, address);

            starDrawTrackLine(latLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void starDrawTrackLine(LatLng latLng) {
        if (latLng == null) {
            return;
        }

        drawTrackLine(latLng);
    }

    /*划线*/
    private void drawTrackLine(LatLng latLng) {
        try {
            if (isPositionAcquisition) {
                points.add(latLng);//如果要运动完成后画整个轨迹，位置点都在这个集合中
                if (points.size() > 1 && baiduMap != null) {
                    drawLineIntoBaiduMap(baiduMap, points);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*划线*/
    private void drawLineIntoBaiduMap(BaiduMap baiduMap, List<LatLng> points) {
        //清除上一次轨迹，避免重叠绘画
        baiduMap.clear();
        //起始点图层也会被清除，重新绘画
        MarkerOptions oStart = new MarkerOptions();
        oStart.position(points.get(0));
        oStart.icon(beginImage);
        this.baiduMap.addOverlay(oStart);

        OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.RED).points(this.points);
        mPolyline = (Polyline) this.baiduMap.addOverlay(ooPolyline);
        BdLocationUtil.MoveMapToCenter(baiduMap, points.get(points.size() - 1), Constans.BAIDU_ZOOM_EIGHTEEN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposePositionAcquisitionEvent(PositionAcquisitionEvent event) {
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:
                PreferencesUtils.putString(MonitoringofJobActivity.this, Constans.ISSINGIN, "");
                resetTime();
                showSuccess();
                break;
            case Constans.MSGCOLECTION_ERROR://上传失败
                resetTime();
                break;

            case Constans.CHANGEPWD_SSUCESS:
                GetMonitoringPlanCountBean.MsgBean bean = (GetMonitoringPlanCountBean.MsgBean) event.getData();
                if (bean != null) {
                    showPlanCountUI(bean);
                }

                break;
            case Constans.CHANGEPWD_ERROR://上传失败

                break;
        }
    }

    /*重置*/
    private void resetTime() {
        isStartTime = true;
        isPositionAcquisition = false;
        if (ctTimer != null) {
            ctTimer.stop();
            ctTimer.setBase(SystemClock.elapsedRealtime());
            mRecordTime = 0;
        }
    }

    private void showSuccess() {
        SucessCacheSureDialog sureDialog = new SucessCacheSureDialog(MonitoringofJobActivity.this);
        sureDialog.setTextContent("监察成功，是否退出当前界面?");
        sureDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                finish();
            }

            @Override
            public void cancle() {
                startMonitoringAgain();

            }
        });
        sureDialog.show();
    }

    /*再次监察*/
    private void startMonitoringAgain() {

        if (rlSelectType != null && rlupload != null && rlCttime != null) {
            ViewUtils.setRlInVisible(rlSelectType, false);

            ViewUtils.setBtnInVisible(btnBeginorstop, false);
            ViewUtils.setRlInVisible(rlCttime, true);
            ViewUtils.setRlInVisible(rlupload, true);
        }
    }

    private void showPlanCountUI(GetMonitoringPlanCountBean.MsgBean bean) {
        String plan = bean.getPlan();
        plan = StringUtils.isEntryStrZero(plan);
        String count = bean.getCount();
        count = StringUtils.isEntryStrZero(count);
        if (tvPlanCount != null) {
            tvPlanCount.setText(plan);
        }
        if (tvActualCount != null) {
            tvActualCount.setText(count);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload:
                startActivity(new Intent(MonitoringofJobActivity.this, ProblemUpdateActivity.class));
                break;
            case R.id.tv_signin:
                startActivity(new Intent(MonitoringofJobActivity.this, MonitoringSngnInActivity.class));
                break;
            case R.id.tv_noexit:
                tvNoexit.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BdLocationUtil.getInstance().stopLocation();//停止定位
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }

        assert mapview != null;
        mapview.getMap().clear();
        mapview.onDestroy();
        mapview = null;
        beginImage.recycle();
        endImage.recycle();
    }


    /*开始监察*/
    private void beginOrStop() {
        ininViewStubCtTimer();
        ininViewStubUpload();
        points.add(new LatLng(latitude, longitude));
        if (ctTimer != null && tvNoexit != null && rlCttime != null) {
            ViewUtils.setRlInVisible(rlCttime, false);
            ctTimer.setVisibility(View.VISIBLE);
            tvNoexit.setVisibility(View.VISIBLE);
        }
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


    @OnClick({R.id.btn_beginorstop, R.id.tv_title_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_beginorstop:
                beginOrStop();
                break;
            case R.id.tv_title_setting:
//                tv_release();
                startActivity(new Intent(MonitoringofJobActivity.this, MonitoringHistoryOfJobActivity.class));
                break;
        }
    }

    /*发布*/
    private void tv_release() {
        if (presenter != null && points.size() > 0) {
            try {

                String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
                String userId = PreferencesUtils.getString(this, Constans.ID, "");
                String startTime = PreferencesUtils.getString(this, Constans.STARTTIME, "");
                String endTime = TimeUtils.getCurrentTime2();
                String lnglat = presenter.getLnglatStr(points);
                if (ctTimer != null) {
                    mTimesum = ctTimer.getText().toString().trim();
                }
                if (!TextUtils.isEmpty(mTimesum)) {
                    mTimesum = TimeUtils.formatTurnSecond(mTimesum);
                }
                String startAddress = PreferencesUtils.getString(MonitoringofJobActivity.this, Constans.STARTADDRESS, "");
                String endAddress = PreferencesUtils.getString(MonitoringofJobActivity.this, Constans.ENDADDRESS, "");
                presenter.uploadJianCcha(ApiService.UPLOADJIANCHA, userId, pid, startTime, endTime, mTimesum, lnglat, startAddress, endAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startCtimer() {
        String effective = PreferencesUtils.getString(this, Constans.ISSINGIN, "");
        String startTime = TimeUtils.getDayStartTime();
        boolean lessThan = TimeUtils.isTimeLessThan(startTime, effective);
        if (!TimeUtils.isEmpty(effective) && lessThan) {
            if (isStartTime) {
                initCtStart();
                if (isPutStartTime) {
                    PreferencesUtils.putString(this, Constans.STARTTIME, TimeUtils.getCurrentTime2());
                    isPutStartTime = false;
                }
                isStartTime = false;
                isPositionAcquisition = true;
                if (ivStop != null) {
                    ivStop.setBackgroundResource(R.drawable.jiancha_but_pause);
                }
            } else {
                initCtStop();
                isStartTime = true;
                isPositionAcquisition = false;
                if (ivStop != null) {
                    ivStop.setBackgroundResource(R.drawable.jiancha_but_continue);
                }
            }
        } else {
            showSuccessToast("上报或者签到后才可开始监察");
        }
    }
}
