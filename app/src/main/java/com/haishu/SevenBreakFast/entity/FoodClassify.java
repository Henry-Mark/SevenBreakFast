package com.haishu.SevenBreakFast.entity;

import java.util.List;

/**
 * Created by zyw on 2016/3/4.
 */
public class FoodClassify {
    private String id;
    private String typeId;
    private String typeName;
    private int count;
    private List<Food> prodList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Food> getProdList() {
        return prodList;
    }

    public void setProdList(List<Food> prodList) {
        this.prodList = prodList;
    }
}
