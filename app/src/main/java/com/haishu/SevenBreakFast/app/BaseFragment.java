package com.haishu.SevenBreakFast.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haishu.SevenBreakFast.utils.LogUtils;


public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected String TAG = "BaseFragment";
    private View view;

    public abstract View getView(LayoutInflater inflater, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        TAG = getClass().getName();
        LogUtils.i(TAG, "---------onCreate----------");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = getView(inflater,savedInstanceState);
        }
        // 缓存的view需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, "---------onResume----------");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG, "---------onPause----------");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "---------onActivityResult----------");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i(TAG, "---------onActivityCreated----------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "---------onDestroy----------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(TAG, "---------onDetach----------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i(TAG, "---------onDestroyView----------");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i(TAG, "---------onStart----------");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(TAG, "---------onStop----------");
    }
}
