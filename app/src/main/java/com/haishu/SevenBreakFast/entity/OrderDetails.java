package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2016/3/24.
 */
public class OrderDetails implements Serializable {

    /**
     * od_id : 357
     * prod_name : 鸡排饭
     * prod_img :
     * prod_price : 13
     * prod_count : 1
     */

    private int od_id;
    private String prod_name;
    private String prod_img;
    private double prod_price;
    private int prod_count;

    public int getOd_id() {
        return od_id;
    }

    public void setOd_id(int od_id) {
        this.od_id = od_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_img() {
        return prod_img;
    }

    public void setProd_img(String prod_img) {
        this.prod_img = prod_img;
    }

    public double getProd_price() {
        return prod_price;
    }

    public void setProd_price(double prod_price) {
        this.prod_price = prod_price;
    }

    public int getProd_count() {
        return prod_count;
    }

    public void setProd_count(int prod_count) {
        this.prod_count = prod_count;
    }
}
