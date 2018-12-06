package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.AnnouncementDetailContract;
import com.yuefeng.home.modle.AnnouncementDataMsgBean;
import com.yuefeng.home.presenter.AnnouncementDetailPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/*信息详情*/
@SuppressLint("Registered")
public class AnnouncementDetailInfosActivtiy extends BaseActivity implements AnnouncementDetailContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.cl_parent)
    ConstraintLayout clParent;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private AnnouncementDetailPresenter mPresenter;
    private String mReviewid;
    private AnnouncementDataMsgBean mMsgData;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_announcementdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initUI();
        mPresenter = new AnnouncementDetailPresenter(this, this);
        getDatas();
    }

    private void initUI() {
        tv_title.setText("公告详情");
        tv_back.setText(R.string.back);
    }


    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mMsgData = (AnnouncementDataMsgBean) bundle.get("msgData");
        if (mMsgData != null && mPresenter != null) {
            mReviewid = mMsgData.getId();
            getDataByNet(mReviewid);
            showUIData(mMsgData);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showUIData(AnnouncementDataMsgBean msgData) {
        String content = msgData.getContent();
        String subject = msgData.getSubject();
        tvTheme.setText("主题:" + StringUtils.isEntryStrWu(subject));
        tvContent.setText("内容:" + StringUtils.isEntryStrWu(content) + "\n时间:"
                + StringUtils.isEntryStrWu(msgData.getIssuedate()));
    }

    private void getDataByNet(String reviewid) {
        if (!TextUtils.isEmpty(reviewid)) {
//            ApiRetrofit.changeApiBaseUrl(NetworkUrl.ANDROID_TEST_SERVICE);
            mPresenter.getAnnouncementDetail(ApiService.GETDETAIL, reviewid);
        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.DETAIL_MSG_SUCCESS:
                break;
            case Constans.DETAIL_MSG_ERROR:
                break;

        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
