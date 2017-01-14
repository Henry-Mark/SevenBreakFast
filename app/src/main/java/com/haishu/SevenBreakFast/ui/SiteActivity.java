package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.AppManager;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.ShowDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class SiteActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.settingName)
    private TextView settingName;
    @ViewInject(R.id.tv_right_title)
    TextView mRight;
    @ViewInject(R.id.rl_user_site)
    RelativeLayout mUser;
    @ViewInject(R.id.rl_phone_site)
    RelativeLayout mPhone;
    @ViewInject(R.id.rl_passwd_site)
    RelativeLayout mPasswd;
    @ViewInject(R.id.exitBtn)
    private Button exitBtn;

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);
        ViewUtils.inject(this);
        init();
        setView();
    }

    //填充数据
    private void setView() {
        users = AppUtility.getSputilsUser(mContext);
        if (users.get(0).getUser_nick().isEmpty()) {
            settingName.setText(users.get(0).getUser_phone());
        } else {
            settingName.setText(users.get(0).getUser_nick());
        }
    }

    public void init() {
        mTitle.setText("设置");
        mRight.setVisibility(View.GONE);
        mBack.setOnClickListener(this);
        mUser.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mPasswd.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.rl_user_site:
                Intent user_intent = new Intent(this, UsernameChangeActivity.class);
                startActivity(user_intent);
                break;
            case R.id.rl_phone_site:
                Toast.makeText(mContext, "手机号码暂时无法修改", Toast.LENGTH_SHORT).show();
//                Intent phone_intent = new Intent(this, PhoneChangeActivity.class);
//                startActivity(phone_intent);
                break;
            case R.id.rl_passwd_site:
                Intent passwd_intent = new Intent(this, PasswdChangeActivity.class);
                startActivity(passwd_intent);
                break;
            case R.id.exitBtn:
                ShowDialog.showQuestionDialog(mContext, "提示", "是否退出登录", new ShowDialog.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        SPUtils.remove(mContext, Constant.LOGIN_URL);
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new ShowDialog.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }
}
