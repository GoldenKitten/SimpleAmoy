package com.xhm.simpleamoy.data.entity;


import java.util.List;

/**
 * Created by xhm on 2018/4/28.
 */

public class IssueGoods {
    private String userName;
    private String schoolAddress;
    private String goodsName;
    private String goodsDes;
    private String goodsPrice;
    private String weixing;
    private String qq;
    private String mobile;
    private List<byte[]> goodsPic;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDes() {
        return goodsDes;
    }

    public void setGoodsDes(String goodsDes) {
        this.goodsDes = goodsDes;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getWeixing() {
        return weixing;
    }

    public void setWeixing(String weixing) {
        this.weixing = weixing;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<byte[]> getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(List<byte[]> goodsPic) {
        this.goodsPic = goodsPic;
    }
}
