package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luoyingmm.R;
import com.luoyingmm.util.StringUtils;

public class WelcomeActivity extends BaseActivity {
    TextView tvAuthor;
    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        tvAuthor = findViewById(R.id.tv_author);
    }

    @Override
    protected void initData() {
        SharedPreferences loginFlag = getSharedPreferences("LoginFlag", MODE_PRIVATE);
        StringUtils.username = loginFlag.getString("loginKey","");
        //判断是否登录，如果登录就跳过cover和login界面
        if (!StringUtils.username.equals("")){
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
}