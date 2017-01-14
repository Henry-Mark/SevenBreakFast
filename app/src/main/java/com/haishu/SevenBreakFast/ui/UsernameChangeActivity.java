package com.haishu.SevenBreakFast.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.CharNum;
import com.haishu.SevenBreakFast.utils.SPUtils;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UsernameChangeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_right_title)
    private TextView mConform;
    @ViewInject(R.id.et_user_uc)
    private EditText mUser;
    @ViewInject(R.id.iv_x_uc)
    private ImageView mX;
    private String userName;
    private CharNum charNum;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_change);
        ViewUtils.inject(this);

        initData();
        init();
    }

    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
        if (null == users || users.size() == 0) {
        } else {
            if (users.get(0).getUser_nick().isEmpty()) {
                mUser.setText(users.get(0).getUser_phone());
            } else {
                mUser.setText(users.get(0).getUser_nick());
            }
        }
    }

    public Boolean isLengthSuit() {
        userName = mUser.getText().toString();
        charNum = new CharNum();
        int length = charNum.String_length(userName);
        if (length < 4 || length > 16) {
            return false;
        } else {
            return true;
        }
    }

    public void init() {
        mTitle.setText("修改用户名");
        mConform.setText("确定");
        mBack.setOnClickListener(this);
        mConform.setOnClickListener(this);
        mX.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_right_title:
                if (!isLengthSuit()) {
                    Toast.makeText(getApplicationContext(), "请输入4-16个字符", Toast.LENGTH_SHORT).show();
                } else {
                    changeUserInfo();
                }
                break;
            case R.id.iv_x_uc:
                mUser.setText("");
                break;
            default:
                break;
        }
    }

    //修改用户名
    private void changeUserInfo() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", "nick");
        params.addBodyParameter("value", userName);
        params.addBodyParameter("userId", users.get(0).getUser_id() + "");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.CHANGEUSERINFO_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonResult = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonResult)) {
                        List<User> users1 = new ArrayList<User>();
                        User user = new User();
                        user.setUser_id(users.get(0).getUser_id());
                        user.setUser_nick(userName);
                        user.setUser_img(users.get(0).getUser_img());
                        user.setUser_phone(users.get(0).getUser_phone());
                        users1.add(user);

                        String userInfo = new Gson().toJson(users1);
                        SPUtils.put(mContext, Constant.LOGIN_URL, userInfo);  //将数据存本地

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
