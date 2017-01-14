package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.TimerCount;
import com.haishu.SevenBreakFast.utils.jsonUtils;
import com.haishu.SevenBreakFast.utils.StringCompare;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_right_title)
    private TextView mLogin;
    @ViewInject(R.id.tv_code_register)
    private TextView mCodeGet;
    @ViewInject(R.id.et_user_register)
    private EditText mUsername;
    @ViewInject(R.id.et_code_register)
    private EditText mCode;
    @ViewInject(R.id.et_passwd_register)
    private EditText mPasswd;
    @ViewInject(R.id.et_passwd_conform_register)
    private EditText mPasswdComform;
    @ViewInject(R.id.et_phonenum_register)
    private EditText mPhone;
    @ViewInject(R.id.tv_register_register)
    private TextView mRegister;
    @ViewInject(R.id.writePhone)
    private TextView writePhone;
    private String user;
    private String passwd;
    private String passwdConfrom;
    private String phone = "";
    private jsonUtils json;
    private StringCompare stringCompare;
    private String codeGet;
    private String code;
    private Boolean isCodeGet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        stringCompare = new StringCompare();

        ViewUtils.inject(this);
        init();

    }

    public void init() {
        mTitle.setText("注册");
        mLogin.setText("登录");
        mLogin.setVisibility(View.GONE);
        mLogin.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCodeGet.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        writePhone.setOnClickListener(this);
    }

    public void getCode() {
        user = mUsername.getText().toString();
        if (user.length() == 11) {
            httpGet();
        } else {
            Toast.makeText(getApplicationContext(), "请输入11位的手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    public void httpGet() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uphone", user);
        params.addQueryStringParameter("type", "register");

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
                            String msg = AppUtility.getMessage(mContext, jsonResult);
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_code_register:
                getCode();
//                Toast.makeText(getApplicationContext(),"获取验证码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_right_title:
                break;
            case R.id.tv_register_register:
                doRegister();
                break;
            case R.id.writePhone:
                writePhone.setVisibility(View.GONE);
                mPhone.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void doRegister() {
        codeGet = mCode.getText().toString() + "";
        passwd = mPasswd.getText().toString() + "";
        phone = mPhone.getText().toString() + "";
        passwdConfrom = mPasswdComform.getText().toString() + "";
        Log.i("passwd", passwd);
        Log.i("phone", String.valueOf(phone.isEmpty()));
        if (isCodeGet) {
            if (code.isEmpty()) {
                Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            } else if (passwd.length() < 6 || passwd.length() > 12) {
                Toast.makeText(getApplicationContext(), "请填写6-12个数字或字母", Toast.LENGTH_SHORT).show();
            } else if (!stringCompare.isStringEqual(passwd, passwdConfrom)) {
                Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
            } else if (!stringCompare.isStringEqual(code, codeGet)) {
                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
            } else {
                httpRegister();
            }
        } else {
            Toast.makeText(getApplicationContext(), "请先获取验证码", Toast.LENGTH_SHORT).show();
        }

    }

    public void httpRegister() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uphone", user);
        params.addQueryStringParameter("upwd", passwd);
        params.addQueryStringParameter("invitePhone", phone);

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST,
                Constant.REGISTER_URL,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i(".....Success", responseInfo.result);
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(responseInfo.result);
                            String back = AppUtility.getMessage(mContext, jsonResult);
                            if (AppUtility.getStatus(mContext, jsonResult)) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(getApplicationContext(), back, Toast.LENGTH_SHORT).show();
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
}
