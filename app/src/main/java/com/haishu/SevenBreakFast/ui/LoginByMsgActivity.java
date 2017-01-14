package com.haishu.SevenBreakFast.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.StringCompare;
import com.haishu.SevenBreakFast.utils.TimerCount;
import com.haishu.SevenBreakFast.utils.jsonUtils;
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

import java.util.ArrayList;
import java.util.List;

public class LoginByMsgActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_right_title)
    private TextView mRight;
    @ViewInject(R.id.et_code_ml)
    private EditText mCodeInput;
    @ViewInject(R.id.iv_x_ml)
    private ImageView mClear;
    @ViewInject(R.id.et_user_ml)
    private EditText mUser;
    @ViewInject(R.id.tv_codeGet_ml)
    private TextView mCodeGet;
    @ViewInject(R.id.tv_login_ml)
    private TextView mLogin;

    private List<User> users = new ArrayList<>();
    private String code = null;
    private String phone = "";
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_msg);
        ViewUtils.inject(this);
        flag = getIntent().getIntExtra(Constant.FALG,Constant.FLAG_DEFAULT);
        Log.d("flag..",flag+"");
        init();

    }

    private void init() {
        mTitle.setText("短信登录");
        mRight.setVisibility(View.GONE);
        mBack.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mCodeGet.setOnClickListener(this);
        mLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.iv_x_ml:
                mUser.setText("");
                break;
            case R.id.tv_codeGet_ml:
                getCode();
                break;
            case R.id.tv_login_ml:
                String codeIput = mCodeInput.getText().toString();
                Log.d("...codeInput", codeIput);
                Log.d("...code", code);
                if (!codeIput.isEmpty()) {
                    StringCompare sc = new StringCompare();
                    if (code.equals(codeIput)) {
                        login();
                    } else {
                        Toast.makeText(mContext, "验证码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 获取验证码
     */
    public void getCode() {

        phone = mUser.getText().toString();
        if (phone.length() == 11) {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("uphone", phone);
            params.addQueryStringParameter("type", "other");

            HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1000 * 10);
            http.send(HttpRequest.HttpMethod.POST,
                    Constant.CODE_GET_URL,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.i(".....Success", responseInfo.result);
                            JSONObject jsonResult = null;
                            try {
                                jsonResult = new JSONObject(responseInfo.result);
                                String msg = AppUtility.getMessage(mContext, jsonResult);

                                if (AppUtility.getStatus(mContext, jsonResult)) {
                                    TimerCount timer = new TimerCount(60000, 1000, mCodeGet);
                                    timer.start();
                                    JSONArray data = jsonResult.getJSONArray("result");
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject obj = data.getJSONObject(i);
                                        code = obj.getString("msgCode");
                                    }
                                } else {
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Log.i(".....Failure", s.toString());

                        }
                    });
        } else {
            Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 登录
     */
    public void login() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uphone", phone);

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST, Constant.LOGIN_MSG_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject jsonResult = new JSONObject(responseInfo.result);
                            Toast.makeText(getApplicationContext(),
                                    AppUtility.getMessage(mContext, jsonResult),
                                    Toast.LENGTH_SHORT).show();
                            if (AppUtility.getStatus(mContext, jsonResult)) {
                                users = jsonUtils.getLogin(mContext, jsonResult);
                                if (null == users || users.size() == 0) {

                                } else {
                                    if(flag == Constant.FLAG_DEFAULT){
                                        LoginActivity.instance.finish();
                                        finish();
                                    }else {
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.putExtra(Constant.FALG, flag);
                                        startActivity(intent);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i(".....Failure", s.toString());
                    }
                }
        );
    }
}
