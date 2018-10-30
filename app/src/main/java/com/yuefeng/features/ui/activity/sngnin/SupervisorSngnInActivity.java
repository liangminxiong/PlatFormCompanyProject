package com.yuefeng.features.ui.activity.sngnin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.ImageUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.common.view.dialog.SubmitDialog;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.common.view.popuwindow.PersonalListPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.SupervisorSngnInContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.event.SupervisorSngnInEvent;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.presenter.SupervisorSngnInPresenter;
import com.yuefeng.photo.adapter.GridImageAdapter;
import com.yuefeng.photo.other.FullyGridLayoutManager;
import com.yuefeng.photo.utils.PictureSelectorUtils;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.DatasUtils;

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

/*主管打卡*/
public class SupervisorSngnInActivity extends BaseActivity implements SupervisorSngnInContract.View {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_setting)
    TextView tvTitleSetting;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_personal)
    TextView tvPersonal;
    @BindView(R.id.ll_personal)
    LinearLayout llPersonal;
    @BindView(R.id.tv_photo_big)
    TextView tvPhotoBig;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.edt_memo)
    TextInputEditText edtMemo;
    @BindView(R.id.ll_edt)
    LinearLayout llEdt;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    private double latitude;
    private double longitude;
    private String address;
    private boolean isFirstLocation = true;

    private CameraPhotoPopupWindow popupWindow;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private String mImagesArrays;
    private List<CarListInfosMsgBean> treeListData = new ArrayList<>();
    private List<Node> nodeList = new ArrayList<>();
    private SupervisorSngnInPresenter presenter;
    private PersonalListPopupWindow popupWindowTree;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_supervisorsngnin;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initUI();
        presenter = new SupervisorSngnInPresenter(this, this);
        isFirstLocation = true;
        requestPermissions();
        selectPhoto();
        getTreeListData();
    }

    /*车辆列表*/
    private void getTreeListData() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
            presenter.getCarListInfos(ApiService.LOADVEHICLELIST, pid, userid, "0");
        }
    }

    private void initUI() {
        tvTitle.setText("主管打卡");
        tvTitleSetting.setText("提交");
    }

    /*图片选择*/
    private void selectPhoto() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(SupervisorSngnInActivity.this,
                Constans.FOUR, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        adapter = new GridImageAdapter(SupervisorSngnInActivity.this, onAddPicClickListener);
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
                            PictureSelector.create(SupervisorSngnInActivity.this).externalPicturePreview(position, selectList);
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
                PictureSelectorUtils.getInstance().onAcCamera(SupervisorSngnInActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, selectList);
            }

            @Override
            public void onPhotoClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onPhoto();
                PictureSelectorUtils.getInstance().onAcAlbum(SupervisorSngnInActivity.this,
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
        popupWindow.showAtLocation(llParent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    PictureFileUtils.deleteCacheDirFile(SupervisorSngnInActivity.this);
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
                if (location == null) {requestPermissions();
                    return;
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = location.getAddrStr();

                if (!TextUtils.isEmpty(address)) {
                    int length = address.length();
                    address = address.substring(2, length);
                }
                if (isFirstLocation) {
                    isFirstLocation = false;
                    if (!TextUtils.isEmpty(address)) {
                        tvAddress.setText(address);
                    } else {
                        isFirstLocation = true;
                        tvAddress.setText("点击重新定位");
                    }
                }
            }
        }, Constans.BDLOCATION_TIME);
    }


    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    @Override
    protected void initData() {
        initTime();
    }

    private void initTime() {
        tvTime.setText(TimeUtils.getCurrentTime2());
        ViewUtils.setLLHightOrWidth(llEdt, (int) AppUtils.mScreenHeight / 5, ActionBar.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSupervisorSngnInEvent(SupervisorSngnInEvent event) {
        switch (event.getWhat()) {
            case Constans.CARLIST_SSUCESS://人员列表成功
                treeListData = (List<CarListInfosMsgBean>) event.getData();
                if (treeListData.size() > 0) {
                    showCarlistDatas(treeListData);
                }
                break;

            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }
    }


    /*展示数据*/
    private void showCarlistDatas(List<CarListInfosMsgBean> organs) {
        try {
        nodeList.clear();
        nodeList = DatasUtils.ReturnTreesDatas(organs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
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
        tvPhotoBig.setText("（已添加" + selectList.size() + "张照片,共" + PictureSelectorUtils.getFileSize(selectList) + "k,限传4张）");
    }

    @OnClick({R.id.tv_title_setting, R.id.tv_address, R.id.ll_personal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_setting:
                showSubmitSucessDialog();
                break;
            case R.id.tv_address:
                if (isFirstLocation) {
                    requestPermissions();
                }
                break;
            case R.id.ll_personal:
//                showSuccessToast("人员选择");
                initTreeListPopupView();
                break;
        }
    }

    /*提交成功*/
    private void showSubmitSucessDialog() {
        try{
        SubmitDialog submitDialog = new SubmitDialog(this);
        submitDialog.setTextContent("您已打卡成功!\n是否退出该界面");
        submitDialog.setDeletaCacheListener(new SubmitDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                finish();
            }

            @Override
            public void cancle() {

            }
        });
        submitDialog.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    /*人员列表*/
    private void initTreeListPopupView() {
        try {

        popupWindowTree = new PersonalListPopupWindow(this, nodeList);
        popupWindowTree.setTitleText("选择人员");
        popupWindowTree.setSettingText("提交");
        popupWindowTree.setOnItemClickListener(new PersonalListPopupWindow.OnItemClickListener() {
            @Override
            public void onGoBack() {
                popupWindowTree.dismiss();
            }


            @Override
            public void onSure(String listName) {
                popupWindowTree.dismiss();
                if (!TextUtils.isEmpty(listName)) {
                    tvPersonal.setText(listName);
                }
            }

            @Override
            public void onSelectCar(String carNumber, String terminal) {
                if (!TextUtils.isEmpty(carNumber)) {
                    tvPersonal.setText(carNumber);
                }
            }
        });

        popupWindowTree.showAtLocation(llParent, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
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
        showFilesSize(selectList);
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
                mImagesArrays = PictureSelectorUtils.compressionPhotos(SupervisorSngnInActivity.this, selectList, address);
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
