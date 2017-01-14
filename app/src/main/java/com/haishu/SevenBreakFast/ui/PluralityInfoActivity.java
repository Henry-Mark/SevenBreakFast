package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.PluralityAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Plurality;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
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

/**
 * 兼职列表页面
 */
public class PluralityInfoActivity extends BaseActivity {

    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_right_title)
    private TextView mRight;
    @ViewInject(R.id.list_pi)
    private ListView mList;

    private List<Plurality> pluralities = new ArrayList<>();;
    private PluralityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plurality_info);
        ViewUtils.inject(this);

        init();
        getPluralityInfo();
    }

    public void init(){
        mTitle.setText("兼职类型");
        mRight.setVisibility(View.GONE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,AddPartTimeActivity.class);
                intent.putExtra(Constant.JOB_ID,pluralities.get(position).getJob_id());
                startActivity(intent);

            }
        });
    }

    public void setListView(){
        adapter = new PluralityAdapter(mContext,pluralities);
        mList.setAdapter(adapter);

    }

    public void getPluralityInfo(){
        RequestParams params = new RequestParams();

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.Plurality_URL, params, new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d("info...", responseInfo.result);
                try {
                    JSONObject result = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, result)) {
                        pluralities = jsonUtils.getPluralityList(result);
                        setListView();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                @Override
            public void onFailure(HttpException e, String s) {
                Log.d("failure",s);
            }
        });
    }
}
