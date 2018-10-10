package com.yuefeng.features.ui.activity.position;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.common.base.codereview.BaseActivity;
import com.common.location.LocationHelper;
import com.common.location.MyLocationListener;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.ImageUtils;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StatusBarUtil;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.common.view.timeview.OptionsPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.event.SuccessProblemEvent;
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
public class PositionAcquisitionActivity extends BaseActivity {

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
    @BindView(R.id.ct_timer)
    Chronometer cvTimer;
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

    @BindDrawable(R.drawable.shape_bg_green)
    Drawable shape_bg_green;
    @BindDrawable(R.drawable.shape_bg_yellow)
    Drawable shape_bg_yellow;
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
    BitmapDescriptor beginImage = BitmapDescriptorFactory.fromResource(R.drawable.worker);
    BitmapDescriptor endImage = BitmapDescriptorFactory.fromResource(R.drawable.problem);
    private double latitude;
    private double longitude;
    private String address;

    private CameraPhotoPopupWindow popupWindow;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;

    private boolean isBeginOrStop = true;
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
        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        isBeginOrStop = true;
        btnBeginorstop.setBackground(shape_bg_green);
        initUI();

        initChronometer();
    }

    private void initChronometer() {
        cvTimer.setBase(SystemClock.elapsedRealtime());
        cvTimer.setFormat("%s");
    }


    private void initUI() {
        initRlType();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBeginorstop.getLayoutParams();
        params.bottomMargin = ((int) AppUtils.mScreenHeight * 2 / 5) - 30;
        btnBeginorstop.setLayoutParams(params);
    }

    private void initRlType() {
        ViewGroup.LayoutParams layoutParams = rlSelectType.getLayoutParams();
        layoutParams.height = (int) AppUtils.mScreenHeight * 2 / 5;
        rlSelectType.setLayoutParams(layoutParams);
    }

    private void initLLTimer() {
        ViewGroup.LayoutParams llTimerLayoutParams = ll_timer.getLayoutParams();
        llTimerLayoutParams.height = (int) AppUtils.mScreenHeight * 2 / 5;
        ll_timer.setLayoutParams(llTimerLayoutParams);
    }

//    private void initLLFinish() {
//        ViewGroup.LayoutParams llTimerLayoutParams = ll_finish.getLayoutParams();
//        llTimerLayoutParams.height = (int) AppUtils.mScreenHeight / 4;
//        ll_finish.setLayoutParams(llTimerLayoutParams);
//    }

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
        baiduMap = mapview.getMap();
        // 地图初始化
        mapview.showZoomControls(false);// 缩放控件是否显示

        // 开启定位图层
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化

        baiduMap.showMapPoi(true);

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
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        MapStatus ms = new MapStatus.Builder().target(latLngTemp)
                                .overlook(-20).zoom(14).build();
                        ooA = new MarkerOptions().icon(beginImage).zIndex(10);
                        ooA.position(latLngTemp);
                        mMarker = null;
                        mMarker = (Marker) (baiduMap.addOverlay(ooA));
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        PreferencesUtils.putString(PositionAcquisitionActivity.this, "Fengrun", "");
                        PreferencesUtils.putString(PositionAcquisitionActivity.this, "mAddress", address);
                    }
                } else {
                    requestPermissions();
                }

            }
        }, Constans.BDLOCATION_TIME);
        initLocation();
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
                LogUtils.d("getLocation== " + latLng + " ++ " + address + " ++ " + distance);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSuccessProblemEvent(SuccessProblemEvent event) {
        switch (event.getWhat()) {
            case Constans.CARRY_SUCESS:

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
    }

    @OnClick({R.id.tv_carryon, R.id.tv_finish, R.id.btn_beginorstop, R.id.tv_release, R.id.recyclerview_left, R.id.recyclerview_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_carryon://继续
                tv_carryon();
                break;
            case R.id.tv_finish://结束
                tv_finish();
                break;
            case R.id.btn_beginorstop:
                beginOrStop();
                break;
            case R.id.tv_release:
                tv_release();
                break;
            case R.id.recyclerview_left:
                showPopuwindow();
                break;
            case R.id.recyclerview_right:
                showPopuwindow();
                break;
        }
    }


    /*选择类型*/
    private void showPopuwindow() {

        getOptionData();
        initOptionPicker();//条件选择框
    }

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        pvOptions = new OptionsPickerView(PositionAcquisitionActivity.this);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String txtLeft = options1Items.get(options1);
                String txtRight = options2Items.get(options1).get(option2)
                        /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                recyclerviewLeft.setText(txtLeft);
                recyclerviewRight.setText(txtRight);
            }
        });
        pvOptions.setOPVhight(2, 5);
        pvOptions.setTitle("类型选择");
        pvOptions.setPicker(options1Items, options2Items, true);
        pvOptions.setCyclic(false, false, false);
        pvOptions.show();
    }

    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        //选项1
        options1Items.clear();
        options1Items.add("广东");
        options1Items.add("湖南");
        options1Items.add("广西");
        options1Items.add("浙江");

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items_02.add("株洲");
        options2Items_02.add("衡阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items_03.add("玉林");
        ArrayList<String> options2Items_04 = new ArrayList<>();
        options2Items_04.add("杭州");
        options2Items_04.add("温州");
        options2Items_04.add("宁波");
        options2Items_04.add("嘉兴");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
        options2Items.add(options2Items_04);

        /*--------数据源添加完毕---------*/
    }

    /*发布*/
    private void tv_release() {
        showSuccessToast("发布");
    }

    /*结束*/
    private void tv_finish() {
//        initLLFinish();
        ll_timer.setVisibility(View.INVISIBLE);
        ll_finish.setVisibility(View.VISIBLE);

        selectPhoto();
    }

    /*继续采集*/
    private void tv_carryon() {
        isBeginOrStop = false;
        isVisibleOrGone(isBeginOrStop);
        initCtStart();
    }

    /*开始采集/暂停*/
    private void beginOrStop() {
        btnBeginorstop.setBackground(shape_bg_yellow);
        btnBeginorstop.setText(R.string.stop);
        rlSelectType.setVisibility(View.INVISIBLE);
        ll_timer.setVisibility(View.VISIBLE);
        if (isBeginOrStop) {
            isBeginOrStop = false;
            initCtStart();
        } else {
            isBeginOrStop = true;
            initCtStop();
        }
        initLLTimer();
        isVisibleOrGone(isBeginOrStop);
    }

    /*计时器start*/
    private void initCtStart() {
        if (cvTimer != null)
            if (mRecordTime != 0) {
                cvTimer.setBase(cvTimer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
            } else {
                cvTimer.setBase(SystemClock.elapsedRealtime());
            }
        cvTimer.start();
    }

    /*计时器stop*/
    private void initCtStop() {
        if (cvTimer != null)
            cvTimer.stop();
        mRecordTime = SystemClock.elapsedRealtime();
    }

    private void isVisibleOrGone(boolean isCarryOnOrStop) {
        if (isCarryOnOrStop) {
            tvCarryon.setVisibility(View.VISIBLE);
            tvFinish.setVisibility(View.VISIBLE);
            btnBeginorstop.setVisibility(View.INVISIBLE);
        } else {
            tvCarryon.setVisibility(View.INVISIBLE);
            tvFinish.setVisibility(View.INVISIBLE);
            btnBeginorstop.setVisibility(View.VISIBLE);
        }

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
