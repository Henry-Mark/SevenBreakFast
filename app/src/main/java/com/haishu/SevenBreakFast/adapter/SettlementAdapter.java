package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.entity.Food;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zyw on 2016/3/15.
 */
public class SettlementAdapter extends BaseAdapter {
    private Context mContent;
    private List<Food> foods;
    private DecimalFormat df = new DecimalFormat("0.00");

    public SettlementAdapter(Context mContent, List<Food> foods) {
        this.mContent = mContent;
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContent, R.layout.activity_settlement_list_item, null);
        }
        TextView foodName = (TextView) convertView.findViewById(R.id.settlement_list_name);
        TextView foodCount = (TextView) convertView.findViewById(R.id.settlement_list_count);
        TextView foodTotal = (TextView) convertView.findViewById(R.id.settlement_list_total);

        foodName.setText(foods.get(position).getProdName());
        foodCount.setText("x " + foods.get(position).getProdCount() + "");
        foodTotal.setText("￥ " + df.format(foods.get(position).getProdTotal()) + "");

        return convertView;
    }
}
