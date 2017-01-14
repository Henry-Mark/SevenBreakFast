package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.ImagePagerAdapter;
import com.haishu.SevenBreakFast.adapter.RecommomdAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.HotFood;
import com.haishu.SevenBreakFast.entity.ImageInfo;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.jsonUtils;
import com.haishu.SevenBreakFast.view.CircleFlowIndicator;
import com.haishu.SevenBreakFast.view.ScrollViewWithListView;
import com.haishu.SevenBreakFast.view.ViewFlow;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.viewflow)
    private ViewFlow mViewFlow;
    @ViewInject(R.id.viewflowindic)
    private CircleFlowIndicator mFlowIndicator;
    @ViewInject(R.id.ll_address_home)
    private LinearLayout mAddressLL;
    @ViewInject((R.id.tv_title_home))
    private TextView mTitle;
    @ViewInject(R.id.iv_yexiao_home)
    private LinearLayout mSupper;
    @ViewInject(R.id.iv_snacks_home)
    private LinearLayout mSnacks;
    @ViewInject(R.id.iv_breakfast_home)
    private LinearLayout mBreakfast;
    @ViewInject(R.id.list_home)
    private ScrollViewWithListView mList;
    @ViewInject(R.id.scrollView_home)
    private ScrollView mScrollview;

    private List<HotFood> hotFoods = new ArrayList<>();
    private RecommomdAdapter recommomdAdapter;
    private List<ImageInfo> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);

//        initData();
        initView();
    }

    private void initData() {
//        getBannerData();
//        getHotFoodData();
    }

    public void initView() {
        if ((int) SPUtils.get(mContext, Constant.PLACE_ID, 0) == 0) {
            Intent intent_place = new Intent(this, PlaceSelectActivity.class);
            startActivity(intent_place);
            finish();
        } else {
            getHotFoodData();
            getBannerData();
        }

        mTitle.setText(SPUtils.get(mContext, Constant.PLACE_NAME, "") + "");
        mList.setFocusable(false);

        mAddressLL.setOnClickListener(this);
        mBreakfast.setOnClickListener(this);
        mSnacks.setOnClickListener(this);
        mSupper.setOnClickListener(this);
    }

    public void setList(final List<HotFood> hotFoods) {
        recommomdAdapter = new RecommomdAdapter(this, hotFoods);
        mList.setAdapter(recommomdAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (hotFoods.get(position).getService_id()) {
                    case 1:
                        Intent intent_breakfast = new Intent(HomeActivity.this, FoodTextActivity.class);
                        intent_breakfast.putExtra("Food", Constant.ZAOCAN);
                        startActivity(intent_breakfast);
                        break;
                    case 2:
                        Intent intent_supper = new Intent(HomeActivity.this, FoodTextActivity.class);
                        intent_supper.putExtra("Food", Constant.YEXIAO);
                        startActivity(intent_supper);
                        break;
                    case 3:
                        Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
//                Intent intent_snacks = new Intent(HomeActivity.this, FoodTextActivity.class);
//                intent_snacks.putExtra("Food", Constant.SUPPER);
//                startActivity(intent_snacks);
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_breakfast_home:
                Intent intent_breakfast = new Intent(HomeActivity.this, FoodTextActivity.class);
                intent_breakfast.putExtra("Food", Constant.ZAOCAN);
                startActivity(intent_breakfast);
                break;
            case R.id.iv_snacks_home:
                Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
//                Intent intent_snacks = new Intent(HomeActivity.this, FoodTextActivity.class);
//                intent_snacks.putExtra("Food", Constant.SUPPER);
//                startActivity(intent_snacks);
                break;
            case R.id.ll_address_home:
                Intent intent_place = new Intent(this, PlaceSelectActivity.class);
                startActivity(intent_place);
                break;
            case R.id.iv_yexiao_home:
                Intent intent_supper = new Intent(HomeActivity.this, FoodTextActivity.class);
                intent_supper.putExtra("Food", Constant.YEXIAO);
                startActivity(intent_supper);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initView();
    }

    //加载banner数据
    private void initBanner(List<ImageInfo> imageUrlList) {
        mViewFlow.setAdapter(new ImagePagerAdapter(mContext, imageUrlList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    //获取广告图片
    private void getBannerData() {
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.BANNER_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        images = jsonUtils.getBanner(json);
                        initBanner(images);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //获取热门推荐
    private void getHotFoodData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.HOTFOOD_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonResult = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonResult)) {
                        hotFoods = jsonUtils.getHotFood(jsonResult);
                        setList(hotFoods);
                    } else {
                        mList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
