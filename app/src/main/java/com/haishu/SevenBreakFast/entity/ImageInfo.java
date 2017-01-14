package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/18.
 */
public class ImageInfo implements Serializable {
    private int adv_id;
    private String adv_img;
    private String adv_desc;

    public int getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(int adv_id) {
        this.adv_id = adv_id;
    }

    public String getAdv_img() {
        return adv_img;
    }

    public void setAdv_img(String adv_img) {
        this.adv_img = adv_img;
    }

    public String getAdv_desc() {
        return adv_desc;
    }

    public void setAdv_desc(String adv_desc) {
        this.adv_desc = adv_desc;
    }
}
