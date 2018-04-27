package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/4/27.
 */

public class FirstPagerGoods {
    private byte[] goodsImage;
    private String goodsTitle;
    private String goodsPrice;

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
}
