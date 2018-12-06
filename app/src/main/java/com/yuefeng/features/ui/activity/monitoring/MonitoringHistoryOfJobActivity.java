package com.yuefeng.features.ui.activity.monitoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.common.view.timeview.TimePickerView;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.HistoryMonitoringAdapter;
import com.yuefeng.features.contract.MonitoringHistoryContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.GetMonitoringHistoryBean;
import com.yuefeng.features.modle.GetMonitoringHistoryDetaiBean;
import com.yuefeng.features.presenter.monitoring.MonitoringHistoryPresenter;
import com.yuefeng.features.ui.activity.HistoryProblemUpdataActivity;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/*作业历史监察*/
public class MonitoringHistoryOfJobActivity extends BaseActivity implements MonitoringHistoryContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_problem)
    RelativeLayout ll_problem;
    @BindView(R.id.rl_child)
    RelativeLayout rlChild;
    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_use_time)
    TextView tvUseTime;
    @BindView(R.id.tv_upload_count)
    TextView tvUploadCount;
    @BindView(R.id.tv_sngnin_count)
    TextView tvSngninCount;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    private BaiduMap baiduMap;
    private double latitude;
    private double longitude;
    private String address;
    BitmapDescriptor personalImage = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    BitmapDescriptor beginImage = BitmapDescriptorFactory.fromResource(R.drawable.start);
    BitmapDescriptor endImage = BitmapDescriptorFactory.fromResource(R.drawable.destination);
    private boolean isOnclick = true;
    private boolean isFirstLocation = true;
    private MonitoringHistoryPresenter presenter;
    private Marker mMarker;
    private MarkerOptions ooA;
    private HistoryMonitoringAdapter adapter;
    private List<GetMonitoringHistoryDetaiBean> listData = new ArrayList();
    private Polyline mPolyline;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_monitoringofjobhistory;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new MonitoringHistoryPresenter(this, this);
        tv_title.setText("监察记录详情");
    }

    @Override
    public void initData() {
        initUI();
        requestPermissions();
    }

    private void initUI() {
        try {
            String startTime = TimeUtils.getYesterdayStartTime();
            String endTime = TimeUtils.getCurrentTime2();
            tvStartTime.setText(startTime);
            tvEndTime.setText(endTime);
            ViewUtils.setRLHightOrWidth(rlChild, (int) (AppUtils.mScreenHeight / 2), ViewGroup.LayoutParams.MATCH_PARENT);
            initRecycler();

            getDatasByNet(startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDatasByNet(String startTime, String endTime) {
        try {
            if (presenter != null) {
                String userid = PreferencesUtils.getString(this, Constans.ID);
                presenter.getWorkJianCha(ApiService.GETWORKJIANCHA, userid, startTime, endTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryMonitoringAdapter(R.layout.recyclerview_item_historymonitoring, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String point = listData.get(position).getPoint();
                if (!TimeUtils.isEmpty(point)) {
                    showDataImageIntoMap(point);
                }
            }
        });
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

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
        try {
            BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
                @Override
                public void myLocation(BDLocation location) {
                    if (location == null) {
                        return;
                    }
//                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    address = location.getAddrStr();
                    if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                        int length = address.length();
                        address = address.substring(2, length);
                    }
                    LatLng latLngTemp = new LatLng(latitude, longitude);
                    if (isFirstLocation) {
                        isFirstLocation = false;
                        PreferencesUtils.putString(MonitoringHistoryOfJobActivity.this, Constans.ADDRESS, address);
                        MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                                .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                        ooA = new MarkerOptions().icon(personalImage).zIndex(10);
//                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                        ooA.position(latLngTemp);
                        mMarker = null;
                        mMarker = (Marker) (baiduMap.addOverlay(ooA));
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                    }
//                }
                }
            }, Constans.BDLOCATION_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:
                GetMonitoringHistoryBean.MsgBean bean = (GetMonitoringHistoryBean.MsgBean) event.getData();
                if (bean != null) {
                    showAdapterDatas(bean);
                }
                break;
            case Constans.MSGCOLECTION_ERROR:
                showErrorToast("获取失败，请重试!");
                break;

        }
    }

    private void showAdapterDatas(GetMonitoringHistoryBean.MsgBean bean) {
        String timesum = "";
        try {
            int time = bean.getTimesum();

            String qiandao = bean.getQiandao() + "";
            if (TimeUtils.isEmpty(qiandao)) {
                qiandao = "0";
            }
            String report = bean.getReport();
            if (TimeUtils.isEmpty(report)) {
                report = "0";
            }
            timesum = TimeUtils.dateFormatFromSeconds(time);
            tvUseTime.setText(timesum);
            tvUploadCount.setText(report);
            tvSngninCount.setText(qiandao);

            List<GetMonitoringHistoryDetaiBean> listDetai = bean.getDetai();
            if (listDetai.size() > 0 && adapter != null) {
                listData.clear();
                listData.addAll(listDetai);
                adapter.setNewData(listData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDataImageIntoMap(String lnglat) {
        try {
            List<LatLng> points = com.yuefeng.utils.StringUtils.getLnglat(lnglat);
            int size = points.size();
            if (size > 1) {
                drawLineIntoBaiduMap(points);
            } else {
                LatLng latLng = points.get(0);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawLineIntoBaiduMap(List<LatLng> points) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BdLocationUtil.getInstance().stopLocation();//停止定位
        assert mapview != null;
        mapview.getMap().clear();
        mapview.onDestroy();
        mapview = null;
        personalImage.recycle();
        beginImage.recycle();
        endImage.recycle();
    }


    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_midle, R.id.tv_upload_count, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                chooseTime(1);
                break;
            case R.id.tv_end_time:
                chooseTime(2);
                break;
            case R.id.tv_midle:
                intoHistoryUpload();
            case R.id.iv_search:
                iv_search();

                break;
            case R.id.tv_upload_count:
                intoHistoryUpload();
                break;
            case R.id.tv_sngnin_count:
                intoHistorySngnIn();
                break;
            case R.id.tv_si:
                intoHistorySngnIn();
                break;
        }
    }

    /*搜索*/
    private void iv_search() {
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        getDatasByNet(startTime, endTime);
    }

    /*历史上报*/
    private void intoHistoryUpload() {
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        Intent intent = new Intent();
        intent.setClass(MonitoringHistoryOfJobActivity.this, HistoryProblemUpdataActivity.class);
        intent.putExtra(Constans.STARTTIME, startTime);
        intent.putExtra(Constans.ENDTIME, endTime);
        startActivity(intent);

    }

    /*历史签到*/
    private void intoHistorySngnIn() {

        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        Intent intent = new Intent();
        intent.setClass(MonitoringHistoryOfJobActivity.this, HistoryMonitoringSngnInActivity.class);
        intent.putExtra(Constans.STARTTIME, startTime);
        intent.putExtra(Constans.ENDTIME, endTime);
        startActivity(intent);

    }

    /*选择时间*/
    private void chooseTime(final int type) {
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        timePickerView.setTitle("时间选择");
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (type == 1) {
                    tvStartTime.setText(TimeUtils.getTimeHourMin(date));
                } else {
                    tvEndTime.setText(TimeUtils.getTimeHourMin(date));
                }
            }
        });
        timePickerView.show();
    }
}
