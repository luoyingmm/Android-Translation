package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
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
        if (getStringFromSp("login_flag").equals("right")){
            Log.e("logout", getStringFromSp("login_flag") );
            navigateTo(MainActivity.class);
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateTo(LoginActivity.class);
                    finish();
                }
            },1000);
        }

    }

    @Override
    protected void initData() {

    }
}