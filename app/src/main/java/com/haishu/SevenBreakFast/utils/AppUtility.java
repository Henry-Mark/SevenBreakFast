package com.haishu.SevenBreakFast.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2016/3/8.
 */
public class AppUtility {

    public static boolean getStatus(Context mContext, JSONObject jsonObject) {
        boolean isSuccess = false;
        try {
            int code = jsonObject.getInt("code");
            switch (code) {
                case Constant.CODE_SUCCESS:
                    isSuccess = true;
                    break;
                case Constant.CODE_FAILURE:
                    isSuccess = false;
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static String getMessage(Context mContext, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("msg");
    }

    /**
     * 获取图片url
     *
     * @param mContext
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static String getImgUrl(Context mContext, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("imgUrl");
    }

    //获取本独存储用户数据
    public static List<User> getSputilsUser(Context mContext) {
        List<User> users = new ArrayList<>();
        String userInfo = (String) SPUtils.get(mContext, Constant.LOGIN_URL, "");
        Gson gson = new Gson();
        if (userInfo.isEmpty()) {

        } else {
            users = gson.fromJson(userInfo,
                    new TypeToken<List<User>>() {
                    }.getType());
        }
        return users;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi.versionName;
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

}
