package com.luoyingmm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.luoyingmm.R;

public class SetActivity extends BaseActivity {
    private Toolbar my_toolbar;
    private ImageView iv_why;
    private SwitchMaterial sw_copy;

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
        if (getStringFromSp("sw_copy").equals("true")){
            sw_copy.setChecked(true);
        }
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetActivity.this, "系统会自动复制翻译出来的内容", Toast.LENGTH_LONG).show();
            }
        });

        sw_copy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveStringToSp("sw_copy","true");
                    System.out.println("true");
                }else {
                    saveStringToSp("sw_copy","false");
                }

            }
        });

    }
}