package com.haishu.SevenBreakFast.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.CreateOrder;
import com.haishu.SevenBreakFast.entity.Food;
import com.haishu.SevenBreakFast.entity.SettleFood;
import com.haishu.SevenBreakFast.entity.prodInfo;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.LogUtils;
import com.haishu.SevenBreakFast.utils.PayResult;
import com.haishu.SevenBreakFast.view.TitleBarView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.apache.commons.codec.Encoder;
import org.apache.commons.collections.Bag;
import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zyw on 2016/3/18.
 */
public class ChoicePayActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.aliPay)
    private RelativeLayout aliPay;
    @ViewInject(R.id.choiceBox)
    private ImageView choiceBox;
    @ViewInject(R.id.payTrue)
    private Button payTrue;
    private boolean isChoice = false;

    private static final int SDK_PAY_FLAG = 1;
    private String orderCode;
    private double totalFree;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        updateOrderState();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ChoicePayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ChoicePayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 修改订单状态（0为已取消 3为代发货 6为待退款）
     */
    public void updateOrderState() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderCode", orderCode);
        params.addBodyParameter("state", "3");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.UPDATE_ORDER_STATE_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        Toast.makeText(ChoicePayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("FLAG", Constant.FLAG_ORDER);
                        startActivity(intent);
                        finish();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicepay);
        ViewUtils.inject(this);

        initData();
        initBar();
        initView();
    }

    //获取订单信息
    private void initData() {
        orderCode = getIntent().getStringExtra("orderCode");
        totalFree = getIntent().getDoubleExtra("totalMoney", 0);
    }

    private void initBar() {
        titleBarView.setTvCenterText("选择支付方式");
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setLyLeftOnclickListener(this);
    }

    private void initView() {
        aliPay.setOnClickListener(this);
        payTrue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aliPay:
                if (isChoice) {
                    choiceBox.setImageResource(R.mipmap.checkbox_pressed);
                    isChoice = false;
                } else {
                    choiceBox.setImageResource(R.mipmap.checkbox_normal);
                    isChoice = true;
                }
                break;
            case R.id.payTrue:
                if (!isChoice) {
                    createOrderInfo();
                }
                break;
            case R.id.common_titlebar_ly_left:
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("FLAG", Constant.FLAG_ORDER);
                startActivity(intent);
                finish();
                break;
        }
    }

    //获取服务端封装好的订单信息
    private void createOrderInfo() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderCode", orderCode);
        params.addBodyParameter("totalFee", totalFree + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.CREATE_ORDER_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        JSONArray data = json.getJSONArray("result");
                        JSONObject obj = data.getJSONObject(0);
                        pay(obj.getString("stateStr"));
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

    //调支付宝接口
    private void pay(final String stateStr) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ChoicePayActivity.this);

                // 调用支付接口，获取支付结果
                String result = alipay.pay(stateStr, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
