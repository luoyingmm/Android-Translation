package com.luoyingmm.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luoyingmm.R;
import com.luoyingmm.activity.MainActivity;
import com.luoyingmm.sql.DatabaseHelper;
import com.luoyingmm.util.StatusBarUtil;
import com.luoyingmm.util.StringUtils;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;


import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    String result = "";
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    ContentValues values;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(initLayout(),container,false);
            initView();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //判断用户是否填写过应用ID
        if (!getStringFromSp("translationId").equals("")){
            YouDaoApplication.init(mRootView.getContext(), getStringFromSp("translationId"));
            Log.e("transs", "1"+ getStringFromSp("translationId"));
        }else {
            YouDaoApplication.init(mRootView.getContext(), StringUtils.ID);
            Log.e("transs", "2"+ getStringFromSp("translationId"));
        }

        //设置上方通知栏的颜色为白色
        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
        //初始化存放翻译内容的数据库
         databaseHelper = new DatabaseHelper(getActivity(), "TranslationData.db", null, 1);
         db = databaseHelper.getWritableDatabase();
        initData();
    }

    //静止纯文本换行
    protected void banEditTextOnlyLine(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void afterTextChanged(Editable s) {
                for(int i = s.length(); i > 0; i--){
                    if(s.subSequence(i-1, i).toString().equals("\n"))
                        s.replace(i-1, i, "");
                }

            }
        });
    }

    //存储sp
    protected void saveStringToSp(String key,String val){
        SharedPreferences sp = getActivity().getSharedPreferences(StringUtils.username, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,val);
        edit.apply();
    }

    //获取Sp
    protected String getStringFromSp(String key){
        SharedPreferences sp = getActivity().getSharedPreferences(StringUtils.username, MODE_PRIVATE);
        return sp.getString(key,"");
    }

    //往数据库插入数据
    protected void insertDataBase(String translation,String result){
        values = new ContentValues();
        values.put("translation",translation);
        values.put("result", result);
        db.insert("translationData", null, values);
    }

    public void navigateToWithBundle(Class cls,Bundle bundle){
        Intent intent = new Intent(getActivity(),cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void navigateToWithFlag(Class cls, int flags) {
        Intent in = new Intent(getActivity(), cls);
        in.setFlags(flags);
        startActivity(in);
    }

    public void navigateTo(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }


    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();
}
