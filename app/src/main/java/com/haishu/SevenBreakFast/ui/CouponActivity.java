package com.haishu.SevenBreakFast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Coupon;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2016/3/14.
 */
public class CouponActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.couponList)
    private ListView mList;
    @ViewInject(R.id.noCoupon)
    private TextView noCoupon;

    private List<Coupon> coupons = new ArrayList<>();
    private List<User> users;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ViewUtils.inject(this);

        initData();
        initBar();

    }

    private void initBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvCenterText("优惠券");
        titleBarView.setLyLeftOnclickListener(this);
    }

    //获取用户优惠券列表
    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
        getCouponData();
    }

    private void initView() {
        noCoupon.setVisibility(View.GONE);
        myAdapter = new MyAdapter(this, coupons);
        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("couponMoney", coupons.get(position).getDiscount());
                intent.putExtra("couponId",coupons.get(position).getCoupon_id());
                setResult(Constant.VOUCHERS, intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_ly_left:
                finish();
                break;
        }

    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<Coupon> coupons;

        public MyAdapter(Context mContext, List<Coupon> coupons) {
            this.mContext = mContext;
            this.coupons = coupons;
        }

        @Override
        public int getCount() {
            return coupons.size();
        }

        @Override
        public Object getItem(int position) {
            return coupons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.activity_coupon_item, null);

                TextView couponName = (TextView) convertView.findViewById(R.id.couponName);
                TextView couponMoney = (TextView) convertView.findViewById(R.id.couponMoney);

                couponName.setText(coupons.get(position).getName());
                couponMoney.setText("￥ " + coupons.get(position).getDiscount());

            }
            return convertView;
        }
    }

    //获取用户优惠券列表
    private void getCouponData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.COUPON_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        coupons = jsonUtils.getCouponData(json);
                        initView();
                    } else {
                        noCoupon.setVisibility(View.VISIBLE);
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
