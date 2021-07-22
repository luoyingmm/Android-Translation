package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.luoyingmm.R;

public class WelcomeActivity extends BaseActivity {
    TextView tvAuthor;
    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        tvAuthor = findViewById(R.id.tv_author);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/welcomeFont.otf");
        tvAuthor.setTypeface(typeface);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateTo(MainActivity.class);
                finish();
            }
        },1000);
    }

    @Override
    protected void initData() {

    }
}