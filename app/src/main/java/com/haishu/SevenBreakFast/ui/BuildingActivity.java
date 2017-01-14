package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.BuildInfo;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.jsonUtils;
import com.haishu.SevenBreakFast.view.TitleBarView;
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
 * Created by zyw on 2016/3/18.
 */
public class BuildingActivity extends BaseActivity {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.buildList)
    private ListView mList;

    private List<BuildInfo> builds = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        ViewUtils.inject(this);

        initData();
        initBar();
    }

    private void initData() {
        getBuildingData();
    }

    private void initBar() {
        titleBarView.setTvCenterText("选择宿舍楼");
    }

    private void initView() {
        myAdapter = new MyAdapter(builds);
        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent = new Intent();
                intent.putExtra("build",builds.get(position).getBuild_id());
                intent.putExtra("buildName",builds.get(position).getBuild_name());
                setResult(0,intent);
                finish();
            }
        });

    }

    private void getBuildingData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.PLACE_ID, SPUtils.get(mContext, Constant.PLACE_ID, 0) + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.BUILDING_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        builds = jsonUtils.getBuild(json);
                        initView();
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

    class MyAdapter extends BaseAdapter {

        private List<BuildInfo> builds;

        public MyAdapter(List<BuildInfo> builds) {
            this.builds = builds;
        }

        @Override
        public int getCount() {
            return builds.size();
        }

        @Override
        public Object getItem(int position) {
            return builds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.activity_building_item, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.buildName);
            name.setText(builds.get(position).getBuild_name());
            return convertView;
        }
    }
}
