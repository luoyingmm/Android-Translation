package com.luoyingmm.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

//首页
public class HomeFragment extends BaseFragment {
    //左侧下拉栏
    private MaterialSpinner spinner_1;
    //右侧下拉栏
    private MaterialSpinner spinner_2;
    private EditText et_enter;
    private EditText et_content;
    //存储左右侧下拉栏数据
    private String sp_1,sp_2;
    private Button btn_collect;
    private FrameLayout frameLayout;
    private FrameLayout fragment_result;
    private ImageView iv_copy;

    public HomeFragment() {

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

        //首页左侧的下拉框
        spinner_1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_1 = (String) view.getItems().get(position);
                if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                    translation(sp_1, sp_2, et_enter.getText().toString());
                }
            }
        });
        //首页右侧侧的下拉框
        spinner_2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sp_2 = (String) view.getItems().get(position);
                if (!TextUtils.isEmpty(et_enter.getText().toString())) {
                    translation(sp_1, sp_2, et_enter.getText().toString());
                }
            }
        });

        //读取的上次关闭的下拉框选项
        if (!StringUtils.isEmpty(getStringFromSp("spinner_1")) && !StringUtils.isEmpty(getStringFromSp("spinner_1"))) {
            spinner_1.setSelectedIndex(Integer.parseInt(getStringFromSp("spinner_1")));
            sp_1 = (String) spinner_1.getItems().get(Integer.parseInt(getStringFromSp("spinner_1")));
            spinner_2.setSelectedIndex(Integer.parseInt(getStringFromSp("spinner_2")));
            sp_2 = (String) spinner_2.getItems().get(Integer.parseInt(getStringFromSp("spinner_2")));
        }

        //点击输入区域让输入框获取焦点
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

        //监听输入框
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

        //判断是否打开设置“打开程序自动弹出输入法”
        if (getStringFromSp("sw_quick").equals("true")){
            et_enter.requestFocus();
        }

        //判断设置是否打开“自动读取剪贴板”
        if (getStringFromSp("sw_read").equals("true")){
            spinner_1.setSelectedIndex(0);
            sp_1 = "自动";
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    et_enter.setText(getClipboardContent());
                }
            },100);
        }

        //收藏翻译内容，并插入数据库，用在收藏界面展示
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //非空就插入
                if (!TextUtils.isEmpty(et_content.getText().toString()) || !TextUtils.isEmpty(et_enter.getText().toString()) ) {
                    insertDataBase(et_enter.getText().toString(),et_content.getText().toString());
                    CollectFragment.data.add(new TranslationData(et_enter.getText().toString(),et_content.getText().toString()));
                    //前两次收藏显示“收藏成功，可前往下方收藏界面查看~”，之后显示"收藏成功".同时显示时间前者更长
                    if (!getStringFromSp("total").equals("11")) {
                        //去重，防止用户多次输入重复值
                        if (duplicateRemoval()) {
                            Toast.makeText(getActivity(), R.string.toast_first_successful, Toast.LENGTH_LONG).show();
                            CollectFragment.mAdapter.notifyItemChanged(CollectFragment.data.size());
                            saveStringToSp("total", getStringFromSp("total") + "1");
                        }else {
                            Toast.makeText(getActivity(), R.string.toast_failed_2, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if ( duplicateRemoval()) {
                            Toast.makeText(getActivity(), R.string.toast_successful, Toast.LENGTH_SHORT).show();
                            CollectFragment.mAdapter.notifyItemChanged(CollectFragment.data.size());
                        }else {
                            Toast.makeText(getActivity(), R.string.toast_failed_2, Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(getActivity(), R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
                //如果有数据就"去除收藏界面没有收藏单词的提示"，反之就显示
                if (CollectFragment.data.size() > 0){
                    CollectFragment.tv_collect.setVisibility(View.GONE);
                }else {
                    CollectFragment.tv_collect.setVisibility(View.VISIBLE);
                }
            }
        });

        //结果区域的复制按钮
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

        //长按结果区域跳转
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

        //如果用户点按了结果区，提示用户长按会跳转，只有初次有效
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


    //根据用户输入的内容发起okhttp请求，获取翻译结果，在结果区内显示
    protected void translation(String str1, String str2, String msg) {
        Language langFrom = LanguageUtils.getLangByName(str1);
        Language langTo = LanguageUtils.getLangByName(str2);
        Log.e("homeTest", langFrom +"->" + langTo );

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
                            Toast.makeText(getActivity(), "提供的应用ID无效，已经自动切换为默认ID，稍后可以在我的->设置界面更改", Toast.LENGTH_LONG).show();
                            saveStringToSp("translationId","");
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
                            if (getStringFromSp("sw_details").equals("true")) {
                                try {
                                    result = translate.getExplains().toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    result = translate.getTranslations().toString();
                                }
                            }else {
                                result = translate.getTranslations().toString();
                            }
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
        //如果设置打开“快捷复制”，会在程序退出后，自动复制结果区的内容
        if (getStringFromSp("sw_copy").equals("true")) {
            ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            String ocrText = et_content.getText().toString();
            ClipData mClipData = ClipData.newPlainText("OcrText", ocrText);
            clipboardManager.setPrimaryClip(mClipData);
        }

        //存储两个下拉框的内容
        saveStringToSp("spinner_1",spinner_1.getSelectedIndex()+"");
        saveStringToSp("spinner_2",spinner_2.getSelectedIndex()+"");
    }

    //读取剪贴板
    public String getClipboardContent () {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 返回数据
        ClipData clipData = clipboard.getPrimaryClip();
        if(clipData == null || clipData.getItemCount() <= 0){
            return "";
        }
        ClipData.Item item = clipData.getItemAt(0);
        if(item == null || item.getText() == null ){
            return "";
        }
        return item.getText().toString();
    }

}
