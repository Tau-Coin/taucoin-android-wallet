package io.taucoin.android.wallet.module.view.manage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
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
import io.taucoin.android.wallet.util.ActivityUtil;
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
    private String mUrl;

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
        mUrl = getIntent().getStringExtra(TransmitKey.URL);
        if (StringUtil.isNotEmpty(mTitle)) {
            toolBar.setTitle(mTitle);
        }

        if (StringUtil.isNotEmpty(mUrl) && mUrl.startsWith("http")) {
            initWebView();
            webView.loadUrl(mUrl);
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
                if(StringUtil.isSame(mUrl, url)){
                    view.loadUrl(url);
                }else{
                    ActivityUtil.openUri(WebViewActivity.this, url);
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }
        });

        // Build.VERSION.SDK_INT > 19
        // Fatal Exception: java.util.concurrent.TimeoutException
        // android.view.ThreadedRenderer.finalize() timed out after 10 seconds
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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