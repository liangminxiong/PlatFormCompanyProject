package com.yuefeng.contacts.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.contract.UserDetailInfosContract;
import com.yuefeng.contacts.modle.UserDeatailInfosBean;
import com.yuefeng.contacts.presenter.UserDetailInfosPresenter;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*个人详情*/
public class UserDetailInfosActivity extends BaseActivity implements UserDetailInfosContract.View {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_organ_name)
    TextView tvOrganName;
    @BindView(R.id.tv_project_name)
    TextView tvProjectName;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.iv_start_chat)
    ImageView ivStartChat;
    private UserDetailInfosPresenter mPresenter;
    private String mUsername;
    private String mOrganname;
    private String mOrgan;
    private String mProjectname;
    private String mTeamname;
    private String mTelNum;
    private String mUserid;
    private String mUseridTargedid;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_userdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        mPresenter = new UserDetailInfosPresenter(this, this);
    }

    private void getWhatGroupDatasByNet(String userId) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.findUserWithID(userId);
        }
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mUserid = (String) bundle.get(Constans.GROUPID);
        getWhatGroupDatasByNet(mUserid);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.USERDETAIL_SUCCESS://获取通讯录成功
                UserDeatailInfosBean.DataBean bean = (UserDeatailInfosBean.DataBean) event.getData();
                showUIDatas(bean);
                break;
            case Constans.USERDETAIL_ERROR:
                showSureGetAgainDataDialog("数据加载失败，是否重新加载?");
                break;
        }
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
        getWhatGroupDatasByNet(mUserid);
    }

    /**/
    private void showUIDatas(UserDeatailInfosBean.DataBean bean) {
        if (bean != null) {
            mUseridTargedid = StringUtils.isEntryStrNull(bean.getId());
            mUsername = StringUtils.isEntryStrWu(bean.getUsername());
            mOrganname = StringUtils.isEntryStrWu(bean.getOrganname());
            mOrgan = StringUtils.isEntryStrWu(bean.getOrgan());
            mProjectname = StringUtils.isEntryStrWu(bean.getProjectname());
            mTeamname = StringUtils.isEntryStrWu(bean.getTeamname());
            mTelNum = StringUtils.isEntryStrWu(bean.getTelNum());

            tvUserName.setText(mUsername);
            tvCompanyName.setText(mOrgan);
            tvOrganName.setText(mOrganname);
            tvProjectName.setText(mProjectname);
            tvClassName.setText(mTeamname);
            tvPhoneNumber.setText(mTelNum);
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
    }

//R.id.iv_find_track,
    @OnClick(R.id.iv_start_chat)
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_find_track:
//                break;
            case R.id.iv_start_chat:
                //发起聊天
                startChat();

                break;
        }
    }

    private void startChat() {
        RongIMUtils.startPrivateChat(UserDetailInfosActivity.this, mUseridTargedid, mUsername);
    }
}
