package com.luoyingmm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.luoyingmm.R;
import com.luoyingmm.activity.LoginActivity;
import com.luoyingmm.activity.MainActivity;
import com.luoyingmm.util.DialogUtil;
import com.luoyingmm.util.PictureSelectUtil;
import com.luoyingmm.util.SPUtils;
import com.luoyingmm.util.StatusBarUtil;
import com.luoyingmm.util.StringUtils;

import java.io.ByteArrayOutputStream;

import static android.content.Context.MODE_PRIVATE;


public class SetFragment extends BaseFragment {
    private TextView tv_title;
    private ImageView iv_img;

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
        iv_img = mRootView.findViewById(R.id.iv_img);
        Bitmap bitmap = SPUtils.getBitmapFromSharedPreferences(getActivity(), "data", "photo", "");
        if (!StringUtils.isEmpty(getStringFromSp("photoFlag"))){
            iv_img.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void initData() {
        if (StringUtils.isEmpty(getStringFromSp("username"))) {
            tv_title.setText("游客");
        } else {
            tv_title.setText(getStringFromSp("username") + "的设置");
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


    }

    @Override
    public void onPause() {
        super.onPause();
        SPUtils.saveBitmapToSharedPreferences(getActivity(),"data","photo",((BitmapDrawable) ((ImageView) iv_img).getDrawable()).getBitmap());
        saveStringToSp("photoFlag","ok");
    }
}