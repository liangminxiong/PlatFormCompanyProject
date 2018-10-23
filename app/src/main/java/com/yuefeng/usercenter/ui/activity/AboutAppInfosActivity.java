package com.yuefeng.usercenter.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.updateapputils.UpdateManager;
import com.common.utils.AppUtils;
import com.common.utils.ResourcesUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

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
        String versionName = AppUtils.getAppVersionName(this);
        tvAppVersion.setText(ResourcesUtils.getString(R.string.app_name) + versionName);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        RxPermissions rxPermission = new RxPermissions(this);
        //请求权限全部结果 Manifest.permission.CAMERA,
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            showSuccessToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
                        }
                        //不管是否获取全部权限，进入主页面
                        checkVersion();
                    }
                });
    }

    //    检查版本更新
    private boolean HasCheckUpdate = false;
    private UpdateManager mUpdateManager;

    private void checkVersion() {
        if (!HasCheckUpdate) {
            mUpdateManager = new UpdateManager(AboutAppInfosActivity.this, true);
            mUpdateManager.checkVersion();
            HasCheckUpdate = true;
        }
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
