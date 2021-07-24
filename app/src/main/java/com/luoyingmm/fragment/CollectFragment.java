package com.luoyingmm.fragment;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.luoyingmm.R;
import com.luoyingmm.adapter.RecyclerViewAdapter;
import com.luoyingmm.entity.TranslationData;

import java.util.ArrayList;
import java.util.List;

public class CollectFragment extends BaseFragment {

    RecyclerView recyclerView;
    public static RecyclerViewAdapter mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    public static List<TranslationData> data;
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
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "第" + position + "条数据", Toast.LENGTH_SHORT).show();
            }
        });



    }
}