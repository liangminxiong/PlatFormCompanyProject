package com.yuefeng.usercenter.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.updateapputils.UpdateManager;
import com.common.utils.AppUtils;
import com.common.utils.ResourcesUtils;
import com.yuefeng.commondemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*关于软件*/
public class AboutAppInfosActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_app_logo)
    ImageView ivAppLogo;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R.id.tv_app_updata)
    TextView tvAppUpdata;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_aboutappinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initVersion();
    }

    @SuppressLint("SetTextI18n")
    private void initVersion() {
        tvTitle.setText("版本信息");
        String versionName = AppUtils.getAppVersionName(this);
        tvAppVersion.setText(ResourcesUtils.getString(R.string.app_name) + versionName);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
    }

    //    检查版本更新
    private UpdateManager mUpdateManager;

    private void checkVersion() {
        mUpdateManager = new UpdateManager(AboutAppInfosActivity.this, true);
        mUpdateManager.checkVersion();
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick(R.id.tv_app_updata)
    public void onViewClicked() {
        checkVersion();
    }
}
