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

import java.util.List;

public class PasswdChangeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_right_title)
    private TextView mRight;
    @ViewInject(R.id.et_code_pc)
    private EditText mCode;
    @ViewInject(R.id.tv_code_pc)
    private TextView mCodeGet;
    @ViewInject(R.id.et_psd_new_pc)
    private EditText mPsdNew;
    @ViewInject(R.id.et_psd_new_conform_pc)
    private EditText mPsdNewConform;
    @ViewInject(R.id.tv_conform_pc)
    private TextView mConform;
    @ViewInject(R.id.tv_phonenum_pc)
    private TextView mPhoneNum;
    private String psd_new;
    private String psd_new1;
    private String code;
    private String codeGet;
    private String msg;
    private Boolean isChanged = false;
    private String result;
    private Boolean isCodeGet = false;
    private StringCompare sc;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd_change);
        ViewUtils.inject(this);

        initData();
        init();
    }

    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
    }


    public void init() {
        mTitle.setText("修改密码");
        mPhoneNum.setText(users.get(0).getUser_nick());
        mRight.setVisibility(View.GONE);
        mCodeGet.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mConform.setOnClickListener(this);
    }

    /**
     * 获取验证码
     */
    public void httpGetCode() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uphone",users.get(0).getUser_phone());
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
                            isCodeGet = AppUtility.getStatus(mContext, jsonResult);
                            msg = AppUtility.getMessage(mContext, jsonResult);
                            if (isCodeGet) {
                                TimerCount timer = new TimerCount(60000, 1000, mCodeGet);
                                timer.start();

                                JSONArray data = jsonResult.getJSONArray("result");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    code = obj.getString("msgCode");
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 修改密码
     */
    public void doPost() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uphone", users.get(0).getUser_phone());
        params.addQueryStringParameter("upwd", psd_new);

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST,
                Constant.MODIFY_PASSWORD_URL,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i(".....Success", responseInfo.result);
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(responseInfo.result);
                            isChanged = AppUtility.getStatus(mContext, jsonResult);

                            Toast.makeText(getApplicationContext(),
                                    AppUtility.getMessage(mContext, jsonResult),
                                    Toast.LENGTH_SHORT).show();
                            if (isChanged) {
                                Intent intent = new Intent(PasswdChangeActivity.this, LoginActivity.class);
                                intent.putExtra(Constant.FALG,Constant.FLAG_MINE);
                                startActivity(intent);
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
    }

    /**
     * 判断密码是否一致
     */
    public void doComform() {
        codeGet = mCode.getText().toString() + "";
        psd_new = mPsdNew.getText().toString() + "";
        psd_new1 = mPsdNewConform.getText().toString() + "";
        sc = new StringCompare();
        if (isCodeGet) {
            if (sc.isStringEqual(codeGet, code)) {
                if (psd_new.length() < 6 || psd_new.length() > 12) {
                    Toast.makeText(getApplicationContext(), "请填写6-12个数字或字母", Toast.LENGTH_SHORT).show();
                }else {
                    if (sc.isStringEqual(psd_new, psd_new1)) {
                        doPost();
                    } else {

                        Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(getApplicationContext(), "验证码填写有误", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_conform_pc:
                if ("".equals(codeGet) || "".equals(psd_new) || "".equals(psd_new1)) {
                    Toast.makeText(getApplicationContext(), "空格必须填写", Toast.LENGTH_SHORT).show();
                } else {
                    doComform();
                }
                break;
            case R.id.tv_code_pc:
                httpGetCode();
                break;
            default:
                break;
        }
    }
}
