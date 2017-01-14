package com.haishu.SevenBreakFast.ui;

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

public class PluralityActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_back_title)private ImageView mBack;
    @ViewInject(R.id.tv_title)private TextView mTitle;
    @ViewInject(R.id.tv_right_title)private TextView mConform;
    @ViewInject(R.id.et_content_plurality)private EditText mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plurality);

        ViewUtils.inject(this);
        init();
    }
    public void init(){
        mTitle.setText("我要兼职");
        mConform.setText("确定");
        mBack.setOnClickListener(this);
        mConform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_right_title:
                Toast.makeText(getApplicationContext(),"sure",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
