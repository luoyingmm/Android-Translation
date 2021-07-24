package com.luoyingmm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.luoyingmm.R;
import com.luoyingmm.jsbridge.BridgeWebView;


@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity{
    private BridgeWebView bridgeWebView;
    private String url;
    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        initWebView();
    }

    private void initWebView(){
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl(url);
    }
}
