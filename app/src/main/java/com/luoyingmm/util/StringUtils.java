package com.luoyingmm.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

//String工具类
public class StringUtils {
    public static final String ID = "0f6e7821cecdcbda";
    public static String username = "te";
    public static boolean isEmpty(String str){
        if (str == null || str.length() <= 0){
            return true;
        }else {
            return false;
        }
    }

}
