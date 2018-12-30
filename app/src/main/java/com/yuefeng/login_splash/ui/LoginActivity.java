package com.yuefeng.login_splash.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.updateapputils.UpdateManager;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.ToastUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.TokenBean;
import com.yuefeng.login_splash.contract.LoginContract;
import com.yuefeng.login_splash.event.LoginEvent;
import com.yuefeng.login_splash.model.LoginDataBean;
import com.yuefeng.login_splash.presenter.LoginPresenter;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.ui.MainActivity;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


public class LoginActivity extends BaseActivity implements LoginContract.View ,RongIM.UserInfoProvider{


    @BindView(R.id.et_account)
    EditText accountEt;
    @BindView(R.id.et_password)
    EditText passwordEt;
    @BindView(R.id.btn_login)
    TextView loginBt;
    @BindView(R.id.btn_eye_type)
    ImageView btn_eye_type;
    @BindView(R.id.cb_pwd)
    CheckBox cb_pwd;

    private String passwords;
    private String userNames;
    private LoginPresenter loginPresenter;
    private LoginDataBean loginInfo;
    private boolean cheche_pwd = false;
    private boolean isRemberPwd;
    private boolean canSee = false;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Subscribe
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this, this);
        RongIMUtils.initUserInfoListener(this);
        initUI();
    }

    private void initUI() {
        try {
            userNames = PreferencesUtils.getString(LoginActivity.this, Constans.USERNAME, "");
            passwords = PreferencesUtils.getString(LoginActivity.this, Constans.USERPASSWORD, "");
            if (!TextUtils.isEmpty(userNames)) {
                accountEt.setText(userNames);
                accountEt.setSelection(accountEt.getText().length());
            }
            if (!TextUtils.isEmpty(passwords)) {
                passwordEt.setSelection(passwordEt.getText().length());
                passwordEt.setText(passwords);
            }
            isRemberPwd = PreferencesUtils.getBoolean(this, "cheche_pwd", false);
            cheche_pwd = isRemberPwd;
            cb_pwd.setChecked(cheche_pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEyeCloseAndOpen() {
        if (!canSee) {
            //如果是不能看到密码的情况下，
            btn_eye_type.setImageDrawable(getResources().getDrawable(R.drawable.visual));
            passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEt.setSelection(passwordEt.getText().toString().length());
            canSee = true;
        } else {
            //如果是能看到密码的状态下
            btn_eye_type.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
            passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordEt.setSelection(passwordEt.getText().toString().length());
            canSee = false;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        RxPermissions rxPermission = new RxPermissions(LoginActivity.this);
        //请求权限全部结果 Manifest.permission.CAMERA,
        rxPermission.request(
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
//                        if (!granted) {
//                            showSuccessToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
//                        }
                        //不管是否获取全部权限，进入主页面
                        checkVersion();
                    }
                });

    }

    private void remenberPwd() {
        if (!cheche_pwd) {
            cheche_pwd = true;
            PreferencesUtils.putBoolean(this, "cheche_pwd", true);
        } else {
            PreferencesUtils.putString(this, Constans.USERPASSWORD, "");
            PreferencesUtils.putBoolean(this, "cheche_pwd", false);
            cheche_pwd = false;
        }
        cb_pwd.setChecked(cheche_pwd);
    }


    @Override
    protected void setLisenter() {
    }

    @Override
    protected void widgetClick(View v) {
    }

    @OnClick({R.id.btn_login, R.id.cb_pwd, R.id.btn_eye_type, R.id.et_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                if (!LocationGpsUtils.isGpsOPen(LoginActivity.this)) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                    ToastUtils.showToast("请先开启GPS");
                    return;
                }
                userNames = accountEt.getText().toString().trim();
                passwords = passwordEt.getText().toString().trim();

                boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
                if (!networkConnected) {
                    showErrorToast("无网络，请检查网络设置");
                    return;
                }
                if (!userNames.isEmpty() && !passwords.isEmpty()) {
                    showLoadingDialog(getString(R.string.login));
                    loginPresenter.login(ApiService.LOGIN, userNames, passwords, Constans.ANDROID);
                } else {
                    showErrorToast(getString(R.string.usernamePwd_noNull));
                }
                break;
            case R.id.cb_pwd:
                remenberPwd();
                break;
            case R.id.btn_eye_type:
                btnEyeCloseAndOpen();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeLoginEvent(LoginEvent loginEvent) {
        dismissLoadingDialog();
        switch (loginEvent.getWhat()) {
            case Constans.LOGIN:
                if (loginEvent.getData() != null) {
                    loginInfo = (LoginDataBean) loginEvent.getData();
                }
                PreferencesUtils.putString(LoginActivity.this, Constans.USERNAME, loginInfo.getLoginid());
                PreferencesUtils.putString(LoginActivity.this, Constans.USERNAME_N, loginInfo.getUsername());
                if (cheche_pwd) {
                    PreferencesUtils.putString(LoginActivity.this, Constans.USERPASSWORD, loginInfo.getPassword());
                }
                PreferencesUtils.putBoolean(LoginActivity.this, Constans.HAVE_USER_DATAS, true);
                PreferencesUtils.putString(LoginActivity.this, Constans.ORGID, loginInfo.getOrgId());
                PreferencesUtils.putString(LoginActivity.this, Constans.ORGNAME, loginInfo.getRoleName());
                PreferencesUtils.putString(LoginActivity.this, Constans.TELNUM, loginInfo.getTelNum());
                PreferencesUtils.putString(LoginActivity.this, Constans.ID, loginInfo.getId());
                LogUtils.d("=============" + loginInfo.getId());
                String email = loginInfo.getEmail();
                PreferencesUtils.putString(LoginActivity.this, Constans.EMAIL, email);
                PreferencesUtils.putInt(LoginActivity.this, Constans.ISADMIN, loginInfo.getIsadmin());
                PreferencesUtils.putBoolean(LoginActivity.this, Constans.ISREG, loginInfo.isIsreg());
                String string = PreferencesUtils.getString(this, Constans.COOKIE_PREF);
                /*极光推送*/
//                String alias = MD5Utils.toString(string);
//                JPushManager.getInstance().setAliasAndTags(alias, "");

                if (email.equals("true")) {//个人
                    if (loginPresenter != null) {
                        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
                        if (!networkConnected) {
                            showErrorToast("无网络，请检查网络设置");
                            return;
                        }
                        loginPresenter.getToken(loginInfo.getId(), loginInfo.getUsername(),
                                "http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg");
                    }
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                break;
            case Constans.USERERROR:
                showSuccessToast("请检查账号密码,网络状态!");
                break;

            case Constans.RONGIM_SUCCESS:
                TokenBean tokenBean = (TokenBean) loginEvent.getData();
                String token = tokenBean.getData();
                PreferencesUtils.putString(LoginActivity.this, Constans.TOKEN, token);
                initRongIMToken(token);
                break;
            case Constans.RONGIM_ERROR:
                showSuccessToast("连接失败，请重试");
                break;
            case Constans.RONGIM_SUCCESS_NET:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public UserInfo getUserInfo(String s) {
        String userid = PreferencesUtils.getString(LoginActivity.this, Constans.ID, "");
        String name = PreferencesUtils.getString(LoginActivity.this, Constans.USERNAME_N, "");
        Uri parse = Uri.parse(Constans.USER_LOGO);
        UserInfo info = new UserInfo(userid, name, parse);
        RongIMUtils.refreshUserInfoCache(info);
        return info;
    }


    /*融云连接token*/
    private void initRongIMToken(String token) {
        String userId = PreferencesUtils.getString(LoginActivity.this, Constans.ID, "");
        String name = PreferencesUtils.getString(LoginActivity.this, Constans.USERNAME_N, "");
        String portraitUrl = "";
//        RongIMUtils.init(userId, name, portraitUrl);
        RongIMUtils.connectToken(LoginActivity.this,token,userId,name,portraitUrl);
    }


    //    检查版本更新
    private boolean HasCheckUpdate = false;
    private UpdateManager mUpdateManager;

    private void checkVersion() {
        if (!HasCheckUpdate) {
            mUpdateManager = new UpdateManager(LoginActivity.this, false);
            mUpdateManager.checkVersion();
            HasCheckUpdate = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (mUpdateManager != null) {
                mUpdateManager.isAndoird8();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

//        MyApplication.getInstance().setupLeakCanary();
        MyApplication.getInstance().getRefWatcher(this);
    }
}
