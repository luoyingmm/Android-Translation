package com.luoyingmm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.luoyingmm.R;
import com.luoyingmm.activity.WebActivity;
import com.luoyingmm.adapter.RecyclerViewAdapter;
import com.luoyingmm.entity.TranslationData;
import com.luoyingmm.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class CollectFragment extends BaseFragment {

    RecyclerView recyclerView;
    public static RecyclerViewAdapter mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    public static List<TranslationData> data;
    public static TextView tv_collect;
    private ImageView iv_deleteAll;
    public CollectFragment() {
        // Required empty public constructor
    }


    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();

        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerview);
        tv_collect = mRootView.findViewById(R.id.tv_collect);
        iv_deleteAll = mRootView.findViewById(R.id.iv_deleteAll);
        data = new ArrayList<>();
    }

    @Override
    protected void initData() {
        Cursor cursor = db.rawQuery("select * from translationData", null);

        while (cursor.moveToNext()){
            data.add(new TranslationData(cursor.getString(cursor.getColumnIndex("translation")),cursor.getString(cursor.getColumnIndex("result"))));
        }
        cursor.close();

        //创建布局管理器，垂直设置LinearLayoutManager.VERTICAL，水平设置LinearLayoutManager.HORIZONTAL
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //创建适配器，将数据传递给适配器
        mAdapter = new RecyclerViewAdapter(data,getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        //设置适配器adapter
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, String str) {
                String url = "https://translate.google.cn/?sl=en&tl=zh-CN&text="+str+"&op=translate";
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                navigateToWithBundle(WebActivity.class,bundle);

            }
        });

        if (data.size() > 0){
            tv_collect.setVisibility(View.GONE);
        }else {
            tv_collect.setVisibility(View.VISIBLE);
        }

        iv_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setMessage("确定删除全部收藏？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.clear();
                        mAdapter.notifyDataSetChanged();
                        CollectFragment.tv_collect.setVisibility(View.VISIBLE);
                        db.execSQL("delete from translationData");
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
    }
}