package com.example.lucian.guardiannews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class DetailActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*GET URL FORM MAIN-ACTIVITY AND PUSH TO WEBVIEW*/
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.URL_TO_GO);

        mWebView = new WebView(this);
        mWebView.loadUrl(url);
        setContentView(mWebView);

    }
}
