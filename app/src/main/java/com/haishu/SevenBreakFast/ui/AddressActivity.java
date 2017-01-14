package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.AddressAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.AddressList;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.LogUtils;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2016/3/4.
 */
public class AddressActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.addressList)
    private ListView myListView;
    @ViewInject(R.id.add_address)
    private LinearLayout add_address;

    private List<AddressList> addressLists = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private AddressAdapter addressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ViewUtils.inject(this);

        initData();
        initView();
    }

    private void initData() {
//        users = AppUtility.getSputilsUser(mContext);
//        getAddToAddress();
    }

    //初始化
    private void initView() {
        //titleBar 的设置
        titleBarView.setTvCenterText(R.string.harvest_address);
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setLyLeftOnclickListener(this);
        add_address.setOnClickListener(this);

    }

    private void setDataView() {
        addressAdapter = new AddressAdapter(this, addressLists);
        myListView.setAdapter(addressAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("addtoaddress".equals(getIntent().getStringExtra("addtoaddress"))) {
                    Intent intent = new Intent(mContext, AddressDetailsActivity.class);
                    intent.putExtra("addressId", addressLists.get(position).getAdd_id() + "");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    intent.putExtra("addressList", (Serializable) addressLists);
                    setResult(Constant.CHOICE_ADDRESS, intent);
                    settingDefault(position, addressLists);
                    finish();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address:
                Intent intent = new Intent(mContext, AddToAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.common_titlebar_ly_left:
                finish();
                break;
        }
    }

    //获取用户收货地址
    private void getAddToAddress() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.GETADDRESS_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonObject)) {
                        addressLists = jsonUtils.getAddress(jsonObject);
                        setDataView();
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

    //设置默认地址
    private void settingDefault(int position, List<AddressList> addressLists) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("addressId", addressLists.get(position).getAdd_id() + "");
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.SETTING_DEFULT_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        LogUtils.i(TAG, "-------------设置默认地址成功--------------");
                    } else {
                        LogUtils.i(TAG, "-------------设置默认地址失败--------------");
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
    protected void onResume() {
        super.onResume();
        users = AppUtility.getSputilsUser(mContext);
        getAddToAddress();
    }
}
