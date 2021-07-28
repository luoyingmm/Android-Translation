package com.luoyingmm.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.system.StructUtsname;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luoyingmm.R;
import com.luoyingmm.activity.WebActivity;
import com.luoyingmm.entity.TranslationData;
import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.PictureSelectUtil;
import com.luoyingmm.util.StringUtils;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;


import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.stream.Collectors;

public class HomeFragment extends BaseFragment {
    private MaterialSpinner spinner_1;
    private MaterialSpinner spinner_2;
    private EditText et_enter;
    private EditText et_content;
    private String sp_1,sp_2;
    private Button btn_collect;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private FrameLayout fragment_result;
    private ImageView iv_copy;

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
        frameLayout = mRootView.findViewById(R.id.frameLayout);
        iv_copy = mRootView.findViewById(R.id.iv_copy);
        recyclerView = mRootView.findViewById(R.id.recyclerview);
        fragment_result = mRootView.findViewById(R.id.fragment_result);
        banEditTextOnlyLine(et_enter);
        et_content.setKeyListener(null);

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

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_enter.setFocusable(true);
                et_enter.setFocusableInTouchMode(true);
                et_enter.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
                    insertDataBase(et_enter.getText().toString(),et_content.getText().toString());
                    CollectFragment.data.add(new TranslationData(et_enter.getText().toString(),et_content.getText().toString()));
                    if (!getStringFromSp("total").equals("11")) {
                        if ( duplicateRemoval()) {
                            Toast.makeText(getActivity(), R.string.toast_first_successful, Toast.LENGTH_SHORT).show();
                            CollectFragment.mAdapter.notifyItemChanged(CollectFragment.data.size());
                            saveStringToSp("total", getStringFromSp("total") + "1");
                        }else {
                            Toast.makeText(getActivity(), R.string.toast_failed_2, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if ( duplicateRemoval()) {
                            Toast.makeText(getActivity(), R.string.toast_first_successful, Toast.LENGTH_SHORT).show();
                            CollectFragment.mAdapter.notifyItemChanged(CollectFragment.data.size());
                        }else {
                            Toast.makeText(getActivity(), R.string.toast_failed_2, Toast.LENGTH_SHORT).show();
                        }
                    }

                    //去重，防止用户多次输入重复值
                    Log.e("Home", CollectFragment.data.toString() );
                }else {
                    Toast.makeText(getActivity(), R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
                if (CollectFragment.data.size() > 0){
                    CollectFragment.tv_collect.setVisibility(View.GONE);
                }else {
                    CollectFragment.tv_collect.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_content.getText().toString())) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    String ocrText = et_content.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("OcrText", ocrText);
                    clipboardManager.setPrimaryClip(mClipData);
                    Toast.makeText(getActivity(), R.string.copy_successful, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragment_result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(et_enter.getText().toString()) && !TextUtils.isEmpty(et_content.getText().toString())) {
                    DialogUtil.showAlertDialog((Activity) getActivity(), R.mipmap.jump, "跳转提示", "是否切换到详情界面？",
                            "确定", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
                                @Override
                                public void clickPositive() {
                                    String url = "https://translate.google.cn/?sl=en&tl=zh-CN&text=" + et_content.getText().toString() + "&op=translate";
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", url);
                                    navigateToWithBundle(WebActivity.class, bundle);
                                }

                                @Override
                                public void clickNegative() {

                                }
                            });
                }
                return true;
            }
        });

        fragment_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"true".equals(getStringFromSp("tip"))) {
                    if (!TextUtils.isEmpty(et_enter.getText().toString()) && !TextUtils.isEmpty(et_content.getText().toString())) {
                        Toast.makeText(getActivity(), "Tip:长按『显示的结果』框可以跳转到详情界面哦~", Toast.LENGTH_LONG).show();
                    }
                }
                saveStringToSp("tip","true");
            }
        });

    }

    //去重，防止用户多次输入重复值
    private boolean duplicateRemoval() {
        for (int i = 0; i < CollectFragment.data.size(); i++) {
            for (int j = 0; j < CollectFragment.data.size(); j++) {
                if(i != j && CollectFragment.data.get(i).getTranslation().equals(CollectFragment.data.get(j).getTranslation()) && CollectFragment.data.get(i).getResult().equals(CollectFragment.data.get(j).getResult()) ) {
                    CollectFragment.data.remove(CollectFragment.data.get(j));
                    return false;
                }
            }
        }
        return true;
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
                        }else if (translateErrorCode.getCode() == 108){
                            Toast.makeText(getActivity(), "注册时提供的应用ID无效，已经自动切换为默认ID，稍后可以在我的->设置界面更改", Toast.LENGTH_LONG).show();
                            YouDaoApplication.mAppKey = StringUtils.ID;
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

    @Override
    public void onPause() {
        super.onPause();
        if (getStringFromSp("sw_copy").equals("true")) {
            ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            String ocrText = et_content.getText().toString();
            ClipData mClipData = ClipData.newPlainText("OcrText", ocrText);
            clipboardManager.setPrimaryClip(mClipData);
        }
    }
}
