package com.haishu.SevenBreakFast.entity;

import java.io.Serializable;

/**
 * Created by zyw on 2016/3/10.
 */
public class Address implements Serializable {
    private int userId;
    private int placeId;
    private int addressId;
    private String buildId;
    private String otherInfo;
    private String receiverName;
    private String receiverSex;
    private String receiverTel;
    private String addDesc;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverSex() {
        return receiverSex;
    }

    public void setReceiverSex(String receiverSex) {
        this.receiverSex = receiverSex;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getAddDesc() {
        return addDesc;
    }

    public void setAddDesc(String addDesc) {
        this.addDesc = addDesc;
    }
}
