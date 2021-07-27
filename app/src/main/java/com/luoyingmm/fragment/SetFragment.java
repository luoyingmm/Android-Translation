package com.luoyingmm.fragment;

import android.widget.TextView;

import com.luoyingmm.R;
import com.luoyingmm.util.StringUtils;


public class SetFragment extends BaseFragment {
    private TextView tv_title;

    public SetFragment() {
        // Required empty public constructor
    }


    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();

        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_set;
    }

    @Override
    protected void initView() {
        tv_title = mRootView.findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        if (StringUtils.isEmpty(getStringFromSp("username"))) {
            tv_title.setText("游客的设置");
        } else {
            tv_title.setText(getStringFromSp("username") + "的设置");
        }
    }
}