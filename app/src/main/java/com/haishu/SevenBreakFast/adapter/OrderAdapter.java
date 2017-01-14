package com.haishu.SevenBreakFast.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.entity.Order;
import com.haishu.SevenBreakFast.ui.ChoicePayActivity;
import com.haishu.SevenBreakFast.ui.CustomerServiceActivity;
import com.haishu.SevenBreakFast.ui.OrderActivity;
import com.haishu.SevenBreakFast.ui.OrderDetailsActivity;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.ShowDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2016/3/3.
 */
public class OrderAdapter extends BaseAdapter {
    private List<Order> total;
    private OrderActivity mContext;

    public OrderAdapter(OrderActivity mContext, List<Order> total) {
        this.mContext = mContext;
        this.total = total;
    }

    @Override
    public int getCount() {
        return total.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        total.clear();
    }

    public void appendToList(List<Order> list) {
        if (list == null) {
            return;
        }
        total.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_order_item,
                    null);
            holder = new ViewHolder();
            holder.orderType = (TextView) convertView.findViewById(R.id.orderType);
            holder.orderStute = (TextView) convertView.findViewById(R.id.orderStute);
            holder.orderIcon = (ImageView) convertView.findViewById(R.id.orderImage);
            holder.orderTotal = (TextView) convertView.findViewById(R.id.orderTotal);
            holder.orderTime = (TextView) convertView.findViewById(R.id.orderTime);
            holder.countinePay = (LinearLayout) convertView.findViewById(R.id.continuePay);
            holder.cancelPay = (LinearLayout) convertView.findViewById(R.id.cancelPay);
            holder.payLayout = (RelativeLayout) convertView.findViewById(R.id.payLayout);
            holder.refundLayout = (LinearLayout) convertView.findViewById(R.id.refund);
            holder.goODetails = (LinearLayout) convertView.findViewById(R.id.goOrderDetail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         *         根据服务ID 判断属于哪个类型
         *         1.早餐
         *         2.夜宵
         *         3.超市
         */

        switch (total.get(position).getService_id()) {
            case 1:
                holder.orderType.setText("早餐");
                holder.orderIcon.setImageResource(R.mipmap.home_btn_zaocan);
                break;
            case 2:
                holder.orderType.setText("夜宵");
                holder.orderIcon.setImageResource(R.mipmap.home_btn_yexiao);
                break;
            case 3:
                holder.orderType.setText("超市");
                holder.orderIcon.setImageResource(R.mipmap.home_btn_chaoshi);
                break;
        }
        /**
         *     根据订单状态 order_state，判断订单状态
         *     0 已取消
         *     1 代付款
         *     2 已付款
         *     3 待发货
         *     4 配送中
         *     5 已完成
         *     6 待退款
         */
        switch (total.get(position).getOrder_state()) {
            case 0:
                holder.orderStute.setText("已取消");
                holder.payLayout.setVisibility(View.GONE);
                break;
            case 1:
                holder.orderStute.setText("待付款");
                holder.countinePay.setVisibility(View.VISIBLE);
                holder.cancelPay.setVisibility(View.VISIBLE);
                holder.refundLayout.setVisibility(View.GONE);
                holder.payLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.orderStute.setText("已付款");
                holder.payLayout.setVisibility(View.GONE);
                break;
            case 3:
                holder.orderStute.setText("待发货");
                holder.countinePay.setVisibility(View.GONE);
                holder.cancelPay.setVisibility(View.GONE);
                holder.refundLayout.setVisibility(View.VISIBLE);
                holder.payLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.orderStute.setText("配送中");
                holder.payLayout.setVisibility(View.GONE);
                break;
            case 5:
                holder.orderStute.setText("已完成");
                holder.payLayout.setVisibility(View.GONE);
                break;
            case 6:
                holder.orderStute.setText("待退款");
                holder.payLayout.setVisibility(View.GONE);
                break;
        }
        //显示数据
        holder.orderTotal.setText("总价: ￥" + total.get(position).getTotal_fee());
        holder.orderTime.setText(total.get(position).getOrderTime());
        //继续支付
        holder.countinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否能继续支付
                isPayState(position, holder);

            }
        });
        //取消支付
        holder.cancelPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog.showQuestionDialog(mContext, "提示", "是否取消订单？？？", new ShowDialog.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        //取消支付
                        updateOrderState(position, holder, 0);
                    }
                }, new ShowDialog.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }
        });
        //申请退款
        holder.refundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog.showQuestionDialog(mContext, "提示", "是否确定退款？？？", new ShowDialog.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        refundMoney(position, holder);
                    }
                }, new ShowDialog.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }
        });
        holder.goODetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("orders", (Serializable) total);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView orderType; //类型
        private TextView orderStute;//订单状态
        private ImageView orderIcon; //图标
        private TextView orderTotal;//订单总价
        private TextView orderTime;//下单时间
        private LinearLayout countinePay;//继续支付
        private LinearLayout cancelPay;//取消支付
        private LinearLayout refundLayout;//退款
        private RelativeLayout payLayout;
        private LinearLayout goODetails; //

    }

    //修改订单状态
    public void updateOrderState(int position, final ViewHolder holder, final int state) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderCode", total.get(position).getOrder_code());
        params.addBodyParameter("state", state + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.UPDATE_ORDER_STATE_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        switch (state) {
                            case 0:
                                Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                                holder.payLayout.setVisibility(View.GONE);
                                holder.orderStute.setText("已取消");

                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                Toast.makeText(mContext, "退款成功", Toast.LENGTH_SHORT).show();
                                holder.payLayout.setVisibility(View.GONE);
                                holder.orderStute.setText("待退款");
                                mContext.upDate();
                                break;
                        }
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

    //申请退款
    private void refundMoney(final int position, final ViewHolder holder) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderCode", total.get(position).getOrder_code());
        params.addBodyParameter("time", total.get(position).getDelivery_time());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.REFUND_MONEY_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        //修改退款状态
                        updateOrderState(position, holder, 6);
                    } else {
                        ShowDialog.showQuestionDialog(mContext, "提示", "超出退款时间，请联系客服", new ShowDialog.OnClickYesListener() {
                            @Override
                            public void onClickYes() {
                                Intent intent = new Intent(mContext, CustomerServiceActivity.class);
                                mContext.startActivity(intent);
                            }
                        }, new ShowDialog.OnClickNoListener() {
                            @Override
                            public void onClickNo() {

                            }
                        });
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

    //判断是否能继续支付
    private void isPayState(final int position, final ViewHolder holder) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderCode", total.get(position).getOrder_code());
        params.addBodyParameter(Constant.SERVICE_ID, total.get(position).getService_id() + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.ISPAYSTATE_UTL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        Intent intent = new Intent(mContext, ChoicePayActivity.class);
                        intent.putExtra("orderCode", total.get(position).getOrder_code());
                        intent.putExtra("totalMoney", total.get(position).getTotal_fee());
                        mContext.startActivity(intent);
                    } else {
                        ShowDialog.showQuestionDialog(mContext, "提示", "超出时间范围，不能继续支付！！！", new ShowDialog.OnClickYesListener() {
                            @Override
                            public void onClickYes() {
                                updateOrderState(position, holder, 0);
                            }
                        }, new ShowDialog.OnClickNoListener() {
                            @Override
                            public void onClickNo() {
                                updateOrderState(position, holder, 0);
                            }
                        });
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
