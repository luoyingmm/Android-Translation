package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;


import com.luoyingmm.R;

public class SetActivity extends BaseActivity {
    private Toolbar my_toolbar;
    private ImageView iv_why;
    private Switch sw_copy;

    @Override
    protected int initLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        my_toolbar = findViewById(R.id.my_toolbar);
        iv_why = findViewById(R.id.iv_why);
        sw_copy = findViewById(R.id.sw_copy);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initData() {
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}