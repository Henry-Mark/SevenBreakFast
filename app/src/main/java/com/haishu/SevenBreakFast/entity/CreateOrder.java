package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2016/3/24.
 */
public class CreateOrder implements Serializable {
    private int userId;
    private int addressId;
    private int serviceId;
    private double totalPrice;
    private double discount;
    private int payWay;
    private double totalFee;
    private double deliveryFee;
    private int couponId;
    private prodInfo prodInfo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public prodInfo getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(prodInfo prodInfo) {
        this.prodInfo = prodInfo;
    }
}
