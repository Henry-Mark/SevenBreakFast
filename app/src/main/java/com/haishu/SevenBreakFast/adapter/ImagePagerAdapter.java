/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.MyApplication;
import com.haishu.SevenBreakFast.entity.ImageInfo;
import com.haishu.SevenBreakFast.ui.BannerDetails;

import java.util.List;

/**
 * @Description: 图片适配器
 */
public class ImagePagerAdapter extends BaseAdapter {

    private Context context;
    private List<ImageInfo> imageIdList;
    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<ImageInfo> imageIdList) {
        super();
        this.context = context;
        this.imageIdList = imageIdList;
        if (imageIdList != null) {
            this.size = imageIdList.size();
        }
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup container) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView
                    .setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (imageIdList.size() == 0 || imageIdList.get(getPosition(position)).getAdv_img().isEmpty()) {
            holder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        } else {
            MyApplication.imageLoader.displayImage(imageIdList.get(getPosition(position)).getAdv_img(), holder.imageView);
        }
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BannerDetails.class);
                intent.putExtra("bannerContent", imageIdList.get(getPosition(position)).getAdv_desc());
                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

}
