package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Address;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.view.TitleBarView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zyw on 2016/3/4.
 */
public class AddToAddressActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.address_college)
    private EditText address_college;
    @ViewInject(R.id.addressBuild)
    private EditText address_Build;
    @ViewInject(R.id.address_door)
    private EditText address_door;
    @ViewInject(R.id.address_name)
    private EditText address_name;
    @ViewInject(R.id.address_phone)
    private EditText address_phone;
    @ViewInject(R.id.rb_man)
    private RadioButton man;
    @ViewInject(R.id.rb_woman)
    private RadioButton woman;
    @ViewInject(R.id.rg_sex)
    private RadioGroup sex;

    private String addressBuild;
    private String addressDoor;
    private String addressSex;
    private String addressName;
    private String addressPhone;
    private String addDesc;
    private int sexN = 1;

    private List<User> users;
    private int placeId;
    private String build;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtoaddress);
        ViewUtils.inject(this);

        initData();
        initView();
        initBar();
    }

    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
        placeId = (int) SPUtils.get(mContext, Constant.PLACE_ID, 0);
    }

    //初始化titleBar
    private void initBar() {
        titleBarView.setTvCenterText("收货地址");
        titleBarView.setTvRightText("新增");
        titleBarView.setTvRightTextColor(getResources().getColor(R.color.main_title));
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvRightOnclickListener(this);
        titleBarView.setLyLeftOnclickListener(this);
    }

    private void initView() {
        address_college.setText((String) SPUtils.get(mContext, Constant.PLACE_NAME, ""));
        address_college.setFocusable(false);
        address_Build.setOnClickListener(this);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == man.getId()){
                    sexN = 1;
                }else{
                    sexN = 0;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_tv_right:
                addToAddressData();
                break;
            case R.id.common_titlebar_ly_left:
                finish();
                break;
            case R.id.addressBuild:
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
                    build = String.valueOf(data.getIntExtra("build",0));
                    addressBuild = data.getStringExtra("buildName");
                    address_Build.setText(addressBuild);
                }
            }
        }
    }

    //添加收货地址
    private void addToAddressData() {
        addressDoor = address_door.getText().toString();
        addressSex = String.valueOf(sexN);
        addressName = address_name.getText().toString();
        addressPhone = address_phone.getText().toString();

        addDesc = SPUtils.get(mContext, Constant.PLACE_NAME, "") + addressBuild + addressDoor;

        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        params.addBodyParameter(Constant.PLACE_ID, placeId + "");
        params.addBodyParameter("buildId", build);
        params.addBodyParameter("otherInfo", addressDoor);
        params.addBodyParameter("receiverName", addressName);
        params.addBodyParameter("receiverSex", addressSex);
        params.addBodyParameter("receiverTel", addressPhone);
        params.addBodyParameter("addDesc", addDesc);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.ADDTOADDRESS_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonResult = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonResult)) {
                        Toast.makeText(mContext, AppUtility.getMessage(mContext, jsonResult), Toast.LENGTH_SHORT).show();
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
