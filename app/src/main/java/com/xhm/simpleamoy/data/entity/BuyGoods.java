package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/5/5.
 */

public class BuyGoods {
    private String buyUserName;
    private String sellUserName;
    private String sellUUID;

    public String getBuyUserName() {
        return buyUserName;
    }

    public void setBuyUserName(String buyUserName) {
        this.buyUserName = buyUserName;
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
}
