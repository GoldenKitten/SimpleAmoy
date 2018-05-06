package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/5/6.
 */

public class SellGoods {
    private String buyGoodsUserName;
    private String sellUserName;
    private String sellUUID;
    private byte[] sellMainPic;
    private String sellPrice;
    private String sellGoodsTitle;
    private boolean isChecked=false;
    public String getBuyGoodsUserName() {
        return buyGoodsUserName;
    }

    public void setBuyGoodsUserName(String buyGoodsUserName) {
        this.buyGoodsUserName = buyGoodsUserName;
    }

    public String getSellUserName() {
        return sellUserName;
    }

    public void setSellUserName(String sellUserName) {
        this.sellUserName = sellUserName;
    }

    public String getSellUUID() {
        return sellUUID;
    }

    public void setSellUUID(String sellUUID) {
        this.sellUUID = sellUUID;
    }

    public byte[] getSellMainPic() {
        return sellMainPic;
    }

    public void setSellMainPic(byte[] sellMainPic) {
        this.sellMainPic = sellMainPic;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellGoodsTitle() {
        return sellGoodsTitle;
    }

    public void setSellGoodsTitle(String sellGoodsTitle) {
        this.sellGoodsTitle = sellGoodsTitle;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
