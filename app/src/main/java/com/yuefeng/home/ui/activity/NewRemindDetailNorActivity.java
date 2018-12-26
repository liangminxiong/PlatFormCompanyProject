package com.yuefeng.home.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.common.Model.BaiduMarkerBean;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.AlarmDataBean;
import com.yuefeng.home.contract.NewRemindNorDetailContract;
import com.yuefeng.home.modle.NewRemindDetailDataBean;
import com.yuefeng.home.presenter.NewRemindNorDetailPresenter;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.ui.view.BaiDuMapMakerWindows;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*消息，报警详情界面*/
public class NewRemindDetailNorActivity extends BaseActivity implements NewRemindNorDetailContract.View, LocationUtils.OnResultMapListener {


    @BindView(R.id.tv_title_setting)
    TextView mTvSetting;
    @BindView(R.id.baidumap)
    TextureMapView mMapView;

    private boolean isFirstLocation = true;
    private LatLng mLatLng;
    BitmapDescriptor map_location = BitmapDescriptorFactory.fromResource(R.drawable.news_warning_location);
    public MarkerOptions ooA;
    private Marker mMarker;
    private BaiduMap mBaiduMap;
    private String address;
    private double latitude;
    private double longitude;
    private NewRemindNorDetailPresenter mPresenter;

    private String mStartTime = "";
    private String mEndTime = "";
    private String mId;
    private AlarmDataBean mMsgData;
    private BaiDuMapMakerWindows mMakerWindows;
    private String mCarNumber;
    private String mContent;
    private String mTime;
    private LocationUtils mLocationUtils;

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
        setTitle("报警详情");
        mPresenter = new NewRemindNorDetailPresenter(this, this);
    }

    @Override
    protected void initData() {
        getDatas();
        initBaidu();
    }

    private void initBaidu() {

        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(true);// 缩放控件是否显示
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setOverlookingGesturesEnabled(false);
        settings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
        settings.setAllGesturesEnabled(true);
    }

    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mMsgData = (AlarmDataBean) bundle.get("msgData");
        if (mMsgData != null && mPresenter != null) {
            mId = mMsgData.getId();
            getNetDatas(mId);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    private void getNetDatas(String id) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            showSuccessToast("请检查网络配置");
            return;
        }
        if (mPresenter != null) {
            mPresenter.getAlarmedit(id, mStartTime, mEndTime);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.REMINDDETAIL_SUCCESS://展示最新消息
                List<NewRemindDetailDataBean> list = (List<NewRemindDetailDataBean>) event.getData();
                if (list.size() > 0) {
                    showDataToMap(list);
                } else {
                    showSuccessToast("无最新消息");
                }
                break;
            case Constans.APP_VERSION_SUCCESS:
                break;
            case Constans.REMINDDETAIL_ERROR:
                showSureGetAgainDataDialog("获取数据失败，是否重新加载?");
                break;
        }
    }

    private void showDataToMap(List<NewRemindDetailDataBean> list) {
        NewRemindDetailDataBean bean = list.get(0);
        mCarNumber = bean.getIssuedate();
        mContent = bean.getContent();
        String latitude = bean.getLatitude();
        String longitude = bean.getLongitude();
        mTime = bean.getSubject();

        if (!TimeUtils.isEmpty(latitude) && !TimeUtils.isEmpty(longitude)) {
            mLatLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
            if (mBaiduMap != null) {
                BdLocationUtil.MoveMapToCenterWithBitmap(mBaiduMap, mLatLng, Constans.BAIDU_ZOOM_FOUTTEEN, map_location, ooA, mMarker);
            }
            getAddress(mLatLng);
        }
    }

    private void getAddress(LatLng latLng) {
// 创建定位管理信息对象
        mLocationUtils = new LocationUtils(this);
//        开启定位
        mLocationUtils.startLocation();
        mLocationUtils.registerOnResult(this);
        mLocationUtils.getAddressLatlng(latLng);
    }

    @Override
    public void getDatasAgain() {
        getNetDatas(mId);
        super.getDatasAgain();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        try {
            address = (String) map.get("address");
            LogUtils.d("===addresss===" + address);
            setMarkerViewData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMarkerViewData() {
        if (mMakerWindows == null) {
            mMakerWindows = new BaiDuMapMakerWindows();
        }

        BaiduMarkerBean markerBean = new BaiduMarkerBean(mCarNumber, mTime, mContent, address, "");
        mMakerWindows.showViewData(mBaiduMap, mLatLng, markerBean);
    }

    @Override
    public void onGeoCodeResult(Map<String, Object> map) {

    }
}
