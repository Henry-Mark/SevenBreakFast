package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.PlaceAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Place;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceSelectActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.iv_back_title)
    private ImageView mBack;
    @ViewInject(R.id.tv_right_title)
    private TextView mRight;
    @ViewInject(R.id.list_ps)
    private ListView mList;
    private PlaceAdapter placeAdapter;
    //    private List<String> list;
    private List<Place> places;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_select);
        ViewUtils.inject(this);

        getPlace();
        init();


    }

    public void init() {
        mBack.setOnClickListener(this);
        mTitle.setText("地点选择");
        mRight.setVisibility(View.GONE);

        mBack.setVisibility(View.VISIBLE);
    }

    public void setList() {
        placeAdapter = new PlaceAdapter(this, places);
        mList.setAdapter(placeAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPUtils.put(mContext, Constant.PLACE_ID, places.get(position).getPlace_id());
                SPUtils.put(mContext, Constant.PLACE_NAME, places.get(position).getPlace_name());
                Intent intent_main = new Intent(PlaceSelectActivity.this, MainActivity.class);
                intent_main.putExtra("FLAG", Constant.FLAG_HOME);
                startActivity(intent_main);
                finish();
            }
        });
    }


    public void getPlace() {

        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST, Constant.GET_PLACE_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject jsonResult = new JSONObject(responseInfo.result);
                            if (AppUtility.getStatus(mContext, jsonResult)) {
                                places = jsonUtils.getPlace(mContext, jsonResult);
                                setList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
        }
    }
}
