package com.haishu.SevenBreakFast.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhoneChangeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)private ImageView mBack;
    @ViewInject(R.id.tv_title)private TextView mTitle;
    @ViewInject(R.id.tv_right_title)private TextView mConform;
    @ViewInject(R.id.et_phone_new_phc)private EditText mPhoneNew;
    @ViewInject(R.id.tv_verification_get_phc)private TextView mVerificationGet;
    @ViewInject(R.id.et_verification_phc)private EditText mVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change_);
        ViewUtils.inject(this);
        init();
    }

    public void init(){
        mTitle.setText("修改手机");
        mConform.setText("确定");
        mBack.setOnClickListener(this);
        mConform.setOnClickListener(this);
        mVerificationGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_right_title:
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_verification_get_phc:
                Toast.makeText(getApplicationContext(),"短信验证",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
