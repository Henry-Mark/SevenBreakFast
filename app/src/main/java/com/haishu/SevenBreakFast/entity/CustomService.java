package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/15.
 */
public class CustomService implements Serializable {
    private String cs_name;
    private String cs_tel;
    private int cs_type;

    public String getCs_name() {
        return cs_name;
    }

    public void setCs_name(String cs_name) {
        this.cs_name = cs_name;
    }

    public String getCs_tel() {
        return cs_tel;
    }

    public void setCs_tel(String cs_tel) {
        this.cs_tel = cs_tel;
    }

    public int getCs_type() {
        return cs_type;
    }

    public void setCs_type(int cs_type) {
        this.cs_type = cs_type;
    }
}
