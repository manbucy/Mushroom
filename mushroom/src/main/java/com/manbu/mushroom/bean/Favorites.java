package com.manbu.mushroom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ManBu
 * com.manbu.mushroom.bean
 * 2016/9/12 22:13
 */
public class Favorites extends BmobObject {
    private String userId;
    private String prodId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
}
