package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/11.
 * 配送信息实体
 */
public class SettlementInfo implements Serializable{
    private String time;
    private int lowestFee;
    private double cost;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLowestFee() {
        return lowestFee;
    }

    public void setLowestFee(int lowestFee) {
        this.lowestFee = lowestFee;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
