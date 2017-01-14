package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.codehaus.jackson.map.ext.JodaDeserializers;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 申请兼职所需填写的个人信息页面
 *
 */
public class AddPartTimeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_right_title)
    private TextView mConform;
    @ViewInject(R.id.et_name_apt)
    private EditText mName;
    @ViewInject(R.id.et_department_apt)
    private EditText mDepartment;
    @ViewInject(R.id.et_major_apt)
    private EditText mMajor;
    @ViewInject(R.id.et_grade_apt)
    private EditText mGrade;
    @ViewInject(R.id.et_build_apt)
    private EditText mBuild;
    @ViewInject(R.id.et_special_apt)
    private EditText mSpecial;
    @ViewInject(R.id.et_qq_apt)
    private EditText mQQ;
    @ViewInject(R.id.rg_sex_apt)
    private RadioGroup mSex;
    @ViewInject(R.id.rb_man_apt)
    private RadioButton mMan;
    @ViewInject(R.id.rb_woman_apt)
    private RadioButton mWoman;

    private String name;
    private String department;
    private String major;
    private String grade;
    private String special;
    private String qq;
    private String build;

    private int job_id;
    private int sexN = 1;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part_time);
        ViewUtils.inject(this);

        init();
    }

    public void init() {
        mTitle.setText("个人信息");
        mConform.setText("申请");
        mBack.setOnClickListener(this);
        mConform.setOnClickListener(this);
        mBuild.setOnClickListener(this);

        Intent intent = getIntent();
        job_id = intent.getIntExtra(Constant.JOB_ID, -1);
        Log.d("job_id", job_id + "");
        if (job_id == -1) {

        }

        mSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mMan.getId()) {
                    sexN = 1;
                    mMan.setChecked(true);
                    mWoman.setChecked(false);
                } else {
                    sexN = 0;
                    mMan.setChecked(false);
                    mWoman.setChecked(true);
                }
            }
        });
    }

    /**
     * 判断界面上所要填写的空，是否填写
     *
     * @return
     */
    public Boolean isEmpty() {
        name = mName.getText().toString();
        department = mDepartment.getText().toString();
        major = mMajor.getText().toString();
        grade = mGrade.getText().toString();
        special = mSpecial.getText().toString();
        qq = mQQ.getText().toString();

        if (name == null || name.equals("")) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (department == null || department.equals("")) {
            Toast.makeText(mContext, "院系不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (major == null || major.equals("")) {
            Toast.makeText(mContext, "专业不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (grade == null || grade.equals("")) {
            Toast.makeText(mContext, "年级不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mBuild.getText().toString() == null || mBuild.getText().toString().equals("")) {
            Toast.makeText(mContext, "楼栋不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (special == null || special.equals("")) {
            Toast.makeText(mContext, "特长不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (qq == null || qq.equals("")) {
            Toast.makeText(mContext, "QQ不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void http() {
        users = AppUtility.getSputilsUser(mContext);

        RequestParams params = new RequestParams();
        params.addBodyParameter("buildId", build);
        params.addBodyParameter("name", name);
        params.addBodyParameter("sex", sexN + "");
        params.addBodyParameter("department", department);
        params.addBodyParameter("major", major);
        params.addBodyParameter("grade", grade);
        params.addBodyParameter("specialty", special);
        params.addBodyParameter("phone", users.get(0).getUser_phone());
        params.addBodyParameter("qq", qq);
        params.addBodyParameter("placeId", SPUtils.get(mContext, Constant.PLACE_ID, 0).toString());
        params.addBodyParameter("jobId", String.valueOf(job_id));

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.AddPlurality_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d("success", responseInfo.result);
                JSONObject jsonResult = null;
                try {
                    jsonResult = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonResult)) {
                        AddPartTimeActivity.this.finish();
                    }
                    Toast.makeText(mContext, AppUtility.getMessage(mContext, jsonResult), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("failure", s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.tv_right_title:
                if (isEmpty()) {
                    http();
                }
                break;
            case R.id.et_build_apt:
                Intent intent = new Intent(mContext, BuildingActivity.class);
                startActivityForResult(intent, Constant.CHOICE_ADDRESS);
                break;
            default:
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
                    build = String.valueOf(data.getIntExtra("build", 0));
                    mBuild.setText(data.getStringExtra("buildName") + "");
                }
            }
        }
    }
}
