package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/4.
 */
public class Food implements Serializable {
    private String id;
    private String prodId;
    private String prodImg; //食物图片
    private String prodName; //食物名称
    private String prodPrice; //食物单价
    private int prodCount;//数量
    private double prodTotal;//总价

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdImg() {
        return prodImg;
    }

    public void setProdImg(String prodImg) {
        this.prodImg = prodImg;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdCount() {
        return prodCount;
    }

    public void setProdCount(int prodCount) {
        this.prodCount = prodCount;
    }

    public double getProdTotal() {
        return prodTotal;
    }

    public void setProdTotal(double prodTotal) {
        this.prodTotal = prodTotal;
    }
}
