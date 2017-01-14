package com.haishu.SevenBreakFast.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.utils.LogUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected String TAG = "BaseActivity";
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        TAG = getClass().getName();
        mContext = this;
//        // 修改状态栏颜色，4.4+生效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus();
//        }
//        tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.main_title);//通知栏所需颜色
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
