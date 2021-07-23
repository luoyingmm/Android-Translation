package com.luoyingmm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.luoyingmm.R;
import com.luoyingmm.util.StatusBarUtil;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        context = this;
        YouDaoApplication.init(this, "0f6e7821cecdcbda");
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

    public String translation(String str1, String str2, String msg) {
        Language langFrom = LanguageUtils.getLangByName(str1);
        Language langTo = LanguageUtils.getLangByName(str2);

        final String[] result = {""};
        TranslateParameters tps = new TranslateParameters.Builder()
                .source("Android-Translation")
                .from(langFrom)
                .to(langTo)
                .build();

        Translator.getInstance(tps).lookup(msg, "requestId", new TranslateListener() {

            @Override
            public void onError(TranslateErrorCode translateErrorCode, String s) {
                Toast.makeText(context, translateErrorCode.getCode(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(Translate translate, String s, String s1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            result[0] = translate.getTranslations().toString();
                            Toast.makeText(context, translate.getTranslations().toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "抱歉，我还在学习该语言中...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

            }
        });

        return result[0];
    }



}
