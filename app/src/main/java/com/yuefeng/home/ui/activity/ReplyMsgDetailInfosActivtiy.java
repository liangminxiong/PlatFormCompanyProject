package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.home.contract.ReplyContract;
import com.yuefeng.home.presenter.ReplyPresenter;
import com.yuefeng.photo.utils.PictureSelectorUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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

//        getDatas();
    }

    private void initUI() {
        mPresenter = new ReplyPresenter(this, this);
        tv_title.setText(R.string.reply_msg);
        tv_back.setText(R.string.back);
        ViewUtils.setEditHightOrWidth(edt_problem_txt, (int) AppUtils.mScreenHeight / 5,
                ActionBar.LayoutParams.MATCH_PARENT);

        PictureSelectorUtils.getInstance().initSselectPhoto(ReplyMsgDetailInfosActivtiy.this,
                clParent, recyclerview, mSelectList);
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
        int size = (int) bundle.get("tempPosition");
    }


    @Override
    protected void initData() {

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
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.PICSTURESUCESS:
                mSelectList = (List<LocalMedia>) event.getData();
                if (mSelectList.size() > 0) {
                    showFilesSize(mSelectList);
                }
                break;

        }
    }

    @OnClick({R.id.tv_back, R.id.tv_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_reply:
                showSuccessToast("回复");
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showFilesSize(List<LocalMedia> selectList) {
        tv_photo_big.setText("（已添加" + selectList.size() + "张照片,共" + PictureSelectorUtils.getFileSize(selectList) + "k,限传4张）");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    mSelectList = PictureSelectorUtils.getInstance().getActivityResult(data);
                    PictureSelectorUtils.getInstance().getImagesArrays(ReplyMsgDetailInfosActivtiy.this,
                            mSelectList, "");
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
