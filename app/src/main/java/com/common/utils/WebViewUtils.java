package com.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.common.event.CommonEvent;
import com.common.view.webview.H5JSInterface;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/27.
 */

public class WebViewUtils {

    public static WebViewUtils instance;
    private WebView webView;
    private Context context;
    private WebSettings webSettings;
    private H5JSInterface h5JSInterface;

    public H5JSInterface getH5JsInterface() {
        return h5JSInterface;
    }

    public WebViewUtils() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView(Context context, String url, final WebView webView, final ProgressBar progressBar) {
        this.context = context;
        this.webView = webView;
//        if (webSettings == null) {
//        }
        webSettings = webView.getSettings();
        WebChromeClient wvcc = new WebChromeClient();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

        //自适应屏幕
        webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // 加载Web地址
        // 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        h5JSInterface = new H5JSInterface(MyApplication.getContext());
        webView.addJavascriptInterface(h5JSInterface, "javaObject");
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {

                super.onPageFinished(webView, s);
            }
        });

        webView.setWebChromeClient(wvcc);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        };
        webView.setWebViewClient(wvc);

        webView.setWebChromeClient(new WebChromeClient() {
            //进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onHideCustomView() {
            }
        });
    }

    public void clearCache() {
        if (webView != null) {
            //清空所有Cookie
            CookieSyncManager.createInstance(context);  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
        }
    }

    //内部类
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            EventBus.getDefault().post(new CommonEvent(Constans.WEBSUCESS, url));
        }

    }


}
