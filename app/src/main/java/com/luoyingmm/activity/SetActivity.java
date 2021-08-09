package com.luoyingmm.activity;

import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.luoyingmm.R;
import com.luoyingmm.util.StringUtils;
import com.youdao.sdk.app.YouDaoApplication;

//设置界面
public class SetActivity extends BaseActivity {
    private Toolbar my_toolbar;
    //快捷复制的详情
    private ImageView iv_why;
    //打开程序自动弹出输入法的详情
    private ImageView iv_quick;
    //自动读取剪贴板的详情
    private ImageView iv_read;
    //快捷复制Switch
    private SwitchMaterial sw_copy;
    private EditText et_id;
    private Button btn_save;
    //打开程序自动弹出输入法的Switch
    private SwitchMaterial sw_quick;
    //自动读取剪贴板的Switch
    private SwitchMaterial sw_read;
    //详情翻译的Switch
    private SwitchMaterial sw_details;


    @Override
    protected int initLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        my_toolbar = findViewById(R.id.my_toolbar);
        iv_why = findViewById(R.id.iv_why);
        sw_copy = findViewById(R.id.sw_copy);
        et_id = findViewById(R.id.et_id);
        btn_save = findViewById(R.id.btn_save);
        sw_quick = findViewById(R.id.sw_quick);
        iv_quick = findViewById(R.id.iv_quick);
        iv_read = findViewById(R.id.iv_read);
        sw_read = findViewById(R.id.sw_read);
        sw_details = findViewById(R.id.sw_details);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initData() {
        //判断快捷复制的开关
        if (getStringFromSp("sw_copy").equals("true")){
            sw_copy.setChecked(true);
        }

        //打开程序自动弹出输入法的开关
        if (getStringFromSp("sw_quick").equals("true")){
            sw_quick.setChecked(true);
        }

        //自动读取剪贴板的开关
        if (getStringFromSp("sw_read").equals("true")){
            sw_read.setChecked(true);
        }

        //详细翻译的开关
        if (getStringFromSp("sw_details").equals("true")){
            sw_details.setChecked(true);
        }
        //toolbar的左上角的返回
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //快捷复制的提示
        iv_why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetActivity.this).setMessage("系统会自动复制翻译出来的内容").setNegativeButton("知道了",null).show();
            }
        });

        //打开程序自动弹出输入法的提示
        iv_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetActivity.this).setMessage("进入本软件后会自动进入输入状态").setNegativeButton("知道了",null).show();
            }
        });

        //自动读取剪贴板的提示
        iv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetActivity.this).setMessage("注意:此功能要与『一键输入』共同打开。进入本软件后自动读取剪贴板内容并进行翻译，但可能存在少许延迟，出现无法读取情况，请谨慎使用!").setNegativeButton("知道了",null).show();
            }
        });

        //存储快捷复制状况
        sw_copy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveStringToSp("sw_copy","true");
                }else {
                    saveStringToSp("sw_copy","false");
                }

            }
        });

        //存储程序自动弹出输入法状况
        sw_quick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveStringToSp("sw_quick","true");
                }else {
                    saveStringToSp("sw_quick","false");
                    sw_read.setChecked(false);
                }
            }
        });

        //存储自动读取剪贴板状况
        sw_read.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveStringToSp("sw_read","true");
                    sw_quick.setChecked(true);
                }else {
                    saveStringToSp("sw_read","false");
                }
            }
        });

        //存储详细翻译状况
        sw_details.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveStringToSp("sw_details","true");
                }else {
                    saveStringToSp("sw_details","false");
                }
            }
        });

        //更改应用ID
        if (getStringFromSp("translationId").equals("")){
            et_id.setHint("系统默认ID，请勿更改！");
        }else {
           et_id.setText(getStringFromSp("translationId"));
        }

        //存储ID更改
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_id.getText().toString().equals("")) {
                    saveStringToSp("translationId", et_id.getText().toString());
                    YouDaoApplication.mAppKey = getStringFromSp("translationId");
                    showToast("更改成功");
                    btn_save.setVisibility(View.INVISIBLE);
                }else {
                    et_id.setHint("系统默认ID，请勿更改！");
                    saveStringToSp("translationId", "");
                    btn_save.setVisibility(View.INVISIBLE);
                    YouDaoApplication.mAppKey = StringUtils.ID;
                }
                hintKbTwo();
                et_id.clearFocus();
            }
        });

        //应用ID输入框
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_save.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 此方法只是关闭软键盘
     */
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)SetActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && SetActivity.this.getCurrentFocus() != null) {
            if (SetActivity.this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(SetActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}