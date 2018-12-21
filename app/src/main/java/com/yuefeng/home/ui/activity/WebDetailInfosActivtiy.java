package com.yuefeng.home.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.common.Model.FileBean;
import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.FileUtils2;
import com.common.utils.WebViewUtils;
import com.common.view.webview.H5Control;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yuefeng.commondemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;


/*web详情*/
public class WebDetailInfosActivtiy extends BaseActivity implements H5Control {

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    WebView webView;

    private String url;
    private String title;
    private WebViewUtils mWebViewUtils;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_webdetail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initWebURL();

    }

    private void initWebURL() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        url = (String) bundle.get("webUrl");
        title = (String) bundle.get("tiTle");
        setTitle(title);
        initWebview(url);
    }

    /*
     * 动态添加webview，解决oom
     * */
    private void initWebview(final String url) {
        mWebViewUtils = new WebViewUtils();

//        webView.post(new Runnable() {
//            @Override
//            public void run() {
        mWebViewUtils.initWebView(
                WebDetailInfosActivtiy.this, url, webView, progressBar);
        mWebViewUtils.getH5JsInterface().registerListener(this);

//            }
//        });

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent ecEvent) {
        switch (ecEvent.getWhat()) {
//            case Constans.WEBH5SUCESS:
//                url = (String) ecEvent.getData();
//                loadWebH5(url);
//                break;

            case Constans.WEBSUCESS:
                String url = (String) ecEvent.getData();
                loadFile(url);
                break;
        }
    }

    private void loadFile(final String url) {

//        FileDisplayActivity.show(WebDetailInfosActivtiy.this, url);
    }

    @Override
    public void H5ControlAndroidEvent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        WebDetailInfosActivtiy.this.startActivityForResult(intent, 1);
    }

    @Override
    public void H5ControlAndroidEvent(String url, Bundle bundle) {


    }

    //文件路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            try {
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String url = FileUtils2.getPath(WebDetailInfosActivtiy.this, uri);
                String url2 = url.trim();
                Log.d("文件路径--", url2 + "");
                UploadFile(url2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadFile(String url) {
        File file = new File(url);
        OkHttpUtils.post(ApiService.UPLOADFILE)
                .params("file", file)
                .execute(new StringCallback() {

                    private FileBean fileBean;

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        Log.d("tag", "upProgress: " + progress);

                    }

                    @Override
                    public void onSuccess(String s, okhttp3.Call call, Response response) {
                        Log.d("tag", "onSuccess: " + s);
//                        if (!TextUtils.isEmpty(s)) {
//                            fileBean = GsonTools.changeGsonToBean(s, FileBean.class);
//                        }
                        if (webView != null) {
//                            webView.loadUrl("javascript:alert(location.href)");
                            webView.loadUrl("javascript:echo('" + s + "')");
//                            webView.loadUrl("javascript:" + "echo('" + fileBean.getMessage() + "')");
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showErrorToast("文件选择失败，请重新选择文件");
                    }
                });
    }


    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView == null) {
            return false;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
//        if (webView != null) {
//            webView.reload();
//        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (webView != null) {
//            webView.reload();
//        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mWebViewUtils != null) {
            mWebViewUtils.getH5JsInterface().unRegisterListener();
            mWebViewUtils.clearCache();
        }
        super.onDestroy();
    }
}
