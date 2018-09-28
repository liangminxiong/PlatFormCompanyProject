package com.yuefeng.features.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.location.LocationClient;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.StatusBarUtil;
import com.common.view.webview.H5Control;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
/*作业考勤*/

public class WebH5ZuoyeKaoqinActivity extends BaseActivity implements H5Control {

    @BindView(R.id.lly_webview_root)
//            NestedScrollWebView
            WebView webView;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    private LocationClient mLocationClient;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_webh5zuoye;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        StatusBarUtil.setStatusBarColorAlpha(mActivity, coloeWhite, coloeWhite);
        requestPermissions();

    }

    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
                        }
                        initWebview();
                    }
                });
    }

    /*
     * 动态添加webview，解决oom
     * */
    @SuppressLint("JavascriptInterface")
    private void initWebview() {
        final String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
        final String userid = PreferencesUtils.getString(this, Constans.ID, "");

        mLocationClient = new LocationClient(getApplicationContext());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);//开启定位
        webView.addJavascriptInterface(WebH5ZuoyeKaoqinActivity.this, "javascript");

        webView.setWebChromeClient(new WebChromeClient() {
            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                result.cancel();
                return true;
            }

           /* // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebH5ZuoyeKaoqinActivity.this);
                b.setTitle("是否允许当前网页定位?");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }*/

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin,
                                                           final GeolocationPermissions.Callback callback) {

                AlertDialog.Builder b = new AlertDialog.Builder(WebH5ZuoyeKaoqinActivity.this);
                b.setTitle("是否允许当前网页定位?");
//                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
                        callback.invoke(origin, true, false);
                    }
                });
                b.setCancelable(false);
                b.create().show();

                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            // 设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                LogUtils.d("=========webwiew====22====" + url);
                if (url.contains("goback")) {
                    EventBus.getDefault().post(new CommonEvent(Constans.GOBACK, url));
                }
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.d("=========webwiew========" + url);
                super.onPageStarted(view, url, favicon);
            }

        });

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(ApiService.H5URL_DAKA + "userid=" + userid + "&pid=" + pid);
            }
        });
// webView.loadUrl("javascript:" + "window.alert('Js injection success')" );
        mLocationClient.start();
        mLocationClient.enableAssistantLocation(webView);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeJobCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.GOBACK:
                finish();
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
    public void H5ControlAndroidEvent(String url, Bundle bundle) {

    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (webView != null) {
//            webView.getH5JsInterface().unRegisterListener();
//        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
