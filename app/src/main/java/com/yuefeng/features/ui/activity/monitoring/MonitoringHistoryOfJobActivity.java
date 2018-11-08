package com.yuefeng.features.ui.activity.monitoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.TimeUtils;
import com.common.view.timeview.TimePickerView;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.StringSingleAdapter;
import com.yuefeng.features.contract.MonitoringContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.GetCaijiTypeMsgBean;
import com.yuefeng.features.presenter.monitoring.MonitoringHistoryPresenter;
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
public class MonitoringHistoryOfJobActivity extends BaseActivity implements MonitoringContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
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
    private StringSingleAdapter adapter;
    private List<GetCaijiTypeMsgBean> listData = new ArrayList();

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
        String startTime = TimeUtils.getDayStartTime();
        String endTime = TimeUtils.getCurrentTime2();
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);

        initRecycler();
    }

    private void initRecycler() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StringSingleAdapter(R.layout.list_item_string, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showSuccessToast(listData.get(position).getData());

            }
        });

        for (int i = 0; i < 10; i++) {
            GetCaijiTypeMsgBean bean = new GetCaijiTypeMsgBean();
            bean.setData("测试测试 ++ " + i);
            listData.add(bean);
        }

        if (adapter != null) {
            adapter.setNewData(listData);
        }
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
                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                }
                LatLng latLngTemp = new LatLng(latitude, longitude);
                if (isFirstLocation) {
                    isFirstLocation = false;
                    MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                            .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
                    ooA = new MarkerOptions().icon(personalImage).zIndex(10);
                    ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                    ooA.position(latLngTemp);
                    mMarker = null;
                    mMarker = (Marker) (baiduMap.addOverlay(ooA));
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                }
//                }
            }
        }, Constans.BDLOCATION_TIME);
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.EQUIPMENTCOUNTSUCESS:
                String count = (String) event.getData();
//                tv_txt_count.setText("还可以输入" + count + "字");
                break;

            case Constans.UPLOADSUCESS:
                showSuccessDialog(getString(R.string.upload_success));
                break;
            case Constans.USERERROR:
                showErrorToast("上传失败，请重试!");
                break;

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


    @OnClick({R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                chooseTime(1);
                break;
            case R.id.tv_end_time:
                chooseTime(2);
                break;
        }
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
