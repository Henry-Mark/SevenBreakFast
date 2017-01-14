package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.adapter.OrderAdapter;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Order;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
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

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class OrderActivity extends BaseActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    public static final int LOADING_DURATION = 2000;
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.refresh_layout)
    private BGARefreshLayout mRefreshLayout;
    @ViewInject(R.id.myOrder)
    private ListView mList;
    @ViewInject(R.id.orderNoLogin)
    private LinearLayout noLogin;
    @ViewInject(R.id.noOrder)
    private TextView noOrder;
    private List<User> users;
    private List<Order> orders = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private int pageSize = 1;
    private int sum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ViewUtils.inject(this);


        initData();
        initRefreshLayout();
        initBar();
        initView();
    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(OrderActivity.this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        // 设置下拉刷新和上拉加载更多的风格
    }

    /**
     * 获取服务数据
     * 首先判断users是否存在，存在之后再判断是否有订单
     */
    private void initData() {
        users = AppUtility.getSputilsUser(mContext);
        if (users == null || users.size() == 0) {
            noLogin.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
            noOrder.setVisibility(View.GONE);
        } else {
            noLogin.setVisibility(View.GONE);
//            getOrderData(pageSize);
//            if (orders == null || orders.size() == 0) {
//                mList.setVisibility(View.GONE);
//                noOrder.setVisibility(View.VISIBLE);
//            } else {
//                mList.setVisibility(View.VISIBLE);
//                noOrder.setVisibility(View.GONE);
//            }
        }
    }

    /**
     * 初始化titleBarView
     */
    private void initBar() {
        noLogin.setOnClickListener(this);
        titleBarView.setTvCenterText(R.string.myOrder);
        titleBarView.setTitleBackgroundResource(R.color.main_title);
        titleBarView.setTvCenterTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 加载数据
     */
    private void initView() {
        orderAdapter = new OrderAdapter(OrderActivity.this, orders);
        mList.setAdapter(orderAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        beginRefreshing();
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        users = AppUtility.getSputilsUser(mContext);
        if (users == null || users.size() == 0) {
            noLogin.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
            noOrder.setVisibility(View.GONE);
        } else {
            noLogin.setVisibility(View.GONE);
//            if (orders == null || orders.size() == 0) {
//                mList.setVisibility(View.GONE);
//                noOrder.setVisibility(View.VISIBLE);
//            } else {
//                mList.setVisibility(View.VISIBLE);
//                noOrder.setVisibility(View.GONE);
//            }
        }
    }

    /**
     * 获取订单列表数据
     *
     * @param page 页数
     */
    private void getOrderData(final int page) {
        RequestParams params = new RequestParams();
        params.addBodyParameter(Constant.USER_ID, users.get(0).getUser_id() + "");
        params.addBodyParameter("page", page + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.GETORDER_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonResult = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, jsonResult)) {
                        noOrder.setVisibility(View.GONE);
                        mList.setVisibility(View.VISIBLE);

                        orders = jsonUtils.getOrder(jsonResult);
                        orderAdapter.appendToList(orders);


                        if (orders.size() < 10) {
                            sum = 1000000;
                        } else {

                        }
                        initView();
                    } else {
                        mList.setVisibility(View.GONE);
                        noOrder.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                noOrder.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderNoLogin:
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 在这里加载最新数据
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(OrderActivity.LOADING_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                orderAdapter.clear();
                pageSize = 1;
                sum = 1;
                users = AppUtility.getSputilsUser(mContext);
                if (users == null || users.size() == 0) {
                    noLogin.setVisibility(View.VISIBLE);
                    mList.setVisibility(View.GONE);
                    noOrder.setVisibility(View.GONE);
                } else {
                    noLogin.setVisibility(View.GONE);
                    getOrderData(pageSize);
                }
                mRefreshLayout.endRefreshing();
            }
        }.execute();
    }

    // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(OrderActivity.LOADING_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 加载完毕后在UI线程结束加载更多
                if (sum > pageSize) {
                    Toast.makeText(mContext, "没有数据了！！！", Toast.LENGTH_SHORT).show();
                } else {
                    pageSize++;
                    sum++;
                    getOrderData(pageSize);
                }
                mRefreshLayout.endLoadingMore();
            }
        }.execute();
        return true;
    }

    public void upDate() {
        orderAdapter.clear();
        getOrderData(1);
    }
}
