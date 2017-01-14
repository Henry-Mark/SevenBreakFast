package com.haishu.SevenBreakFast.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.OdetailsAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Order;
import com.haishu.SevenBreakFast.entity.OrderDetails;
import com.haishu.SevenBreakFast.utils.AppUtility;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2016/3/3.
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.oDetailsType)
    private TextView oDetailsType;     //下单食物类型
    @ViewInject(R.id.oDetailsList)
    private ScrollViewWithListView mList;            //下单食物列表
    @ViewInject(R.id.oDetailsCost)
    private TextView oDetailsCost;     //配送费
    @ViewInject(R.id.oDetailsCoupon)
    private TextView oDetailsCoupon;   //优惠券费用
    @ViewInject(R.id.oDetailsMoney)
    private TextView oDetailsMoney;   //总价
    @ViewInject(R.id.oDetailsNum)
    private TextView oDetailsNum;      //订单号
    @ViewInject(R.id.oDetailsDownTime)
    private TextView oDetailsTime;     //下单时间
    @ViewInject(R.id.oDetailsPay)
    private TextView oDetailsPay;      //支付方式
    @ViewInject(R.id.oDetailsContacts)
    private TextView oDetailsContacts;  //联系人
    @ViewInject(R.id.oDetailsTel)
    private TextView oDetailsTel;       //联系电话
    @ViewInject(R.id.oDetailsToTime)
    private TextView oDetailsToTime;    //送达时间
    @ViewInject(R.id.oDetailsAddress)
    private TextView oDetailsAddress;   //送货地址

    private OdetailsAdapter odetailsAdapter;
    private List<OrderDetails> oDetails = new ArrayList<>();
    private List<Order> orders;
    private Order order;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        ViewUtils.inject(this);

        initData();
        initView();
        intBar();
    }

    private void initData() {
        position = getIntent().getIntExtra("position", 0);
        orders = (List<Order>) getIntent().getSerializableExtra("orders");
        oDetailsCost.setText("￥ " + orders.get(position).getDelivery_fee() + "");
        oDetailsCoupon.setText("-￥ " + orders.get(position).getDiscount() + "");
        oDetailsMoney.setText("合计：￥" + (orders.get(position).getTotal_fee()) + "元");
        oDetailsNum.setText(orders.get(position).getOrder_code());
        oDetailsTime.setText(orders.get(position).getOrderTime());
        oDetailsPay.setText("支付宝");
        oDetailsContacts.setText(orders.get(position).getReceiver_name());
        oDetailsTel.setText(orders.get(position).getReceiver_tel());
        oDetailsToTime.setText(orders.get(position).getDelivery_time());
        oDetailsAddress.setText(orders.get(position).getAdd_desc());
        getOrderDetails();
    }

    private void intBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvCenterText("订单详情");
        titleBarView.setLyLeftOnclickListener(this);
    }

    private void initView() {
        odetailsAdapter = new OdetailsAdapter(this, oDetails);
        mList.setAdapter(odetailsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_ly_left:
                finish();
                break;
        }
    }

    //获取订单详情
    private void getOrderDetails() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderId", orders.get(position).getOrder_id() + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.ORDER_DETAILS_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        oDetails = jsonUtils.getOrderDetails(json);
                        initView();
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
