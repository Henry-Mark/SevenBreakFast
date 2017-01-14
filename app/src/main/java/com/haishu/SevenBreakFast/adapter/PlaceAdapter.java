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
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.entity.Place;
import com.haishu.SevenBreakFast.ui.PlaceSelectActivity;

import java.util.List;

/**
 * Created by henry on 2016/3/10.
 */
public class PlaceAdapter extends BaseAdapter {

    private Context context;
    private List<Place> list;
    private LayoutInflater layoutInflater;

    public PlaceAdapter(Context context,List<Place> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
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
        View v = convertView;
        if(v == null) {
            v = layoutInflater.inflate(R.layout.listitem_place_select, null);
            TextView textView  = (TextView)v.findViewById(R.id.item_tv_place);
//            String place = list.get(position).toString();
//            Log.d(".....",place);
            textView.setText(list.get(position).getPlace_name());
        }
        return v;
    }
}
