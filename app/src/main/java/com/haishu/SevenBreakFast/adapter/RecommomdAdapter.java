package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.MyApplication;
import com.haishu.SevenBreakFast.entity.HotFood;
import com.haishu.SevenBreakFast.ui.HomeActivity;

import java.util.HashMap;
import java.util.List;


/**
 * Created by henry on 2016/3/1.
 */
public class RecommomdAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<HotFood> hotFoods;

    public RecommomdAdapter(Context context, List<HotFood> hotFoods) {
        this.context = context;
        this.hotFoods = hotFoods;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return hotFoods.size();
    }

    @Override
    public Object getItem(int i) {
        return hotFoods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.listitem_recommond_food, null);

            ImageView iv = (ImageView) v.findViewById(R.id.iv_pic_item_r);
            TextView mFood = (TextView) v.findViewById(R.id.tv_food_item_r);
            TextView mPrice = (TextView) v.findViewById(R.id.tv_price_item_r);

            if (hotFoods.get(i).getProd_img().isEmpty()) {
                iv.setImageResource(R.mipmap.icon_empty);
            } else {
                MyApplication.imageLoader.displayImage(hotFoods.get(i).getProd_img(), iv);
            }
            mFood.setText(hotFoods.get(i).getProd_name());
            mPrice.setText("￥ " + hotFoods.get(i).getProd_price());
        }
        return v;
    }
}
