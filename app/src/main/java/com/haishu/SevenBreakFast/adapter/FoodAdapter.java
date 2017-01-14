package com.haishu.SevenBreakFast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.MyApplication;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.ui.FoodTextActivity;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zyw on 2016/3/9.
 */
public class FoodAdapter extends BaseAdapter {
    private List<Food> foods = new ArrayList<>();
    private Bag productList = new HashBag();
    private FoodTextActivity mContext;
    private FoodTextActivity.MyAdapter myAdapter;
    private int num;

    public FoodAdapter(FoodTextActivity mContext, List<Food> foods, FoodTextActivity.MyAdapter myAdapter) {
        this.mContext = mContext;
        this.foods = foods;
        this.myAdapter = myAdapter;
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

    public void clear() {
        foods.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Food food = foods.get(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_foods_item, null);
            holder = new ViewHolder();

            holder.foodImg = (ImageView) convertView.findViewById(R.id.foodImg);
            holder.foodName = (TextView) convertView.findViewById(R.id.foodName);
            holder.foodPrice = (TextView) convertView.findViewById(R.id.foodPrice);
            holder.foodAdd = (ImageView) convertView.findViewById(R.id.foodAdd);
            holder.foodNum = (TextView) convertView.findViewById(R.id.foodNum);
            holder.foodReduce = (ImageView) convertView.findViewById(R.id.foodReduce);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //判断图片是否为空
        if (foods.get(position).getProdImg().isEmpty()) {
            holder.foodImg.setImageResource(R.mipmap.icon_empty);
        } else {
            MyApplication.imageLoader.displayImage(foods.get(position).getProdImg(), holder.foodImg);
        }
        holder.foodName.setText(foods.get(position).getProdName());
        holder.foodPrice.setText("￥" + foods.get(position).getProdPrice());

        final int foodNum = productList.getCount(food);
        if (foodNum == 0) {
            isShow(holder, View.GONE);// 在没有选中的商品的时候，隐藏减少按钮
        } else if (foodNum > 0) {
            isShow(holder, View.VISIBLE);// 显示减少按钮
        }
        food.setProdTotal(foodNum * Double.parseDouble(foods.get(position).getProdPrice()));
        holder.foodNum.setText(foodNum + "");

        //减少按钮功能
        holder.foodReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = foods.get(position).getProdCount();
                num--;
                foods.get(position).setProdCount(num);
                productList.remove(food, 1);
                mContext.updateBottomStatus(getTotalPrice(), productList);
                FoodAdapter.this.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
            }
        });
        //增加按钮功能
        holder.foodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = foods.get(position).getProdCount();
                num++;
                foods.get(position).setProdCount(num);
                productList.add(food);
                mContext.updateBottomStatus(getTotalPrice(), productList);
                FoodAdapter.this.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * 获取总金额
     *
     * @return
     */
    public double getTotalPrice() {
        double totalProce = 0;
        if (productList.size() == 0) {
            return totalProce;
        }
        for (Iterator<?> itr = productList.uniqueSet().iterator(); itr
                .hasNext(); ) {
            Food food = (Food) itr.next();
            totalProce += Double.parseDouble(food.getProdPrice()) * productList.getCount(food);
        }
        return totalProce;
    }

    class ViewHolder {
        private TextView foodName; //食物名称
        private ImageView foodImg; //食物图片
        private TextView foodPrice; //食物单价
        private ImageView foodAdd;  //添加
        private TextView foodNum;  //数量
        private ImageView foodReduce;  //减少

    }

    /**
     * 是否显示减少按钮和商品数量
     *
     * @param holder
     */
    private void isShow(ViewHolder holder, int isVisible) {
        holder.foodNum.setVisibility(isVisible);
        holder.foodReduce.setVisibility(isVisible);
    }
}
