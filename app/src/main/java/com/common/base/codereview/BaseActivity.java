package com.common.base.codereview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.RxLifeManager;
import com.common.utils.AppManager;
import com.common.utils.AppUtils;
import com.common.utils.StatusBarUtil;
import com.common.utils.ToastUtils;
import com.common.view.dialog.LoadingDialog;
import com.common.view.dialog.SucessCacheSureDialog;
import com.yuefeng.commondemo.R;


/**
 * Created  on 2018-01-04.
 * author:seven
 * email:seven2016s@163.com
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public BaseActivity mActivity;
    public RelativeLayout iv_back;
    public TextView tv_title;
    private LoadingDialog loadingDialog;
    private boolean isOnclick = true;

    static {
        //5.0以下兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private SucessCacheSureDialog sureDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        mActivity = this;
        if (isNeedTranslateBar()) {
            StatusBarUtil.setTranslate(mActivity, true);
        }
        init(savedInstanceState);
        initData();
        setLisenter();
        AppManager.getAppManager().addActivity(this);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    protected boolean isNeedTranslateBar() {
        return true;
    }

    protected void init(Bundle savedInstanceState) {
        setContentView(getContentViewResId());
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_back.setOnClickListener(this);
        isOnclick = true;
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        initView(savedInstanceState);
    }

    public void showSuccessDialog(String txt) {
        try {
            if (sureDialog == null) {
                sureDialog = new SucessCacheSureDialog(this);
            }
            sureDialog.setTextContent(txt);
            sureDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
                @Override
                public void sure() {
                    sureDialog.dismiss();
                    finish();
                }

                @Override
                public void cancle() {
                    sureDialog.dismiss();
                }
            });

            if (!isFinishing()) {
                sureDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoadingDialog(String txt) {
        if (!mActivity.isFinishing()) {
            loadingDialog.setMessage(txt);
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && !mActivity.isFinishing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                AppUtils.hideKeyboard(ev, view, this);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected abstract int getContentViewResId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void setLisenter();

    protected abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (isOnclick) {
                    isOnclick = false;
                    finish();
                }
                break;
            default:
                widgetClick(v);
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        RxLifeManager.getRxLifeManager().onStopDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        RxLifeManager.getRxLifeManager().onDestroy();
    }

    /*
     * 统一处理toolbar的基本设置
     * */
    protected void initTitleBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setBackground(mActivity.getResources().getDrawable(R.drawable.home_toolbar_bg));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /*
     * 带图片的toast
     * */
    public void showSuccessToast(String msg) {
        ToastUtils.success(msg);
    }

    /*
     * error的toast
     * */
    public void showErrorToast(String msg) {
        ToastUtils.error(msg);
    }

    /*设置字体不随系统更改*/
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return resources;

    }

    /**
     * 跳转到其他Activity
     *
     * @param cls 目标Activity的Class
     */
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 延迟执行某个任务
     *
     * @param action Runnable对象
     */
    public boolean post(Runnable action) {
        return getWindow().getDecorView().post(action);
    }

    /**
     * 延迟某个时间执行某个任务
     *
     * @param action      Runnable对象
     * @param delayMillis 延迟的时间
     */
    public boolean postDelayed(Runnable action, long delayMillis) {
        return getWindow().getDecorView().postDelayed(action, delayMillis);
    }

    /**
     * 删除某个延迟任务
     *
     * @param action Runnable对象
     */
    public boolean removeCallbacks(Runnable action) {
        if (getWindow().getDecorView() != null) {
            return getWindow().getDecorView().removeCallbacks(action);
        } else {
            return true;
        }
    }

}
