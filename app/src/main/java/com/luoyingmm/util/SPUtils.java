package com.luoyingmm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * SharedPreferences 工具类
 *
 */
public class SPUtils {
    /**
     * 将图片保存到SharedPreferences
     * @param context
     * @param preference
     * @param key
     * @param bitmap
     */
    public static void saveBitmapToSharedPreferences(Context context, String preference,
                                                     String key, Bitmap bitmap){
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, imageString);
        editor.commit();
    }

    /**
     * 从SharedPreferences取出图片
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static Bitmap getBitmapFromSharedPreferences(Context context,
                                                        String preference, String key, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString(key,defaultValue);
        if (imageString!=null){
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
            //第三步:利用ByteArrayInputStream生成Bitmap
            return BitmapFactory.decodeStream(byteArrayInputStream);
        }else{
            return null;
        }
    }
    /**
     * 设置字符
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setStringPreferences(Context context, String preference,
                                            String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringPreference(Context context,
                                             String preference, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 设置长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setLongPreference(Context context, String preference,
                                         String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLongPreference(Context context, String preference,
                                         String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 设置boolean类型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setBooleanPreferences(Context context,
                                             String preference, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanPreference(Context context,
                                               String preference, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 设置int
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setIntPreferences(Context context, String preference,
                                         String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取int
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntPreference(Context context, String preference,
                                       String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 删除一个属性
     *
     * @param context
     * @param preference
     * @param key
     */
    public static void deletePrefereceKey(Context context, String preference,
                                          String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

}
