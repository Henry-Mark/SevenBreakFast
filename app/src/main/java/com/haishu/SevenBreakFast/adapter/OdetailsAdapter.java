package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.entity.OrderDetails;

import java.util.List;

/**
 * Created by zyw on 2016/3/3.
 */
public class OdetailsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderDetails> name;

    public OdetailsAdapter(Context mContext, List<OrderDetails> name) {
        this.mContext = mContext;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_detailslist_item, null);
            holder = new ViewHolder();
            holder.oDetailsName = (TextView) convertView.findViewById(R.id.odetailsName);
            holder.oDetailsSum = (TextView) convertView.findViewById(R.id.oDetailsSum);
            holder.oDetailsSumMoney = (TextView) convertView.findViewById(R.id.odetailsSumMoney);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.oDetailsName.setText(name.get(position).getProd_name());
        holder.oDetailsSum.setText(name.get(position).getProd_count() + "");
        holder.oDetailsSumMoney.setText("￥ "+name.get(position).getProd_price() + "");

        return convertView;
    }

    class ViewHolder {
        private TextView oDetailsName;//购买菜品
        private TextView oDetailsSum;//购买数量
        private TextView oDetailsSumMoney;//总价

    }
}
