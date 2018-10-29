package com.yuefeng.usercenter.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.AppManager;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.yuefeng.commondemo.R;
import com.yuefeng.login_splash.ui.LoginActivity;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.usercenter.contract.ChangePwdContract;
import com.yuefeng.usercenter.event.ChangePwdEvent;
import com.yuefeng.usercenter.presenter.ChangePwdPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*修改密码*/
public class ChangePassWordActivity extends BaseActivity implements ChangePwdContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_visible_gone)
    TextView tvVisiblGone;
    @BindView(R.id.edt_oldpwd)
    EditText edtOldpwd;
    @BindView(R.id.edt_newpwd)
    EditText edtNewpwd;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.cb_show_pwd)
    CheckBox cbShowPwd;

    private ChangePwdPresenter presenter;
    private boolean isCheckedPwd = true;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_changepwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new ChangePwdPresenter(this, this);
        initUI();
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        tvTitle.setText(R.string.change_pwd);

        tvVisiblGone.setText("显示密码");
        cbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //do something
                    tvVisiblGone.setText("隐藏密码");
                    isCheckedPwd = false;
                } else {
                    //do something else
                    isCheckedPwd = true;
                    tvVisiblGone.setText("显示密码");
                }
                isShowPassWord(isCheckedPwd);
            }
        });
        cbShowPwd.setChecked(isCheckedPwd);
    }

    private void isShowPassWord(boolean isChecked) {
        if (isChecked) {// 显示密码
            edtOldpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtOldpwd.setSelection(edtOldpwd.getText().toString().length());
            edtNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtNewpwd.setSelection(edtNewpwd.getText().toString().length());
        } else {// 隐藏密码
            edtOldpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtOldpwd.setSelection(edtOldpwd.getText().toString().length());
            edtNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtNewpwd.setSelection(edtNewpwd.getText().toString().length());
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
    }


    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeChangePwdEvent(ChangePwdEvent event) {
        switch (event.getWhat()) {
            case Constans.CHANGEPWD_SSUCESS:
                outOfLogin();
                break;
            default:
                break;
        }
    }

    private void outOfLogin() {

        final SucessCacheSureDialog dialog = new SucessCacheSureDialog(this);
        dialog.setTextContent("修改密码成功");
        dialog.setCancleGone();
        dialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                dialog.dismiss();
                PreferencesUtils.putBoolean(MyApplication.getContext(), Constans.HAVE_USER_DATAS, false);
                PreferencesUtils.putString(MyApplication.getContext(), Constans.COOKIE_PREF, "");
                startActivity(new Intent(ChangePassWordActivity.this, LoginActivity.class));
                AppManager.getAppManager().removedAlllActivity(ChangePassWordActivity.this);
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                changePassWord();
                break;
        }
    }


    private void changePassWord() {
        String oldPwd = edtOldpwd.getText().toString().trim();
        String newPwd = edtNewpwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            showSuccessToast("请填写旧密码");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            showSuccessToast("请填写新密码");
            return;
        }

        String userid = PreferencesUtils.getString(ChangePassWordActivity.this, Constans.ID);
        if (presenter != null && !TextUtils.isEmpty(userid)) {
            presenter.changePwd(ApiService.UPDATEPASSWORD, userid, oldPwd, newPwd);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
