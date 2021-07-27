package com.luoyingmm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import androidx.appcompat.widget.Toolbar;

import com.luoyingmm.R;
import com.luoyingmm.jsbridge.BridgeWebView;



public class WebActivity extends BaseActivity{
    private BridgeWebView bridgeWebView;
    private String url;
    private Toolbar my_toolbar;
    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
         my_toolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        initWebView();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWebView(){
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl(url);
    }
}
