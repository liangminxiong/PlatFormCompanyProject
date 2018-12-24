package com.yuefeng.home.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.home.contract.ReplyContract;
import com.yuefeng.home.presenter.ReplyPresenter;
import com.yuefeng.home.modle.ReplyContentBean;
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
import io.reactivex.functions.Consumer;


/*信息详情回复*/
@SuppressLint("Registered")
public class ReplyMsgDetailInfosActivtiy extends BaseActivity implements ReplyContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.cl_parent)
    ConstraintLayout clParent;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.edt_problem_txt)
    EditText edt_problem_txt;
    @BindView(R.id.tv_txt_count)
    TextView tv_txt_count;
    @BindView(R.id.tv_photo_big)
    TextView tv_photo_big;
    private ReplyPresenter mPresenter;
    private List<LocalMedia> mSelectList = new ArrayList<>();
    private String mCountEdit;
    private String mContent;
    private String mImagesArrays;
    private String mReviewid;
    private String mPersonal;
    private String mAddress;
    private boolean mFirstLocation;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_replymsgdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initUI();
        getDatas();
    }

    private void initUI() {
        mPresenter = new ReplyPresenter(this, this);
        tv_title.setText(R.string.reply_msg);
        tv_back.setText(R.string.back);
        ViewUtils.setEditHightOrWidth(edt_problem_txt, (int) AppUtils.mScreenHeight / 5,
                ActionBar.LayoutParams.MATCH_PARENT);

        PictureSelectorUtils.getInstance().initSselectPhoto(ReplyMsgDetailInfosActivtiy.this,
                clParent, recyclerview, mSelectList, Constans.IMAGES_SIX);
        initEditText();
    }

    @SuppressLint("SetTextI18n")
    private void initEditText() {
        if (edt_problem_txt != null) {
            edt_problem_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int length = s.length();
                    if (length >= 0) {
                        tv_txt_count.setText("还可以输入" + String.valueOf(100 - length) + "字");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mReviewid = (String) bundle.get("reViewId");
        mPersonal = (String) bundle.get("mPersonal");
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
        try {
            RxPermissions rxPermission = new RxPermissions(this);
            rxPermission.request(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
//                            if (!granted) {
//                                showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
//                            }
                            getLocation();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {
        try {
            BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
                @Override
                public void myLocation(BDLocation location) {
                    if (location == null) {
                        requestPermissions();
                        return;
                    }
                    mAddress = location.getAddrStr();
                    if (mFirstLocation) {
                        if (!TextUtils.isEmpty(mAddress) && mAddress.contains(getString(R.string.CHINA))) {
                            int length = mAddress.length();
                            mAddress = mAddress.substring(2, length);
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            mFirstLocation = false;
                            PreferencesUtils.putString(ReplyMsgDetailInfosActivtiy.this, Constans.ADDRESS, mAddress);
                        }
                    }
                }
//            }
            }, Constans.BDLOCATION_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeProblemEvent(ProblemEvent event) {
        switch (event.getWhat()) {
            case Constans.MSG_REPLY_SSUCESS:
                ReplyContentBean.ResultBean data = (ReplyContentBean.ResultBean) event.getData();
                String imageurls = data.getImageurls();
                EventBus.getDefault().postSticky(new CommonEvent(Constans.ONE_MSG_REPLY, mReviewid));
                showSuccessDialog("回复成功，是否退出当前界面!");
                break;
            case Constans.MSG_REPLY_ERROR:
                showSuccessToast("回复失败，请重试");
                break;
            case Constans.PICSTURESUCESS:
                mSelectList = (List<LocalMedia>) event.getData();
                if (mSelectList.size() > 0) {
                    showFilesSize(mSelectList);
                }
                break;

        }
    }

    @OnClick({R.id.tv_back, R.id.iv_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_reply:
                uploadReply();
                break;
        }
    }


    private void uploadReply() {
        if (mPresenter != null) {
            String content = edt_problem_txt.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                showSuccessToast("请填写回复内容");
                return;
            }
            if (TextUtils.isEmpty(mImagesArrays)) {
                showSuccessToast("请至少选择一张图片");
                return;
            }

            String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
            if (TextUtils.isEmpty(mPersonal)) {
                mPersonal = PreferencesUtils.getString(this, Constans.USERNAME, "");
            }
            mPresenter.doReview(pid, mReviewid, mPersonal, content, mImagesArrays);

        }
    }


    @SuppressLint("SetTextI18n")
    private void showFilesSize(List<LocalMedia> selectList) {
        tv_photo_big.setText("（已添加" + selectList.size() + "张照片,共" + PictureSelectorUtils.getFileSize(selectList) + "k,限传" + Constans.IMAGES_SIX + "张）");
    }


//    PreferencesUtils.putString(MainActivity.this, Constans.ADDRESS, address);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    mSelectList = PictureSelectorUtils.getInstance().getActivityResult(data);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingDialog(getString(R.string.photo_processing));
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String string = PreferencesUtils.getString(AppUtils.getContext(), Constans.ADDRESS, "");
                            mImagesArrays = PictureSelectorUtils.compressionPhotos(ReplyMsgDetailInfosActivtiy.this, mSelectList, string);
                        }
                    }).start();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                        }
                    });
                    showFilesSize(mSelectList);
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
