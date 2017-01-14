package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/10.
 */
public class AddressList implements Serializable {
    private int add_id;
    private String add_desc;
    private String receiver_name;
    private String receiver_tel;

    public int getAdd_id() {
        return add_id;
    }

    public void setAdd_id(int add_id) {
        this.add_id = add_id;
    }

    public String getAdd_desc() {
        return add_desc;
    }

    public void setAdd_desc(String add_desc) {
        this.add_desc = add_desc;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_tel() {
        return receiver_tel;
    }

    public void setReceiver_tel(String receiver_tel) {
        this.receiver_tel = receiver_tel;
    }
}
