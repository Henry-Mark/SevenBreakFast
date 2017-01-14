package com.haishu.SevenBreakFast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.FoodAdapter;
import com.haishu.SevenBreakFast.app.BaseFragment;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.ui.FoodActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2016/3/9.
 */
public class FoodFragment extends BaseFragment {
    @ViewInject(R.id.foodList)
    private ListView foodList;
    private List<Food> foods = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private Bundle bundle;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("food", (Serializable) foods);
    }

    @Override
    public View getView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, null);
        ViewUtils.inject(this, view);
        initData();
        initView();
        return view;
    }

    //获取数据
    private void initData() {
        bundle = getArguments();
        foods = (List<Food>) bundle.getSerializable(Constant.FOOD_CLASSIFY);
    }

    private void initView() {
//        foodAdapter = new FoodAdapter(mContext, foods);
//        foodList.setAdapter(foodAdapter);
    }
}
