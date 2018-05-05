package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/4/27.
 */

public class FirstPagerGoods {
    private String goodsUUID;
    private String userName;
    private byte[] goodsImage;
    private String goodsTitle;
    private String goodsPrice;
    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public byte[] getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(byte[] goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsUUID() {
        return goodsUUID;
    }

    public void setGoodsUUID(String goodsUUID) {
        this.goodsUUID = goodsUUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
