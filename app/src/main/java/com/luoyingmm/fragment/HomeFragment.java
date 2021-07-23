package com.luoyingmm.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
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
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;

public class HomeFragment extends BaseFragment {
    private MaterialSpinner spinner_1;
    private MaterialSpinner spinner_2;
    private EditText et_enter;
    private EditText et_content;
    private String sp_1,sp_2;
    private Button btn_collect;
    private RecyclerView recyclerView;
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
        btn_collect = mRootView.findViewById(R.id.btn_collect);
        banEditTextOnlyLine(et_enter);
    }

    @Override
    protected void initData() {
        spinner_1.setItems("自动","英文","中文","日文");
        spinner_2.setItems("中文","英文","日文");
        sp_1 = "自动";
        sp_2 = "中文";
        spinner_1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_1 = (String) view.getItems().get(position);
                if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                    translation(sp_1, sp_2, et_enter.getText().toString());
                }
            }
        });
        spinner_2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_2 = (String) view.getItems().get(position);
                if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                    translation(sp_1, sp_2, et_enter.getText().toString());
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

        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_content.getText().toString()) || !TextUtils.isEmpty(et_enter.getText().toString()) ) {
                    if (!getStringFromSp("total").equals("11")) {
                        Toast.makeText(getActivity(), R.string.toast_first_successful, Toast.LENGTH_SHORT).show();
                        saveStringToSp("total", getStringFromSp("total") + "1");
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_successful, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (translateErrorCode.getCode() == 1){
                            Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "连接失败，错误代码: " + translateErrorCode.getCode(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onResult(Translate translate, String s, String s1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                            result = translate.getTranslations().toString();
                            result = result.replace("[", "");
                            result = result.replace("]", "");
                            et_content.setText(result);
                        }else {
                            et_content.setText("");
                        }

                    }
                });

            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

            }
        });
    }
    }
