package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luoyingmm.R;
import com.luoyingmm.api.Api;
import com.luoyingmm.api.ApiConfig;
import com.luoyingmm.api.TtitCallback;
import com.luoyingmm.entity.LoginResponse;


import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.PictureSelectUtil;
import com.luoyingmm.util.StringUtils;

import java.util.HashMap;

//登录界面
public class LoginActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNoRegistered;
    private Button btn_login;
    private Button btn_registered;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvNoRegistered = findViewById(R.id.tv_registered);
        btn_login = findViewById(R.id.btn_login);
        btn_registered = findViewById(R.id.btn_registered);
    }

    @Override
    protected void initData() {
        btn_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
            }
        });

        //点击暂不登录
        tvNoRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showAlertDialog((Activity) LoginActivity.this, R.mipmap.jump, "提示", "为了更好的用户体验，建议登录账户",
                        "继续登录", "狠心拒绝", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {

                            }
                            @Override
                            public void clickNegative() {
                                //设置登录标记并跳转
                                saveSpFlag("temp");
                                StringUtils.username = "temp";
                                saveStringToSp("login_flag","right");
                                //清除栈
                                navigateToWithFlag(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                        });
            }
        });
        //点击登录按钮
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                login(account,password);
            }
        });



    }

    private void login(String account, String password) {
        if (StringUtils.isEmpty(account)){
            showToast("请输入用户名");
            return;
        }
        if (StringUtils.isEmpty(password)){
            showToast("请输入密码");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile",account);
        params.put("password",password);
        //根据OkHttp请求数据进行登录校验
        Api.config(ApiConfig.LOGIN,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 0){
                    saveSpFlag(account);
                    StringUtils.username = account;
                    saveStringToSp("username",account);
                    saveStringToSp("login_flag","right");
                    navigateToWithFlag(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showToastSync("登陆成功");
                }else {
                    showToastSync("账号或密码错误");
                }

            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("网络异常，请检查你的网络连接");
            }
        });
    }

    private void saveSpFlag(String username) {
        SharedPreferences loginFlag = getSharedPreferences("LoginFlag", MODE_PRIVATE);
        SharedPreferences.Editor edit = loginFlag.edit();
        edit.putString("loginKey",username);
        edit.apply();
    }
}