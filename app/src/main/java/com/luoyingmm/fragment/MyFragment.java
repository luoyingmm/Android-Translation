package com.luoyingmm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.luoyingmm.R;
import com.luoyingmm.activity.LoginActivity;
import com.luoyingmm.activity.MainActivity;
import com.luoyingmm.activity.WebActivity;
import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.PictureSelectUtil;
import com.luoyingmm.util.SPUtils;
import com.luoyingmm.util.StringUtils;


public class MyFragment extends BaseFragment {
    private TextView tv_title;
    private ImageView iv_img;
    private  RelativeLayout rl_collect;
    private RelativeLayout rl_set;
    private RelativeLayout rl_logout;

    public MyFragment() {
        // Required empty public constructor
    }


    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();

        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        tv_title = mRootView.findViewById(R.id.tv_title);
        iv_img = mRootView.findViewById(R.id.iv_img);
        rl_collect = mRootView.findViewById(R.id.rl_collect);
        rl_set = mRootView.findViewById(R.id.rl_set);
        rl_logout = mRootView.findViewById(R.id.rl_logout);
        Bitmap bitmap = SPUtils.getBitmapFromSharedPreferences(getActivity(), "data", "photo", "");
        if (!StringUtils.isEmpty(getStringFromSp("photoFlag"))){
            iv_img.setImageBitmap(bitmap);
        }else {
            iv_img.setImageResource(R.mipmap.change_head);
        }
    }

    @Override
    protected void initData() {
        if (StringUtils.isEmpty(getStringFromSp("username"))) {
            tv_title.setText("游客");
        } else {
            tv_title.setText(getStringFromSp("username"));
        }

        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.showAlertDialog(getActivity(), R.mipmap.jump, "更换头像", "请选择你的图片来源",
                        "相册", "相机", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                PictureSelectUtil.with((AppCompatActivity) getActivity())
                                        .gallery()
                                        .crop()
                                        .setCallback(new PictureSelectUtil.OnCallback() {
                                            @Override
                                            public void onCallback(Uri uri) {
                                                Glide.with(getActivity()).load(uri).into(iv_img);
                                            }
                                        }).select();
                                SPUtils.saveBitmapToSharedPreferences(getActivity(),"data","photo",((BitmapDrawable) ((ImageView) iv_img).getDrawable()).getBitmap());
                                saveStringToSp("photoFlag","ok");
                            }
                            @Override
                            public void clickNegative() {
                                PictureSelectUtil.with((AppCompatActivity) getActivity())
                                        .camera()
                                        .crop()
                                        .setCallback(new PictureSelectUtil.OnCallback() {
                                            @Override
                                            public void onCallback(Uri uri) {
                                                Glide.with(getActivity()).load(uri).into(iv_img);

                                            }
                                        }).select();
                                SPUtils.saveBitmapToSharedPreferences(getActivity(),"data","photo",((BitmapDrawable) ((ImageView) iv_img).getDrawable()).getBitmap());
                                saveStringToSp("photoFlag","ok");
                            }
                        });

            }
        });
        rl_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(1 );
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showAlertDialog((Activity) getActivity(), R.mipmap.logout, "退出提示", "你确定要退出吗？",
                        "确定", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                SharedPreferences data = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = data.edit();
                                edit.remove("login_flag");
                                edit.remove("translationId");
                                edit.remove("photo");
                                edit.remove("photoFlag");
                                edit.apply();
                                navigateToWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            @Override
                            public void clickNegative() {

                            }
                        });

            }
        });

    }

}