package com.yuefeng.features.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.ImageUtils;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StatusBarUtil;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.EvaluationContract;
import com.yuefeng.features.event.SuccessProblemEvent;
import com.yuefeng.features.presenter.SuccessProblemPresenter;
import com.yuefeng.photo.adapter.GridImageAdapter;
import com.yuefeng.photo.other.FullyGridLayoutManager;
import com.yuefeng.photo.utils.PictureSelectorUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/*完成问题*/
public class SuccessProblemActivity extends BaseActivity implements EvaluationContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_upload)
    TextView tv_upload;


    private CameraPhotoPopupWindow popupWindow;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private String mImages;
    private String problemid;
    private AlertDialog alertDilaog;
    private SuccessProblemPresenter presenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_successproblem;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new SuccessProblemPresenter(this, this);

        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText(R.string.uploaded_event);
        tv_upload.setText(R.string.uploaded);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            problemid = (String) bundle.get("PROBLEMID");
        }
        selectPhoto();
        PreferencesUtils.putString(this, "Fengrun", "");
    }


    @Override
    public void initData() {
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    private void selectPhoto() {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(SuccessProblemActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        adapter = new GridImageAdapter(SuccessProblemActivity.this, onAddPicClickListener);
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
                            PictureSelector.create(SuccessProblemActivity.this).externalPicturePreview(position, selectList);
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

    private void initPopupView() {
        popupWindow = new CameraPhotoPopupWindow(this);
        popupWindow.setOnItemClickListener(new CameraPhotoPopupWindow.OnItemClickListener() {
            @Override
            public void onCaremaClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onCarema();
                PictureSelectorUtils.getInstance().onAcCamera(SuccessProblemActivity.this,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, selectList);
            }

            @Override
            public void onPhotoClicked() {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
//                onPhoto();
                PictureSelectorUtils.getInstance().onAcAlbum(SuccessProblemActivity.this,
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

    private void delectSelectPhotos() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(SuccessProblemActivity.this);
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


    @OnClick(R.id.iv_back)
    public void btn_back() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.tv_upload)
    public void tv_upload() {
        if (TextUtils.isEmpty(mImages)) {
            showSuccessToast("请上传图片");
            return;
        }
        String userId = PreferencesUtils.getString(this, "id", "");
        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, problemid,
                "3", mImages, "", "", "");
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSuccessProblemEvent(SuccessProblemEvent event) {
        switch (event.getWhat()) {
            case Constans.CARRY_SUCESS:
                String msg = (String) event.getData();
                if (msg.contains(getString(R.string.submit_success))) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    showSuccessDialog("处理成功，退出当前界面?");
                } else {
                    showErrorToast("提交失败，请重试！");
                }
                break;
            default:
                showErrorToast("处理失败，请重试");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    StringBuffer stringBuffer = new StringBuffer();
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
        LogUtils.d(selectList.size() + "================" + selectList.get(0).getCompressPath());
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
                mImages = PictureSelectorUtils.compressionPhotos(SuccessProblemActivity.this, selectList, "");
            }
        }).start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
    }

}
