package com.yuefeng.features.ui.activity.monitoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.ImageUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.ViewUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.MonitoringContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.presenter.monitoring.MonitoringSngnInPresenter;
import com.yuefeng.photo.adapter.GridImageAdapter;
import com.yuefeng.photo.other.FullyGridLayoutManager;
import com.yuefeng.photo.utils.PictureSelectorUtils;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/*问题上报*/
public class MonitoringSngnInActivity extends BaseActivity implements MonitoringContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_setting)
    TextView tv_title_setting;
    @BindView(R.id.tv_problem_address)
    TextView tv_problem_address;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.edt_problem_txt)
    EditText edt_problem_txt;
    @BindView(R.id.tv_txt_count)
    TextView tv_txt_count;
    @BindView(R.id.tv_photo_big)
    TextView tv_photo_big;
    @BindView(R.id.tv_personal)
    TextView tv_personal;


    private CameraPhotoPopupWindow popupWindow;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private double latitude;
    private double longitude;
    private String address;
    private String type = "0";
    private static final int BDLOCATION_TIME = 10000;
    //    private SysUser sysUser;
    private boolean isOnclick = true;
    private boolean isFirstLocation = true;
    private MonitoringSngnInPresenter presenter;
    private String mImagesArrays;
    private SucessCacheSureDialog sureDialog;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_monitoringsngnin;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new MonitoringSngnInPresenter(this, this);
        ViewUtils.setEditHightOrWidth(edt_problem_txt, (int) AppUtils.mScreenHeight / 5, ActionBar.LayoutParams.MATCH_PARENT);
        tv_title.setText(getString(R.string.problem_updata));
        tv_title_setting.setText(R.string.submit);
        selectPhoto();
        tv_problem_address.setText(R.string.locationing);
    }


    @Override
    public void initData() {
        requestPermissions();
        initEditText();
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

    private void getLocation() {

        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    requestPermissions();
                    return;
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                    tv_problem_address.setText(address);
                }else {
                    tv_problem_address.setText(R.string.locationing);
                }
            }
        }, Constans.BDLOCATION_TIME);
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
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();
                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                }
                if (isFirstLocation) {
                    isFirstLocation = false;
                    PreferencesUtils.putString(MonitoringSngnInActivity.this, "Fengrun", "");
                    PreferencesUtils.putString(MonitoringSngnInActivity.this, "mAddress", address);
                    if (!TextUtils.isEmpty(address)) {
                        tv_problem_address.setText(address);
                    } else {
                        isFirstLocation = true;
                    }
                }
//                }
            }
        }, BDLOCATION_TIME);
    }

    private void initEditText() {
        edt_problem_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length >= 0) {
                    EventBus.getDefault().post(new ProblemEvent(Constans.EQUIPMENTCOUNTSUCESS, String.valueOf(100 - length)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.EQUIPMENTCOUNTSUCESS:
                String count = (String) event.getData();
                tv_txt_count.setText("还可以输入" + count + "字");
                break;

            case Constans.UPLOADSUCESS:
                showSuccessDialog(getString(R.string.upload_success));
                break;
            case Constans.USERERROR:
                showErrorToast("上传失败，请重试!");
                break;
            case Constans.PICSTURESUCESS:
                selectList = (List<LocalMedia>) event.getData();
                if (selectList.size() > 0) {
                    showFilesSize(selectList);
                }
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    private void showFilesSize(List<LocalMedia> selectList) {
        tv_photo_big.setText("（已添加" + selectList.size() + "张照片,共" + PictureSelectorUtils.getFileSize(selectList) + "k,限传4张）");
    }

    /*图片选择*/
    private void selectPhoto() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(MonitoringSngnInActivity.this, Constans.FOUR, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        adapter = new GridImageAdapter(MonitoringSngnInActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(Constans.FOUR);
        recyclerview.setAdapter(adapter);
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
                            PictureSelector.create(MonitoringSngnInActivity.this).externalPicturePreview(position, selectList);
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
                PictureSelectorUtils.getInstance().onAcCamera(MonitoringSngnInActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, selectList);
            }

            @Override
            public void onPhotoClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onPhoto();
                PictureSelectorUtils.getInstance().onAcAlbum(MonitoringSngnInActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, Constans.FOUR,
                        true, ImageUtils.getPath(), selectList);
            }

            @Override
            public void onCancelClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    PictureFileUtils.deleteCacheDirFile(MonitoringSngnInActivity.this);
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


    /*紧急*/
    @OnClick({R.id.tv_personal, R.id.tv_problem_address, R.id.tv_title_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_problem_address:
                tv_problem_address.setText(R.string.locationing);
                requestPermissions();/*重新定位*/
                break;
            case R.id.tv_title_setting:
                showSuccessToast(getString(R.string.submit));
//                uploadProblem();
                break;
            case R.id.tv_personal:
                showSuccessToast(getString(R.string.personal));
//                uploadProblem();
                break;
        }
    }


    /*提交问题*/
    private void uploadProblem() {

        String problem = edt_problem_txt.getText().toString().trim();
        if (TextUtils.isEmpty(problem)) {
            showSuccessToast("请填写问题描述");
            return;
        }
        if (TextUtils.isEmpty(mImagesArrays)) {
            showSuccessToast("请选择图片上传");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            showSuccessToast("请确定位置");
            return;
        }

        LatLng latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        String pid = PreferencesUtils.getString(this, "orgId", "");
        String userid = PreferencesUtils.getString(this, "id", "");
//        presenter.uploadRubbishEvent(ApiService.UPLOADRUBBISHEVENT, userid, pid, problem, address,
//                String.valueOf(longitude), String.valueOf(latitude), type, mImagesArrays);

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
    @SuppressLint("SetTextI18n")
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
        tv_photo_big.setText("（已添加" + selectList.size() + "张照片,共"
                + PictureSelectorUtils.getFileSize(selectList) + "k,限传4张）");
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
                mImagesArrays = PictureSelectorUtils.compressionPhotos(MonitoringSngnInActivity.this, selectList, address);
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
//        if (!TextUtils.isEmpty(mImagesArrays)) {
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BdLocationUtil.getInstance().stopLocation();//停止定位
    }

}
