package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/18.
 */
public class BuildInfo implements Serializable {
    private int build_id;
    private String build_name;

    public int getBuild_id() {
        return build_id;
    }

    public void setBuild_id(int build_id) {
        this.build_id = build_id;
    }

    public String getBuild_name() {
        return build_name;
    }

    public void setBuild_name(String build_name) {
        this.build_name = build_name;
    }
}
