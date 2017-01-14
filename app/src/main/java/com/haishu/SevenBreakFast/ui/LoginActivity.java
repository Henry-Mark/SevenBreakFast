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
import com.haishu.SevenBreakFast.utils.jsonUtils;
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


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_right_title)
    private TextView mRegister;
    @ViewInject(R.id.et_user_login)
    private EditText mUsername;
    @ViewInject(R.id.et_passwd_login)
    private EditText mPassWd;
    @ViewInject(R.id.tv_login_login)
    private TextView mLogin;
    @ViewInject(R.id.iv_x_login)
    private ImageView mX;
    @ViewInject(R.id.tv_msg_login)
    private TextView mMsgLogin;

    public static LoginActivity instance = null;

    private List<User> users = new ArrayList<>();
    private String userName;
    private String passwd;
    private String msg;
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        instance = this;
        flag = getIntent().getIntExtra(Constant.FALG,Constant.FLAG_DEFAULT);

        init();

    }

    public void init() {
        mTitle.setText("登录");
        mRegister.setText("注册");
        mBack.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mMsgLogin.setOnClickListener(this);
        mX.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title:
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
                break;
            case R.id.tv_login_login:
                getLoginInfo();
                break;
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_msg_login:
                Intent intent_MsgLogin = new Intent(mContext, LoginByMsgActivity.class);
                intent_MsgLogin.putExtra(Constant.FALG,flag);
                Log.d("flag.",flag+"");
                startActivity(intent_MsgLogin);
                break;
            case R.id.iv_x_login:
                mUsername.setText("");
            default:
                break;
        }
    }

    //登录 获取用户信息
    public void getLoginInfo() {
        userName = mUsername.getText().toString();
        passwd = mPassWd.getText().toString();
        if ((!userName.isEmpty()) && (!passwd.isEmpty())) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("uphone", userName);
            params.addBodyParameter("upwd", passwd);

            HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1000 * 10);
            http.send(HttpRequest.HttpMethod.POST, Constant.LOGIN_URL, params,
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
                                            finish();
                                        }else {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
}

