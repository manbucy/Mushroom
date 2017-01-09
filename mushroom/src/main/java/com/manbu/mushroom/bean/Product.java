package com.manbu.mushroom.bean;


import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by ManBu
 * com.manbu.mushroom.entity
 * 2016/9/4 14:26
 */
public class Product extends BmobObject {

    private String prodName;
    private String prodSpec;
    private Float prodPrice;
    private Integer prodNum;
    private String prodAddress;
    private List<String> prodImgs;
    private String prodInfo;
    private String prodSupport;
    private String phone;
    private String prodCreaterId;
    private String remark;
    private MyUser prodCreater;

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdSpec() {
        return prodSpec;
    }

    public void setProdSpec(String prodSpec) {
        this.prodSpec = prodSpec;
    }

    public Float getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(Float prodPrice) {
        this.prodPrice = prodPrice;
    }

    public Integer getProdNum() {
        return prodNum;
    }

    public void setProdNum(Integer prodNum) {
        this.prodNum = prodNum;
    }

    public String getProdAddress() {
        return prodAddress;
    }

    public void setProdAddress(String prodAddress) {
        this.prodAddress = prodAddress;
    }

    public List<String> getProdImgs() {
        return prodImgs;
    }

    public void setProdImgs(List<String> prodImgs) {
        this.prodImgs = prodImgs;
    }

    public String getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(String prodInfo) {
        this.prodInfo = prodInfo;
    }

    public String getProdSupport() {
        return prodSupport;
    }

    public void setProdSupport(String prodSupport) {
        this.prodSupport = prodSupport;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProdCreaterId() {
        return prodCreaterId;
    }

    public void setProdCreaterId(String prodCreaterId) {
        this.prodCreaterId = prodCreaterId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MyUser getProdCreater() {
        return prodCreater;
    }

    public void setProdCreater(MyUser prodCreater) {
        this.prodCreater = prodCreater;
    }

}
