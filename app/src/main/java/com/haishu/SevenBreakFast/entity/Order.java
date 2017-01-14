package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2016/3/24.
 */
public class Order implements Serializable {

    /**
     * order_id : 198
     * order_code : 4936231458804122471
     * receiver_name : zhangsan
     * receiver_sex : 1
     * receiver_tel : 12345678999
     * add_desc : 南京工程学院211
     * service_id : 1
     * total_fee : 6.5
     * delivery_fee : 0.5
     * discount : 0
     * order_state : 1
     * coupon_id : 0
     * delivery_time : 次日07:00-07:40
     * orderTime : 2016/03/24 15:22
     */

    private int order_id;
    private String order_code;
    private String receiver_name;
    private int receiver_sex;
    private String receiver_tel;
    private String add_desc;
    private int service_id;
    private double total_fee;
    private double delivery_fee;
    private int discount;
    private int order_state;
    private int coupon_id;
    private String delivery_time;
    private String orderTime;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public int getReceiver_sex() {
        return receiver_sex;
    }

    public void setReceiver_sex(int receiver_sex) {
        this.receiver_sex = receiver_sex;
    }

    public String getReceiver_tel() {
        return receiver_tel;
    }

    public void setReceiver_tel(String receiver_tel) {
        this.receiver_tel = receiver_tel;
    }

    public String getAdd_desc() {
        return add_desc;
    }

    public void setAdd_desc(String add_desc) {
        this.add_desc = add_desc;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
