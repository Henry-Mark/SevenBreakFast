package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.SettlementAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.AddressList;
import com.haishu.SevenBreakFast.entity.Coupon;
import com.haishu.SevenBreakFast.entity.CreateOrder;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.entity.SettleFood;
import com.haishu.SevenBreakFast.entity.SettlementInfo;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.entity.prodInfo;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.LogUtils;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.jsonUtils;
import com.haishu.SevenBreakFast.view.ScrollViewWithListView;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by zyw on 2016/3/11.
 */
public class SettlementActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.choiceAddress)           //选择地址
    private RelativeLayout choiceAddress;
    @ViewInject(R.id.settlementAddress)      //选择地址后
    private LinearLayout settlementAddress;
    @ViewInject(R.id.settlementName)          //地址名字
    private TextView settlementName;
    @ViewInject(R.id.settlementCollege)       //地址大学
    private TextView settlementCollege;
    @ViewInject(R.id.settlementTel)           //地址电话
    private TextView settlementTel;
    @ViewInject(R.id.settlementTime)          //送餐时间
    private TextView settlementTime;
    @ViewInject(R.id.vouchers)               //优惠券点击
    private RelativeLayout vouchers;
    @ViewInject(R.id.settlementCost)          //配送费
    private TextView settlementCost;
    @ViewInject(R.id.settlementCouponMoney)   //优惠券价格
    private TextView settlementCouponMoney;
    @ViewInject(R.id.settleTotal)            //合计
    private TextView settleTotal;
    //    @ViewInject(R.id.sCartMoney)             //购物车价格，确认价格
//    private TextView trueMoney;
    @ViewInject(R.id.settlementDetailsList)   //购物清单列表
    private ScrollViewWithListView mList;
    @ViewInject(R.id.submitOrder)            //提交订单
    private LinearLayout submitOrder;
    @ViewInject(R.id.qizao_type)             //早餐的类型
    private TextView qizao_type;

    //    private List<SettlementInfo> settlementInfos = new ArrayList<>();  //创建一个订单详情list
    private List<Coupon> coupons = new ArrayList<>(); //获取优惠券list
    private List<User> users; //创建用户lsit，用来将获取的用户信息放入
    private int serviceId; //食物种类id
    private int addressId;  //地点id
    private double totalFee;//实际支付
    public static Bag productListFood;  //
    private List<Food> foods = new ArrayList<>();  //食物list
    private Food food;  //食物实体
    private double Total;//总价
    private double cost; //配送费
    private String time;//配送时间
    private int couponId;  //优惠券id
    private double couponMoney; //优惠价格
    private SettlementAdapter mAdapter; //订单详情适配器
    private DecimalFormat df = new DecimalFormat("0.00");
    private String foodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        ViewUtils.inject(this);


        initData();
        initBar();
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        getDefaultAddress();
    }

    private void initBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvCenterText("提交订单");
        titleBarView.setLyLeftOnclickListener(this);
    }

    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
        serviceId = getIntent().getIntExtra(Constant.SERVICE_ID, 0);
        //根据点击的种类显示订餐类型
        if (serviceId == 1) {
            foodType = "早餐";
        } else if (serviceId == 2) {
            foodType = "夜宵";
        } else if (serviceId == 3) {
            foodType = "超市";
        }
        productListFood = (Bag) getIntent().getSerializableExtra("foodList"); //获取点击食物列表
        Total = getIntent().getDoubleExtra("TotalMoney", 0); //获取总金额
        cost = getIntent().getDoubleExtra("cost", 0);
        time = getIntent().getStringExtra("time");

        qizao_type.setText(foodType);
        settlementCost.setText("￥ " + df.format(cost));
        settlementTime.setText("您本时段订购的" + foodType + ",将于" + time + "送达");

        for (Iterator<?> itr = productListFood.uniqueSet().iterator(); itr
                .hasNext(); ) {
            food = (Food) itr.next();
            foods.add(food);
        }
        mAdapter = new SettlementAdapter(this, foods);
        mList.setAdapter(mAdapter);
        getDefaultAddress();
//        getSettlementInfo();
        getCouponInfo();

    }

    private void initView() {
        choiceAddress.setOnClickListener(this);
        vouchers.setOnClickListener(this);
        settlementAddress.setOnClickListener(this);
        submitOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.choiceAddress:
                intent = new Intent(mContext, AddressActivity.class);
                startActivityForResult(intent, Constant.CHOICE_ADDRESS);
                break;
            case R.id.settlementAddress:
                intent = new Intent(mContext, AddressActivity.class);
                startActivityForResult(intent, Constant.CHOICE_ADDRESS);
                break;
            case R.id.submitOrder:
                if (addressId == 0) {
                    Toast.makeText(mContext, "请选择收货地址", Toast.LENGTH_SHORT).show();
                } else {
                    if (Total + cost > couponMoney) {
                        createOrderInfo();
                    } else {
                        Toast.makeText(mContext, "必须大于优惠券的金额", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.vouchers:
                intent = new Intent(mContext, CouponActivity.class);
                startActivityForResult(intent, Constant.VOUCHERS);
                break;
            case R.id.common_titlebar_ly_left:
                finish();
                break;
        }
    }

    //根据返回结果显示数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constant.CHOICE_ADDRESS:
                if (data == null) {

                } else {
                    List<AddressList> addressList = new ArrayList<>();
                    addressList = (List<AddressList>) data.getSerializableExtra("addressList");
                    //设置地址
                    choiceAddress.setVisibility(View.GONE);
                    settlementAddress.setVisibility(View.VISIBLE);
                    addressId = addressList.get(data.getIntExtra("position", 0)).getAdd_id();
                    settlementName.setText(addressList.get(data.getIntExtra("position", 0)).getReceiver_name());
                    settlementCollege.setText(addressList.get(data.getIntExtra("position", 0)).getAdd_desc());
                    settlementTel.setText(addressList.get(data.getIntExtra("position", 0)).getReceiver_tel());
                }
                break;
            case Constant.VOUCHERS:
                if (data == null) {
                    couponId = 0;
                    couponMoney = 0;
                } else {
                    couponMoney = data.getDoubleExtra("couponMoney", 0);
                    couponId = data.getIntExtra("couponId", 0);
                    if ((Total + cost - couponMoney) >= 1) {
                        totalFee = Double.parseDouble(df.format(Total + cost - couponMoney));
                        settleTotal.setText("￥ " + totalFee);
                        settlementCouponMoney.setText("￥" + couponMoney);
                    } else {
                        Toast.makeText(mContext, "满6元才能使用5元优惠券", Toast.LENGTH_SHORT).show();
                        couponMoney = 0.0;
                        totalFee = Total + cost;
                        settleTotal.setText("￥ " + totalFee);
                        settlementCouponMoney.setText("￥" + couponMoney);
                    }
                }
                break;
        }
    }

    //获取默认地址
    private void getDefaultAddress() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.GET_DEFAULT_ADDRESS, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        //设置地址
                        choiceAddress.setVisibility(View.GONE);
                        settlementAddress.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = json.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);

                        addressId = obj.getInt("add_id");
                        settlementName.setText(obj.getString("receiver_name"));
                        settlementCollege.setText(obj.getString("add_desc"));
                        settlementTel.setText(obj.getString("receiver_tel"));

                    } else {
                        choiceAddress.setVisibility(View.VISIBLE);
                        settlementAddress.setVisibility(View.GONE);
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

    //获取优惠券信息
    private void getCouponInfo() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.DO_COUPON_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        coupons = jsonUtils.getCouponData(json);
                    }
                    setDataView2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

//    //填充数据
//    private void setDataView() {
//        settlementCost.setText("￥ " + df.format(cost));
//        settlementTime.setText("您本时段订购的早餐,将于" + time + "送达");
//
//    }

    //填充数据
    private void setDataView2() {
        //判断是否有优惠券
        if (null == coupons || coupons.size() == 0) {
            couponId = 0;
            couponMoney = 0;
            settlementCouponMoney.setText("￥ " + 0.0);
            totalFee = Double.parseDouble(df.format(Total + cost - couponMoney));
            if (totalFee <= 0) {
                totalFee = 0;
                settleTotal.setText("￥ " + totalFee);
            } else {
                settleTotal.setText("￥ " + totalFee);
            }
        } else {
            couponId = coupons.get(0).getCoupon_id();
            couponMoney = coupons.get(0).getDiscount();
//            settlementCouponMoney.setText("￥ " + df.format(couponMoney));
            totalFee = Double.parseDouble(df.format(Total + cost));
            if (totalFee <= 0) {
                settleTotal.setText("￥ " + 0.0);
            } else {
                settleTotal.setText("￥ " + totalFee);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        totalFee = Total + settlementInfos.get(0).getCost() - couponMoney;
//        if (totalFee <= 0) {
//            totalFee = 0;
//            settleTotal.setText("-￥ " + totalFee);
//        } else {
//            settleTotal.setText("-￥ " + totalFee);
//        }
    }

    //生成订单信息
    private void createOrderInfo() {
        //创建实体对象
        CreateOrder order = new CreateOrder();
        order.setUserId(users.get(0).getUser_id());
        order.setAddressId(addressId);
        order.setServiceId(serviceId);
        order.setTotalPrice(Total);
        if (null == coupons || coupons.size() == 0) {
            order.setDiscount(couponMoney);
            order.setCouponId(couponId);
        } else {
            if ((Total + cost - couponMoney) >= 1) {
                order.setDiscount(couponMoney);
                order.setCouponId(couponId);
            } else {
                order.setDiscount(0);
                order.setCouponId(0);
            }
        }
        order.setPayWay(1);
        if (totalFee <= 0) {
            totalFee = 0;
            order.setTotalFee(totalFee);
        } else {
            order.setTotalFee(totalFee);
        }
        order.setDeliveryFee(cost);

        prodInfo list = new prodInfo();
        List<SettleFood> sFoods = new ArrayList<>();
        for (int i = 0; i < foods.size(); i++) {
            SettleFood sFood = new SettleFood();
            sFood.setPid(Integer.parseInt(foods.get(i).getProdId()));
            sFood.setCount(foods.get(i).getProdCount());
            sFoods.add(sFood);
        }

        list.setProdList(sFoods);
        order.setProdInfo(list);
        //对象转JSON 字符串
        String prodInfo = new Gson().toJson(order);

        RequestParams params = new RequestParams();
        params.addBodyParameter("paramsJson", prodInfo);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.ADD_ORDER_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonObject)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String orderCode = obj.getString("orderCode");

                        Intent intent = new Intent(mContext, ChoicePayActivity.class);
                        intent.putExtra("orderCode", orderCode);
                        intent.putExtra("totalMoney", totalFee);
                        intent.putExtra("couponId", couponId);
                        startActivity(intent);
                        finish();
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
}
