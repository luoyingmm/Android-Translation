package com.luoyingmm.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luoyingmm.R;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;

public class HomeFragment extends BaseFragment {
    private MaterialSpinner spinner_1;
    private MaterialSpinner spinner_2;
    private EditText et_enter;
    private EditText et_content;
    private String sp_1,sp_2;
    Handler handler;
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        spinner_1 = mRootView.findViewById(R.id.spinner_1);
        spinner_2 = mRootView.findViewById(R.id.spinner_2);
        et_enter = mRootView.findViewById(R.id.et_enter);
        et_content = mRootView.findViewById(R.id.et_content);
        banEditTextOnlyLine(et_enter);
    }

    @Override
    protected void initData() {
        spinner_1.setItems("英文","中文");
        spinner_2.setItems("中文","英文");
        sp_1 = "英文";
        sp_2 = "中文";
        spinner_1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_1 = (String) view.getItems().get(position);
                if ("中文".equals(view.getItems().get(position))){
                    spinner_2.setSelectedIndex(1);
                }else {
                    spinner_2.setSelectedIndex(0);
                }
            }
        });
        spinner_2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_2 = (String) view.getItems().get(position);
                if ("中文".equals(view.getItems().get(position))){
                   spinner_1.setSelectedIndex(0);
                }else {
                    spinner_1.setSelectedIndex(1);
                }
            }
        });


        et_enter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                    translation(sp_1, sp_2, et_enter.getText().toString());
                }else {
                    et_content.setText("");
                }
            }
        });
    };


    protected void translation(String str1, String str2, String msg) {
        Language langFrom = LanguageUtils.getLangByName(str1);
        Language langTo = LanguageUtils.getLangByName(str2);


        TranslateParameters tps = new TranslateParameters.Builder()
                .source("Android-Translation")
                .from(langFrom)
                .to(langTo)
                .build();

        Translator.getInstance(tps).lookup(msg, "requestId", new TranslateListener() {

            @Override
            public void onError(TranslateErrorCode translateErrorCode, String s) {

            }

            @Override
            public void onResult(Translate translate, String s, String s1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result =  translate.getTranslations().toString();
                        result = result.replace("[","");
                        result = result.replace("]","");
                        et_content.setText(result);

                    }
                });

            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

            }
        });
    }
    }
