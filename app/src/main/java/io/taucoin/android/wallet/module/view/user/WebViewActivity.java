package io.taucoin.android.wallet.module.view.user;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mofei.tau.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.widget.ToolbarView;
import io.taucoin.foundation.util.StringUtil;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    ToolbarView toolBar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        progressBar.setMax(100);
        mTitle = getIntent().getStringExtra(TransmitKey.TITLE);
        String url = getIntent().getStringExtra(TransmitKey.URL);
        if (StringUtil.isNotEmpty(mTitle)) {
            toolBar.setTitle(mTitle);
        }

        if (StringUtil.isNotEmpty(url) && url.startsWith("http")) {
            initWebView();
            webView.loadUrl(url);
        }
    }

    private void initWebView() {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setProgress(newProgress);
                }
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtil.isEmpty(mTitle) && StringUtil.isNotEmpty(title)) {
                    toolBar.setTitle(title);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }
        });

        WebSettings webSettings = webView.getSettings();
        if (webSettings == null) return;
        webSettings.setJavaScriptEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());

        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}