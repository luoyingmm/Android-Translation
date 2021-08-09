 package com.luoyingmm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.luoyingmm.R;
import com.luoyingmm.activity.LoginActivity;
import com.luoyingmm.activity.MainActivity;
import com.luoyingmm.activity.SetActivity;
import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.PictureSelectUtil;
import com.luoyingmm.util.SPUtils;
import com.luoyingmm.util.StringUtils;

import static android.content.Context.MODE_PRIVATE;


public class MyFragment extends BaseFragment {
    private TextView tv_title;
    private ImageView iv_img;
    private RelativeLayout rl_collect;
    private RelativeLayout rl_set;
    private RelativeLayout rl_logout;

    public MyFragment() {

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
        //读取头像
        Bitmap bitmap = SPUtils.getBitmapFromSharedPreferences(getActivity(), StringUtils.username, "photo", "");
        if (!StringUtils.isEmpty(getStringFromSp("photoFlag"))){
            iv_img.setImageBitmap(bitmap);
        }else {
            iv_img.setImageResource(R.mipmap.change_head);
        }
    }

    @Override
    protected void initData() {
        //如果之前选择"暂不登录"名字就叫做“游客”，否则显示自定义的名字
        if (StringUtils.isEmpty(getStringFromSp("username"))) {
            tv_title.setText("游客");
        } else {
            tv_title.setText(getStringFromSp("username"));
        }

        //更换头像
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
                            }
                        });

            }
        });

        //收藏界面跳转
        rl_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(1);
            }
        });

        //设置界面跳转
        rl_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(SetActivity.class);
            }
        });

        //退出登录
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showAlertDialog((Activity) getActivity(), R.mipmap.logout, "退出提示", "你确定要退出吗？",
                        "确定", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                SharedPreferences loginFlag = getActivity().getSharedPreferences("LoginFlag", MODE_PRIVATE);
                                SharedPreferences.Editor edit = loginFlag.edit();
                                edit.remove("loginKey");
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

    //关闭app保存头像
    @Override
    public void onPause() {
        super.onPause();
        SPUtils.saveBitmapToSharedPreferences(getActivity(),StringUtils.username,"photo",((BitmapDrawable) ((ImageView) iv_img).getDrawable()).getBitmap());
        saveStringToSp("photoFlag","ok");
    }
}