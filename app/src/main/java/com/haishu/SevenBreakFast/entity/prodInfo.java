package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2016/3/24.
 */
public class prodInfo implements Serializable {
    private List<SettleFood> prodList;

    public List<SettleFood> getProdList() {
        return prodList;
    }

    public void setProdList(List<SettleFood> prodList) {
        this.prodList = prodList;
    }
}