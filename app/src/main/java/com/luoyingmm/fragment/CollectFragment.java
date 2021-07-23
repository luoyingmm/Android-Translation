package com.luoyingmm.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.luoyingmm.R;

import java.util.ArrayList;

public class CollectFragment extends BaseFragment {

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

    }

    @Override
    protected void initData() {

    }
}