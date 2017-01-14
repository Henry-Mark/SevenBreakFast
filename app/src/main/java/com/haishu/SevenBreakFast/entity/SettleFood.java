package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/24.
 */
public class SettleFood implements Serializable {
    private int pid;
    private int count;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
