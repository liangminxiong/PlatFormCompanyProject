package com.yuefeng.home.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.utils.WebViewUtils;
import com.common.view.webview.H5Control;
import com.yuefeng.commondemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/*web详情*/
public class WebDetailInfosActivtiy extends BaseActivity implements H5Control {

    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.space)
//    View view;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    WebView webView;

    private String url = "https://www.baidu.com/";

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_webdetail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_title.setText(R.string.reply_msg);
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        initWebview();
    }

    /*
     * 动态添加webview，解决oom
     * */
    private void initWebview() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                WebViewUtils.getInstance().initWebView(
                        WebDetailInfosActivtiy.this, url, webView, progressBar);
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
    protected void onDestroy() {
        WebViewUtils.getInstance().clearCache();
        super.onDestroy();
    }
}
