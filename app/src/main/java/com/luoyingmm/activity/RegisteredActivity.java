package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luoyingmm.R;
import com.luoyingmm.api.Api;
import com.luoyingmm.api.ApiConfig;
import com.luoyingmm.api.TtitCallback;
import com.luoyingmm.entity.LoginResponse;
import com.luoyingmm.util.CodeUtils;
import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.StringUtils;

import java.util.BitSet;
import java.util.HashMap;

//注册界面
public class RegisteredActivity extends BaseActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_password_repeat;
    private EditText et_translation_id;
    private Button btn_registered;
    private Button btn_back;
    private boolean flag = true;
    private String codeStr;
    private CodeUtils codeUtils;
    private ImageView iv_verification;
    private EditText et_verification;
    @Override
    protected int initLayout() {
        return R.layout.activity_registered;
    }

    @Override
    protected void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_password_repeat = findViewById(R.id.et_password_repeat);
        btn_registered = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_back);
        et_translation_id = findViewById(R.id.et_translation_id);
        iv_verification = findViewById(R.id.iv_verification);
        et_verification = findViewById(R.id.et_verification);

        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        iv_verification.setImageBitmap(bitmap);

    }

    @Override
    protected void initData() {
        //点击切换验证码
        iv_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeUtils = CodeUtils.getInstance();
                Bitmap bitmap = codeUtils.createBitmap();
                iv_verification.setImageBitmap(bitmap);
            }
        });
        //返回按钮
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //注册按钮
        btn_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String passwordRepeat = et_password_repeat.getText().toString().trim();
                register(account,password,passwordRepeat);
            }
        });

        //有道智云的输入
        et_translation_id.setFocusable(false);//让EditText失去焦点，然后获取点击事件
        et_translation_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    DialogUtil.showAlertDialog((Activity) RegisteredActivity.this, R.mipmap.jump, "关于ID", "为了防止同一api请求过多，可以去免费注册一个应用ID，只需要选择文本翻译(如果你不明白请勿随意填写)",
                            "前往注册", "否", true, new DialogUtil.AlertDialogBtnClickListener() {
                                @Override
                                public void clickPositive() {
                                    Uri uri = Uri.parse("https://ai.youdao.com/doc.s#guide");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void clickNegative() {
                                }
                            });
                    flag = false;
                }else {
                    et_translation_id.setFocusable(true);
                    et_translation_id.setFocusableInTouchMode(true);
                    et_translation_id.requestFocus();
                    et_translation_id.findFocus();
                }
                }

        });
    }

    private void register(String account, String password,String passwordRepeat) {
        if (StringUtils.isEmpty(account)){
            showToast("请输入用户名");
            return;
        }
        if (StringUtils.isEmpty(password)){
            showToast("请输入密码");
            return;
        }

        if (account.length() < 3){
            showToast("用户名不能少于3个字符");
            return;
        }
        if (account.length() > 10){
            showToast("用户名不能多于10个字符");
            return;
        }


        if (password.length() < 6){
            showToast("密码不能少于6个字符");
            return;
        }
        if (password.length() > 16){
            showToast("密码不能多于16个字符");
            return;
        }

        if (!password.equals(passwordRepeat)){
            showToast("重复密码不一致");
            return;
        }


        if (!codeUtils.getCode().equals(et_verification.getText().toString())){
            showToast("验证码错误");
            return;
        }

        //请求OkHttp
        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile",account);
        params.put("password",password);
        Api.config(ApiConfig.REGISTER,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 500){
                    showToastSync("账号已存在，请勿多次注册");
                }
                if (loginResponse.getCode() == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showAlertDialog((Activity) RegisteredActivity.this, R.mipmap.jump, "注册成功", "是否返回登录界面",
                                    "是", "否", true, new DialogUtil.AlertDialogBtnClickListener() {
                                        @Override
                                        public void clickPositive() {
                                            StringUtils.username = account;
                                            if (!StringUtils.isEmpty(et_translation_id.getText().toString())){
                                                saveSpFlag(account);
                                            }
                                            navigateTo(LoginActivity.class);
                                            finish();
                                        }
                                        @Override
                                        public void clickNegative() {
                                            StringUtils.username = account;
                                            if (!StringUtils.isEmpty(et_translation_id.getText().toString())){
                                                saveSpFlag(account);
                                            }
                                        }
                                    });
                        }
                    });

                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("网络异常，请检查你的网络连接");
            }
        });
    }

    private void saveSpFlag(String username) {
        SharedPreferences loginFlag = getSharedPreferences(username, MODE_PRIVATE);
        SharedPreferences.Editor edit = loginFlag.edit();
        edit.putString("translationId",et_translation_id.getText().toString());
        edit.apply();
    }
}