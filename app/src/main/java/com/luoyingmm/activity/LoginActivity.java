package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class LoginActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNoRegistered;
    private Button btn_login;
    private Button btn_registered;
    private ImageView iv_verification;

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
        iv_verification = findViewById(R.id.iv_verification);

    }

    @Override
    protected void initData() {
        btn_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
            }
        });

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
                                saveStringToSp("login_flag","right");
                                navigateTo(MainActivity.class);
                                finish();
                            }
                        });
            }
        });
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
        Api.config(ApiConfig.LOGIN,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 0){
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
}