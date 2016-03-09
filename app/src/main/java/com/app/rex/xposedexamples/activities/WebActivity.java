package com.app.rex.xposedexamples.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.rex.xposedexamples.R;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "WebActivity_EXTRA_URL";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web);


        mWebView = (WebView) findViewById(R.id.webview);


        mWebView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

//        mWebView.loadUrl("http://developer.android.com/");
        mWebView.loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }
}
