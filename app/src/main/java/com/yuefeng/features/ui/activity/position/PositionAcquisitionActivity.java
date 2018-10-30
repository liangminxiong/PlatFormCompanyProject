package com.yuefeng.features.ui.activity.position;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.location.LocationHelper;
import com.common.location.MyLocationListener;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.ImageUtils;
import com.common.utils.LocationGpsUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.common.view.timeview.OptionsPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.StringSingleAdapter;
import com.yuefeng.features.contract.PositionAcquisitionContract;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.presenter.PositionAcquisitionPresenter;
import com.yuefeng.features.ui.view.MsgCollectionPopupWindow;
import com.yuefeng.photo.adapter.GridImageAdapter;
import com.yuefeng.photo.other.FullyGridLayoutManager;
import com.yuefeng.photo.utils.PictureSelectorUtils;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/*定位采集*/
public class PositionAcquisitionActivity extends BaseActivity implements PositionAcquisitionContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_root_position)
    LinearLayout ll_root_position;
    @BindView(R.id.mapview)
    TextureMapView mapview;
    @BindView(R.id.tv_title_setting)
    TextView tvTitleSetting;
    @BindView(R.id.rly_home_title)
    RelativeLayout rlyHomeTitle;
    @BindView(R.id.recyclerview_left)
    TextView recyclerviewLeft;
    @BindView(R.id.recyclerview_right)
    TextView recyclerviewRight;
    @BindView(R.id.rl_select_type)
    RelativeLayout rlSelectType;
    @BindView(R.id.rl_select_start)
    RelativeLayout rl_select_start;
    @BindView(R.id.rl_time)
    RelativeLayout rl_time;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.ct_timer_circle)
    Chronometer ctTimerCircle;
    @BindView(R.id.tv_carryon)
    TextView tvCarryon;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.ll_timer)
    LinearLayout ll_timer;

    @BindView(R.id.ll_finish)
    LinearLayout ll_finish;
    @BindView(R.id.btn_beginorstop)
    Button btnBeginorstop;


    @BindView(R.id.tv_time_distance)
    TextView tvTimeDistance;
    @BindView(R.id.tv_quduan_type)
    TextView tvQuduanType;
    @BindView(R.id.edt_msg_name)
    EditText edtMsgName;
    @BindView(R.id.recycler_photo)
    RecyclerView recyclerPhoto;

    @BindDrawable(R.drawable.shape_bg_transtions)
    Drawable transt;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.black)
    int black;
    @BindColor(R.color.titel_color)
    int titel_color;
    @BindColor(R.color.gray)
    int gray;

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

    private CameraPhotoPopupWindow popupWindow;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;

    private String mImagesArrays;
    private int current = 0;
    private long mRecordTime;
    private LatLng latLng;
    private LocationHelper mLocationHelper;
    private LatLng latLngTemp = null;
    /*距离*/
    private double distance = 0;
    /*条件选择框*/
    private OptionsPickerView pvOptions;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    List<LatLng> points = new ArrayList<>();
    private Polyline mPolyline;
    private String timeLong;
    private String infrastructureStr;//基础设施
    private String workAreaStr;//作业区段
    private String type;
    private MsgCollectionPopupWindow msgPopupWindow;
    private StringSingleAdapter singleAdapter;
    private List<String> listData = new ArrayList<>();
    private String typeWhat;
    private PositionAcquisitionPresenter presenter;

    /*是否采集*/
    private boolean isPositionAcquisition = false;
    /*选择网格还是路段*/
    private int typePosition = 0;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_positionacquistion;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        tv_title.setText(R.string.position_acquisition);
        presenter = new PositionAcquisitionPresenter(this, this);
        infrastructureStr = "";
        workAreaStr = "";
        initRlType();
        initChronometer();
    }

    private void initChronometer() {
        ctTimerCircle.setBase(SystemClock.elapsedRealtime());
        ctTimerCircle.setFormat("%s");
    }

    private void initRlType() {
        ViewUtils.setRLHightOrWidth(rlSelectType, (int) AppUtils.mScreenHeight / 4, ActionBar.LayoutParams.MATCH_PARENT);
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
                        } else {
                            getLocation();
                        }
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
        initLocation();
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
                    requestPermissions();
                    return;
                }
//                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                }
                firstLocation(latitude, longitude, address);
//                } else {
//                    requestPermissions();
//                }

            }
        }, Constans.BDLOCATION_TIME);
    }

    private void firstLocation(double latitude, double longitude, String address) {

        if (!TextUtils.isEmpty(address)) {
            int length = address.length();
            address = address.substring(2, length);
        }
        latLngTemp = new LatLng(latitude, longitude);
        if (isFirstLoc) {
            isFirstLoc = false;
            MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                    .overlook(-20).zoom(Constans.BAIDU_ZOOM_EIGHTEEN).build();
            ooA = new MarkerOptions().icon(personalImage).zIndex(10);
            ooA.position(latLngTemp);
            mMarker = null;
            mMarker = (Marker) (baiduMap.addOverlay(ooA));
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
//            BdLocationUtil.MoveMapToCenter(baiduMap, latLngTemp, 14);
            PreferencesUtils.putString(PositionAcquisitionActivity.this, "Fengrun", "");
            PreferencesUtils.putString(PositionAcquisitionActivity.this, "mAddress", address);
        }
    }

    /*定时刷新*/
    private void initLocation() {
        mLocationHelper = new LocationHelper(this, Constans.TEN);
        mLocationHelper.initLocation(new MyLocationListener() {

            @Override
            public void updateLastLocation(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateLocation(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
                starDrawTrackLine(latLng);
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

    private void starDrawTrackLine(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        distance = DistanceUtil.getDistance(latLngTemp, latLng);
        if (distance < 1000) {


            String stringDouble = StringUtils.getStringDistance(distance);
//        LogUtils.d("getLocation== +++" + points.size() + " ++ ++ " + stringDouble);
            latLngTemp = latLng;
            drawTrackLine(latLng, distance);
        }
    }

    private void drawTrackLine(LatLng latLng, double distance) {
        int zoom = 0;
        if (distance > 100) {
            zoom = Constans.BAIDU_ZOOM_FOUTTEEN;
        } else {
            zoom = Constans.BAIDU_ZOOM_EIGHTEEN;
        }
        try {
            if (mPolyline != null) {
                mPolyline.remove();
            }
            if (isPositionAcquisition) {
                points.add(latLng);//如果要运动完成后画整个轨迹，位置点都在这个集合中
            } else {
                return;
            }
            if (points.size() > 1 && baiduMap != null) {
                //清除上一次轨迹，避免重叠绘画
                baiduMap.clear();

                //起始点图层也会被清除，重新绘画
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(0));
                oStart.icon(beginImage);
                baiduMap.addOverlay(oStart);

                OverlayOptions ooPolyline = new PolylineOptions().width(12).color(Color.RED).points(points);
                mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                BdLocationUtil.MoveMapToCenter(baiduMap, points.get(points.size() - 1), zoom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected double getLongitude() {
        return longitude;
    }

    protected double getLatitude() {
        return latitude;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposePositionAcquisitionEvent(PositionAcquisitionEvent event) {
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:

                break;
            default:

                break;
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
        EventBus.getDefault().unregister(this);
        BdLocationUtil.getInstance().stopLocation();//停止定位
        if (mLocationHelper != null) {
            mLocationHelper.removeLocationUpdatesListener();
        }
        assert mapview != null;
        mapview.getMap().clear();
        mapview.onDestroy();
        mapview = null;
        beginImage.recycle();
        endImage.recycle();
    }

    @OnClick({R.id.tv_carryon, R.id.tv_finish, R.id.btn_beginorstop, R.id.tv_release,
            R.id.recyclerview_left, R.id.recyclerview_right, R.id.rl_select_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_carryon://继续
                tv_carryon();
                break;
            case R.id.tv_finish://结束
                tv_finish("1");
                break;
            case R.id.btn_beginorstop:
                beginOrStop();
                break;
            case R.id.tv_release:
                tv_release();
                break;
            case R.id.recyclerview_left://基础设施
                initMsgPopuwindow(recyclerviewLeft, true);
                initInFrastDatas();
                workAreaStr = "";
                recyclerviewRight.setText(workAreaStr);
                break;
            case R.id.recyclerview_right://作业区段:
                infrastructureStr = "";
                recyclerviewLeft.setText(infrastructureStr);
                initMsgPopuwindow(recyclerviewRight, false);
                initWorkAreaDatas();
                break;
            case R.id.rl_select_start://点击暂停
                cvTimerStop();
                break;
        }
    }

    /*作业区段数据*/
    private void initWorkAreaDatas() {
        listData.clear();
        listData.add("网格");
        listData.add("路段");
        if (singleAdapter != null) {
            singleAdapter.setNewData(listData);
        }
    }

    /*基础设施数据*/
    private void initInFrastDatas() {
        listData.clear();
        listData.add("生活垃圾收集点");
        listData.add("垃圾站");
        listData.add("公厕");
        listData.add("中转站");
        if (singleAdapter != null) {
            singleAdapter.setNewData(listData);
        }
    }

    /*选择基础设施*/
    private void initMsgPopuwindow(View parent, final boolean isWhatType) {
        msgPopupWindow = new MsgCollectionPopupWindow(this);
        msgPopupWindow.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        singleAdapter = new StringSingleAdapter(R.layout.list_item_string, listData);
        msgPopupWindow.recyclerview.setAdapter(singleAdapter);
        singleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                typeWhat = listData.get(position);
                if (msgPopupWindow != null) {
                    msgPopupWindow.dismiss();
                }
                if (TextUtils.isEmpty(typeWhat)) {
                    showSuccessToast("类型未知");
                    return;
                }
                if (isWhatType) {
                    recyclerviewLeft.setText(typeWhat);
                } else {
                    recyclerviewRight.setText(typeWhat);
                }

            }
        });
        msgPopupWindow.showPopuWindow(parent);
    }

    /*暂停*/
    private void cvTimerStop() {
        timeLong = ctTimerCircle.getText().toString().trim();
        rl_select_start.setVisibility(View.INVISIBLE);
        ll_timer.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(timeLong)) {
            tvTimer.setText(timeLong);
        }
        isPositionAcquisition = false;
        initCtStop();
    }


    /*发布*/
    private void tv_release() {
        showSuccessToast("发布");
    }

    /*结束*/
    @SuppressLint("SetTextI18n")
    private void tv_finish(String typeDistance) {
        ll_timer.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(infrastructureStr)) {
            type = infrastructureStr;
        }
        if (!TextUtils.isEmpty(workAreaStr)) {
            type = workAreaStr;
        }
        ll_finish.setVisibility(View.VISIBLE);
        tvQuduanType.setText(type);
        selectPhoto();
        if (typeDistance.equals("2")) {
            tvTimeDistance.setText("本次采集地址:" + address);
        } else {
            if (presenter != null) {
                String time = presenter.showHowLongTime(timeLong);
                tvTimeDistance.setText(time);
            }
            //起始点图层也会被清除，重新绘画
            if (points.size() > 0 && baiduMap != null) {
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(points.size() - 1));
                oStart.icon(endImage);
                baiduMap.addOverlay(oStart);
            }
        }
    }

    /*继续采集*/
    private void tv_carryon() {
        ll_timer.setVisibility(View.INVISIBLE);
        rl_select_start.setVisibility(View.VISIBLE);
        isPositionAcquisition = true;
        initCtStart();
    }

    /*开始采集*/
    private void beginOrStop() {
        infrastructureStr = recyclerviewLeft.getText().toString().trim();
        workAreaStr = recyclerviewRight.getText().toString().trim();
        if (TextUtils.isEmpty(infrastructureStr) && TextUtils.isEmpty(workAreaStr)) {
            showSuccessToast("请选择一种标注类型");
            return;
        }
        if (!TextUtils.isEmpty(workAreaStr)) {//选择作业区段
            isPositionAcquisition = true;
            initCtStart();
            rlSelectType.setVisibility(View.INVISIBLE);
            btnBeginorstop.setVisibility(View.INVISIBLE);
            rl_select_start.setVisibility(View.VISIBLE);
        } else {//选择基础设施
            tv_finish("2");
            isPositionAcquisition = false;
        }
    }

    /*计时器start*/
    private void initCtStart() {

        if (ctTimerCircle != null)
            if (mRecordTime != 0) {
                ctTimerCircle.setBase(ctTimerCircle.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
            } else {
                ctTimerCircle.setBase(SystemClock.elapsedRealtime());
            }
        assert ctTimerCircle != null;
        ctTimerCircle.start();
    }

    /*计时器stop*/
    private void initCtStop() {
        if (ctTimerCircle != null) {
            ctTimerCircle.stop();
        }
        mRecordTime = SystemClock.elapsedRealtime();
    }

    /*图片选择*/
    private void selectPhoto() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PositionAcquisitionActivity.this, Constans.THREE, GridLayoutManager.VERTICAL, false);
        recyclerPhoto.setLayoutManager(manager);
        adapter = new GridImageAdapter(PositionAcquisitionActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(Constans.THREE);
        recyclerPhoto.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(PositionAcquisitionActivity.this).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
        delectSelectPhotos();
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            initPopupView();
        }

    };

    /*拍照，图片*/
    private void initPopupView() {
        popupWindow = new CameraPhotoPopupWindow(this);
        popupWindow.setOnItemClickListener(new CameraPhotoPopupWindow.OnItemClickListener() {
            @Override
            public void onCaremaClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onCarema();
                PictureSelectorUtils.getInstance().onAcCamera(PositionAcquisitionActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.THREE, selectList);
            }

            @Override
            public void onPhotoClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onPhoto();
                PictureSelectorUtils.getInstance().onAcAlbum(PositionAcquisitionActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.THREE, Constans.FOUR,
                        true, ImageUtils.getPath(), selectList);
            }

            @Override
            public void onCancelClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(ll_root_position, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
    private void delectSelectPhotos() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(PositionAcquisitionActivity.this);
                } else {
                    showSuccessToast(getString(R.string.picture_jurisdiction));
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    showPhotos(data);
                    break;
            }
        }
    }

    /*展示图片*/
    private void showPhotos(Intent data) {
        // 图片选择结果回调
        selectList = PictureSelector.obtainMultipleResult(data);
        // 例如 LocalMedia 里面返回三种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
        if (selectList.size() <= 0) {
            return;
        }
        assert adapter != null;
        adapter.setList(selectList);
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog(getString(R.string.photo_processing));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                mImagesArrays = PictureSelectorUtils.compressionPhotos(PositionAcquisitionActivity.this, selectList, address);
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
        if (!TextUtils.isEmpty(mImagesArrays)) {
        }
    }

}
