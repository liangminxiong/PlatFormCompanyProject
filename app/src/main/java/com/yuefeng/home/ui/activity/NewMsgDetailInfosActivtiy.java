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
import com.yuefeng.home.contract.NewMsgDetailContract;
import com.yuefeng.home.presenter.NewMsgDetailPresenter;
import com.yuefeng.home.modle.NewMsgListDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/*最新详情*/
@SuppressLint("Registered")
public class NewMsgDetailInfosActivtiy extends BaseActivity implements NewMsgDetailContract.View {

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

    private NewMsgDetailPresenter mPresenter;
    private String mReviewid;
    private NewMsgListDataBean mMsgData;


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
        mPresenter = new NewMsgDetailPresenter(this, this);
        getDatas();
    }

    private void initUI() {
        tv_title.setText("消息详情");
        tv_back.setText(R.string.back);
    }


    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mMsgData = (NewMsgListDataBean) bundle.get("dataBean");
        if (mMsgData != null && mPresenter != null) {
            mReviewid = mMsgData.getId();
            getDataByNet(mReviewid);
            showUIData(mMsgData);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showUIData(NewMsgListDataBean msgData) {
        String title = "";
        String time = "";

        String genre = msgData.getGenre();
        if (genre.equals("1")) {//公告
            title = "[主题]";
        } else if (genre.equals("2")) { //项目信息
            title = "[项目]" + title;
        } else {//更新
            title = "[版本]" + title;
        }
        String content = msgData.getContent();
        String subject = msgData.getSubject();
        time = msgData.getIssuedate();
        tvTheme.setText(title + StringUtils.isEntryStrWu(subject));
        tvContent.setText("[内容]" + StringUtils.isEntryStrWu(content) + "\n时间: " + time);
    }

    private void getDataByNet(String reviewid) {
        if (!TextUtils.isEmpty(reviewid)) {
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
