package com.haishu.SevenBreakFast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.FoodAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.entity.FoodClassify;
import com.haishu.SevenBreakFast.entity.SettlementInfo;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.ShowDialog;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FoodTextActivity extends BaseActivity implements View.OnClickListener {
    private double totalMoney;
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.foodClassify)
    private ListView foodLidst;
    @ViewInject(R.id.sCartMoney)
    public static TextView carMoney;
    @ViewInject(R.id.shoppingCount)
    public static TextView carCount;
    @ViewInject(R.id.foodFrameLayout1)
    private ListView frameLayout;
    @ViewInject(R.id.goSettlement)
    private LinearLayout goSettlement;
    @ViewInject(R.id.noFood)
    private TextView noFood;

    private List<SettlementInfo> settlementInfos = new ArrayList<>();  //创建一个订单详情list
    private List<FoodClassify> classifys = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private List<User> users;

    private MyAdapter myAdapter;
    private FoodAdapter foodAdapter;
    private Bundle bundle;

    private int serviceId;
    private String food;
    private Bag productListFood;
    private boolean isGoSettle = true;
    private String rangeTime;
    private static DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_text);
        ViewUtils.inject(this);

        getFromIntent();
    }

    private void initSetView() {
        if (classifys == null || classifys.size() == 0) {
            noFood.setVisibility(View.VISIBLE);
        } else {
            noFood.setVisibility(View.GONE);
            myAdapter = new MyAdapter(mContext, classifys, 0);
            foodLidst.setAdapter(myAdapter);

            foods.addAll(classifys.get(0).getProdList());
            foodAdapter = new FoodAdapter(FoodTextActivity.this, foods, myAdapter);
            frameLayout.setAdapter(foodAdapter);
        }

        //左边list点击监听
        foodLidst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAdapter.clearSelection(position);
                foods.clear();
                foods.addAll(classifys.get(position).getProdList());
                foodAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initBar(int name) {
        titleBarView.setTvCenterText(name);
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setLyLeftOnclickListener(this);
        goSettlement.setOnClickListener(this);
    }

    /**
     * @param totalPrice
     */
    public void updateBottomStatus(double totalPrice, Bag productList) {
        productListFood = productList;
        totalMoney = totalPrice;
        carMoney.setText("￥" + df.format(totalPrice) + "元");
        if (productList.size() == 0) {
            carCount.setVisibility(View.GONE);
        } else {
            carCount.setVisibility(View.VISIBLE);
            carCount.setText(productList.size() + "");
        }
    }

    /**
     * 从intent中获取早餐类型
     */
    public void getFromIntent() {
        food = getIntent().getStringExtra("Food");
        switch (food) {
            case Constant.ZAOCAN:
                serviceId = 1;
                initBar(R.string.breakList);
                getFoodData(Constant.ZAOCAN_ID);
                break;
            case Constant.YEXIAO:
                serviceId = 2;
                initBar(R.string.snacksList);
                getFoodData(Constant.YEXIAO_ID);
                break;
            case Constant.SUPPER:
                serviceId = 3;
                initBar(R.string.superList);
                getFoodData(Constant.SUPPER_ID);
                break;
        }
        getSettlementInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        users = AppUtility.getSputilsUser(mContext);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        carCount.setVisibility(View.GONE);
//        carMoney.setText("￥" + 0.00 + "元");
//        updateBottomStatus(0,new HashBag());
//        getFromIntent();
//    }

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
                        if (isGoSettle) {
                            if (totalMoney < settlementInfos.get(0).getLowestFee()) {
                                Toast.makeText(mContext, "起送价" + settlementInfos.get(0).getLowestFee() + "元起,请继续购物！！！", Toast.LENGTH_SHORT).show();
                            } else {
                                if (users == null || users.size() == 0) {
                                    ShowDialog.showLogin(mContext,Constant.FLAG_DEFAULT);
                                } else {
                                    intent = new Intent(mContext, SettlementActivity.class);
                                    intent.putExtra(Constant.SERVICE_ID, Constant.ZAOCAN_ID);
                                    intent.putExtra("foodList", (Serializable) productListFood);
                                    intent.putExtra("TotalMoney", totalMoney);
                                    intent.putExtra("cost", settlementInfos.get(0).getCost());
                                    intent.putExtra("time", settlementInfos.get(0).getTime());
                                    startActivity(intent);
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "请在" + rangeTime + "订餐", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constant.YEXIAO:
                        if (isGoSettle) {
                            if (totalMoney < settlementInfos.get(0).getLowestFee()) {
                                Toast.makeText(mContext, "起送价" + settlementInfos.get(0).getLowestFee() + "元起,请继续购物！！！", Toast.LENGTH_SHORT).show();
                            } else {
                                if (users == null || users.size() == 0) {
                                    ShowDialog.showLogin(mContext,Constant.FLAG_DEFAULT);
                                } else {
                                    intent = new Intent(mContext, SettlementActivity.class);
                                    intent.putExtra(Constant.SERVICE_ID, Constant.YEXIAO_ID);
                                    intent.putExtra("foodList", (Serializable) productListFood);
                                    intent.putExtra("TotalMoney", totalMoney);
                                    intent.putExtra("cost", settlementInfos.get(0).getCost());
                                    intent.putExtra("time", settlementInfos.get(0).getTime());
                                    startActivity(intent);
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "请在" + rangeTime + "订餐", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constant.SUPPER:
                        if (isGoSettle) {
                            if (totalMoney < settlementInfos.get(0).getLowestFee()) {
                                Toast.makeText(mContext, "起送价" + settlementInfos.get(0).getLowestFee() + "元起,请继续购物！！！", Toast.LENGTH_SHORT).show();
                            } else {
                                if (users == null || users.size() == 0) {
                                    ShowDialog.showLogin(mContext,Constant.FLAG_DEFAULT);
                                } else {
                                    intent = new Intent(mContext, SettlementActivity.class);
                                    intent.putExtra(Constant.SERVICE_ID, Constant.SUPPER_ID);
                                    intent.putExtra("foodList", (Serializable) productListFood);
                                    intent.putExtra("TotalMoney", totalMoney);
                                    intent.putExtra("cost", settlementInfos.get(0).getCost());
                                    intent.putExtra("time", settlementInfos.get(0).getTime());
                                    startActivity(intent);
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "请在" + rangeTime + "订餐", Toast.LENGTH_SHORT).show();
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

    //获取配送信息
    private void getSettlementInfo() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.SERVICE_ID, serviceId + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.SETTLEMENT_INFO_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        settlementInfos = jsonUtils.getSettlementInfo(json);
                    } else {
                        JSONArray jsonArray = json.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        rangeTime = obj.getString("rangeTime");
                        isGoSettle = false;
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

    /**
     * 创建分类适配器
     */
    public class MyAdapter extends BaseAdapter {
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
                textCount.setVisibility(View.GONE);
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
            myAdapter.notifyDataSetChanged();
        }
    }
}
