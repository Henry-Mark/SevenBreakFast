package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Address;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by zyw on 2016/3/11.
 */
public class AddressDetailsActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.add_details_college)
    private EditText add_details_college;
    @ViewInject(R.id.add_details_floor)
    private EditText add_details_floor;
    @ViewInject(R.id.add_details_door)
    private EditText add_details_door;
    @ViewInject(R.id.add_details_otherinfo)
    private EditText add_details_otherinfo;
    @ViewInject(R.id.add_details_name)
    private EditText add_details_name;
    @ViewInject(R.id.add_details_sex)
    private EditText add_details_sex;
    @ViewInject(R.id.add_details_tel)
    private EditText add_details_tel;

    @ViewInject(R.id.add_details_del)
    private Button delButton;

    @ViewInject(R.id.rb_man)
    private RadioButton man;
    @ViewInject(R.id.rb_woman)
    private RadioButton woman;
    @ViewInject(R.id.rg_sex)
    private RadioGroup sex;
    private int sexN;


    private String addressDetailsFloor;
    private String addressDetailsDoor;
    private String addressDetailsOtherInfo;
    private String addressDetailsName;
    private int addressDetailsSex;
    private String addressDetailsTel;

    private String addressId;
    private int build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressdetails);
        ViewUtils.inject(this);

        addressId = getIntent().getStringExtra("addressId");

        initData();
        initView();
        initBar();
    }

    private void initData() {
        getAddressDetails();
    }

    private void initBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvCenterText("管理收货地址");
        titleBarView.setTvRightText("修改");
        titleBarView.setLyLeftOnclickListener(this);
        titleBarView.setTvRightOnclickListener(this);
    }

    private void initView() {
        delButton.setOnClickListener(this);
        add_details_floor.setFocusable(false);
        add_details_floor.setOnClickListener(this);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == man.getId()) {
                    sexN = 1;
                } else {
                    sexN = 0;
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
            case R.id.common_titlebar_tv_right:
                modifyUserAddress();
                break;
            case R.id.add_details_del:
                delAddressData();
                break;
            case R.id.add_details_floor:
                Intent intent = new Intent(mContext, BuildingActivity.class);
                startActivityForResult(intent, Constant.CHOICE_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (Constant.CHOICE_ADDRESS == requestCode) {
                if (data == null) {

                } else {
                    build = data.getIntExtra("build", 0);
                    addressDetailsFloor = data.getStringExtra("buildName");
                    add_details_floor.setText(addressDetailsFloor);
                }
            }
        }
    }

    //修改用户收货地址
    private void modifyUserAddress() {
        addressDetailsFloor = add_details_floor.getText().toString();
//        addressDetailsDoor = add_details_door.getText().toString();
        addressDetailsOtherInfo = add_details_otherinfo.getText().toString();
        addressDetailsName = add_details_name.getText().toString();
        addressDetailsTel = add_details_tel.getText().toString();

        String addDesc = SPUtils.get(mContext, Constant.PLACE_NAME, "") + addressDetailsFloor + addressDetailsOtherInfo;

        RequestParams params = new RequestParams();
        params.addBodyParameter("addressId", addressId);
        params.addBodyParameter("buildId", build + "");
        params.addBodyParameter("otherInfo", addressDetailsOtherInfo);
        params.addBodyParameter("receiverName", addressDetailsName);
        params.addBodyParameter("receiverSex", sexN + "");
        params.addBodyParameter("receiverTel", addressDetailsTel);
        params.addBodyParameter("addDesc", addDesc);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.MODIFY_USER_ADDRESS_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        Toast.makeText(mContext, AppUtility.getMessage(mContext, json), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, AppUtility.getMessage(mContext, json), Toast.LENGTH_SHORT).show();
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

    //删除用户数据
    private void delAddressData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("addressId", addressId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.DELADDRESS_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonObject)) {
                        Toast.makeText(mContext, AppUtility.getMessage(mContext, jsonObject), Toast.LENGTH_SHORT).show();
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

    //获取用户收货地址详情
    private void getAddressDetails() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("addressId", addressId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.ADDRESSDETAILS_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonObject)) {
                        JSONArray jsonarray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            build = obj.getInt("build_id");
                            addressDetailsFloor = obj.getString("build_name");
//                            addressDetailsDoor = obj.getString("build_name");
                            addressDetailsOtherInfo = obj.getString("other_info");
                            addressDetailsName = obj.getString("receiver_name");
                            addressDetailsSex = obj.getInt("receiver_sex");
                            addressDetailsTel = obj.getString("receiver_tel");
                        }
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

    private void setDataView() {
        add_details_college.setText((String) SPUtils.get(mContext, Constant.PLACE_NAME, ""));
        add_details_college.setFocusable(false);
        add_details_floor.setText(addressDetailsFloor);
//        add_details_door.setText(addressDetailsDoor);
        add_details_otherinfo.setText(addressDetailsOtherInfo);
        add_details_name.setText(addressDetailsName);
        if (addressDetailsSex == 0) {
            woman.setText("女");
            woman.setChecked(true);
            man.setChecked(false);
        } else if (addressDetailsSex == 1) {
            man.setText("男");
            man.setChecked(true);
            woman.setChecked(false);
        }
        add_details_tel.setText(addressDetailsTel);
    }
}
