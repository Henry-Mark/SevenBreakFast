package com.haishu.SevenBreakFast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.FoodClassify;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.fragment.FoodFragment;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.jsonUtils;
import com.haishu.SevenBreakFast.view.TitleBarView;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class FoodActivity extends BaseActivity implements View.OnClickListener {
    private static double totalMoney;
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.foodClassify)
    private ListView foodLidst;
    @ViewInject(R.id.sCartMoney)
    public static TextView carMoney;
    @ViewInject(R.id.foodFrameLayout)
    private FrameLayout frameLayout;
    @ViewInject(R.id.goSettlement)
    private LinearLayout goSettlement;

    private List<FoodClassify> classifys = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private List<User> users;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private FoodFragment foodFragment;
    private MyAdapter myAdapter;
    private Bundle bundle;

    private String food;
    private static Bag productListFood;
    private static DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ViewUtils.inject(this);

        getFromIntent();
    }

    private void initSetView() {
        users = AppUtility.getSputilsUser(mContext);

        goSettlement.setOnClickListener(this);
        myAdapter = new MyAdapter(this, classifys, 1);
        foodLidst.setAdapter(myAdapter);

        fm = getSupportFragmentManager();
        foods.addAll(classifys.get(0).getProdList());
        //默认加载第一个
        getChangeFragment(0, foods);

        //左边list点击监听
        foodLidst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foods.clear();
                foods.addAll(classifys.get(position).getProdList());
                getChangeFragment(position, foods);
            }
        });
    }

    private void getChangeFragment(int position, List<Food> foods) {
        ft = fm.beginTransaction();
        foodFragment = new FoodFragment();
        bundle = new Bundle();
        bundle.putSerializable(Constant.FOOD_CLASSIFY, (Serializable) foods);
        foodFragment.setArguments(bundle);
        ft.replace(R.id.foodFrameLayout, foodFragment);
        ft.commit();
        myAdapter.clearSelection(position);
        myAdapter.notifyDataSetChanged();
    }

    private void initBar(int name) {
        titleBarView.setTvCenterText(name);
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setLyLeftOnclickListener(this);
    }

    /**
     * @param totalPrice
     */
    public static void updateBottomStatus(double totalPrice, Bag productList) {
        productListFood = new HashBag();
        productListFood = productList;
        totalMoney = totalPrice;
//        carMoney.setText("￥" + df.format(totalPrice) + "元");
        carMoney.setText("￥" + productList.size() + "元");
    }

    /*
        从intent中获取早餐类型
     */
    public void getFromIntent() {
        food = getIntent().getStringExtra("Food");
        switch (food) {
            case Constant.ZAOCAN:
                initBar(R.string.breakList);
                getFoodData(Constant.ZAOCAN_ID);
//                Toast.makeText(getApplicationContext(), "早餐", Toast.LENGTH_SHORT).show();
                break;
            case Constant.YEXIAO:
                initBar(R.string.snacksList);
                getFoodData(Constant.YEXIAO_ID);
//                Toast.makeText(getApplicationContext(), "零食", Toast.LENGTH_SHORT).show();
                break;
            case Constant.SUPPER:
                initBar(R.string.superList);
                getFoodData(Constant.SUPPER_ID);
//                Toast.makeText(getApplicationContext(), "晚餐", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_ly_left:
                finish();
                break;
            case R.id.goSettlement:
                Intent intent = null;
                switch (food) {
                    case Constant.ZAOCAN:
                        if (totalMoney <= 0.0) {
                            Toast.makeText(mContext, "你还没有点餐！！！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (users == null || users.size() == 0) {
                                Toast.makeText(mContext, "你还没有登录！！！", Toast.LENGTH_SHORT).show();
                                intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                intent = new Intent(mContext, SettlementActivity.class);
                                intent.putExtra(Constant.SERVICE_ID, Constant.ZAOCAN_ID);
                                intent.putExtra("foodList", (Serializable) productListFood);
                                intent.putExtra("TotalMoney", totalMoney);
                                startActivity(intent);
                            }
                        }
                        break;
                    case Constant.YEXIAO:
                        if (totalMoney <= 0.0) {
                            Toast.makeText(mContext, "你还没有点餐！！！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (users == null || users.size() == 0) {
                                Toast.makeText(mContext, "你还没有登录！！！", Toast.LENGTH_SHORT).show();
                                intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                intent = new Intent(mContext, SettlementActivity.class);
                                intent.putExtra(Constant.SERVICE_ID, Constant.YEXIAO_ID);
                                intent.putExtra("foodList", (Serializable) productListFood);
                                intent.putExtra("TotalMoney", totalMoney);
                                startActivity(intent);
                            }
                        }
                        break;
                    case Constant.SUPPER:
                        if (totalMoney <= 0.0) {
                            Toast.makeText(mContext, "你还没有点餐！！！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (users == null || users.size() == 0) {
                                Toast.makeText(mContext, "你还没有登录！！！", Toast.LENGTH_SHORT).show();
                                intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                intent = new Intent(mContext, SettlementActivity.class);
                                intent.putExtra(Constant.SERVICE_ID, Constant.SUPPER_ID);
                                intent.putExtra("foodList", (Serializable) productListFood);
                                intent.putExtra("TotalMoney", totalMoney);
                                startActivity(intent);
                            }
                        }

                        break;
                }
                break;
        }
    }

    //获取商品数据
    private void getFoodData(int type) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("placeId", SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        params.addBodyParameter("serviceId", type + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.FOOD_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject result = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, result)) {
                        classifys = jsonUtils.getFoodList(result);
                        initSetView();
                    } else {

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

    class MyAdapter extends BaseAdapter {
        private List<FoodClassify> classifys = new ArrayList<>();
        private Context mContext;
        private int selectedPosition = 0;

        public MyAdapter(Context mContext, List<FoodClassify> classifys, int position) {
            this.mContext = mContext;
            this.classifys = classifys;
            this.selectedPosition = position;
        }

        @Override
        public int getCount() {
            return classifys.size();
        }

        @Override
        public Object getItem(int position) {
            return classifys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.activity_foodlist_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.foodClassify_item_name);
            TextView textCount = (TextView) convertView.findViewById(R.id.tv_count);
            textView.setText(classifys.get(position).getTypeName());

            int count = 0;
            for (int i = 0; i < classifys.get(position).getProdList().size(); i++) {
                count += classifys.get(position).getProdList().get(i).getProdCount();
            }
            classifys.get(position).setCount(count);
            if (count <= 0) {
                textCount.setVisibility(View.INVISIBLE);
            } else {
                textCount.setVisibility(View.VISIBLE);
                textCount.getPaint().setAntiAlias(true);
                textCount.setText(count + "");
            }

            if (selectedPosition == position) {
                convertView.setBackgroundResource(R.color.white);
                textView.setTextColor(getResources().getColor(R.color.main_title));
            } else {
                convertView.setBackgroundResource(R.color.listBack);
                textView.setTextColor(getResources().getColor(R.color.text_normal));
            }
            return convertView;
        }

        /**
         * 为外部提供接口设置item是否选中的状态
         *
         * @param position
         */
        public void clearSelection(int position) {
            selectedPosition = position;
        }
    }
}
