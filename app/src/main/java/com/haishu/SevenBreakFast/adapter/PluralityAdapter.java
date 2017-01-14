package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.entity.Plurality;

import java.util.List;

/**
 * Created by henry on 2016/3/22.
 */
public class PluralityAdapter extends BaseAdapter {
    private List<Plurality>list;
    private Context context;

    public PluralityAdapter(Context context ,List<Plurality> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listitem_plurality,null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView)convertView.findViewById(R.id.item_tv_plurality);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(list.get(position).getJob_name());
        return convertView;
    }



    class ViewHolder {
        private TextView tv;
    }
}
