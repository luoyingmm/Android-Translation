package com.luoyingmm.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.luoyingmm.R;
import com.luoyingmm.util.StatusBarUtil;
import com.luoyingmm.util.StringUtils;


import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    public Context context;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        context = this;

        initView();
        initData();
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void navigateTo(Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    protected void saveStringToSp(String key,String val){
        SharedPreferences sp = getSharedPreferences(StringUtils.username, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,val);
        edit.apply();
    }
    protected String getStringFromSp(String key){
        SharedPreferences sp = getSharedPreferences(StringUtils.username, MODE_PRIVATE);
        return sp.getString(key,"");
    }


    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateToWithBundle(Class cls,Bundle bundle){
        Intent intent = new Intent(context,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void navigateToWithFlag(Class cls,int flag){
        Intent intent = new Intent(context, cls);
        intent.setFlags(flag);
        startActivity(intent);
    }

}
