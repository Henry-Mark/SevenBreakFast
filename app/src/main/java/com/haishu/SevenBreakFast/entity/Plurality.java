package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by henry on 2016/3/22.
 */
public class Plurality implements Serializable {

    private int job_id;
    private String job_name;

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_name() {
        return job_name;
    }
}
