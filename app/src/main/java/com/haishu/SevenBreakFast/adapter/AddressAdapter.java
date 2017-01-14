package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.entity.AddressList;
import com.haishu.SevenBreakFast.ui.AddressActivity;

import java.util.List;

/**
 * Created by zyw on 2016/3/10.
 */
public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressList> addressLists;

    public AddressAdapter(Context mContext, List<AddressList> addressLists) {
        this.mContext = mContext;
        this.addressLists = addressLists;
    }

    @Override
    public int getCount() {
        return addressLists.size();
    }

    @Override
    public Object getItem(int position) {
        return addressLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_address_item, null);
            holder = new ViewHolder();

            holder.addressName = (TextView) convertView.findViewById(R.id.addressList_name);
            holder.addressCollege = (TextView) convertView.findViewById(R.id.addressList_college);
            holder.addressTel = (TextView) convertView.findViewById(R.id.addressList_tel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.addressName.setText(addressLists.get(position).getReceiver_name());
        holder.addressCollege.setText(addressLists.get(position).getAdd_desc());
        holder.addressTel.setText(addressLists.get(position).getReceiver_tel());

        return convertView;
    }

    class ViewHolder {
        private TextView addressName;
        private TextView addressCollege;
        private TextView addressTel;
    }
}
