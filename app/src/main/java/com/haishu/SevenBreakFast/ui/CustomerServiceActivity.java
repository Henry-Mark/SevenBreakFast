package com.haishu.SevenBreakFast.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.CustomService;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.customerCenter)
    private ListView mList;

    private List<CustomService> customers = new ArrayList<>();
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        ViewUtils.inject(this);

        getCustomerCenter();
        initData();
//        initView();
        initBar();
    }

    //获取客服中心数据
    private void initData() {

    }

    private void initBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvCenterText("客服中心");
        titleBarView.setLyLeftOnclickListener(this);
    }

    public void initView() {
        myAdapter = new MyAdapter(customers);
        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customers.get(position).getCs_tel()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_ly_left:
                finish();
                break;
            default:
                break;
        }
    }

    //获取客服中心数据
    public void getCustomerCenter() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.CUSTOM_SERVICE_CENTER_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    Log.d("....",responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        customers = jsonUtils.getCustomService(json);
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

    class MyAdapter extends BaseAdapter {
        private List<CustomService> customers;

        public MyAdapter(List<CustomService> customers) {
            this.customers = customers;
        }

        @Override
        public int getCount() {
            return customers.size();
        }

        @Override
        public Object getItem(int position) {
            return customers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.activity_custom_item, null);
            }
            TextView customName = (TextView) convertView.findViewById(R.id.customName);
            TextView customTel = (TextView) convertView.findViewById(R.id.customTel);

            if(customers.get(position).getCs_type() == 0) {
                customName.setText("7早客服");
            }else if(customers.get(position).getCs_type() == 1){
                customName.setText("校园客服");
            }
            customTel.setText(customers.get(position).getCs_tel());
            return convertView;
        }
    }
}
