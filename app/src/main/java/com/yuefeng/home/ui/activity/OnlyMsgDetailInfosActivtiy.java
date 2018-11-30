package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.StringUtils;
import com.common.utils.ViewUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.contract.MsgDetailInfosContract;
import com.yuefeng.home.modle.MsgDataDetailListBean;
import com.yuefeng.home.modle.MsgListDataBean;
import com.yuefeng.home.presenter.MsgDetailInfosPresenter;
import com.yuefeng.photo.utils.ImageHelper;
import com.yuefeng.photo.view.MyGridView2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*信息详情*/
@SuppressLint("Registered")
public class OnlyMsgDetailInfosActivtiy extends BaseActivity implements MsgDetailInfosContract.View {

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
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.tv_reply_content)
    TextView tvReplyContent;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.gridview)
    MyGridView2 gridview;
    @BindView(R.id.iv_reply)
    ImageView ivReply;
    private String mContent;
    private int mInvisible;
    private MsgListDataBean mMsgData;
    private MsgDetailInfosPresenter mPresenter;
    private String mReviewid;
    private String mPersonal;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_onlymsgdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initUI();
        mPresenter = new MsgDetailInfosPresenter(this, this);
        getDatas();
    }

    private void initUI() {
        tv_title.setText(R.string.msg_detail);
        tv_back.setText(R.string.back);
    }

    private void setViewInVisible(boolean visible) {
        ViewUtils.setTvInVisible(tvMine, visible);
        ViewUtils.setTvInVisible(tvReplyContent, visible);
        ViewUtils.setTvInVisible(tvPhoto, visible);
        if (visible) {
            mInvisible = View.INVISIBLE;
        } else {
            mInvisible = View.VISIBLE;
        }
        gridview.setVisibility(mInvisible);
        ViewUtils.setIvVisible(ivReply, visible);
    }

    @SuppressLint("SetTextI18n")
    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mMsgData = (MsgListDataBean) bundle.get("msgData");
        if (mMsgData != null && mPresenter != null) {
            mReviewid = mMsgData.getReviewid();
            mPersonal = mMsgData.getReviewpersonel();
            tvTheme.setText("项目: " + mMsgData.getReviewtitle());
            tvContent.setText("内容: " + mMsgData.getReviewcontent() + "\n时间: " + com.yuefeng.utils.StringUtils.returnStrTime(mMsgData.getReviewdate()));
            getDataByNet(mReviewid);
        }
    }

    private void getDataByNet(String reviewid) {
        if (!TextUtils.isEmpty(reviewid)) {
            mPresenter.getMsgDetail(reviewid);
            mPresenter.getMsgDetail(ApiService.GETDETAIL, reviewid);
        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.MSG_DETAIL_SSUCESS:
                List<MsgDataDetailListBean> list = (List<MsgDataDetailListBean>) event.getData();
                if (list.size() > 0) {
                    showListData(list);
                }
                break;
            case Constans.MSG_DETAIL_ERROR:
                showSuccessToast("加载失败");
                break;
            case Constans.ONE_MSG_REPLY:
                String reviewId = (String) event.getData();
                getDataByNet(reviewId);
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    private void showListData(List<MsgDataDetailListBean> list) {
        int size = list.size();
        if (size < 1) {
            return;
        }
        String name = StringUtils.isEntryStrWu(list.get(0).getReviewtitle());
        String theme = StringUtils.isEntryStrWu(list.get(0).getReviewcontent());

        tvTheme.setText("项目: " + name);
        tvContent.setText("内容: " + theme + "\n时间: " + com.yuefeng.utils.StringUtils.returnStrTime(mMsgData.getReviewdate()));
        if (size == 1) {
            setViewInVisible(true);
        } else {
            String content = StringUtils.isEntryStrWu(list.get(1).getReviewcontent());
            String personal = StringUtils.isEntryStrWu(list.get(1).getReviewpersonel());
            String imageUrl = StringUtils.isEntryStrNull(list.get(1).getImageurls());
            setViewInVisible(false);
            tvMine.setText(personal + ": 回复");
            tvReplyContent.setText("内容: " + content);
            if (!TextUtils.isEmpty(imageUrl)) {
                ImageHelper.showImageBitmap(gridview, OnlyMsgDetailInfosActivtiy.this, imageUrl);
            } else {
                tvPhoto.setText("上传图片: 无");
            }
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


    @OnClick({R.id.tv_back, R.id.iv_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_reply:
                gotoReplyActivity();
                break;
        }
    }

    private void gotoReplyActivity() {
        Intent intent = new Intent();
        intent.setClass(OnlyMsgDetailInfosActivtiy.this, ReplyMsgDetailInfosActivtiy.class);
        intent.putExtra("reViewId", mReviewid);
        intent.putExtra("mPersonal", mPersonal);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
