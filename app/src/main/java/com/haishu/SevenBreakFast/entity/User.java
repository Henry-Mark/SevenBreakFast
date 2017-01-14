package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by henry on 2016/3/7.
 */
public class User implements Serializable {
    private int id;
    private int user_id;
    private String user_nick;
    private String user_phone;
    private String user_img;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_img() {
        return user_img;
    }
}
